package com.example.webrtc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebRtcMediator {
    private static final Logger logger = LogManager.getLogger(WebRtcMediator.class);
    private long id;

    public WebRtcMediator(long id) {
        this.id = id;
    }

    public void act() {
        try (WebRtcMessagingClient client = new WebRtcMessagingClient(id);
                WebRtcWebSocketClient socketClient = new WebRtcWebSocketClient(client.getMessageHandler())) {
            client.setWebSocketClient(socketClient);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
