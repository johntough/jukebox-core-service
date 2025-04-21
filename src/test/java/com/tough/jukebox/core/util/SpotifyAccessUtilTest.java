package com.tough.jukebox.core.util;

import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.model.SpotifyToken;
import com.tough.jukebox.core.model.User;
import com.tough.jukebox.core.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpotifyAccessUtilTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    SpotifyAccessUtil spotifyAccessUtil;

    @Test
    void testSendGetRequestSuccess() throws UserTokenException, SpotifyAPIException {
        setupMockUser();

        when(restTemplate.exchange(
                eq("https://api.spotify.com/v1/test-uri"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.status(HttpStatus.OK).body("success"));

        String response = spotifyAccessUtil.sendGetRequest("test-user-id", "test-uri");

        assertEquals("success", response);
    }

    @Test
    void testSendGetRequestFailureUserTokenException() {
        when(userRepository.findBySpotifyUserId("test-user-id")).thenReturn(Optional.empty());

        assertThrows(UserTokenException.class, () -> spotifyAccessUtil.sendGetRequest("test-user-id", "test-uri"));
    }

    @Test
    void testSendGetRequestFailureSpotifyAPIException() {
        setupMockUser();

        when(restTemplate.exchange(
                eq("https://api.spotify.com/v1/test-uri"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        assertThrows(SpotifyAPIException.class, () -> spotifyAccessUtil.sendGetRequest("test-user-id", "test-uri"));
    }

    @Test
    void testSendPutRequestSuccess() throws UserTokenException, SpotifyAPIException {
        setupMockUser();

        when(restTemplate.exchange(
                eq("https://api.spotify.com/v1/test-uri"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.status(HttpStatus.OK).body("success"));

        spotifyAccessUtil.sendPutRequest("test-user-id", "test-uri", new HashMap<>());

        verify(restTemplate, times(1)).exchange(
                eq("https://api.spotify.com/v1/test-uri"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void testSendPutRequestFailureUserTokenException() {
        when(userRepository.findBySpotifyUserId("test-user-id")).thenReturn(Optional.empty());

        assertThrows(UserTokenException.class, () -> spotifyAccessUtil.sendPutRequest("test-user-id", "test-uri", new HashMap<>()));
    }

    @Test
    void testSendPutRequestFailureSpotifyAPIException() {
        setupMockUser();

        when(restTemplate.exchange(
                eq("https://api.spotify.com/v1/test-uri"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        assertThrows(SpotifyAPIException.class, () -> spotifyAccessUtil.sendPutRequest("test-user-id", "test-uri", new HashMap<>()));
    }

    private void setupMockUser() {
        User mockUser = new User();
        SpotifyToken mockSpotifyToken = new SpotifyToken();
        mockSpotifyToken.setAccessToken("mock-access-token");
        mockUser.setSpotifyToken(mockSpotifyToken);

        when(userRepository.findBySpotifyUserId("test-user-id")).thenReturn(Optional.of(mockUser));
    }
}
