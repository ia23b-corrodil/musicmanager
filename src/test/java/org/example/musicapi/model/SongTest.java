package org.example.musicapi.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SongTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Song createValidSong() {
        Song song = new Song();
        song.setSong("Valid Song");
        song.setTitel("Titel");
        song.setKuenstler("Artist");
        song.setAlbum("Album");
        song.setVeroeffentlichungsdatum(LocalDate.of(2020, 1, 1));
        song.setLaenge(120);
        song.setGenre("Pop");
        song.setBeliebtheit("mittel");
        return song;
    }

    @Test
    void testVeroeffentlichungsdatumInZukunft() {
        Song song = createValidSong();
        song.setVeroeffentlichungsdatum(LocalDate.of(2100, 1, 1)); // Zukunft

        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));

        boolean found = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("veroeffentlichungsdatum"));
        assertTrue(found, "Sollte Fehler wegen Past-Annotation geben");
    }

    @Test
    void testBeliebtheitUngueltig() {
        Song song = createValidSong();
        song.setBeliebtheit("sehr hoch"); // ungültig

        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));

        boolean found = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("beliebtheit"));
        assertTrue(found, "Sollte Pattern-Fehler für beliebtheit geben");
    }

    @Test
    void testLaengeZuKurz() {
        Song song = createValidSong();
        song.setLaenge(10); // kleiner als 30

        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));

        boolean found = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("laenge"));
        assertTrue(found, "Sollte Min-Fehler für laenge geben");
    }

    @Test
    void testBlankKuenstler() {
        Song song = createValidSong();
        song.setKuenstler(""); // leerer Künstler

        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));

        boolean found = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("kuenstler"));
        assertTrue(found, "Sollte NotBlank Fehler für kuenstler geben");
    }

    @Test
    void testSongMitSonderzeichen() {
        Song song = createValidSong();
        song.setSong("!@#Invalid123"); // enthält unerlaubte Zeichen

        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        violations.forEach(v -> System.out.println(v.getPropertyPath() + ": " + v.getMessage()));

        boolean found = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("song"));
        assertTrue(found, "Sollte Pattern-Fehler für song geben");
    }
}
