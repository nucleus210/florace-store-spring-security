package com.nucleus.floracestore.config;

import com.nucleus.floracestore.interceptors.LogInterceptor;
import com.nucleus.floracestore.jwt.JwtConfiguration;
import com.nucleus.floracestore.jwt.JwtTokenVerifier;
import com.nucleus.floracestore.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.nucleus.floracestore.model.enums.UserRoleEnum;
import com.nucleus.floracestore.service.impl.MyUserDetailsService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.handler.MappedInterceptor;

import javax.crypto.SecretKey;
import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;
    private final JwtConfiguration jwtConfiguration;
    private final SecretKey secretKey;
    private final MyCustomDSL myCustomDSL;
    @Autowired
    public SecurityConfiguration(MyUserDetailsService userDetailsService,
                                 PasswordEncoder passwordEncoder,
                                 DataSource dataSource,
                                 JwtConfiguration jwtConfiguration,
                                 SecretKey secretKey, MyCustomDSL myCustomDSL) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
        this.jwtConfiguration = jwtConfiguration;
        this.secretKey = secretKey;
        this.myCustomDSL = myCustomDSL;
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/resources/**",
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
                                        .antMatchers(HttpMethod.GET, "/addresses/address-types").permitAll()
                                        .antMatchers(HttpMethod.GET, "/countries").permitAll()
                                        .antMatchers(HttpMethod.GET, "/phone-prefixes").permitAll()
                                        .antMatchers(HttpMethod.GET, "/products").permitAll()
                                        .antMatchers(HttpMethod.GET, "/products/**").permitAll()
                                        .antMatchers(HttpMethod.GET, "/products-categories").permitAll()

                                        .antMatchers(HttpMethod.GET, "/products-sub-categories").permitAll()
                                        .antMatchers(HttpMethod.GET, "/products-sub-categories/**").permitAll()

                                        .antMatchers(HttpMethod.POST, "/storages/uploads").permitAll()
                                        .antMatchers(HttpMethod.POST, "/storages/files").permitAll()
                                        .antMatchers(HttpMethod.POST, "/storages/file").permitAll()
                                        .antMatchers(HttpMethod.GET, "/products-statuses").permitAll()

                                        .antMatchers(HttpMethod.POST, "/login").permitAll()
                                        .antMatchers(HttpMethod.POST, "/register").permitAll()
                                        .antMatchers(HttpMethod.POST, "/facebook/signin").permitAll()
                                        .antMatchers("/orders/**").hasAnyRole(UserRoleEnum.USER.name(), UserRoleEnum.ADMIN.name())
                                        .antMatchers("/order-items/**").hasAnyRole(UserRoleEnum.USER.name(), UserRoleEnum.ADMIN.name())
                                        .anyRequest().authenticated()
                                        .and()
                                        .addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
                                        .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(http),  jwtConfiguration, secretKey))
                                        .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfiguration), JwtUsernameAndPasswordAuthenticationFilter.class);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }


                )

                .csrf().disable()
                .cors().disable()
                .httpBasic().disable();
//                .apply(myCustomDSL);
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
