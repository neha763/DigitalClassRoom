package com.digital.google_meet_config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/auth")
public class GoogleAuthController {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${google.auth.url}")
    private String authUrl;

    @GetMapping("/google/{id}")
    public void googleLogin(HttpServletResponse response, @PathVariable Long id) throws IOException {

        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        String encodedScope = URLEncoder.encode("https://www.googleapis.com/auth/calendar.events", StandardCharsets.UTF_8);

        String url = authUrl
                + "?client_id=" + clientId
                + "&redirect_uri=" + encodedRedirectUri
                + "&response_type=code"
                + "&scope=" + encodedScope
                + "&access_type=offline"
                + "&prompt=consent"
                + "&state=" + id;

        response.sendRedirect(url);
    }
}
