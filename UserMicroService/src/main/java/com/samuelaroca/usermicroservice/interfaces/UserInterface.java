package com.samuelaroca.usermicroservice.interfaces;

import com.samuelaroca.usermicroservice.dto.Message;
import com.samuelaroca.usermicroservice.dto.RegisterDto;
import com.samuelaroca.usermicroservice.dto.UserDataDto;
import com.samuelaroca.usermicroservice.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserInterface {

    UserDataDto listUser(String token);
    List<User> findUsers(String parameter);
    List<UserDataDto> listAll();
    Message update(Long idUser, RegisterDto user);
    String delete(Long idUser);
    List<UserDataDto> lastFiveUsers();
    boolean isActive(Long idUSer);
    Optional<User> findById(Long idUser);
}
