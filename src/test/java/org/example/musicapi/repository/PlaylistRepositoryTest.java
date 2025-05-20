package org.example.musicapi.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.example.musicapi.model.Playlist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;


@DataJpaTest
class PlaylistRepositoryTest {

    @Autowired
    private PlaylistRepository playlistRepository;

    public PlaylistRepository getPlaylistRepository() {
        return playlistRepository;
    }

    public void setPlaylistRepository(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

}