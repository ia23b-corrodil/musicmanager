package org.example.musicapi.service;

import jakarta.validation.Validator;
import org.example.musicapi.model.Playlist;
import org.example.musicapi.repository.PlaylistRepository;
import org.example.musicapi.security.Role;
import org.example.musicapi.security.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlaylistServiceTest {

    @Mock
    private PlaylistRepository repository;

    @Mock
    private Validator validator;

    @InjectMocks
    private PlaylistService service;

    private Playlist playlist;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Beispiel-Playlist
        playlist = new Playlist();
        playlist.setPlaylistname("Test Playlist");
        playlist.setSong("Test Song");
        playlist.setErstelldatum(LocalDate.parse("2025-05-20"));

        // Role im UserContext setzen
        UserContext.setCurrentRole(Role.USER);
    }

    // Rolle nach Test beenden (optional, falls in Zukunft mehrere Tests laufen)
    // @AfterEach
    // void tearDown() {
    //     UserContext.clear();
    // }

    @Test
    void testCreate_Success() {
        when(validator.validate(any(Playlist.class))).thenReturn(Collections.emptySet());
        when(repository.save(any(Playlist.class))).thenReturn(playlist);

        Playlist result = service.create(playlist);

        assertNotNull(result);
        verify(repository).save(playlist);
    }

    @Test
    void testUpdate_AsAdmin_Success() {
        UserContext.setCurrentRole(Role.ADMIN);
        when(validator.validate(any(Playlist.class))).thenReturn(Collections.emptySet());
        when(repository.findById(anyInt())).thenReturn(Optional.of(playlist));
        when(repository.save(any(Playlist.class))).thenReturn(playlist);

        Playlist updatedPlaylist = new Playlist();
        updatedPlaylist.setPlaylistname("New Name");
        updatedPlaylist.setSong("New Song");
        updatedPlaylist.setErstelldatum(LocalDate.parse("2025-05-20"));

        Playlist result = service.update(1, updatedPlaylist);

        assertNotNull(result);
        verify(repository).save(any(Playlist.class));
        UserContext.setCurrentRole(Role.USER); // wieder auf USER setzen, falls nötig
    }

    @Test
    void testUpdate_WithoutAdmin_ZugriffVerweigert() {
        UserContext.setCurrentRole(Role.USER);
        assertThrows(SecurityException.class, () -> {
            service.update(1, playlist);
        });
    }

    @Test
    void testDeleteById_AsAdmin() {
        UserContext.setCurrentRole(Role.ADMIN);
        doNothing().when(repository).deleteById(anyInt());

        service.deleteById(1);

        verify(repository).deleteById(1);
        UserContext.setCurrentRole(Role.USER); // Rolle wieder zurücksetzen
    }

    @Test
    void testDeleteById_AufNichtAdmin_ZugriffVerweigert() {
        UserContext.setCurrentRole(Role.USER);
        assertThrows(SecurityException.class, () -> {
            service.deleteById(1);
        });
    }
}
