package com.nucleus.floracestore.jwt;


import com.nucleus.floracestore.repository.TokenRepository;
import com.nucleus.floracestore.service.impl.JwtTokenProvider;
import com.nucleus.floracestore.service.impl.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@Component
@NoArgsConstructor

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private  JwtTokenProvider jwtTokenProvider;
    @Autowired
    private  MyUserDetailsService userDetailsService;
    @Autowired
    private  TokenRepository tokenRepository;



    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (request.getServletPath().contains("/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        if(authHeader != null) {
            log.info("Start with Bearer: " + authHeader.startsWith("Bearer "));
        }
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("Authentication header " + authHeader);
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        username = jwtTokenProvider.extractUsername(jwt);
        log.info("Token user: " + username);
        var isTokenValide = tokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
        if(username != null) {

            log.info("Token valid: " + isTokenValide);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.info("Security context: " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

            log.info("UserDetails get auth: " + userDetails.getAuthorities());

            log.info("Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        } else {
            log.info("Username is null: " + username);

        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            log.info("Token valid: " + isTokenValid);
            if (jwtTokenProvider.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                log.info("Token valid: " + isTokenValid);


                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
