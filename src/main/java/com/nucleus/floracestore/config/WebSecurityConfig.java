package com.nucleus.floracestore.config;

import com.nucleus.floracestore.jwt.JwtConfiguration;
import com.nucleus.floracestore.jwt.JwtTokenVerifier;
import com.nucleus.floracestore.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.nucleus.floracestore.model.enums.UserRoleEnum;
import com.nucleus.floracestore.service.UserService;
import com.nucleus.floracestore.service.impl.JwtTokenProvider;
import com.nucleus.floracestore.service.impl.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.SecretKey;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;
    private final JwtConfiguration jwtConfiguration;
    private final SecretKey secretKey;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;


    @Autowired
    public WebSecurityConfig(MyUserDetailsService userDetailsService,
                             PasswordEncoder passwordEncoder,
                             DataSource dataSource,
                             JwtConfiguration jwtConfiguration,
                             SecretKey secretKey,
                             JwtTokenProvider tokenProvider,
                             UserService userService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
        this.jwtConfiguration = jwtConfiguration;
        this.secretKey = secretKey;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                        "/resources/**",
                        "/static/**",
                        "/images/**",
                        "/webfonts/**",
                        "/css/**",
                        "/js/**",
                        "/src/**",
                        "/api/**",
                        "/error");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        super.configure(auth);
    }

    /**
     * Request :
     * --> Filter1({@link JwtUsernameAndPasswordAuthenticationFilter})
     * --> Filter2({@link JwtTokenVerifier})
     * --> api
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtUsernameAndPasswordAuthenticationFilter jwtUsernameAndPasswordAuthenticationFilter =
                new JwtUsernameAndPasswordAuthenticationFilter(authenticationManagerBean(), jwtConfiguration, secretKey);
        jwtUsernameAndPasswordAuthenticationFilter.setFilterProcessesUrl("/login");
        http

                .csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
                .addFilter(jwtUsernameAndPasswordAuthenticationFilter)
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfiguration), JwtUsernameAndPasswordAuthenticationFilter.class)
//                .addFilterAfter(new CsrfTokenResponseHeaderBindingFilter(), CsrfFilter.class)
                .authorizeRequests()
//                .antMatchers(HttpMethod.GET,"/products/all").permitAll()
                .antMatchers(HttpMethod.GET, "/category/all").permitAll()


                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .antMatchers(HttpMethod.POST, "/facebook/signin").permitAll()
                .antMatchers("/products/**").hasRole(UserRoleEnum.ADMIN.name())
                .antMatchers("/orders/**").hasRole(UserRoleEnum.ADMIN.name())

                .anyRequest().authenticated();
//                .and()
//                .addFilter(customAuthenticationFilter)
//                .addFilterBefore(new JwtTokenAuthenticationFilter(jwtConfig, tokenProvider, userService), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
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
}
