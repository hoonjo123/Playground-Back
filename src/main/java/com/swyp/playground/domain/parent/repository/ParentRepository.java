package com.swyp.playground.domain.parent.repository;

import com.swyp.playground.domain.parent.domain.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent,Long> {
    Optional<Parent> findByEmail(String email);

    @Modifying
    @Query("UPDATE Parent p SET p.password = :password WHERE p.email = :email")
    void updatePasswordByEmail(@Param("email") String email, @Param("password") String password);

    Optional<Parent> findByNickname(String nickname);

}
