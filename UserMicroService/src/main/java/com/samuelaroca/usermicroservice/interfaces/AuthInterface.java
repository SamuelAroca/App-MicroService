package com.samuelaroca.usermicroservice.interfaces;

import com.samuelaroca.usermicroservice.dto.AuthResponse;
import com.samuelaroca.usermicroservice.dto.LoginDto;
import com.samuelaroca.usermicroservice.dto.RegisterDto;

public interface AuthInterface {

    AuthResponse login(LoginDto data);

    AuthResponse register(RegisterDto data);

}
