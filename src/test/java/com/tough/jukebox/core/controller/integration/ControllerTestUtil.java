package com.tough.jukebox.core.controller.integration;

import com.tough.jukebox.core.security.JwtUtil;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ControllerTestUtil {

    public static void setupJwtAuthenticationFilterMock(JwtUtil jwtUtil) throws NoSuchAlgorithmException, InvalidKeySpecException {
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
        when(jwtUtil.getUserIdFromToken("mock-jwt-value")).thenReturn("test-user-id");
    }
}