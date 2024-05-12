package com.example.webrtc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.onvoid.webrtc.PeerConnectionFactory;
import dev.onvoid.webrtc.RTCConfiguration;
import dev.onvoid.webrtc.RTCPeerConnection;

public class WebRtcPeerConnection implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(WebRtcPeerConnection.class);

    private final RTCPeerConnection peerConnection;

    public WebRtcPeerConnection(WebRtcWebSocketClient webSocketClient, RTCConfiguration config, String peer,
            String[] urls) {
        this.peerConnection = new PeerConnectionFactory().createPeerConnection(config,
                new WebRtcPeerConnectionObserver(webSocketClient, peer));
    }

    @Override
    public void close() throws Exception {
        logger.info("Closing WebRTC peer connection");
        peerConnection.close();
    }
}
