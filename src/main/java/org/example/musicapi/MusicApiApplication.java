package org.example.musicapi;

import org.example.musicapi.service.PlaylistService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MusicApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MusicApiApplication.class, args);
    }
}
