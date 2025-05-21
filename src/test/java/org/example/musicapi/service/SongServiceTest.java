package org.example.musicapi.service;

import jakarta.validation.Path;
import org.example.musicapi.model.Song;
import org.example.musicapi.repository.SongRepository;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
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

public class SongServiceTest {

    @Mock
    private SongRepository repository;

    @Mock
    private Validator validator;

    @InjectMocks
    private SongService service;

    private Song song;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Beispiel-Song
        song = new Song();
        song.setTitel("Test Titel");
        song.setKuenstler("Test Künstler");
        song.setAlbum("Test Album");
        song.setVeroeffentlichungsdatum(LocalDate.parse("2025-05-20"));
        song.setLaenge(180);
        song.setGenre("Pop");
        song.setBeliebtheit("5");
        // Leere ID, damit beim Erstellen eine UUID generiert wird
        song.setId(null);
        // Rollen in UserContext lassen wir weg, da keine Rollenprüfung im Service vorhanden ist
    }

    @Test
    void testCreate_Success() {
        when(validator.validate(any(Song.class))).thenReturn(Collections.emptySet());
        when(repository.save(any(Song.class))).thenAnswer(invocation -> {
            Song savedSong = invocation.getArgument(0);
            // Simuliere UUID-Generation
            savedSong.setId("generated-uuid");
            return savedSong;
        });

        Song result = service.create(song);

        assertNotNull(result);
        assertEquals("generated-uuid", result.getId()); // UUID sollte gesetzt sein
        verify(repository).save(any(Song.class));
    }


    @Test
    void testUpdate_Success() {
        String songId = "abc123";
        Song updated = new Song();
        updated.setTitel("Updated Titel");
        updated.setKuenstler("Updated Künstler");
        // weitere Felder setzen...

        when(validator.validate(any(Song.class))).thenReturn(Collections.emptySet());
        when(repository.findById(eq(songId))).thenReturn(Optional.of(song));
        when(repository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Song result = service.update(songId, updated);

        assertNotNull(result);
        verify(repository).save(any(Song.class));
    }

    @Test
    void testUpdate_SongNotFound() {
        String songId = "nichtvorhanden";
        Song updated = new Song();
        when(repository.findById(eq(songId))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            service.update(songId, updated);
        });
    }

    // Hilfsmethode zum Mocken eines ConstraintViolations mit korrektem Path-Return
    private ConstraintViolation<Song> mockConstraintViolation(String message, String propertyPath) {
        ConstraintViolation<Song> violation = mock(ConstraintViolation.class);
        Path mockPath = mock(Path.class);
        when(violation.getPropertyPath()).thenReturn(mockPath);
        when(mockPath.toString()).thenReturn(propertyPath);
        when(violation.getMessage()).thenReturn(message);
        return violation;
    }
}
