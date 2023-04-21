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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.handler.MappedInterceptor;

import javax.crypto.SecretKey;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final JwtConfiguration jwtConfiguration;
    private final SecretKey secretKey;
    private final MyCustomDSL myCustomDSL;
    @Autowired
    public SecurityConfiguration(JwtConfiguration jwtConfiguration,
                                 SecretKey secretKey, MyCustomDSL myCustomDSL) {

        this.jwtConfiguration = jwtConfiguration;
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
                                        .requestMatchers( "/users/search/**").permitAll()
                                        .requestMatchers( "/users/search/**").permitAll()
                                        .requestMatchers("/users/search").permitAll()
                                        .requestMatchers( "/users/user-names/**").permitAll()
                                        .requestMatchers( "/users/search").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/users/search").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/error").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/actuator").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()

                                        .requestMatchers(HttpMethod.POST, "/register").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/facebook/signin").permitAll()
                                        .requestMatchers("/orders/**").hasAnyRole(UserRoleEnum.USER.name(), UserRoleEnum.ADMIN.name())
                                        .requestMatchers("/order-items/**").hasAnyRole(UserRoleEnum.USER.name(), UserRoleEnum.ADMIN.name())
                                        .anyRequest().authenticated()
                                        .and()
                                        .addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
//                                        .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(http),  jwtConfiguration, secretKey))
                                        .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfiguration), JwtUsernameAndPasswordAuthenticationFilter.class);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }


                )

                .csrf().disable()
                .cors().disable()
                .httpBasic().disable()
                 .apply(myCustomDSL);
        return http.build();
    }
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader(CorsConfiguration.ALL);
        config.addAllowedMethod(CorsConfiguration.ALL);

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
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
