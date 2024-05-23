package com.example.webrtc;

/**
 * A manager class for media streams.
 */
public class MediaStreamManager {

    private MediaStreamManager() {
        // private constructor to hide the implicit public one
    }

    public static String[] parseRequestedUrls(String payload) {
        return payload.split(",");
    }

    public static String[] getAvailableUrls(String[] payload) {
        return payload;
    }

    public static String[] getAvailableUrls(String payload) {
        return getAvailableUrls(parseRequestedUrls(payload));
    }

    public static Object getMediaStream(String[] availableUrls, String from) {
        throw new UnsupportedOperationException("Unimplemented method 'getMediaStream'");
    }

}
