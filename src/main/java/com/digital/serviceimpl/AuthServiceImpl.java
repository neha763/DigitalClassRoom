//package com.digital.serviceimpl;
//
//
//import com.digital.dto.AuthRequest;
//import com.digital.dto.AuthResponse;
//import com.digital.entity.User;
//import com.digital.repository.UserRepository;
//import com.digital.securityConfig.JwtService;
//import com.digital.servicei.AuthService;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//
//public class AuthServiceImpl implements AuthService {
//
//    private final AuthenticationManager authenticationManager;
//    private final UserRepository userRepository;
//    private final JwtService jwtService;
//
//    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService) {
//        this.authenticationManager = authenticationManager;
//        this.userRepository = userRepository;
//        this.jwtService = jwtService;
//    }

//    @Override
//    public AuthResponse login(AuthRequest request) {
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//        );
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//
////        String token = jwtService.generateToken(userDetails);
//String token = jwtService.generateToken(userDetails);
//
//        User user = userRepository.findByEmail(userDetails.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//
//        return new AuthResponse(token, user.getRole().name());
//    }

