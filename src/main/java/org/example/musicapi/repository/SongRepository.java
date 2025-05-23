package org.example.musicapi.repository;

import org.example.musicapi.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, String> {
    // Standardmethoden werden von JpaRepository bereitgestellt
}