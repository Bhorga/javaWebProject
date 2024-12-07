package com.javaWebProject.ecomproj.controller;

import com.javaWebProject.ecomproj.model.User;
import com.javaWebProject.ecomproj.service.UserService;
import com.javaWebProject.ecomproj.utils.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Optional<User> existingUser = userService.findByUsername(user.getUsername());
        if (existingUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid credentials!");
        }

        String jwtToken = jwtUtil.generateToken(existingUser.get());

    // Set the token in the response cookie
        Cookie cookie = new Cookie("token", jwtToken);
        cookie.setHttpOnly(true); // Prevent access via JavaScript
        cookie.setSecure(true);   // Use HTTPS in production
        cookie.setPath("/");
        cookie.setMaxAge(86400);  // 1 day

        response.addCookie(cookie);

        return ResponseEntity.ok("Login successful");
    }
    
}
