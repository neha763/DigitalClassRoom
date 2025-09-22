package com.digital.securityConfig;

import com.digital.exception.CustomErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Configuration
public class AppConfig {

    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    private final CustomUserDetailsService userDetailsService;
    private final AppFilter appFilter;

    public AppConfig(CustomUserDetailsService userDetailsService, AppFilter appFilter) {
        this.userDetailsService = userDetailsService;
        this.appFilter = appFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Building Security Filter Chain...");

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/user/otp", "/api/user/password").permitAll()
                        .requestMatchers("/api/user/create", "/api/user/status/*").hasRole("ADMIN")
                        .requestMatchers("/api/auditLog").hasAnyRole("TEACHER", "STUDENT", "LIBRARIAN", "TRANSPORT")
                        .requestMatchers("/api/auditLog/*").hasRole("ADMIN")
                        .requestMatchers("/api/attendanceRules/**").hasRole("ADMIN")
                        .requestMatchers("/api/session").hasRole("TEACHER")
                        .requestMatchers("/api/session/get").hasAnyRole("TEACHER", "STUDENT", "ADMIN")
                        .requestMatchers("/api/attendance/join-session/*", "/api/attendance/leave-session/*").hasRole("STUDENT")
                        .requestMatchers("/api/attendance/auto-mark/*",
                                "/api/attendance/check-in-list/*",
                                "/api/attendance/view/*",
                                "/api/attendance/view-all/*",
                                "/api/attendance/update/*").hasRole("TEACHER")
                        .requestMatchers("/api/attendance/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/attendance/admin/pdf/*").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/api/teacher/**").hasRole("ADMIN")
                        .requestMatchers("/api/class/**", "/api/section/**").hasRole("ADMIN")
                        .requestMatchers("/api/students/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/students/**").hasRole("STUDENT")
                        .requestMatchers("/api/admin/**", "/api/gateway**").hasRole("ADMIN")
                        .requestMatchers("/api/payment/**").hasRole("STUDENT")
                        .requestMatchers("/api/studentFee/**").hasRole("STUDENT")
                        .requestMatchers("/api/**").authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                        .accessDeniedHandler(customAccessDeniedHandler())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(appFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            CustomErrorResponse error = new CustomErrorResponse(
                    HttpStatus.FORBIDDEN,
                    "Unauthorized - Please log in",
                    request.getRequestURI()
            );
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            try (OutputStream out = response.getOutputStream()) {
                new ObjectMapper().writeValue(out, error);
                out.flush();
            }
        };
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            CustomErrorResponse error = new CustomErrorResponse(
                    HttpStatus.FORBIDDEN,
                    "Forbidden - You do not have access to this resource",
                    request.getRequestURI()
            );

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            try (OutputStream out = response.getOutputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                mapper.writeValue(out, error);
                out.flush();
            }
        };
    }

}



