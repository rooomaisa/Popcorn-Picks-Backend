package com.popcornpicks.filter;

import com.popcornpicks.service.CustomUserDetailsService;
import com.popcornpicks.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        String path = request.getServletPath();
        System.out.println(">>> JwtRequestFilter.doFilterInternal() on path = " + path);


        if (path.startsWith("/api/v1/auth/")) {
            chain.doFilter(request, response);
            return;
        }


        final String authHeader = request.getHeader("Authorization");
        System.out.println(">>> JwtRequestFilter Authorization header = " + authHeader);

        String jwt = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                System.out.println(">>> JwtRequestFilter extractUsername(jwt) = " + username);
            } catch (Exception ex) {
                System.out.println(">>> JwtRequestFilter: invalid JWT, skipping auth");
            }
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            boolean valid = jwtUtil.validateToken(jwt, userDetails);
            System.out.println(">>> JwtRequestFilter validateToken returned = " + valid);
            if (valid) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println(">>> JwtRequestFilter Authentication granted for " + username);
            } else {
                System.out.println(">>> JwtRequestFilter: token invalid or expired");
            }
        } else if (username == null) {
            System.out.println(">>> JwtRequestFilter: no Bearer token found, leaving unauthenticated");
        }


        chain.doFilter(request, response);
    }
}


