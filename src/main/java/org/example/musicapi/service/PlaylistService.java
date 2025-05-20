package org.example.musicapi.service;

import org.example.musicapi.model.Playlist;
import org.example.musicapi.repository.PlaylistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {

    private final PlaylistRepository repository;

    public PlaylistService(PlaylistRepository repository) {
        this.repository = repository;
    }

    public Playlist create(Playlist playlist) {
        return repository.save(playlist);
    }

    public List<Playlist> findAll() {
        return repository.findAll();
    }

    public Optional<Playlist> findById(Integer id) {
        return repository.findById(id);
    }

    public Playlist update(Integer id, Playlist updated) {
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
}
