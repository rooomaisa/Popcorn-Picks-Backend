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

        // ──────────────────────────────────────────────────────────────────────────
        // 1) Skip JWT logic entirely for /api/v1/auth/** (register & login):
        if (path.startsWith("/api/v1/auth/")) {
            chain.doFilter(request, response);
            return;
        }
        // ──────────────────────────────────────────────────────────────────────────

        // 2) Otherwise, extract the Bearer token (if present):
        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // strip off “Bearer ”
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception ex) {
                // invalid JWT → proceed without setting auth
            }
        }

        // 3) If we got a username and no one’s authenticated yet, validate & set Authentication:
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 4) Continue the filter chain
        chain.doFilter(request, response);
    }
}
