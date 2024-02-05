package com.samuelaroca.usermicroservice.repositories;

import com.samuelaroca.usermicroservice.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = "select t from Token t inner join User u on t.user.id = u.id where u.id = ?1 and (t.expired = false or t.revoked = false)")
    List<Token> findAllValidTokenByUser(Long idUser);

    Optional<Token> findByToken(String token);

    @Query(value = "select case when count(t) > 0 then true else false end from Token t where t.user.id = ?1 and t.revoked = false and t.expired = false")
    boolean isActive(Long idUser);
}
