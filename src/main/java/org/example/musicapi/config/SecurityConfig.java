package org.example.musicapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // In-Memory Benutzer erstellen
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("password"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Swagger-OpenAPI Endpunkte - öffentlich
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Öffentliche GET-Endpunkte
                        .requestMatchers(HttpMethod.GET, "/api/playlists").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/playlists/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/songs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/songs/**").permitAll()

                        // Playlist und Song erstellen - nur USER und ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/playlists").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/songs").hasAnyRole("USER", "ADMIN")

                        // Aktualisieren - nur ADMIN
                        .requestMatchers(HttpMethod.PUT, "/api/playlists/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/songs/**").hasRole("ADMIN")

                        // Löschen - nur ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/api/playlists/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/songs/**").hasRole("ADMIN")

                        // Alle anderen Anfragen benötigen Authentifizierung
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();
    }
}