package org.example.musicapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String song;

    @NotNull
    @Size(max = 50, message = "Playlistname darf maximal 50 Zeichen lang sein")
    private String playlistname;

    // Erstelldatum muss vor der aktuellen Zeit liegen
    @PastOrPresent(message = "Erstelldatum darf nicht in der Zukunft liegen")
    private LocalDate erstelldatum;


    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Song> songs;
}
