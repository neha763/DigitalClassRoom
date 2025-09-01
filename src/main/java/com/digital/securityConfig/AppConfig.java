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
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Building Security Filter Chain...");


                         // auth login
                        .requestMatchers("/api/auth/login").permitAll()

                        // user apis
                        .requestMatchers("/api/user/otp", "/api/user/password").permitAll()
                        .requestMatchers("/api/user/create", "/api/user/status/*").hasRole("ADMIN")

                        // audit log apis
                        .requestMatchers("/api/auditLog").hasAnyRole("TEACHER", "STUDENT", "LIBRARIAN", "TRANSPORT")
                        .requestMatchers("/api/auditLog/*").hasRole("ADMIN")

                        // attendance rule apis
                        .requestMatchers( "/api/attendanceRules/**").hasRole("ADMIN")

                        // class session apis
                        .requestMatchers("/api/session").hasRole("TEACHER")
                        .requestMatchers("/api/session/get").hasAnyRole("TEACHER', 'STUDENT', 'ADMIN")

                         // attendance apis for STUDENT AND TEACHER
                        .requestMatchers("/api/attendance/join-session/*", "/api/attendance/leave-session/*")
                           .hasRole("STUDENT")

                        .requestMatchers("/api/attendance/auto-mark/*", "/api/attendance/check-in-list/*",
                                "/api/attendance/view/*", "/api/attendance/view-all/*", "/api/attendance/update/*")
                           .hasRole("TEACHER")

                        // attendance apis for ADMIN
                        .requestMatchers("/api/attendance/admin/update/*", "/api/attendance/admin/view/*",
                                "/api/attendance/admin/view-all/*", "/api/attendance/admin/delete/*").hasRole("ADMIN")

                        .requestMatchers("/api/attendance/admin/pdf/*").hasAnyRole("ADMIN", "TEACHER")


                           .requestMatchers("/api/teacher/**").hasRole("ADMIN")
//                           .requestMatchers("/api/teacher/**").permitAll()

                            //class and section

                        .requestMatchers("/api/class/**").hasRole("ADMIN")
                        .requestMatchers("/api/section/**").hasRole("ADMIN")

//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/admin/classes/**").hasRole("ADMIN")
//                        .requestMatchers("/admin/classes/**/sections").hasRole("ADMIN")
//                        .requestMatchers("/admin/classes").hasRole("ADMIN")
//                        //.requestMatchers("/admin/classes/{id}").hasRole("ADMIN")
//                        .requestMatchers("/admin/classes/*").hasRole("ADMIN")


                        .requestMatchers("/api/**").authenticated())

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/user/otp", "/api/user/password").permitAll()
                        .requestMatchers("/api/user/create", "/api/user/status/*").hasRole("ADMIN")
                        .requestMatchers("/api/auditLog").hasAnyRole("TEACHER", "STUDENT", "LIBRARIAN", "TRANSPORT")
                        .requestMatchers("/api/auditLog/*").hasRole("ADMIN")
                        .requestMatchers("/api/teacher/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/admin/classes/**").hasRole("ADMIN")
                        .requestMatchers("/admin/classes/**/sections").hasRole("ADMIN")
                        .requestMatchers("/admin/classes").hasRole("ADMIN")
                        .requestMatchers("/admin/classes/*").hasRole("ADMIN")


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