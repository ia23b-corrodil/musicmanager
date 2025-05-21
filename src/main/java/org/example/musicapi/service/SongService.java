package org.example.musicapi.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.example.musicapi.model.Song;
import org.example.musicapi.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class SongService {

    private final SongRepository repository;
    private final Validator validator;

    public SongService(SongRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public Song create(Song song) {
        validate(song);
        // Falls keine ID gesetzt wurde, generieren wir eine
        if (song.getId() == null || song.getId().isEmpty()) {
            song.setId(UUID.randomUUID().toString());
        }
        return repository.save(song);
    }

    public List<Song> findAll() {
        return repository.findAll();
    }

    public Optional<Song> findById(String id) {
        return repository.findById(id);
    }

    public Song update(String id, Song updated) {
        validate(updated);
        return repository.findById(id)
                .map(existing -> {
                    existing.setSong(updated.getSong());
                    existing.setTitel(updated.getTitel());
                    existing.setKuenstler(updated.getKuenstler());
                    existing.setAlbum(updated.getAlbum());
                    existing.setVeroeffentlichungsdatum(updated.getVeroeffentlichungsdatum());
                    existing.setLaenge(updated.getLaenge());
                    existing.setGenre(updated.getGenre());
                    existing.setBeliebtheit(updated.getBeliebtheit());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Song with ID " + id + " not found"));
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public boolean exists(String id) {
        return repository.existsById(id);
    }

    public long count() {
        return repository.count();
    }

    private void validate(Song song) {
        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Song> violation : violations) {
                sb.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            throw new IllegalArgumentException("Validation failed: " + sb.toString());
        }
    }
}