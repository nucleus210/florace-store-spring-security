package com.nucleus.floracestore.config;

import com.nucleus.floracestore.service.UserService;
import com.nucleus.floracestore.service.impl.JwtTokenProvider;
import com.nucleus.floracestore.service.impl.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
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
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class SecurityConfigOld {
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;

    private final JwtConfig jwtConfig;

    private final JwtTokenProvider tokenProvider;

    private final UserService userService;

    public SecurityConfigOld(MyUserDetailsService userDetailsService, PasswordEncoder passwordEncoder, DataSource dataSource, JwtConfig jwtConfig, JwtTokenProvider tokenProvider, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
        this.jwtConfig = jwtConfig;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

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

    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .cors().and()
                .authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/",
                        "/about",
                        "/shop",
                        "/contact",
                        "/products/all",
                        "/products/**/details",
                        "/name/**",
                        "/edit/**",
                        "/users/login",
                        "/users/register",
                        "/users/reset-password",
                        "/storages/upload"

                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/users/login")
                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                .defaultSuccessUrl("/")
                .failureForwardUrl("/users/login-error")
                .and()
                .rememberMe()
                .rememberMeParameter("remember")
                .key("remember Me Encryption Key")
                .rememberMeCookieName("rememberMeCookieName")
                .tokenValiditySeconds(10000)
                .tokenRepository(persistentTokenRepository())
                .and()
                .logout()
                .logoutUrl("/users/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
//        super.configure(auth);
    }

    @Bean(name = "persistentTokenRepository")
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }
//    @Bean
//    CorsConfigurationSource corsConfigurationSource()
//    {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}
//--------------------------------------------------------------------------------------------------------------------/

//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    private final MyUserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//    private final DataSource dataSource;
//    private final JwtConfig jwtConfig;
//    private final JwtTokenProvider tokenProvider;
//    private final UserService userService;



//    @Autowired
//    public WebSecurityConfig(MyUserDetailsService userDetailsService,
//                             PasswordEncoder passwordEncoder,
//                             DataSource dataSource,
//                             JwtConfig jwtConfig,
//                             JwtTokenProvider tokenProvider,
//                             UserService userService) {
//        this.userDetailsService = userDetailsService;
//        this.passwordEncoder = passwordEncoder;
//        this.dataSource = dataSource;
//        this.jwtConfig = jwtConfig;
//        this.tokenProvider = tokenProvider;
//        this.userService = userService;
//    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web
//                .ignoring()
//                .antMatchers(
//                        "/resources/**",
//                        "/static/**",
//                        "/images/**",
//                        "/webfonts/**",
//                        "/css/**",
//                        "/js/**",
//                        "/src/**",
//                        "/api/**",
//                        "/error");
//    }
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder);
//        super.configure(auth);
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
//                .cors()
//                .csrf()
//                .and()
//                .cors().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                .and()
//                .addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
//                .addFilterBefore(new JwtTokenAuthenticationFilter(jwtConfig, tokenProvider, userService), UsernamePasswordAuthenticationFilter.class)
//                .authorizeRequests()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                .antMatchers(HttpMethod.POST, "/signin").permitAll()
//                .antMatchers(HttpMethod.POST, "/facebook/signin").permitAll()
//                .antMatchers(HttpMethod.POST, "/users").anonymous()
//                .antMatchers(HttpMethod.GET, "/users").anonymous()
//
//                .antMatchers(HttpMethod.GET, "/products/all").anonymous()

//                .antMatchers("/",
//                        "/about",
//                        "/shop",
//                        "/contact",
//                        "/products/add",
//                        "/products/all",
//                        "/products/**/details",
//                        "/category/{id}/details",
//                        "/category/all",
//                        "/categories/add",
//                        "/name/**",
//                        "/edit/**",
//                        "/users/login",
//                        "/sing-in",
//                        "/users/register",
//                        "/users/reset-password",
//                        "/storages/upload"
//
//                        ).permitAll()
//                .anyRequest().authenticated();
//                .and()
//                .formLogin()
//                .loginPage("/users/login")
//                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
//                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
//                .defaultSuccessUrl("/")
//                .failureForwardUrl("/users/login-error")
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(authEntryPoint)
//                .and()
//                .rememberMe()
//                .rememberMeParameter("remember")
//                .key("remember Me Encryption Key")
//                .rememberMeCookieName("rememberMeCookieName")
//                .tokenValiditySeconds(10000)
//                .tokenRepository(persistentTokenRepository())
//                .and()
//                .logout()
//                .logoutUrl("/users/logout")
//                .logoutSuccessUrl("/")
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID");
//    }


//
//    @Bean(BeanIds.AUTHENTICATION_MANAGER)
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

//    @Bean(name = "persistentTokenRepository")
//    public PersistentTokenRepository persistentTokenRepository() {
//        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
//        tokenRepository.setDataSource(dataSource);
//        return tokenRepository;
//    }

//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        String hierarchy = "ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER \n ROLE_USER > ROLE_FACEBOOK_USER";
//        roleHierarchy.setHierarchy(hierarchy);
//        return roleHierarchy;
//    }

//    @Bean
//    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
//        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
//        expressionHandler.setRoleHierarchy(roleHierarchy());
//        return expressionHandler;
//    }



//    @Bean
//    public CorsFilter corsFilter() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        final CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("OPTIONS");
//        config.addAllowedMethod("HEAD");
//        config.addAllowedMethod("GET");
//        config.addAllowedMethod("PUT");
//        config.addAllowedMethod("POST");
//        config.addAllowedMethod("DELETE");
//        config.addAllowedMethod("PATCH");
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//}