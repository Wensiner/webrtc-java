package com.example.webrtc;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.webrtc.model.WebRtcSignalingMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.onvoid.webrtc.PeerConnectionFactory;
import dev.onvoid.webrtc.RTCConfiguration;

public class WebRtcWebMessageHandler {
    private static final Logger logger = LogManager.getLogger(WebRtcWebMessageHandler.class);
    private static final long ID = 1L;

    private static final PeerConnectionFactory factory = new PeerConnectionFactory();
    private static final RTCConfiguration config = new RTCConfiguration();

    private WebRtcWebMessageHandler() {
        // Private constructor to hide the implicit public one
    }

    public static void handleMessage(WebRtcSignalingMessage signalingMessage) {
        logger.info("Handling WebRTC signaling message from {} of type {} with payload: {}",
                signalingMessage.getFrom(), signalingMessage.getType(), signalingMessage.getPayload());

        // Handle the WebRTC signaling message
        switch (signalingMessage.getType()) {
            case CONFIG:
                // Handle the CONFIG message
                try {
                    new ObjectMapper().readerForUpdating(config).readValue(signalingMessage.getPayload());
                } catch (Exception e) {
                    logger.error("Error processing CONFIG message: {}", e.getMessage());
                }
                break;
            case REGISTER:
                // REGISTER message not expected here
                logger.warn("Unexpected REGISTER message: {}", signalingMessage);
                break;
            case REQUEST:
                // Handle the REQUEST message
                logger.info("Request received from {} with payload {}", signalingMessage.getFrom(),
                        signalingMessage.getPayload());
                String[] requestedUrls = MediaStreamManager.getRequestedUrls(signalingMessage.getPayload());
                String[] availableUrls = MediaStreamManager.getAvailableUrls(requestedUrls);
                Object stream = MediaStreamManager.getMediaStream(availableUrls, signalingMessage.getFrom());

                try (WebRTCPeerConnection peerConnection = new WebRTCPeerConnection(config, signalingMessage.getFrom(),
                        signalingMessage.getPayload())) {
                    // Send the stream to the other peer
                } catch (Exception e) {
                    logger.error("Error creating WebRTC peer connection: {}", e.getMessage());
                }

                break;
            case RESPONSE:
                // Handle the RESPONSE message
                break;
            case OFFER:
                // Handle the OFFER message
                break;
            case ANSWER:
                // Handle the ANSWER message
                break;
            case CANDIDATE:
                // Handle the CANDIDATE message
                break;
            case ERROR:
                // Handle the ERROR message
                break;
            default:
                // Handle unsupported message types
                logger.warn("Unsupported message type: {}", signalingMessage.getType());
                break;
        }
    }

    public static void onConnectionEstablished() {
        // Handle the WebSocket connection established event
        try {
            new WebRtcSignalingMessage(ID).toTextMessage();
        } catch (IOException e) {
            logger.error("Error sending REGISTER message: {}", e.getMessage());
        }
    }

    public static void onConnectionClosed() {
        // Handle the WebSocket connection closed event
    }
}
