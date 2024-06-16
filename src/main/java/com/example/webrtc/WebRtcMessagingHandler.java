package com.example.webrtc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.webrtc.model.WebRtcSignalingMessage;

public class WebRtcMessagingHandler {
    private static final Logger logger = LogManager.getLogger(WebRtcMessagingHandler.class);

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
                PeerConnectionManager.addConfiguration(message);
                break;

            case REGISTER:
                // REGISTER message not expected here
                logger.warn("Unexpected REGISTER message: {}", message);
                break;

            case REQUEST:
                PeerConnectionManager.handleRequest(message);
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
                PeerConnectionManager.addAnswer(message);
                break;

            case CANDIDATE:
                PeerConnectionManager.addIceCandidate(message);
                break;

            case ERROR:
                PeerConnectionManager.closeConnection(message);
                break;

            default:
                // Handle unsupported message types
                logger.warn("Unsupported message type: {}", message.getType());
                break;
        }
    }

    public void onConnectionEstablished() {
        try {
            webSocketClient.sendMessage(new WebRtcSignalingMessage(id));
            PeerConnectionManager.setWebSocketClient(webSocketClient);
        } catch (Exception e) {
            logger.error("Error sending REGISTER message: {}", e.getMessage());
        }
    }

    public void onConnectionClosed() {
        PeerConnectionManager.closeConnection();
    }
}
