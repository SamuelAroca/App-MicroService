package com.samuelaroca.usermicroservice.repositories;

import com.samuelaroca.usermicroservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(value = "select u from User u order by u.id ASC")
    List<User> findAllUsers();

    @Query(value = "select u from User u where u.id=?1 or u.name=?2 or u.lastname=?3 or u.password=?4")
    List<User> findUsers(Long id, String name,String lastname, String password);

    @Query(value = "select u from User u inner join Token t on t.user.id = u.id where t.token = ?1")
    Optional<User> findByToken(String token);

    @Query(value = "select u from User u order by u.id desc limit 5")
    List<User> findTop5ByIdDesc();
}
