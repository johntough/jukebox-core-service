package com.tough.jukebox.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tough.jukebox.core.api.SpotifyDeviceResponse;
import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.service.DeviceService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("devices")
    public ResponseEntity<SpotifyDeviceResponse> getDevices(HttpServletRequest request) {
        try {
            SpotifyDeviceResponse devices = deviceService.getDevices((String) request.getAttribute("userId"));
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(devices);
        } catch (UserTokenException userTokenException) {
            LOGGER.error("Unauthorised (401) when getting devices: {}", userTokenException.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (JsonProcessingException | SpotifyAPIException exception) {
            LOGGER.error("Internal Server Error (500) when getting devices: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}