package com.samuelaroca.usermicroservice.services;

import java.util.Optional;

import com.samuelaroca.usermicroservice.client.CountryClient;
import com.samuelaroca.usermicroservice.dto.AuthResponse;
import com.samuelaroca.usermicroservice.dto.LoginDto;
import com.samuelaroca.usermicroservice.dto.RegisterDto;
import com.samuelaroca.usermicroservice.entities.Token;
import com.samuelaroca.usermicroservice.entities.User;
import com.samuelaroca.usermicroservice.enums.Role;
import com.samuelaroca.usermicroservice.enums.TokenType;
import com.samuelaroca.usermicroservice.exceptions.AppException;
import com.samuelaroca.usermicroservice.interfaces.AuthInterface;
import com.samuelaroca.usermicroservice.jwt.JwtService;
import com.samuelaroca.usermicroservice.repositories.TokenRepository;
import com.samuelaroca.usermicroservice.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthInterface {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final CountryClient countryClient;

    @Transactional
    @Override
    public AuthResponse login(LoginDto data) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword()));
        var user = userRepository.findByEmail(data.getEmail()).orElseThrow();
        var token = jwtService.getToken(user);
        revokedAllUserTokens(user);
        saveUserToken(user, token);
        return AuthResponse.builder()
                .token(token)
                .role(user.getRole().toString())
                .build();
    }

    @Transactional
    @Override
    public AuthResponse register(RegisterDto data) {

        Optional<User> userOptional = userRepository.findByEmail(data.getEmail());

        if (userOptional.isPresent()) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }

        Long name = countryClient.name(data.getCountry());

        User user = User.builder()
                .email(data.getEmail())
                .password(passwordEncoder.encode(data.getPassword()))
                .name(data.getName())
                .lastname(data.getLastname())
                .countryId(name)
                .role(Role.valueOf(data.getRole()))
                .build();
        var savedUser = userRepository.save(user);
        var token = jwtService.getToken(user);
        saveUserToken(savedUser, token);
        return AuthResponse.builder()
                .token(token)
                .role(user.getRole().toString())
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokedAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
