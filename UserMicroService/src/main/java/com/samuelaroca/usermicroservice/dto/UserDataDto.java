package com.samuelaroca.usermicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDto {

    private Long id;
    private String email;
    private String name;
    private String lastname;
    private String country;
    private String role;
    private boolean active;

    public UserDataDto(Long id, String email, String name, String lastname, String country, String role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.lastname = lastname;
        this.country = country;
        this.role = role;
    }
}
