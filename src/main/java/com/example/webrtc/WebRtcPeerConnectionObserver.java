package com.example.webrtc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.webrtc.model.WebRtcSignalingMessage;
import com.fasterxml.jackson.core.JsonProcessingException;

import dev.onvoid.webrtc.PeerConnectionObserver;
import dev.onvoid.webrtc.RTCIceCandidate;

public class WebRtcPeerConnectionObserver implements PeerConnectionObserver {
    private static final Logger logger = LogManager.getLogger(WebRtcPeerConnectionObserver.class);

    private final String peer;

    public WebRtcPeerConnectionObserver(String peer) {
        this.peer = peer;
    }

    @Override
    public void onIceCandidate(RTCIceCandidate candidate) {
        // Send the ICE candidate to the other peer
        try {
            new WebRtcSignalingMessage(peer, candidate);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing ICE candidate: {}", e.getMessage());
        }
    }
}
