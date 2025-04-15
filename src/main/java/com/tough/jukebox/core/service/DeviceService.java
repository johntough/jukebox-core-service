package com.tough.jukebox.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tough.jukebox.core.api.SpotifyDeviceResponse;
import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.util.SpotifyAccessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private static final String DEVICE_URI = "me/player/devices";

    private final SpotifyAccessUtil spotifyAccessUtil;
    private final ObjectMapper objectMapper;

    @Autowired
    public DeviceService(SpotifyAccessUtil spotifyAccessUtil, ObjectMapper objectMapper) {
        this.spotifyAccessUtil = spotifyAccessUtil;
        this.objectMapper = objectMapper;
    }

    public SpotifyDeviceResponse getDevices(String userId) throws UserTokenException, SpotifyAPIException, JsonProcessingException {
        String responseBody = spotifyAccessUtil.sendGetRequest(userId, DEVICE_URI);
        SpotifyDeviceResponse parsedResponse = objectMapper.readValue(responseBody, SpotifyDeviceResponse.class);

        LOGGER.info("{} devices found for user: {}", parsedResponse.getDevices().size(), userId);

        parsedResponse.getDevices().forEach(device -> {
            LOGGER.info("Device {} with Id: {}", device.getName(), device.getId());
        });

        return parsedResponse;
    }
}