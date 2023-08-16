package com.nucleus.floracestore.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nucleus.floracestore.config.JwtConfig;
import com.nucleus.floracestore.model.payloads.AuthenticationRequest;
import io.jsonwebtoken.Jwts;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Class for verifying credentials
 **/
@Slf4j
@NoArgsConstructor
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private  AuthenticationManager authenticationManager;
    private  JwtConfig jwtConfiguration;
    private  SecretKey secretKey;

    @Autowired
    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                      JwtConfig jwtConfiguration,
                                                      SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.jwtConfiguration = jwtConfiguration;
        this.secretKey = secretKey;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            AuthenticationRequest authenticationRequest = new ObjectMapper().readValue(request.getInputStream(),
                    AuthenticationRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword());
            return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws ServletException, IOException {
        if (request.getServletPath().contains("/login")) {
            chain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }


        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfiguration.getExpiration())))
                .signWith(secretKey)
                .compact();

        log.info("JwtUsernameAndPasswordAuthenticationFilter.class - JWT Token: {}", token);
        response.addHeader(jwtConfiguration.getHeader(), jwtConfiguration.getPrefix() + token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), String.format(jwtConfiguration.getPrefix() + token));
    }

}
