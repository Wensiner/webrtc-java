package com.example.webrtc;

import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.example.webrtc.model.WebRtcSignalingMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.socket.WebSocketSession;

import java.io.Closeable;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketConnectionManager;

public class WebRtcWebSocketClient1 implements Closeable {
    private static final Logger logger = LogManager.getLogger(WebRtcWebSocketClient1.class);
    private WebSocketConnectionManager connectionManager;
    ObjectMapper objectMapper = new ObjectMapper();

    private static final String WS_URL = "ws://localhost:8080/websocket-webrtc";

    public WebRtcWebSocketClient1() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketHandler webSocketHandler = new WebRtcWebSocketHandler();

        connectionManager = new WebSocketConnectionManager(webSocketClient, webSocketHandler,
                WS_URL);
        connectionManager.start();
    }

    private class WebRtcWebSocketHandler implements WebSocketHandler {
        @Override
        public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
            // WebSocket connection has been established
            logger.info("WebSocket connection established with session: {}", session.getId());
            WebRtcWebMessageHandler.onConnectionEstablished(session);
        }

        @Override
        public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message)
                throws Exception {
            // Handle incoming WebSocket messages
            if (message instanceof TextMessage) {
                String content = ((TextMessage) message).getPayload();
                logger.info("Received WebSocket message: {}", content);

                // Convert the JSON payload to a WebRtcSignalingMessage object
                try {
                    WebRtcSignalingMessage signalingMessage = objectMapper.readValue(content,
                            WebRtcSignalingMessage.class);
                    WebRtcWebMessageHandler.handleMessage(session, signalingMessage);
                } catch (Exception e) {
                    logger.error("Error parsing WebSocket message: {}", e.getMessage());
                }
            } else {
                logger.error("Received unsupported WebSocket message: {}", message);
            }
        }

        @Override
        public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception)
                throws Exception {
            // Handle transport errors
            logger.error("Transport error occurred for session id {} as: {}", session.getId(), exception.getMessage());
        }

        @Override
        public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus)
                throws Exception {
            // WebSocket connection has been closed
            logger.info("WebSocket connection closed with session id {} as: {}", session.getId(),
                    closeStatus.getReason());
            WebRtcWebMessageHandler.onConnectionClosed(session);
        }

        @Override
        public boolean supportsPartialMessages() {
            return false;
        }
    }

    @Override
    public void close() throws IOException {
        connectionManager.stop();
    }
}
