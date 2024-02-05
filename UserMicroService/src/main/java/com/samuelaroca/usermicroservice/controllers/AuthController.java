package com.samuelaroca.usermicroservice.controllers;

import com.samuelaroca.usermicroservice.dto.AuthResponse;
import com.samuelaroca.usermicroservice.dto.LoginDto;
import com.samuelaroca.usermicroservice.dto.RegisterDto;
import com.samuelaroca.usermicroservice.exceptions.AppException;
import com.samuelaroca.usermicroservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto data) {
        try {
            return ResponseEntity.ok(authService.login(data));
        } catch (RuntimeException e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterDto data) {
        try {
            return ResponseEntity.ok(authService.register(data));
        } catch (RuntimeException e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
