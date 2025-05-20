package org.example.musicapi.controller;

import org.example.musicapi.model.Playlist;
import org.example.musicapi.service.PlaylistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaylistControllerTest {

    @Mock
    private PlaylistService service;

    @InjectMocks
    private PlaylistController controller;

    private Playlist playlist;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        playlist = new Playlist();
        playlist.setId(1);
        playlist.setPlaylistname("Test Playlist");
        playlist.setSong("Song 1");
        playlist.setErstelldatum(LocalDate.now());
    }

    @Test
    void addPlaylist_ShouldReturnCreatedPlaylist() {
        when(service.create(any(Playlist.class))).thenReturn(playlist);

        ResponseEntity<?> response = controller.addPlaylist(playlist);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Playlist);
        verify(service).create(playlist);
    }

    @Test
    void getAllPlaylists_ShouldReturnPlaylistList() {
        List<Playlist> playlists = List.of(playlist);
        when(service.findAll()).thenReturn(playlists);

        ResponseEntity<List<Playlist>> response = controller.getAllPlaylists();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(service).findAll();
    }

    @Test
    void getPlaylistById_WhenFound_ShouldReturnPlaylist() {
        when(service.findById(1)).thenReturn(Optional.of(playlist));

        ResponseEntity<?> response = controller.getPlaylistById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Playlist);
        verify(service).findById(1);
    }

    @Test
    void getPlaylistById_WhenNotFound_ShouldReturn404() {
        when(service.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.getPlaylistById(1);

        assertEquals(404, response.getStatusCodeValue());
        verify(service).findById(1);
    }

    @Test
    void updatePlaylist_WhenFound_ShouldReturnUpdated() {
        Playlist updated = new Playlist();
        updated.setPlaylistname("Updated Playlist");
        updated.setSong("New Song");
        updated.setErstelldatum(LocalDate.now());

        when(service.update(eq(1), any(Playlist.class))).thenReturn(updated);

        ResponseEntity<?> response = controller.updatePlaylist(1, updated);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Playlist);
        verify(service).update(1, updated);
    }


    @Test
    void deletePlaylist_ShouldReturnNoContent() {
        doNothing().when(service).deleteById(1);

        ResponseEntity<?> response = controller.deletePlaylist(1);

        assertEquals(204, response.getStatusCodeValue());
        verify(service).deleteById(1);
    }

    @Test
    void deleteAllPlaylists_ShouldReturnNoContent() {
        doNothing().when(service).deleteAll();

        ResponseEntity<?> response = controller.deleteAllPlaylists();

        assertEquals(204, response.getStatusCodeValue());
        verify(service).deleteAll();
    }
}
