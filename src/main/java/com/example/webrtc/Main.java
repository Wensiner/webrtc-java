package com.example.webrtc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Hello, World!");
        
        try (WebRtcWebSocketClient client = new WebRtcWebSocketClient()) {
        } catch (Exception e) {
            logger.error("Error:", e);
        }
    }
}
