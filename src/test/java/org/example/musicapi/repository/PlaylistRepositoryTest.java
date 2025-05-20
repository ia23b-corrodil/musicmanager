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

    @Test
    void saveAndFindById_ShouldReturnPlaylist() {
        Playlist playlist = new Playlist();
        playlist.setPlaylistname("Test Playlist");

        Playlist saved = playlistRepository.save(playlist);

        Optional<Playlist> found = playlistRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Test Playlist", found.get().getPlaylistname());
    }}