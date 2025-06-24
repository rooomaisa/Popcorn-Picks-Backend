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

                .cors(cors -> cors.configurationSource(corsSource))


                .csrf(AbstractHttpConfigurer::disable)


                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )


                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()


                        .requestMatchers(HttpMethod.GET, "/api/v1/movies/**").permitAll()


                        .requestMatchers(HttpMethod.POST, "/api/v1/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/api/v1/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/movies/**").hasRole("ADMIN")


                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/**").permitAll()


                        .requestMatchers(HttpMethod.POST,   "/api/v1/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.PUT,    "/api/v1/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/reviews/**").authenticated()

                        .requestMatchers(HttpMethod.POST,   "/api/v1/users/*/watchlist").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/*/watchlist/**").authenticated()
                        .requestMatchers(HttpMethod.GET,    "/api/v1/users/*/watchlist").authenticated()

                        .requestMatchers(HttpMethod.POST,   "/api/v1/files/upload").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/api/v1/files/**").authenticated()



                        .anyRequest().authenticated()
                )


                .addFilterBefore(jwtRequestFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
