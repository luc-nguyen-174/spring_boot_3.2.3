package org.example.spring_3_2_3.config.auth;

import org.example.spring_3_2_3.config.exceptions.AccessDenied;
import org.example.spring_3_2_3.config.exceptions.RestAuthEntryPoint;
import org.example.spring_3_2_3.repositories.UserInfoRepository;
import org.example.spring_3_2_3.rest.role.service.IRoleService;
import org.example.spring_3_2_3.rest.user.services.impl.UserInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter authFilter;
    private final UserInfoRepository userInfoRepository;
    private final IRoleService roleService;
    private final PasswordEncoder encoder;

    public SecurityConfig(@Lazy JwtAuthFilter authFilter,
                          @Lazy UserInfoRepository userInfoRepository,
                          @Lazy IRoleService roleService,
                          @Lazy PasswordEncoder encoder) {
        this.authFilter = authFilter;
        this.userInfoRepository = userInfoRepository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @Bean
    public AccessDenied accessDenied() {
        return new AccessDenied();
    }

    @Bean
    public RestAuthEntryPoint restAuthEntryPoint() {
        return new RestAuthEntryPoint();
    }

    // User Creation
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoService(userInfoRepository, roleService, encoder);
    }

    // Configuring HttpSecurity
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((AbstractHttpConfigurer::disable))
                .authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                        .requestMatchers(
                                "/auth/welcome",
                                "/auth/addNewUser",
                                "/auth/generateToken",
                                "/swagger-ui/**",
                                "/v3/**",
                                "/auth/get-all-user").permitAll()
                        .requestMatchers("/auth/user/**").authenticated()
                        .requestMatchers("/auth/admin/**").authenticated())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(handlingConfigurer -> handlingConfigurer
                        .accessDeniedHandler(accessDenied()))
                .httpBasic(httpBasic -> httpBasic
                        .authenticationEntryPoint(restAuthEntryPoint()))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Password Encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}