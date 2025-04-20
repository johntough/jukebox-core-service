package com.tough.jukebox.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tough.jukebox.core.api.SpotifyDeviceResponse;
import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.util.SpotifyAccessUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    SpotifyAccessUtil spotifyAccessUtil;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    private DeviceService deviceService;

    @Test
    void testGetDevicesSuccess() throws UserTokenException, JsonProcessingException, SpotifyAPIException {
        when(spotifyAccessUtil.sendGetRequest(
                "test-user-id",
                "me/player/devices"
        )).thenReturn("{\"mockJsonKey\":\"mockJsonValue\"}");

        when(objectMapper.readValue(
                "{\"mockJsonKey\":\"mockJsonValue\"}",
                SpotifyDeviceResponse.class
        )).thenReturn(new SpotifyDeviceResponse());

        SpotifyDeviceResponse response = deviceService.getDevices("test-user-id");

        assertNotNull(response);
    }

    @Test
    void testGetDevicesFailureUserTokenException() throws UserTokenException, SpotifyAPIException {
        doThrow(UserTokenException.class).when(spotifyAccessUtil).sendGetRequest(
                anyString(),
                anyString());

        assertThrows(UserTokenException.class, () ->
                deviceService.getDevices("test-user-id")
        );
    }

    @Test
    void testGetDevicesFailureSpotifyAPIException() throws UserTokenException, SpotifyAPIException {
        doThrow(SpotifyAPIException.class).when(spotifyAccessUtil).sendGetRequest(
                anyString(),
                anyString()
        );

        assertThrows(SpotifyAPIException.class, () ->
                deviceService.getDevices("test-user-id")
        );
    }

    @Test
    void testGetDevicesFailureJsonProcessingException() throws UserTokenException, SpotifyAPIException, JsonProcessingException {
        when(spotifyAccessUtil.sendGetRequest(
                anyString(),
                anyString()
        )).thenReturn("{\"mockJsonKey\":\"mockJsonValue\"}");

        doThrow(JsonProcessingException.class).when(objectMapper).readValue(
                anyString(),
                eq(SpotifyDeviceResponse.class)
        );

        assertThrows(JsonProcessingException.class, () ->
                deviceService.getDevices("test-user-id")
        );
    }
}
