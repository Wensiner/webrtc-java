package com.example.webrtc;

public class WebRtcMessagingClient implements AutoCloseable{

    private final WebRtcMessagingHandler messageHandler;

    public WebRtcMessagingClient(long id) {
        messageHandler = new WebRtcMessagingHandler(id);
    }

    @Override
    public void close() throws Exception {
        throw new UnsupportedOperationException("Unimplemented method 'close'");
    }

    public WebRtcMessagingHandler getMessageHandler() {
        return messageHandler   ;
    }

    public void setWebSocketClient(WebRtcWebSocketClient socketClient) {
        messageHandler.setWebSocketClient(socketClient);
    }
}
