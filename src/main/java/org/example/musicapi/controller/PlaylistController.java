package org.example.musicapi.controller;

import org.example.musicapi.model.Playlist;
import org.example.musicapi.service.PlaylistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private static final Logger logger = LoggerFactory.getLogger(PlaylistController.class);

    private final PlaylistService service;

    public PlaylistController(PlaylistService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> addPlaylist(@RequestBody Playlist playlist) {
        try {
            logger.info("Request to add new playlist: {}", playlist);
            Playlist created = service.create(playlist);
            logger.info("Created playlist with id: {}", created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            logger.error("Fehler beim Erstellen der Playlist", e);
            return ResponseEntity.status(500).body("Fehler beim Erstellen der Playlist");
        }
    }

    @GetMapping
    public ResponseEntity<List<Playlist>> getAllPlaylists() {
        logger.info("Request to get all playlists");
        List<Playlist> playlists = service.findAll();
        logger.info("Returning {} playlists", playlists.size());
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlaylistById(@PathVariable Integer id) {
        logger.info("Request to get playlist with id: {}", id);
        try {
            return service.findById(id)
                    .<ResponseEntity<?>>map(playlist -> {
                        logger.info("Found playlist: {}", playlist);
                        return ResponseEntity.ok(playlist);
                    })
                    .orElseGet(() -> {
                        logger.warn("Playlist with id {} not found", id);
                        return ResponseEntity.status(404).body("Playlist nicht gefunden");
                    });
        } catch (Exception e) {
            logger.error("Fehler beim Abrufen der Playlist", e);
            return ResponseEntity.status(500).body("Fehler beim Abrufen der Playlist");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlaylist(@PathVariable Integer id, @RequestBody Playlist playlist) {
        try {
            logger.info("Request to update playlist with id: {} with data: {}", id, playlist);
            Playlist updated = service.update(id, playlist);
            logger.info("Updated playlist: {}", updated);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            logger.warn("Playlist mit id {} nicht gefunden zum Updaten", id);
            return ResponseEntity.status(404).body("Playlist nicht gefunden");
        } catch (Exception e) {
            logger.error("Fehler beim Aktualisieren der Playlist!", e);
            return ResponseEntity.status(500).body("Fehler beim Aktualisieren der Playlist");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Integer id) {
        try {
            logger.info("Request to delete playlist with id: {}", id);
            service.deleteById(id);
            logger.info("Deleted playlist with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            logger.warn("Playlist mit id {} nicht gefunden zum Löschen", id);
            return ResponseEntity.status(404).body("Playlist nicht gefunden");
        } catch (Exception e) {
            logger.error("Fehler beim Löschen der Playlist", e);
            return ResponseEntity.status(500).body("Fehler beim Löschen der Playlist");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllPlaylists() {
        try {
            logger.info("Request to delete all playlists");
            service.deleteAll();
            logger.info("Deleted all playlists");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Fehler beim Löschen aller Playlists", e);
            return ResponseEntity.status(500).body("Fehler beim Löschen aller Playlists");
        }
    }
}
