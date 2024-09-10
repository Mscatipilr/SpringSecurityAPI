package org.joann.springsecurityapi.controllers;

import jakarta.transaction.Transactional;
import org.joann.springsecurityapi.models.User;
import org.joann.springsecurityapi.repositories.UserRepository;
import org.joann.springsecurityapi.services.JwtUserDetailsService;
import org.joann.springsecurityapi.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;

    // Inject PasswordEncoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Inject UserRepository
    @Autowired
    private UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user to the database
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    // Endpoint for user login
    @Transactional
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (Exception e) {
            throw new Exception("Invalid username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    // Endpoint for fetching the profile of the currently authenticated user
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        // Extract the JWT token from the Authorization header (Bearer <token>)
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Extract the username from the JWT token
        String username = jwtTokenUtil.getUsernameFromToken(token);

        // Fetch the user details from the repository
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Create a response map
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to your profile!"); // Add custom message
        response.put("user", user); // Add user object
        // Return user profile information (you can customize this response as needed)
        return ResponseEntity.ok(response);
    }

}

