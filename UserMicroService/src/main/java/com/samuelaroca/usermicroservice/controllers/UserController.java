package com.samuelaroca.usermicroservice.controllers;

import com.samuelaroca.usermicroservice.dto.Message;
import com.samuelaroca.usermicroservice.dto.RegisterDto;
import com.samuelaroca.usermicroservice.dto.UserDataDto;
import com.samuelaroca.usermicroservice.entities.User;
import com.samuelaroca.usermicroservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/list/{token}")
    public ResponseEntity<UserDataDto> list(@PathVariable String token) {
        return ResponseEntity.ok(userService.listUser(token));
    }

    @Secured("ADMIN")
    @GetMapping("/findUsers/{parameter}")
    public ResponseEntity<List<User>> findUsers(@PathVariable String parameter) {
        return ResponseEntity.ok(userService.findUsers(parameter));
    }

    @Secured("ADMIN")
    @GetMapping("/listAll")
    public List<UserDataDto> listAll() {
        return userService.listAll();
    }

    @Secured("ADMIN")
    @PutMapping("/update/{idUser}")
    public ResponseEntity<Message> update(@PathVariable Long idUser, @RequestBody RegisterDto user) {
        return ResponseEntity.ok(userService.update(idUser, user));
    }

    @Secured("ADMIN")
    @DeleteMapping("/delete/{idUser}")
    public ResponseEntity<String> delete(@PathVariable Long idUser) {
        return ResponseEntity.ok(userService.delete(idUser));
    }

    @Secured("ADMIN")
    @GetMapping("/lastFiveUsers")
    public ResponseEntity<List<UserDataDto>> lastFiveUsers() {
        return ResponseEntity.ok(userService.lastFiveUsers());
    }

    @Secured("ADMIN")
    @GetMapping("/isActive/{idUser}")
    public ResponseEntity<Boolean> isActive(@PathVariable Long idUser) {
        return ResponseEntity.ok(userService.isActive(idUser));
    }
}
