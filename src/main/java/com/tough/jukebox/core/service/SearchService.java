package com.tough.jukebox.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tough.jukebox.core.api.SpotifySearchResponse;
import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.util.SpotifyAccessUtil;
import org.springframework.http.HttpMethod;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchService.class);
    private static final String SEARCH_URI = "search?q=";
    private static final String ARTIST_TYPE_SUFFIX = "&type=artist";

    private final SpotifyAccessUtil spotifyAccessUtil;
    private final ObjectMapper objectMapper;

    @Autowired
    public SearchService(SpotifyAccessUtil spotifyAccessUtil, ObjectMapper objectMapper) {
        this.spotifyAccessUtil = spotifyAccessUtil;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public String searchArist(String userId, String query) throws UserTokenException, SpotifyAPIException, JsonProcessingException {

        String responseBody = spotifyAccessUtil.sendRequest(userId, HttpMethod.GET, SEARCH_URI + query + ARTIST_TYPE_SUFFIX);
        SpotifySearchResponse parsedResponse = objectMapper.readValue(responseBody, SpotifySearchResponse.class);
        String artistImageUri = parsedResponse.getArtists().getItems().get(0).getImages().get(0).getUrl();

        LOGGER.info("Search artist result: {}", artistImageUri);

        return artistImageUri;
    }
}
