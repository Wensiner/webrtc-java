package com.example.webrtc;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebRtcMediator {
    private static final Logger logger = LogManager.getLogger(WebRtcMediator.class);
    private static final AtomicBoolean running = new AtomicBoolean(false);

    private long id;

    public WebRtcMediator(long id) {
        this.id = id;
    }

    public void act() {
        if (running.get()) {
            logger.warn("WebSocket and WebRTC peer connection(s) are already active with id {}", id);
        } else {
            running.set(true);
            try (WebRtcMessagingClient client = new WebRtcMessagingClient(id);
                    WebRtcWebSocketClient socketClient = new WebRtcWebSocketClient(client.getMessageHandler())) {
                client.setWebSocketClient(socketClient);
            } catch (Exception e) {
                running.set(false);
                logger.error(e);
            }
        }
    }
}
