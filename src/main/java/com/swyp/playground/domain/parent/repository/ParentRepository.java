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

    @Query("SELECT p FROM Parent p LEFT JOIN FETCH p.children WHERE p.parentId = :id")
    Optional<Parent> findParentWithChildren(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM Parent p WHERE p.phoneNumber = :phoneNumber")
    boolean isPhoneNumberDuplicate(@Param("phoneNumber") String phoneNumber);

    Optional<Parent> findByPhoneNumber(String phoneNumber);

}
