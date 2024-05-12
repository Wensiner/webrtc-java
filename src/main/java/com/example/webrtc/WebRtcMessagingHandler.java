package com.example.webrtc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.webrtc.model.WebRtcSignalingMessage;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.onvoid.webrtc.RTCConfiguration;

public class WebRtcMessagingHandler {
    private static final Logger logger = LogManager.getLogger(WebRtcMessagingHandler.class);

    private final RTCConfiguration config = new RTCConfiguration();
    private WebRtcWebSocketClient webSocketClient;
    private long id;

    public WebRtcMessagingHandler(long id) {
        this.id = id;
    }

    public void setWebSocketClient(WebRtcWebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    public void handleMessage(WebRtcSignalingMessage message) {
        logger.info("Handling WebRTC signaling message from {} of type {} with payload: {}",
                message.getFrom(), message.getType(), message.getPayload());

        // Handle the WebRTC signaling message
        switch (message.getType()) {

            case CONFIG:
                handleConfigMessage(message);
                break;

            case REGISTER:
                // REGISTER message not expected here
                logger.warn("Unexpected REGISTER message: {}", message);
                break;

            case REQUEST:
                handleRequestMessage(message);
                break;

            case RESPONSE:
                // RESPONSE message not expected here
                logger.warn("RESPONSE unexpected here: {}", message);
                break;

            case OFFER:
                // This peer supposed to OFFER SDP, not expected here
                logger.warn("OFFER unexpected here: {}", message);
                break;

            case ANSWER:
                handleAnswerMessage(message);
                break;

            case CANDIDATE:
                handleCandidateMessage(message);
                break;

            case ERROR:
                handleErrorMessage(message);
                break;

            default:
                // Handle unsupported message types
                logger.warn("Unsupported message type: {}", message.getType());
                break;
        }
    }

    private void handleErrorMessage(WebRtcSignalingMessage message) {
    }

    private void handleCandidateMessage(WebRtcSignalingMessage message) {
    }

    private void handleAnswerMessage(WebRtcSignalingMessage message) {
    }

    private void handleRequestMessage(WebRtcSignalingMessage signalingMessage) {
        String peer = signalingMessage.getFrom();
        String[] urls = MediaStreamManager.getAvailableUrls(signalingMessage.getPayload());

        try {
            webSocketClient.sendMessage(new WebRtcSignalingMessage(peer, urls));

            if (urls == null || urls.length == 0) {
                logger.error("No available media stream URLs found");
            } else {
                logger.info("Available number of media stream URLs: {}", urls.length);
                newWebRtcClient(peer, urls);
            }
        } catch (Exception e) {
            logger.error("Error sending RESPONSE message: {}", e.getMessage());
        }
    }

    private void newWebRtcClient(String peer, String[] urls) {
        try {
            PeerConnectionManager.add(webSocketClient, config, peer, urls);
        } catch (Exception e) {
            logger.error("Error connecting to peer: {}", e.getMessage());
        }
    }

    private void handleConfigMessage(WebRtcSignalingMessage signalingMessage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.readerForUpdating(this.config).readValue(signalingMessage.getPayload());
        } catch (Exception e) {
            logger.error("Error processing CONFIG message: {}", e.getMessage());
        }
    }

    public void onConnectionEstablished() {
        try {
            webSocketClient.sendMessage(new WebRtcSignalingMessage(id));
        } catch (Exception e) {
            logger.error("Error sending REGISTER message: {}", e.getMessage());
        }
    }

    public void onConnectionClosed() {
    }

}
