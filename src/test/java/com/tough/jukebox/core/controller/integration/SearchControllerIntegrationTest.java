package com.tough.jukebox.core.controller.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tough.jukebox.core.api.SpotifySearchResponse;
import com.tough.jukebox.core.controller.SearchController;
import com.tough.jukebox.core.exception.SpotifyAPIException;
import com.tough.jukebox.core.exception.UserTokenException;
import com.tough.jukebox.core.security.JwtUtil;
import com.tough.jukebox.core.service.SearchService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SearchController.class)
class SearchControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtUtil jwtUtil;

    @MockitoBean
    SearchService searchService;

    @Test
    void testArtistSearchSuccess() throws Exception {
        ControllerTestUtil.setupJwtAuthenticationFilterMock(jwtUtil);

        when(searchService.searchArist(
                "test-user-id",
                "test-query"
        )).thenReturn(setupMockArtist());

        mockMvc.perform(get("/artist/search")
                        .param("searchQuery", "test-query")
                        .cookie(new Cookie("jwt", "mock-jwt-value")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":\"mock-id\",\"name\":\"mock-name\",\"followers\":{\"total\":100000},\"genres\":[\"mock-genre\"],\"images\":[{\"url\":\"mock-url\",\"height\":100,\"width\":100}],\"externalUrls\":{\"spotify\":\"mock-external-url\"},\"popularity\":1}"));
    }

    @Test
    void testArtistSearchFailureNoJwt() throws Exception {
        mockMvc.perform(get("/artist/search"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testArtistSearchFailureUserTokenException401() throws Exception {
        ControllerTestUtil.setupJwtAuthenticationFilterMock(jwtUtil);

        doThrow(UserTokenException.class).when(searchService).searchArist(anyString(), anyString());

        mockMvc.perform(get("/artist/search")
                        .param("searchQuery", "test-query")
                        .cookie(new Cookie("jwt", "mock-jwt-value")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testArtistSearchFailureJsonProcessingException500() throws Exception {
        ControllerTestUtil.setupJwtAuthenticationFilterMock(jwtUtil);

        doThrow(JsonProcessingException.class).when(searchService).searchArist(anyString(), anyString());

        mockMvc.perform(get("/artist/search")
                        .param("searchQuery", "test-query")
                        .cookie(new Cookie("jwt", "mock-jwt-value")))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testArtistSearchFailureSpotifyAPIException500() throws Exception {
        ControllerTestUtil.setupJwtAuthenticationFilterMock(jwtUtil);

        doThrow(SpotifyAPIException.class).when(searchService).searchArist(anyString(), anyString());

        mockMvc.perform(get("/artist/search")
                        .param("searchQuery", "test-query")
                        .cookie(new Cookie("jwt", "mock-jwt-value")))
                .andExpect(status().isInternalServerError());
    }

    private static SpotifySearchResponse.Artists.Item setupMockArtist() {
        SpotifySearchResponse.Artists.Item mockArtist = new SpotifySearchResponse.Artists.Item();
        mockArtist.setId("mock-id");
        mockArtist.setName("mock-name");
        mockArtist.setPopularity(1);
        mockArtist.setGenres(new ArrayList<>(List.of("mock-genre")));

        SpotifySearchResponse.Artists.Image mockImage = new SpotifySearchResponse.Artists.Image();
        mockImage.setHeight(100);
        mockImage.setWidth(100);
        mockImage.setUrl("mock-url");
        mockArtist.setImages(new ArrayList<>(List.of(mockImage)));

        SpotifySearchResponse.Artists.Followers mockFollowers = new SpotifySearchResponse.Artists.Followers();
        mockFollowers.setTotal(100000);
        mockArtist.setFollowers(mockFollowers);

        SpotifySearchResponse.Artists.ExternalUrls mockExternalUrls = new SpotifySearchResponse.Artists.ExternalUrls();
        mockExternalUrls.setSpotify("mock-external-url");
        mockArtist.setExternalUrls(mockExternalUrls);
        return mockArtist;
    }
}
