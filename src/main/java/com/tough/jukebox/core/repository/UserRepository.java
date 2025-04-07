package com.tough.jukebox.core.repository;

import com.tough.jukebox.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findBySpotifyUserId(String spotifyUserId);
}