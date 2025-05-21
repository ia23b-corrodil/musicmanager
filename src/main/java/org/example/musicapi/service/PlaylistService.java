package org.example.musicapi.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.example.musicapi.model.Playlist;
import org.example.musicapi.repository.PlaylistRepository;
import org.example.musicapi.security.Role;
import org.example.musicapi.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PlaylistService {

    private final PlaylistRepository repository;
    private final Validator validator;

    public PlaylistService(PlaylistRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public Playlist create(Playlist playlist) {
        requireRole(Role.USER);
        validate(playlist);
        return repository.save(playlist);
    }

    public List<Playlist> findAll() {
        return repository.findAll(); // Alle dürfen lesen
    }

    public Optional<Playlist> findById(Integer id) {
        return repository.findById(id); // Alle dürfen lesen
    }

    public Playlist update(Integer id, Playlist updated) {
        requireRole(Role.ADMIN);
        validate(updated);
        return repository.findById(id)
                .map(existing -> {
                    existing.setSong(updated.getSong());
                    existing.setPlaylistname(updated.getPlaylistname());
                    existing.setErstelldatum(updated.getErstelldatum());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Playlist with ID " + id + " not found"));
    }


    public void deleteById(Integer id) {
        requireRole(Role.ADMIN);
        repository.deleteById(id);
    }

    public void deleteAll() {
        requireRole(Role.ADMIN);
        repository.deleteAll();
    }

    public boolean exists(Integer id) {
        return repository.existsById(id); // Alle dürfen prüfen
    }

    public long count() {
        return repository.count(); // Alle dürfen zählen
    }

    private void validate(Playlist playlist) {
        Set<ConstraintViolation<Playlist>> violations = validator.validate(playlist);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Playlist> violation : violations) {
                sb.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            throw new IllegalArgumentException("Validation failed: " + sb.toString());
        }
    }


    private void requireRole(Role requiredRole) {
        Role current = UserContext.getCurrentRole();
        if (current == null || !current.equals(requiredRole)) {
            if (requiredRole == Role.USER && current == Role.ADMIN) {
                return;
            }
            throw new SecurityException("Zugriff verweigert für Rolle: " + current);
        }
    }

}
