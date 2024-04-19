package com.example.webrtc;

import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.client.WebSocketConnectionManager;

public class WebRtcWebSocketClient implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(WebRtcWebSocketClient.class);

    private WebSocketConnectionManager connectionManager;

    private static final String WS_URL = "ws://localhost:8080/websocket-webrtc";

    public WebRtcWebSocketClient() {
        connectionManager = new WebSocketConnectionManager(new StandardWebSocketClient(), new WebRtcWebSocketHandler(),
                WS_URL);
        connectionManager.start();
    }

    @Override
    public void close() throws Exception {
        logger.info("Closing WebSocket client");
    }
}
