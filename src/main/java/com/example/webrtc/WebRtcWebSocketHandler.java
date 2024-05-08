package com.example.webrtc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.example.webrtc.model.WebRtcSignalingMessage;

public class WebRtcWebSocketHandler implements WebSocketHandler {
    private static final Logger logger = LogManager.getLogger(WebRtcWebSocketHandler.class);
    private WebSocketSession webSocketSession;

    // Send message
    public boolean sendMessage(WebRtcSignalingMessage message) {
        if (webSocketSession == null) {
            logger.error("WebSocket session is null, cannot send message");
        } else {
            try {
                webSocketSession.sendMessage(message.toTextMessage());
                return true;
            } catch (Exception e) {
                logger.error("Error sending WebSocket message: {}", e.getMessage());
            }
        }
        return false;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        logger.info("WebSocket connection established with session: {}", session.getId());
        this.webSocketSession = session;
        WebRtcWebMessageHandler.onConnectionEstablished();
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message)
            throws Exception {
        if (message instanceof TextMessage) {
            String textMessage = ((TextMessage) message).getPayload();
            logger.info("Received WebSocket message: {}", textMessage);

            try {
                WebRtcWebMessageHandler.handleMessage(new WebRtcSignalingMessage(textMessage));
            } catch (Exception e) {
                logger.error("Error processing WebSocket message: {}", e.getMessage());
            }
        } else {
            logger.error("Received unsupported WebSocket message: {}", message);
        }
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception)
            throws Exception {
        logger.error("Transport error occurred for session id {} as: {}", session.getId(), exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus)
            throws Exception {
        logger.info("WebSocket connection closed with session id {} as: {}", session.getId(),
                closeStatus.getReason());
        WebRtcWebMessageHandler.onConnectionClosed();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
