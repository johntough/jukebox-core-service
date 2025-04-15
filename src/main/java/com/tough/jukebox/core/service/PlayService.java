package com.tough.jukebox.core.service;

import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.util.SpotifyAccessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PlayService {

    private final static Logger LOGGER =  LoggerFactory.getLogger(PlayService.class);

    private final static String PLAY_URI = "me/player/play?deviceId=";
    private final static String PAUSE_URI = "me/player/pause?deviceId=";
    private final static String CONTEXT_URI_LABEL = "context_uri";

    private final SpotifyAccessUtil spotifyAccessUtil;

    @Autowired
    public PlayService(SpotifyAccessUtil spotifyAccessUtil) {
        this.spotifyAccessUtil = spotifyAccessUtil;
    }

    public void executePlayRequest(String userId, String deviceId, String playContextUri) throws UserTokenException, SpotifyAPIException {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put(CONTEXT_URI_LABEL, playContextUri);

        spotifyAccessUtil.sendPutRequest(userId, PLAY_URI + deviceId, requestBodyMap);
    }

    public void executePauseRequest(String userId, String deviceId) throws UserTokenException, SpotifyAPIException {
        spotifyAccessUtil.sendPutRequest(userId, PAUSE_URI + deviceId, new HashMap<>());
    }
}
