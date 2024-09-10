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
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) throws Exception {
//        // Fetch user from the database
//        User user = userRepository.findByUsername(authRequest.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        // Manually compare passwords
//        boolean isPasswordMatch = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
//
//        // Print the result for debugging
//        System.out.println("Password Match: " + isPasswordMatch);
//
//        if (isPasswordMatch) {
//            // Password matches, proceed with generating JWT
//            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
//            final String jwt = jwtTokenUtil.generateToken(userDetails.getUsername());
//
//            return ResponseEntity.ok(new AuthResponse(jwt));
//        } else {
//            throw new Exception("Invalid username or password");
//        }
//    }

}
