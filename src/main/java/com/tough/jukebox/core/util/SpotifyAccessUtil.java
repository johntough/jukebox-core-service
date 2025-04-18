package com.tough.jukebox.core.util;

import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.model.SpotifyToken;
import com.tough.jukebox.core.model.User;
import com.tough.jukebox.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Component
public class SpotifyAccessUtil {

    private static final String BASE_URI = "https://api.spotify.com/v1/";

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public SpotifyAccessUtil(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public String sendGetRequest(String userId, String uri) throws UserTokenException, SpotifyAPIException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + getSpotifyAccessToken(userId));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = executeRequest(HttpMethod.GET, uri, request);

        return Objects.requireNonNull(response.getBody(), "Response body is unexpectedly null");
    }

    public void sendPutRequest(String userId, String uri, Map<String, String> requestBodyMap) throws UserTokenException, SpotifyAPIException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + getSpotifyAccessToken(userId));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBodyMap, headers);

        executeRequest(HttpMethod.PUT, uri, request);
    }

    private ResponseEntity<String> executeRequest(HttpMethod method, String uri, HttpEntity<?> request) throws SpotifyAPIException {

        ResponseEntity<String> response = restTemplate.exchange(
                BASE_URI + uri,
                method,
                request,
                new ParameterizedTypeReference<>() {}
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new SpotifyAPIException("Request to: " + uri + " failed. Response code: " + response.getStatusCode());
        }

        return response;
    }

    private String getSpotifyAccessToken(String userId) throws UserTokenException {

        return userRepository.findBySpotifyUserId(userId)
                .map(User::getSpotifyToken)
                .map(SpotifyToken::getAccessToken)
                .orElseThrow(() -> new UserTokenException("Unable to return access token for user: " + userId));
    }
}