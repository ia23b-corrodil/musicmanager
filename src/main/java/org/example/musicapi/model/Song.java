package org.example.musicapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {

    @Id
    @GeneratedValue(generator = "uuid")
    private String id;

    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Songname darf nur Buchstaben und Leerzeichen enthalten")
    private String song;

    private String titel;

    @NotBlank
    private String kuenstler;

    @NotBlank
    private String album;

    @NotNull
    @Past
    private LocalDate veroeffentlichungsdatum;


    @Min(30)
    private int laenge; // in Sekunden

    private String genre;

    @Pattern(regexp = "hoch|mittel|niedrig")
    private String beliebtheit;



    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;
}
