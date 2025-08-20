package com.digital.securityConfig;

import com.digital.exception.CustomUnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AppFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(AppFilter.class);

    private final CustomUserDetailsService userDetailsService;

    private final JwtService jwtService;

    public AppFilter(CustomUserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String username = null;
            String bearerToken = null;

            String header = request.getHeader("Authorization"); // Authorization is the key that we passed inside header

            if (header != null && header.startsWith("Bearer")) {
                bearerToken = header.substring(7);
                username = jwtService.extractUsername(bearerToken);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                logger.debug(userDetails.getUsername());

                if (jwtService.validateToken(bearerToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response); // doFilter means proceed further
        }catch (CustomUnauthorizedException e) {
            // Let @ControllerAdvice handle it
            throw e;
        } catch (Exception e) {
            // For any other unexpected errors
            throw new RuntimeException("Error in JWT authentication: " + e.getMessage(), e);
        }
    }
}

