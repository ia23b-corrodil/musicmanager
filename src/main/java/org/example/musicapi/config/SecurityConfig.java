package org.example.musicapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize

                        // Öffentliche GET-Endpunkte
                        .requestMatchers(HttpMethod.GET, "/api/playlists").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/playlists/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/songs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/songs/**").permitAll()

                        // Authentifizierung erforderlich für Änderungen authenticated
                        .requestMatchers(HttpMethod.POST, "/api/playlists/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/playlists/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/playlists/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/songs/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/songs/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/songs/**").permitAll()

                        // Alle anderen Anfragen zulassen
                        .anyRequest().permitAll()
                )
                .httpBasic(withDefaults());
        return http.build();
    }
}