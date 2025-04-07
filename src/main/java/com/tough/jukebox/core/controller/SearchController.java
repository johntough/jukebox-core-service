package com.tough.jukebox.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    private final static Logger LOGGER =  LoggerFactory.getLogger(SearchController.class);

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("test")
    public ResponseEntity<String> test(HttpServletRequest request) {
        LOGGER.info("/test request received");

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"success\": true}");
    }

    @GetMapping("artist/search")
    public ResponseEntity<String> artistSearch(@RequestParam String searchQuery, HttpServletRequest request) throws UserTokenException, SpotifyAPIException, JsonProcessingException {

        String imageUri = searchService.searchArist(
                (String) request.getAttribute("userId"),
                searchQuery
        );

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"imageUri\":\"" + imageUri + "\"}");
    }
}
