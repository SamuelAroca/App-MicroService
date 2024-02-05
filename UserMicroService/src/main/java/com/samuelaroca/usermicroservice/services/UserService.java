package com.samuelaroca.usermicroservice.services;

import com.samuelaroca.usermicroservice.client.CountryClient;
import com.samuelaroca.usermicroservice.dto.Message;
import com.samuelaroca.usermicroservice.dto.RegisterDto;
import com.samuelaroca.usermicroservice.dto.UserDataDto;
import com.samuelaroca.usermicroservice.entities.Token;
import com.samuelaroca.usermicroservice.entities.User;
import com.samuelaroca.usermicroservice.enums.Role;
import com.samuelaroca.usermicroservice.enums.TokenType;
import com.samuelaroca.usermicroservice.exceptions.AppException;
import com.samuelaroca.usermicroservice.interfaces.UserInterface;
import com.samuelaroca.usermicroservice.jwt.JwtService;
import com.samuelaroca.usermicroservice.repositories.TokenRepository;
import com.samuelaroca.usermicroservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CountryClient countryClient;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserDataDto listUser(String token) {

        Optional<User> userData = userRepository.findByToken(token);
        if (userData.isEmpty()) {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }

        String country = countryClient.idCountry(userData.get().getCountryId());

        return UserDataDto.builder()
                .id(userData.get().getId())
                .email(userData.get().getEmail())
                .name(userData.get().getName())
                .lastname(userData.get().getLastname())
                .country(country)
                .role(userData.get().getRole().toString()).build();
    }

    @Override
    @Transactional
    public List<User> findUsers(String parameter) {
        Long id = null;
        try {
            id = Long.parseLong(parameter);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        List<User> userList = userRepository.findUsers(id, parameter, parameter, parameter);
        if (userList.isEmpty()) {
            throw new AppException("No se encontraron usuarios por el siguiente parametro: " + parameter, HttpStatus.NOT_FOUND);
        }
        return userList;
    }

    @Override
    @Transactional
    public List<UserDataDto> listAll() {
        List<User> userList = userRepository.findAllUsers();

        return userList.stream()
                .map(user -> {
                    boolean isActive = tokenRepository.isActive(user.getId());
                    String countryName = countryClient.idCountry(user.getCountryId());
                    return new UserDataDto(
                            user.getId(),
                            user.getEmail(),
                            user.getName(),
                            user.getLastname(),
                            countryName,
                            user.getRole().name(),
                            isActive
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Message update(Long idUser, RegisterDto user) {

        if (user.getEmail().isEmpty() || user.getName().isEmpty() || user.getLastname().isEmpty()) {
            throw new AppException("Email, name y lastname no pueden estar vacios", HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userRepository.findById(idUser);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException();
        }

        User user1 = optionalUser.get();

        if (!user.getEmail().equals(user1.getEmail())) {
            Optional<User> optionalUser1 = userRepository.findByEmail(user.getEmail());
            if (optionalUser1.isPresent()) {
                throw new AppException("Email ya se encuentra registrado", HttpStatus.BAD_REQUEST);
            }
            user1.setEmail(user.getEmail());
        }

        Long idCountry = countryClient.name(user.getCountry());

        user1.setCountryId(idCountry);

        if (!user.getName().equals(user1.getName())) {
            user1.setName(user.getName());
        }

        if (!user.getLastname().equals(user1.getLastname())) {
            user1.setLastname(user.getLastname());
        }

        if (user.getPassword() != null) {
            if (!user.getPassword().isEmpty() || !passwordEncoder.matches(user.getPassword(), user1.getPassword())) {
                user1.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }

        if (!user.getRole().equals(user1.getRole().name())) {
            user1.setRole(Role.valueOf(user.getRole()));
        }

        var savedUser = userRepository.save(user1);
        var token = jwtService.getToken(user1);
        revokedAllUserTokens(savedUser);
        saveUserToken(savedUser, token);

        return Message.builder()
                .message("Usuario actualizado")
                .token(token)
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

    @Override
    @Transactional
    public String delete(Long idUser) {
        Optional<User> optionalUser = userRepository.findById(idUser);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException();
        }
        userRepository.delete(optionalUser.get());
        return "Usuario eliminado";
    }

    @Override
    @Transactional
    public List<UserDataDto> lastFiveUsers() {
        List<User> userList = userRepository.findTop5ByIdDesc();

        return userList.stream()
                .map(user -> {
                    String countryName = countryClient.idCountry(user.getCountryId());
                    return new UserDataDto(
                            user.getId(),
                            user.getEmail(),
                            user.getName(),
                            user.getLastname(),
                            countryName,
                            user.getRole().name()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean isActive(Long idUSer) {
        return tokenRepository.isActive(idUSer);
    }

    @Override
    public Optional<User> findById(Long idUser) {
        return userRepository.findById(idUser);
    }
}
