package com.example.webrtc.model;

import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebRtcSignalingMessage {
    private String from;
    private String to;
    private WebRtcSignalingMessageType type;
    private String payload;

    public WebRtcSignalingMessage(long id) {
        this.type = WebRtcSignalingMessageType.REGISTER;
        this.payload = String.valueOf(id);
    }
    public WebRtcSignalingMessage(WebRtcSignalingMessageType type, String to, String payload) {
        this.type = type;
        this.to = to;
        this.payload = payload;
    }
    public WebRtcSignalingMessage(WebRtcSignalingMessageType type, String from, String to, String payload) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.payload = payload;
    }

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

    public TextMessage toTextMessage() throws JsonProcessingException {
        return new TextMessage(new ObjectMapper().writeValueAsString(this));
    }
}
