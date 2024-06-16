package com.example.webrtc.model;

import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.onvoid.webrtc.RTCIceCandidate;
import dev.onvoid.webrtc.RTCSessionDescription;

/**
 * A WebRTC signaling message.
 * It can be used to send messages to remote peer via WebSocket server.
 */
public class WebRtcSignalingMessage {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String from;
    private String to;
    private WebRtcSignalingMessageType type;
    private String payload;

    /**
     * Jackson requires a public no-argument constructor to deserialize objects.
     */
    public WebRtcSignalingMessage() {
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

    @Override
    public String toString() {
        return "WebRtcSignalingMessage [from=" + from + ", to=" + to + ", type=" + type + ", payload=" + payload + "]";
    }

    /**
     * Convert the WebRtcSignalingMessage to a TextMessage.
     * 
     * @return The TextMessage representation of the WebRtcSignalingMessage.
     * @throws JsonProcessingException If the message cannot be serialized.
     */
    public TextMessage toTextMessage() throws JsonProcessingException {
        return new TextMessage(objectMapper.writeValueAsString(this));
    }

    /**
     * Create a copy of the given WebRtcSignalingMessage.
     * 
     * @param message The message to copy.
     */
    public WebRtcSignalingMessage(WebRtcSignalingMessage message) {
        this.from = message.from;
        this.to = message.to;
        this.type = message.type;
        this.payload = message.payload;
    }

    /**
     * Convert to WebRTC signaling message from a text message.
     * 
     * @param textMessage The text message to parse.
     * @throws JsonProcessingException If the message cannot be deserialized.
     */
    public WebRtcSignalingMessage(String textMessage) throws JsonProcessingException {
        WebRtcSignalingMessage obj = objectMapper.readValue(textMessage, WebRtcSignalingMessage.class);
        this.from = obj.from;
        this.to = obj.to;
        this.type = obj.type;
        this.payload = obj.payload;
    }

    /**
     * Create a REGISTER message with the given ID.
     * 
     * @param id The ID to register.
     */
    public WebRtcSignalingMessage(long id) {
        this.type = WebRtcSignalingMessageType.REGISTER;
        this.payload = String.valueOf(id);
    }

    /**
     * Create a new WebRTC signaling message from an ICE candidate.
     * 
     * @param peer      The receiver of the message.
     * @param candidate The ICE candidate.
     * @throws JsonProcessingException If the message cannot be serialized.
     */
    public WebRtcSignalingMessage(String peer, RTCIceCandidate candidate) throws JsonProcessingException {
        this.type = WebRtcSignalingMessageType.CANDIDATE;
        this.to = peer;
        this.payload = objectMapper.writeValueAsString(candidate);
    }

    /**
     * Create a new WebRTC signaling message from a peer and a list of media stream
     * URLs.
     * 
     * @param peer The peer to send the message to.
     * @param urls The list of ICE server URLs.
     */
    public WebRtcSignalingMessage(String peer, String[] urls) {
        this.type = WebRtcSignalingMessageType.RESPONSE;
        this.to = peer;
        this.payload = String.join(",", urls);
    }

    /**
     * Create a new WebRTC signaling message from a peer and a session description.
     * 
     * @param peer        The peer to send the message to.
     * @param description The session description.
     * @throws JsonProcessingException
     */
    public WebRtcSignalingMessage(String peer, RTCSessionDescription description) throws JsonProcessingException {
        this.type = WebRtcSignalingMessageType.OFFER;
        this.to = peer;
        this.payload = objectMapper
                .writeValueAsString(new WebRTCSessionDescription(WebRTCSdpType.offer, description.sdp));
    }

    /**
     * Get SDP from ANSWER payload.
     * 
     * @param peer        The peer to send the message to.
     * @param description The session description.
     * @throws JsonProcessingException
     */
    public String toSdp() throws JsonProcessingException {
        return objectMapper.readTree(payload).get("sdp").asText();
    }

}
