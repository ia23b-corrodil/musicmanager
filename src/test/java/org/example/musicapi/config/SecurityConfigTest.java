package org.example.musicapi.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    // Öffentlicher GET-Endpunkt (ohne Auth)
    @Test
    void publicGetPlaylists_shouldAllowAnonymous() throws Exception {
        mockMvc.perform(get("/api/playlists"))
                .andExpect(status().isOk());
    }

    // POST Endpunkt mit Rolle USER (erlaubt für USER)
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void postPlaylist_asUser_shouldAllow() throws Exception {
        mockMvc.perform(post("/api/playlists")
                        .contentType("application/json")
                        .content("{\"playlistname\": \"Test\", \"song\": \"Song\"}"))
                .andExpect(status().isOk()); // oder 201, je nach Controller
    }

    // POST Endpunkt ohne Anmeldung (nicht erlaubt)
    @Test
    void postPlaylist_unauthenticated_shouldFail() throws Exception {
        mockMvc.perform(post("/api/playlists")
                        .contentType("application/json")
                        .content("{\"playlistname\": \"Test\", \"song\": \"Song\"}"))
                .andExpect(status().isUnauthorized());
    }

    // DELETE Endpunkt für Nicht-Admin
    @Test
    @WithMockUser(roles = {"USER"})
    void deletePlaylist_asUser_shouldFail() throws Exception {
        mockMvc.perform(delete("/api/playlists/1"))
                .andExpect(status().isForbidden());
    }
}
