package com.nucleus.floracestore.config;

import com.nucleus.floracestore.interceptors.LogInterceptor;
import com.nucleus.floracestore.jwt.JwtConfiguration;
import com.nucleus.floracestore.jwt.JwtTokenVerifier;
import com.nucleus.floracestore.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.nucleus.floracestore.model.enums.UserRoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.handler.MappedInterceptor;

import javax.crypto.SecretKey;
import java.util.Arrays;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)

//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;

    private final SecretKey secretKey;
    private final MyCustomDSL myCustomDSL;
    private final JwtConfig jwtConfiguration;

    @Autowired
    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter,
                                 SecretKey secretKey, MyCustomDSL myCustomDSL, JwtConfig jwtConfiguration) {
        this.jwtAuthFilter = jwtAuthFilter;

        this.secretKey = secretKey;
        this.myCustomDSL = myCustomDSL;
        this.jwtConfiguration = jwtConfiguration;
    }

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//
//        authProvider.setUserDetailsService(userDetailsService);
////        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }
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
//                                        .requestMatchers(HttpMethod.POST,"/orders/**").permitAll()
//
//                                        .requestMatchers(HttpMethod.GET,"/orders/**").permitAll()
//                                        .requestMatchers("/order-items/**").hasAuthority("ROLE_ADMIN")
                                        .requestMatchers("/orders/**").hasRole("ADMIN")

                                        .requestMatchers("/order-items/**").hasRole("ADMIN")

                                        .anyRequest().authenticated()
                                        .and()
                                        .addFilterBefore(corsFilter(), ChannelProcessingFilter.class);
//                                        .addFilterAfter(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//                                        .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);


//                                        .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(http),  jwtConfiguration, secretKey));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
//                                        .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfiguration), JwtUsernameAndPasswordAuthenticationFilter.class);

                        }


                )


                .httpBasic().disable()
                 .apply(myCustomDSL);
        return http.build();
    }

//    @Bean
//    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
//        return new JwtAuthenticationFilter();
//    }
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
