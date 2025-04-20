package com.tough.jukebox.core.respository.integration;

import com.tough.jukebox.core.model.User;
import com.tough.jukebox.core.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void testFindBySpotifyUserId() {
        User user = new User();
        user.setSpotifyUserId("testSpotifyUserId");
        user.setEmailAddress("test@email.address");
        user.setDisplayName("testDisplayName");
        userRepository.save(user);

        Optional<User> returnedUser = userRepository.findBySpotifyUserId("testSpotifyUserId");
        assertTrue(returnedUser.isPresent());
        assertEquals("testSpotifyUserId", returnedUser.get().getSpotifyUserId());
        assertEquals("test@email.address", returnedUser.get().getEmailAddress());
        assertEquals("testDisplayName", returnedUser.get().getDisplayName());
    }

    @Test
    void testFindBySpotifyUserIdNoUserExists() {
        Optional<User> user = userRepository.findBySpotifyUserId("testSpotifyUserId");
        assertTrue(user.isEmpty());
    }
}