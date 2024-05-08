package com.example.webrtc;

import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.example.webrtc.model.WebRtcSignalingMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.client.WebSocketConnectionManager;

public class WebRtcWebSocketClient implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(WebRtcWebSocketClient.class);

    private WebSocketConnectionManager connectionManager;
    private StandardWebSocketClient webSocketClient;
    private WebRtcWebSocketHandler webSocketHandler;

    private static final String WEBSOCKET_URL = "ws://localhost:8080/websocket-webrtc";

    public WebRtcWebSocketClient() {
        this.webSocketClient = new StandardWebSocketClient();
        this.webSocketHandler = new WebRtcWebSocketHandler();

        connectionManager = new WebSocketConnectionManager(webSocketClient, webSocketHandler, WEBSOCKET_URL);
        connectionManager.start();
    }

    public boolean sendMessage(WebRtcSignalingMessage message) {
        return webSocketHandler.sendMessage(message);
    }

    @Override
    public void close() throws Exception {
        logger.info("Closing WebSocket client");
        connectionManager.stop();
    }
}
