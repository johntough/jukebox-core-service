package com.tough.jukebox.core.service;

import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.util.SpotifyAccessUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayServiceTest {

    @Mock
    private SpotifyAccessUtil spotifyAccessUtil;

    @InjectMocks
    private PlayService playService;

    @Test
    void testExecutePlayRequestSuccess() throws UserTokenException, SpotifyAPIException {
        doNothing().when(spotifyAccessUtil).sendPutRequest(
                eq("test-user-id"),
                eq("me/player/play?deviceId=test-device-id"),
                anyMap());

        playService.executePlayRequest("test-user-id", "test-device-id", "test-context-uri");

        verify(spotifyAccessUtil, times(1)).sendPutRequest(
                eq("test-user-id"),
                eq("me/player/play?deviceId=test-device-id"),
                anyMap()
        );
    }

    @Test
    void testExecutePlayRequestFailureUserTokenException() throws UserTokenException, SpotifyAPIException {
        doThrow(UserTokenException.class).when(spotifyAccessUtil).sendPutRequest(
                anyString(),
                anyString(),
                anyMap());

        assertThrows(UserTokenException.class, () -> {
            playService.executePlayRequest("test-user-id", "test-device-id", "test-context-uri");
        });
    }

    @Test
    void testExecutePlayRequestFailureSpotifyAPIException() throws UserTokenException, SpotifyAPIException {
        doThrow(SpotifyAPIException.class).when(spotifyAccessUtil).sendPutRequest(
                anyString(),
                anyString(),
                anyMap());

        assertThrows(SpotifyAPIException.class, () -> {
            playService.executePlayRequest("test-user-id", "test-device-id", "test-context-uri");
        });
    }

    @Test
    void testExecutePauseRequestSuccess() throws UserTokenException, SpotifyAPIException {
        doNothing().when(spotifyAccessUtil).sendPutRequest(
                eq("test-user-id"),
                eq("me/player/pause?deviceId=test-device-id"),
                anyMap());

        playService.executePauseRequest("test-user-id", "test-device-id");

        verify(spotifyAccessUtil, times(1)).sendPutRequest(
                eq("test-user-id"),
                eq("me/player/pause?deviceId=test-device-id"),
                anyMap()
        );
    }

    @Test
    void testExecutePauseRequestFailureUserTokenException() throws UserTokenException, SpotifyAPIException {
        doThrow(UserTokenException.class).when(spotifyAccessUtil).sendPutRequest(
                anyString(),
                anyString(),
                anyMap());

        assertThrows(UserTokenException.class, () -> {
            playService.executePauseRequest("test-user-id", "test-device-id");
        });
    }

    @Test
    void testExecutePauseRequestFailureSpotifyAPIException() throws UserTokenException, SpotifyAPIException {
        doThrow(SpotifyAPIException.class).when(spotifyAccessUtil).sendPutRequest(
                anyString(),
                anyString(),
                anyMap());

        assertThrows(SpotifyAPIException.class, () -> {
            playService.executePauseRequest("test-user-id", "test-device-id");
        });
    }
}
