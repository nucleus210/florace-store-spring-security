package com.nucleus.floracestore.config;

import com.nucleus.floracestore.interceptors.LogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.handler.MappedInterceptor;

import javax.crypto.SecretKey;
import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;

    private final SecretKey secretKey;
    private final MyCustomDSL myCustomDSL;

    @Autowired
    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter,
                                 SecretKey secretKey, MyCustomDSL myCustomDSL) {
        this.jwtAuthFilter = jwtAuthFilter;

        this.secretKey = secretKey;
        this.myCustomDSL = myCustomDSL;
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/resources/**",
                                        "/static/**",
                                        "/images/**",
                                        "/webfonts/**",
                                        "/css/**",
                                        "/js/**",
                                        "/src/**",
                                        "/api/**",
                                        "/error");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authz) -> {
                            try {
                                authz
                                        .requestMatchers(HttpMethod.GET, "/addresses/address-types").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/blog-posts").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/countries").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/contacts").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/phone-prefixes").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/products").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/products-categories").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/products-statuses").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/products-sub-categories").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/products-sub-categories/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/products-sub-categories").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/products-sub-categories/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/storages/uploads").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/storages/files").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/storages/file").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/slider").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/profile/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/profiles").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/profiles").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/profile").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/profile").permitAll()
                                        .requestMatchers( "/users/**").permitAll()
                                        .requestMatchers("/users/search").permitAll()
                                        .requestMatchers( "/users/user-names/**").permitAll()
                                        .requestMatchers( "/users/search").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/users/search").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/error").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/actuator").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/refresh-token").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/search-results/search/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/register").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/facebook/signin").permitAll()
                                        .requestMatchers("/orders/**").hasAnyRole("ADMIN", "USER", "STAFF")
                                        .requestMatchers("/order-items/**").hasAnyRole("ADMIN", "USER", "STAFF")
                                        .requestMatchers("/order-status-codes/**").hasAnyRole("ADMIN", "USER", "STAFF")
                                        .anyRequest().authenticated()
                                        .and()
                                        .addFilterBefore(corsFilter(), ChannelProcessingFilter.class);

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .httpBasic().disable()
                 .apply(myCustomDSL);
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        source.registerCorsConfiguration("/**", configuration);
        return  new CorsFilter(source);
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    public MappedInterceptor myInterceptor()
    {
        return new MappedInterceptor(null, new LogInterceptor());
    }
}
