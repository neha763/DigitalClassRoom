package com.digital.securityConfig;

import com.digital.exception.CustomForbiddenException;
import com.digital.exception.CustomUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

                        // auth/login
                        .requestMatchers("/api/auth/login").permitAll()

                        // user APIs
                        .requestMatchers("/api/user/otp", "/api/user/password").permitAll()
                        .requestMatchers("/api/user/create", "/api/user/status/*").hasRole("ADMIN")

                        // audit log
                        .requestMatchers("/api/auditLog").hasAnyRole("TEACHER", "STUDENT", "LIBRARIAN", "TRANSPORT")
                        .requestMatchers("/api/auditLog/*").hasRole("ADMIN")

                        // attendance rules
                        .requestMatchers("/api/attendanceRules/**").hasRole("ADMIN")

                        // session
                        .requestMatchers("/api/session").hasRole("TEACHER")
                        .requestMatchers("/api/session/get").hasAnyRole("TEACHER", "STUDENT", "ADMIN")

                        // student attendance
                        .requestMatchers("/api/attendance/join-session/*", "/api/attendance/leave-session/*").hasRole("STUDENT")

                        // teacher attendance
                        .requestMatchers("/api/attendance/auto-mark/*", "/api/attendance/check-in-list/*",
                                "/api/attendance/view/*", "/api/attendance/view-all/*", "/api/attendance/update/*")
                        .hasRole("TEACHER")

                        // admin attendance
                        .requestMatchers("/api/attendance/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/attendance/admin/pdf/*").hasAnyRole("ADMIN", "TEACHER")

                        // teacher APIs
                        .requestMatchers("/api/teacher/**").hasRole("ADMIN")

                        // class/section
                        .requestMatchers("/api/class/**", "/api/section/**").hasRole("ADMIN")

                        // students
                        .requestMatchers("/api/students/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/students/**").hasRole("STUDENT")

                        // any other API requires auth
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
            throw new CustomUnauthorizedException("Unauthorized - Please log in");
        };
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            throw new CustomForbiddenException("Forbidden - You do not have access to this resource");
        };
    }
}
