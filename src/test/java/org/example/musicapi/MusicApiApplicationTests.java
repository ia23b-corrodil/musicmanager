package org.example.musicapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootTest
public class MusicApiApplicationTests {

    @Autowired
    private MusicApiApplication application;

    @Test
    void contextLoads() {
        // Der Test besteht darin, dass der Spring-Kontext erfolgreich geladen wird
        assertNotNull(application);
    }
}
