package com.example.webrtc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.webrtc.model.WebRtcSignalingMessage;

import dev.onvoid.webrtc.PeerConnectionObserver;
import dev.onvoid.webrtc.RTCIceCandidate;

public class WebRtcPeerConnectionObserver implements PeerConnectionObserver {
    private static final Logger logger = LogManager.getLogger(WebRtcPeerConnectionObserver.class);

    private final String peer;
    private WebRtcWebSocketClient webRtcWebSocketClient;

    public WebRtcPeerConnectionObserver(WebRtcWebSocketClient webRtcWebSocketClient, String peer) {
        this.webRtcWebSocketClient = webRtcWebSocketClient;
        this.peer = peer;
    }

    @Override
    public void onIceCandidate(RTCIceCandidate candidate) {
        // Send the ICE candidate to the other peer
        try {
            webRtcWebSocketClient.sendMessage(new WebRtcSignalingMessage(peer, candidate));
        } catch (Exception e) {
            logger.error("Unable to send ICE candidate to peer {} as {}", peer, e.getMessage());
        }
    }
}
