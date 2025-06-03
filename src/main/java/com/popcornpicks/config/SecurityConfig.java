
package com.popcornpicks.config;

import com.popcornpicks.filter.JwtRequestFilter;
import com.popcornpicks.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final CorsConfigurationSource corsSource;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(
            CorsConfigurationSource corsSource,
            CustomUserDetailsService userDetailsService
    ) {
        this.corsSource = corsSource;
        this.userDetailsService = userDetailsService;
    }

    // 1) Expose a BCryptPasswordEncoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2) Expose the AuthenticationManager from AuthenticationConfiguration
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 3) Create a DaoAuthenticationProvider that uses our CustomUserDetailsService + BCrypt
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // 4) Define the SecurityFilterChain, registering the DaoAuthenticationProvider
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtRequestFilter jwtRequestFilter
    ) throws Exception {
        http
                // Register our custom DaoAuthenticationProvider here:
                .authenticationProvider(daoAuthenticationProvider())

                // Enable CORS
                .cors(cors -> cors.configurationSource(corsSource))

                // Disable CSRF because we are stateless
                .csrf(AbstractHttpConfigurer::disable)

                // No session—every request must be authenticated by JWT or HTTP Basic
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // URL‐based authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Unauthenticated users may register or log in:
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()

                        // Public endpoints:
                        .requestMatchers(HttpMethod.GET, "/api/v1/movies/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/**").permitAll()

                        // Only ADMIN may create/update/delete movies:
                        .requestMatchers(HttpMethod.POST,   "/api/v1/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/v1/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/movies/**").hasRole("ADMIN")

                        // Authenticated users may post/update/delete reviews and watchlists:
                        .requestMatchers(HttpMethod.POST,   "/api/v1/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.PUT,    "/api/v1/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.POST,   "/api/v1/users/*/watchlist").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/*/watchlist/**").authenticated()
                        .requestMatchers(HttpMethod.GET,    "/api/v1/users/*/watchlist").authenticated()

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )

                // Insert our JWT filter before Spring’s UsernamePasswordAuthenticationFilter:
                .addFilterBefore(jwtRequestFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)

                // We will use HTTP Basic to allow testing of /login (it will be overridden once JWT is issued)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}
