package com.tough.jukebox.core.controller;

import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.service.PlayService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayController {

    private static final Logger LOGGER =  LoggerFactory.getLogger(PlayController.class);

    private final PlayService playService;

    @Autowired
    public PlayController(PlayService playService) {
        this.playService = playService;
    }

    @GetMapping("artist/play")
    public ResponseEntity<Void> play(@RequestParam String deviceId, @RequestParam String playContextUri, HttpServletRequest request) {
        LOGGER.info("/artist/play request received");

        try {
            playService.executePlayRequest(
                    (String) request.getAttribute("userId"),
                    deviceId,
                    playContextUri
            );
            return ResponseEntity.ok().build();
        } catch (UserTokenException userTokenException) {
            LOGGER.error("Unauthorised (401) when attempting play request: {}", userTokenException.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (SpotifyAPIException spotifyAPIException) {
            LOGGER.error("Internal Server Error (500) when attempting play request: {}", spotifyAPIException.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("player/pause")
    public ResponseEntity<Void> pause(@RequestParam String deviceId, HttpServletRequest request) {
        LOGGER.info("/player/pause request received");

        try {
            playService.executePauseRequest(
                    (String) request.getAttribute("userId"),
                    deviceId
            );
            return ResponseEntity.ok().build();
        } catch (UserTokenException userTokenException) {
            LOGGER.error("Unauthorised (401) when attempting pause request: {}", userTokenException.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (SpotifyAPIException spotifyAPIException) {
            LOGGER.error("Internal Server Error (500) when attempting pause request: {}", spotifyAPIException.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
