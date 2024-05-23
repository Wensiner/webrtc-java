package com.example.webrtc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.webrtc.model.WebRtcSignalingMessage;

import dev.onvoid.webrtc.CreateSessionDescriptionObserver;
import dev.onvoid.webrtc.PeerConnectionFactory;
import dev.onvoid.webrtc.PeerConnectionObserver;
import dev.onvoid.webrtc.RTCConfiguration;
import dev.onvoid.webrtc.RTCDataChannel;
import dev.onvoid.webrtc.RTCIceCandidate;
import dev.onvoid.webrtc.RTCIceConnectionState;
import dev.onvoid.webrtc.RTCIceGatheringState;
import dev.onvoid.webrtc.RTCOfferOptions;
import dev.onvoid.webrtc.RTCPeerConnection;
import dev.onvoid.webrtc.RTCPeerConnectionIceErrorEvent;
import dev.onvoid.webrtc.RTCPeerConnectionState;
import dev.onvoid.webrtc.RTCRtpReceiver;
import dev.onvoid.webrtc.RTCRtpTransceiver;
import dev.onvoid.webrtc.RTCSdpType;
import dev.onvoid.webrtc.RTCSessionDescription;
import dev.onvoid.webrtc.RTCSignalingState;
import dev.onvoid.webrtc.SetSessionDescriptionObserver;
import dev.onvoid.webrtc.media.MediaStream;

public class WebRtcPeerConnection implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(WebRtcPeerConnection.class);

    private final RTCPeerConnection peerConnection;
    private final String peer;

    private WebRtcWebSocketClient webSocketClient;

    public WebRtcPeerConnection(WebRtcWebSocketClient webSocketClient, RTCConfiguration config, String peer,
            String[] urls) {
        this.webSocketClient = webSocketClient;
        this.peer = peer;

        this.peerConnection = new PeerConnectionFactory().createPeerConnection(config,
                new PeerConnectionObserver() {
                    @Override
                    public void onConnectionChange(RTCPeerConnectionState state) {
                        logger.info("Connection state changed to {} for peer {}", state, peer);
                    }

                    @Override
                    public void onSignalingChange(RTCSignalingState state) {
                        logger.info("Signaling state changed to {} for peer {}", state, peer);
                    }

                    @Override
                    public void onIceCandidate(RTCIceCandidate candidate) {
                        try {
                            logger.info("Sending ICE candidate to peer {}", peer);
                            webSocketClient.sendMessage(new WebRtcSignalingMessage(peer, candidate));
                        } catch (Exception e) {
                            logger.error("Unable to send ICE candidate to peer {}", peer, e);
                        }
                    }

                    @Override
                    public void onIceConnectionChange(RTCIceConnectionState state) {
                        logger.info("ICE connection state changed to {} for peer {}", state, peer);
                    }

                    @Override
                    public void onStandardizedIceConnectionChange(RTCIceConnectionState state) {
                        logger.info("Standardized ICE connection state changed to {} for peer {}", state, peer);
                    }

                    @Override
                    public void onIceConnectionReceivingChange(boolean receiving) {
                        logger.info("ICE connection receiving changed to {} for peer {}", receiving, peer);
                    }

                    @Override
                    public void onIceGatheringChange(RTCIceGatheringState state) {
                        logger.info("ICE gathering state changed to {} for peer {}", state, peer);
                    }

                    @Override
                    public void onIceCandidatesRemoved(RTCIceCandidate[] candidates) {
                        logger.info("ICE candidates removed for peer {}", peer);
                    }

                    @Override
                    public void onIceCandidateError(RTCPeerConnectionIceErrorEvent event) {
                        logger.error("ICE candidate error for peer {} on event {}", peer, event);
                    }

                    @Override
                    public void onRemoveStream(MediaStream stream) {
                        logger.info("Stream removed for peer {}", peer);
                    }

                    @Override
                    public void onDataChannel(RTCDataChannel dataChannel) {
                        logger.info("Data channel created for peer {}", peer);
                    }

                    @Override
                    public void onAddTrack(RTCRtpReceiver receiver, MediaStream[] mediaStreams) {
                        logger.info("Track added for peer {}", peer);
                    }

                    @Override
                    public void onRemoveTrack(RTCRtpReceiver receiver) {
                        logger.info("Track removed for peer {}", peer);
                    }

                    @Override
                    public void onTrack(RTCRtpTransceiver transceiver) {
                        logger.info("Track added for peer {}", peer);
                    }
                });

        init(urls);
    }

    private void init(String[] urls) {
        logger.info("Initializing WebRTC peer connection of peer {}", peer);
        createOffer();
    }

    private void createOffer() {
        peerConnection.createOffer(new RTCOfferOptions(), new CreateSessionDescriptionObserver() {

            @Override
            public void onSuccess(RTCSessionDescription description) {
                logger.info("Setting local description for peer {}", peer);
                peerConnection.setLocalDescription(description, new SetSessionDescriptionObserver() {

                    @Override
                    public void onSuccess() {
                        logger.info("Sending offer to peer {}", peer);
                        try {
                            webSocketClient.sendMessage(new WebRtcSignalingMessage(peer, description));
                        } catch (Exception e) {
                            logger.error("Failed to send offer to peer {}", peer, e);
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        logger.error("Failed to set local description for peer {} as {}", peer, error);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                logger.error("Failed to create offer for peer {} as {}", peer, error);
            }
        });
    }

    @Override
    public void close() throws Exception {
        logger.info("Closing WebRTC peer connection of peer {}", peer);
        peerConnection.close();
    }

    public void addIceCandidate(String payload) {
        logger.info("Adding ICE candidate to WebRTC peer connection of peer {} with payload: {}", peer, payload);
        peerConnection.addIceCandidate(new RTCIceCandidate(null, 0, payload));
    }

    public void answer(String payload) {
        logger.info("Answering WebRTC peer connection of peer {} with payload: {}", peer, payload);
        peerConnection.setRemoteDescription(new RTCSessionDescription(RTCSdpType.ANSWER, payload),
                new SetSessionDescriptionObserver() {

                    @Override
                    public void onSuccess() {
                        logger.info("Successfully set remote description for peer {}", peer);
                    }

                    @Override
                    public void onFailure(String error) {
                        logger.error("Failed to set remote description for peer {} as {}", peer, error);
                    }
                });
    }

    public WebRtcPeerConnection update(String[] urls) {
        logger.info("Updating WebRTC peer connection of peer {} with new URLs: {}", peer, String.join(", ", urls));
        init(urls);
        return this;
    }

}
