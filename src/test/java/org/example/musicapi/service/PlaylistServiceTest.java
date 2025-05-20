package org.example.musicapi.service;

import org.example.musicapi.model.Playlist;
import org.example.musicapi.repository.PlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @InjectMocks
    private PlaylistService playlistService;

    private Playlist samplePlaylist;

    @BeforeEach
    void setup() {
        samplePlaylist = new Playlist();
        samplePlaylist.setId(1);
        samplePlaylist.setPlaylistname("Test Playlist");
        // weitere Felder setzen, falls nötig
    }

    // -------- POSITIV --------
    @Test
    void create_ShouldReturnSavedPlaylist() {
        when(playlistRepository.save(any(Playlist.class))).thenReturn(samplePlaylist);

        Playlist result = playlistService.create(samplePlaylist);

        assertNotNull(result);
        assertEquals("Test Playlist", result.getPlaylistname());

        // Verifizieren, dass save genau das samplePlaylist Objekt gespeichert hat
        verify(playlistRepository, times(1)).save(samplePlaylist);
    }

    @Test
    void findById_WhenPlaylistFound_ShouldReturnPlaylist() {
        when(playlistRepository.findById(1)).thenReturn(Optional.of(samplePlaylist));

        Optional<Playlist> result = playlistService.findById(1);

        assertTrue(result.isPresent());
        assertEquals("Test Playlist", result.get().getPlaylistname());
        verify(playlistRepository, times(1)).findById(1);
    }

    // -------- NEGATIV --------
    @Test
    void findById_WhenPlaylistNotFound_ShouldReturnEmptyOptional() {
        when(playlistRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Playlist> result = playlistService.findById(1);

        assertTrue(result.isEmpty());
        verify(playlistRepository, times(1)).findById(1);
    }
}
