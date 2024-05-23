package com.example.webrtc;

import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.example.webrtc.WebRtcWebSocketHandler.WebSocketSessionNullException;
import com.example.webrtc.model.WebRtcSignalingMessage;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.client.WebSocketConnectionManager;

public class WebRtcWebSocketClient implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(WebRtcWebSocketClient.class);

    private final WebSocketConnectionManager connectionManager;
    private final StandardWebSocketClient webSocketClient;
    private final WebRtcWebSocketHandler webSocketHandler;

    private static final String WEBSOCKET_URL = "ws://localhost:8080/websocket-webrtc";

    public WebRtcWebSocketClient(final WebRtcMessagingHandler webRtcMessageHandler) {
        this.webSocketClient = new StandardWebSocketClient();
        this.webSocketHandler = new WebRtcWebSocketHandler(webRtcMessageHandler);

        connectionManager = new WebSocketConnectionManager(webSocketClient, webSocketHandler, WEBSOCKET_URL);
        connectionManager.start();
    }

    public void sendMessage(WebRtcSignalingMessage message) throws WebSocketSessionNullException, IOException {
        webSocketHandler.sendMessage(message);
    }

    @Override
    public void close() throws Exception {
        logger.info("Stopping WebSocket client");
        connectionManager.stop();
    }
}
