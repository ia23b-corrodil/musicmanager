package org.example.musicapi.repository;

import org.example.musicapi.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    // habe nichts drin
    // List<Playlist> findByPlaylistname(String name);
}
