package com.example.webrtc;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.WebSocketSession;

import com.example.webrtc.model.WebRtcSignalingMessage;

public class WebRtcWebMessageHandler {
    private static final Logger logger = LogManager.getLogger(WebRtcWebMessageHandler.class);
    private static final long ID = 1L;

    private WebRtcWebMessageHandler() {
        // Private constructor to hide the implicit public one
    }

    public static void handleMessage(WebSocketSession session, WebRtcSignalingMessage signalingMessage) {
        logger.info("Handling WebRTC signaling message from {} of type {} using session {} with payload: {}",
                signalingMessage.getFrom(), signalingMessage.getType(), session.getId(), signalingMessage.getPayload());

        // Handle the WebRTC signaling message
        switch (signalingMessage.getType()) {
            case CONFIG:
                // Handle the CONFIG message
                break;
            case REGISTER:
                // Handle the REGISTER message
                break;
            case REQUEST:
                // Handle the REQUEST message
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

    public static void onConnectionEstablished(WebSocketSession session) {
        logger.info("WebSocket connection established with session: {}", session.getId());

        // Handle the WebSocket connection established event
        try {
            session.sendMessage(new WebRtcSignalingMessage(ID).toTextMessage());
        } catch (IOException e) {
            logger.error("Error sending REGISTER message: {}", e.getMessage());
        }
    }

    public static void onConnectionClosed(WebSocketSession session) {
        logger.info("WebSocket connection closed with session: {}", session.getId());

        // Handle the WebSocket connection closed event
    }
}
