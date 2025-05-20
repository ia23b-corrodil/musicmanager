package org.example.musicapi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {
    @Test
    void getterSetterTest() {
        Playlist playlist = new Playlist();

        playlist.setId(10);
        playlist.setPlaylistname("My Playlist");

        assertEquals(10, playlist.getId());
        assertEquals("My Playlist", playlist.getPlaylistname());
    }
}