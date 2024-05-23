package com.example.webrtc.model;

public class WebRTCSessionDescription {
    WebRTCSdpType type;
    String sdp;

    public WebRTCSessionDescription(WebRTCSdpType type, String sdp) {
        this.type = type;
        this.sdp = sdp;
    }

    public WebRTCSdpType getType() {
        return type;
    }

    public String getSdp() {
        return sdp;
    }
    
}
