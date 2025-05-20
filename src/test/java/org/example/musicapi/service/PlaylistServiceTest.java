import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.example.musicapi.model.Playlist;
import org.example.musicapi.repository.PlaylistRepository;
import org.example.musicapi.service.PlaylistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private PlaylistService playlistService;

    private Playlist samplePlaylist;

    @BeforeEach
    void setup() {
        samplePlaylist = new Playlist();
        samplePlaylist.setId(1);
        samplePlaylist.setPlaylistname("Test Playlist");

        // Standardmäßig: keine Validierungsfehler
        when(validator.validate(any(Playlist.class))).thenReturn(Collections.emptySet());
    }

    @Test
    void create_ShouldReturnSavedPlaylist() {
        when(playlistRepository.save(any(Playlist.class))).thenReturn(samplePlaylist);

        Playlist result = playlistService.create(samplePlaylist);

        assertNotNull(result);
        assertEquals("Test Playlist", result.getPlaylistname());

        verify(validator).validate(samplePlaylist);
        verify(playlistRepository).save(samplePlaylist);
    }

    @Test
    void create_WithValidationError_ShouldThrowException() {
        ConstraintViolation<Playlist> violation = mock(ConstraintViolation.class);
        Path mockPath = mock(Path.class);

        when(violation.getPropertyPath()).thenReturn(mockPath);
        when(mockPath.toString()).thenReturn("playlistname");
        when(violation.getMessage()).thenReturn("darf nicht leer sein");

        when(validator.validate(any(Playlist.class))).thenReturn(Set.of(violation));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> playlistService.create(samplePlaylist));

        assertTrue(exception.getMessage().contains("playlistname"));
        assertTrue(exception.getMessage().contains("darf nicht leer sein"));

        verify(validator).validate(samplePlaylist);
        verify(playlistRepository, never()).save(any());
    }
}
