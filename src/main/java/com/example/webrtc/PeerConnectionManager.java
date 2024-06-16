package com.example.webrtc;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.webrtc.model.WebRtcSignalingMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.onvoid.webrtc.RTCConfiguration;

public class PeerConnectionManager {
    private static final Logger logger = LogManager.getLogger(PeerConnectionManager.class);
    private static final Map<String, WebRtcPeerConnection> peerConnections = new HashMap<>();
    private static RTCConfiguration config = new RTCConfiguration();
    private static WebRtcWebSocketClient webSocketClient;

    private PeerConnectionManager() {
        // Private constructor to hide the implicit public one
    }

    public static void handleRequest(WebRtcSignalingMessage message) {
        String peer = message.getFrom();
        String[] urls = MediaStreamManager.getAvailableUrls(message.getPayload());

        try {
            webSocketClient.sendMessage(new WebRtcSignalingMessage(peer, urls));

            if (urls == null || urls.length == 0) {
                logger.error("No available media stream URLs found");
            } else {
                logger.info("Available number of media stream URLs: {}", urls.length);
                addOrUpdate(peer, urls);
            }
        } catch (Exception e) {
            logger.error("Error sending RESPONSE message: {}", e.getMessage());
        }
    }

    private static void addOrUpdate(String peer,
            String[] urls) {
        peerConnections.merge(peer, new WebRtcPeerConnection(webSocketClient, config, peer, urls),
                (key, existingConnection) -> existingConnection.update(urls));
    }

    public static void addIceCandidate(WebRtcSignalingMessage message) {
        WebRtcPeerConnection connection = peerConnections.get(message.getFrom());
        if (connection == null) {
            logger.warn("No connection found for peer {}", message.getFrom());
        } else {
            connection.addIceCandidate(message.getPayload());
        }
    }

    public static void closeConnection(WebRtcSignalingMessage message) {
        peerConnections.computeIfPresent(message.getFrom(), (key, connection) -> {
            try {
                connection.close();
            } catch (Exception e) {
                logger.error("Error closing connection for peer {}", connection, e);
            }
            return null;
        });
    }

    public static void closeConnection() {
        peerConnections.values().forEach(connection -> {
            try {
                connection.close();
            } catch (Exception e) {
                logger.error("Error closing connection for peer {}", connection, e);
            }
        });
    }

    public static void addAnswer(WebRtcSignalingMessage message) {
        WebRtcPeerConnection connection = peerConnections.get(message.getFrom());
        if (connection == null) {
            logger.warn("No connection found for peer {}", message.getFrom());
        } else {
            try {
                connection.answer(message.toSdp());
            } catch (JsonProcessingException e) {
                logger.error("Error processing ANSWER message from {} as {}", message.getFrom(), e.getMessage());
            }
        }
    }

    public static void addConfiguration(WebRtcSignalingMessage message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.readerForUpdating(config).readValue(message.getPayload());
        } catch (Exception e) {
            logger.error("Error processing CONFIG message: {}", e.getMessage());
        }
    }

    public static void setWebSocketClient(WebRtcWebSocketClient webSocketClient) {
        PeerConnectionManager.webSocketClient = webSocketClient;
    }

}
