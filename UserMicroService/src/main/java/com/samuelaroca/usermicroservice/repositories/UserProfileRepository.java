package com.samuelaroca.usermicroservice.repositories;

import com.samuelaroca.usermicroservice.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUser_Id(Long userId);
}
