package com.example.webrtc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebRtcMessagingClient implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(WebRtcMessagingClient.class);
    private final WebRtcMessagingHandler messageHandler;

    public WebRtcMessagingClient(long id) {
        messageHandler = new WebRtcMessagingHandler(id);
    }

    @Override
    public void close() throws Exception {
        logger.info("Closing WebRtcMessaging client");
    }

    public WebRtcMessagingHandler getMessageHandler() {
        return messageHandler;
    }

    public void setWebSocketClient(WebRtcWebSocketClient socketClient) {
        messageHandler.setWebSocketClient(socketClient);
    }
}
