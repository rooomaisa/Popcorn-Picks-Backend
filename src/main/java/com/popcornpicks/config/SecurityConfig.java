package com.popcornpicks.config;

import com.popcornpicks.filter.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final CorsConfigurationSource corsSource;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(CorsConfigurationSource corsSource,
                          JwtRequestFilter jwtRequestFilter) {
        this.corsSource = corsSource;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // Expose the AuthenticationManager so AuthController can call authenticate(...)
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1) Enable CORS from your CorsConfigurationSource bean
                .cors(cors -> cors.configurationSource(corsSource))

                // 2) Disable CSRF (we’re stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // 3) No session: every request must carry its own auth token
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4) Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // permit register & login under /api/v1/auth/**
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()

                        // anyone can read movies
                        .requestMatchers(HttpMethod.GET, "/api/v1/movies/**").permitAll()

                        // only ADMIN may POST /api/v1/movies/**
                        .requestMatchers(HttpMethod.POST, "/api/v1/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/api/v1/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/movies/**").hasRole("ADMIN")

                        // anyone can GET reviews
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/**").permitAll()

                        // authenticated users can POST/PUT/DELETE reviews & watchlist
                        .requestMatchers(HttpMethod.POST,   "/api/v1/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.PUT,    "/api/v1/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/reviews/**").authenticated()

                        .requestMatchers(HttpMethod.POST,   "/api/v1/users/*/watchlist").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/*/watchlist/**").authenticated()
                        .requestMatchers(HttpMethod.GET,    "/api/v1/users/*/watchlist").authenticated()

                        .requestMatchers(HttpMethod.POST,   "/api/v1/files/upload").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/v1/files/**").authenticated()


                        // everything else requires authentication
                        .anyRequest().authenticated()
                )

                // 5) Insert our JWT filter *before* Spring’s UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtRequestFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
