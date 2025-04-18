package com.tough.jukebox.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tough.jukebox.core.api.SpotifySearchResponse;
import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    private static final Logger LOGGER =  LoggerFactory.getLogger(SearchController.class);

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("artist/search")
    public ResponseEntity<SpotifySearchResponse.Artists.Item> artistSearch(@RequestParam String searchQuery, HttpServletRequest request) {
        LOGGER.info("/artist/search request received");
        try {
            SpotifySearchResponse.Artists.Item artist = searchService.searchArist(
                    (String) request.getAttribute("userId"),
                    searchQuery
            );
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(artist);
        } catch (UserTokenException userTokenException) {
            LOGGER.error("Unauthorised (401) when searching artists: {}", userTokenException.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (JsonProcessingException | SpotifyAPIException exception) {
            LOGGER.error("Internal Server Error (500) when getting devices: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}