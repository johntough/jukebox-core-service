package com.tough.jukebox.core.controller.integration;

import com.tough.jukebox.core.controller.PlayController;
import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.security.JwtUtil;
import com.tough.jukebox.core.service.PlayService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayController.class)
class PlayControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    JwtUtil jwtUtil;

    @MockitoBean
    PlayService playService;

    @Test
    void testPlaySuccess() throws Exception {
        ControllerTestUtil.setupJwtAuthenticationFilterMock(jwtUtil);

        doNothing().when(playService).executePlayRequest(
                "test-user-id",
                "test-device-id",
                "test-play-context-uri");

        mockMvc.perform(get("/artist/play")
                        .param("deviceId", "test-device-id")
                        .param("playContextUri", "test-play-context-uri")
                        .cookie(new Cookie("jwt", "mock-jwt-value")))
                .andExpect(status().isOk());

        verify(playService, times(1)).executePlayRequest(
                "test-user-id",
                "test-device-id",
                "test-play-context-uri"
        );
    }

    @Test
    void testPlayFailureNoJwt401() throws Exception {
        mockMvc.perform(get("/artist/play"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testPlayFailureUserTokenException401() throws Exception {
        ControllerTestUtil.setupJwtAuthenticationFilterMock(jwtUtil);

        doThrow(UserTokenException.class).when(playService).executePlayRequest(anyString(), anyString(), anyString());

        mockMvc.perform(get("/artist/play")
                        .param("deviceId", "test-device-id")
                        .param("playContextUri", "test-play-context-uri")
                        .cookie(new Cookie("jwt", "mock-jwt-value")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testPlayFailureSpotifyAPIException500() throws Exception {
        ControllerTestUtil.setupJwtAuthenticationFilterMock(jwtUtil);

        doThrow(SpotifyAPIException.class).when(playService).executePlayRequest(anyString(), anyString(), anyString());

        mockMvc.perform(get("/artist/play")
                        .param("deviceId", "test-device-id")
                        .param("playContextUri", "test-play-context-uri")
                        .cookie(new Cookie("jwt", "mock-jwt-value")))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testPauseSuccess() throws Exception {
        ControllerTestUtil.setupJwtAuthenticationFilterMock(jwtUtil);

        doNothing().when(playService).executePauseRequest(
                "test-user-id",
                "test-device-id"
        );

        mockMvc.perform(get("/player/pause")
                        .param("deviceId", "test-device-id")
                        .cookie(new Cookie("jwt", "mock-jwt-value")))
                .andExpect(status().isOk());

        verify(playService, times(1)).executePauseRequest(
                "test-user-id",
                "test-device-id"
        );
    }

    @Test
    void testPauseFailureNoJwt401() throws Exception {
        mockMvc.perform(get("/player/pause")
                        .param("deviceId", "test-device-id"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testPauseFailureUserTokenException401() throws Exception {
        ControllerTestUtil.setupJwtAuthenticationFilterMock(jwtUtil);

        doThrow(UserTokenException.class).when(playService).executePauseRequest(anyString(), anyString());

        mockMvc.perform(get("/player/pause")
                        .param("deviceId", "test-device-id")
                        .cookie(new Cookie("jwt", "mock-jwt-value")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testPauseFailureSpotifyAPIException500() throws Exception {
        ControllerTestUtil.setupJwtAuthenticationFilterMock(jwtUtil);

        doThrow(SpotifyAPIException.class).when(playService).executePauseRequest(anyString(), anyString());

        mockMvc.perform(get("/player/pause")
                        .param("deviceId", "test-device-id")
                        .cookie(new Cookie("jwt", "mock-jwt-value")))
                .andExpect(status().isInternalServerError());
    }
}
