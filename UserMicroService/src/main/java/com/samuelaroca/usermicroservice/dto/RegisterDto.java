package com.samuelaroca.usermicroservice.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    String email;
    String password;
    String name;
    String lastname;
    String country;
    String role;
}
