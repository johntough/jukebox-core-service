package com.tough.jukebox.core.controller.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tough.jukebox.core.api.SpotifyDeviceResponse;
import com.tough.jukebox.core.controller.DeviceController;
import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.security.JwtUtil;
import com.tough.jukebox.core.service.DeviceService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DeviceController.class)
class DeviceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    JwtUtil jwtUtil;

    @MockitoBean
    DeviceService deviceService;

    @Test
    void testGetDevicesSuccess() throws Exception {
        ControllerTestUtil.setupJwtAuthenticationFilterMock(jwtUtil);

        SpotifyDeviceResponse mockDevice = new SpotifyDeviceResponse();
        SpotifyDeviceResponse.Device device = new SpotifyDeviceResponse.Device();
        device.setId("mock-id");
        device.setName("mock-name");
        device.setType("mock-type");
        device.setActive(true);
        mockDevice.setDevices(new ArrayList<>(List.of(device)));

        when(deviceService.getDevices("test-user-id")).thenReturn(mockDevice);

        mockMvc.perform(get("/devices")
                .cookie(new Cookie("jwt", "mock-jwt-value")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"devices\":[{\"id\":\"mock-id\",\"name\":\"mock-name\",\"type\":\"mock-type\",\"active\":true}]}"));
    }

    @Test
    void testGetDevicesFailureNoJwt401() throws Exception {
        doThrow(UserTokenException.class).when(deviceService).getDevices("test-user-id");

        mockMvc.perform(get("/devices"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetDevicesFailureUserTokenException401() throws Exception {
        ControllerTestUtil.setupJwtAuthenticationFilterMock(jwtUtil);

        doThrow(UserTokenException.class).when(deviceService).getDevices("test-user-id");

        mockMvc.perform(get("/devices")
                .cookie(new Cookie("jwt", "mock-jwt-value")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetDevicesFailureJsonProcessingException500() throws Exception {
        ControllerTestUtil.setupJwtAuthenticationFilterMock(jwtUtil);

        doThrow(JsonProcessingException.class).when(deviceService).getDevices("test-user-id");

        mockMvc.perform(get("/devices")
                        .cookie(new Cookie("jwt", "mock-jwt-value")))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetDevicesFailureSpotifyAPIException500() throws Exception {
        ControllerTestUtil.setupJwtAuthenticationFilterMock(jwtUtil);

        doThrow(SpotifyAPIException.class).when(deviceService).getDevices("test-user-id");

        mockMvc.perform(get("/devices")
                        .cookie(new Cookie("jwt", "mock-jwt-value")))
                .andExpect(status().isInternalServerError());
    }
}
