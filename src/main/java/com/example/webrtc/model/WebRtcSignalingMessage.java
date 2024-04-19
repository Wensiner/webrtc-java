package com.example.webrtc.model;

public class WebRtcSignalingMessage {
    private String from;
    private String to;
    private WebRtcSignalingMessageType type;
    private String payload;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public WebRtcSignalingMessageType getType() {
        return type;
    }

    public void setType(WebRtcSignalingMessageType type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
