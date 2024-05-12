package com.example.webrtc;

import dev.onvoid.webrtc.RTCConfiguration;

public class PeerConnectionManager {
    private PeerConnectionManager() {
        // Private constructor to hide the implicit public one
    }

    public static void add(WebRtcWebSocketClient webSocketClient, RTCConfiguration config, String peer, String[] urls) {
        WebRtcPeerConnection peerConnection = new WebRtcPeerConnection(webSocketClient, config, peer, urls);
    }

}
