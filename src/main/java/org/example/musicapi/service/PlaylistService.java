package org.example.musicapi.service;

import org.example.musicapi.model.Playlist;
import org.example.musicapi.repository.PlaylistRepository;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

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
        validate(playlist);
        return repository.save(playlist);
    }

    public List<Playlist> findAll() {
        return repository.findAll();
    }

    public Optional<Playlist> findById(Integer id) {
        return repository.findById(id);
    }

    public Playlist update(Integer id, Playlist updated) {
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
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public boolean exists(Integer id) {
        return repository.existsById(id);
    }

    public long count() {
        return repository.count();
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
}
