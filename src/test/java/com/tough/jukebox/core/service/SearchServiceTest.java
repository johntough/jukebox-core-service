package com.tough.jukebox.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tough.jukebox.core.api.SpotifySearchResponse;
import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.util.SpotifyAccessUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    SpotifyAccessUtil spotifyAccessUtil;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    SearchService searchService;

    @Test
    void testSearchAristSuccess() throws UserTokenException, SpotifyAPIException, JsonProcessingException {
        when(spotifyAccessUtil.sendGetRequest(
                "test-user-id",
                "search?q=test-search-query&type=artist"
        )).thenReturn("{\"mockJsonKey\":\"mockJsonValue\"}");

        SpotifySearchResponse mockResponse = new SpotifySearchResponse();
        SpotifySearchResponse.Artists.Item mockItem = new SpotifySearchResponse.Artists.Item();
        mockItem.setName("mock-item-name");
        SpotifySearchResponse.Artists mockArtist = new SpotifySearchResponse.Artists();
        mockArtist.setItems(new ArrayList<>(List.of(mockItem)));
        mockResponse.setArtists(mockArtist);

        when(objectMapper.readValue(
                "{\"mockJsonKey\":\"mockJsonValue\"}",
                SpotifySearchResponse.class
        )).thenReturn(mockResponse);

        SpotifySearchResponse.Artists.Item response = searchService.searchArist("test-user-id", "test-search-query");

        assertEquals("mock-item-name", response.getName());
    }

    @Test
    void testSearchAristFailureUserTokenException() throws UserTokenException, SpotifyAPIException {
        doThrow(UserTokenException.class).when(spotifyAccessUtil).sendGetRequest(
                anyString(),
                anyString()
        );

        assertThrows(UserTokenException.class, () -> {
            searchService.searchArist("test-user-id", "test-search-query");
        });
    }

    @Test
    void testSearchAristFailureSpotifyAPIException() throws UserTokenException, SpotifyAPIException {
        doThrow(SpotifyAPIException.class).when(spotifyAccessUtil).sendGetRequest(
                anyString(),
                anyString()
        );

        assertThrows(SpotifyAPIException.class, () -> {
            searchService.searchArist("test-user-id", "test-search-query");
        });
    }

    @Test
    void testSearchAristFailureJsonProcessingException() throws JsonProcessingException, UserTokenException, SpotifyAPIException {
        when(spotifyAccessUtil.sendGetRequest(
                "test-user-id",
                "search?q=test-search-query&type=artist"
        )).thenReturn("{\"mockJsonKey\":\"mockJsonValue\"}");

        doThrow(JsonProcessingException.class).when(objectMapper).readValue(
                anyString(),
                eq(SpotifySearchResponse.class)
        );

        assertThrows(JsonProcessingException.class, () -> {
            searchService.searchArist("test-user-id", "test-search-query");
        });
    }
}
