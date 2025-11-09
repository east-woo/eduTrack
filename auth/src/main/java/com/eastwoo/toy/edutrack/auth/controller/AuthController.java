package com.eastwoo.toy.edutrack.auth.controller;

import com.eastwoo.toy.edutrack.auth.entity.User;
import com.eastwoo.toy.edutrack.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        String token = authService.login(email, password);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestParam String name,
                                         @RequestParam String email,
                                         @RequestParam String password,
                                         @RequestParam(defaultValue = "STUDENT") String role) {
        User user = authService.register(name, email, password, role);
        return ResponseEntity.ok(user);
    }
}