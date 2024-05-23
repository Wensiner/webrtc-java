package com.example.webrtc.model;

/**
 * The type of a WebRTC signaling message.
 */
public enum WebRtcSignalingMessageType {
    CONFIG,
    REGISTER,
    REQUEST,
    RESPONSE,
    OFFER,
    ANSWER,
    CANDIDATE,
    ERROR
}
