package com.example.webrtc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.onvoid.webrtc.PeerConnectionFactory;
import dev.onvoid.webrtc.RTCConfiguration;
import dev.onvoid.webrtc.RTCPeerConnection;


public class WebRTCPeerConnection implements AutoCloseable{
    private static final Logger logger = LogManager.getLogger(WebRTCPeerConnection.class);

    private final RTCPeerConnection peerConnection;

    public WebRTCPeerConnection(RTCConfiguration config, String peer, String payload) {
        this.peerConnection = new PeerConnectionFactory().createPeerConnection(config, new WebRtcPeerConnectionObserver(peer));
    }

    @Override
    public void close() throws Exception {
        logger.info("Closing WebRTC peer connection");
        peerConnection.close();
    }
}
