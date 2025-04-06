package com.tough.jukebox.core.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    private final static Logger LOGGER =  LoggerFactory.getLogger(SearchController.class);

    @GetMapping("test")
    public ResponseEntity<String> test(HttpServletRequest request) {
        LOGGER.info("/test request received");

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"success\": true}");
    }
}
