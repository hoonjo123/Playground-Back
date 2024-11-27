package com.swyp.playground.domain.parent.repository;

import com.swyp.playground.domain.parent.domain.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent,Long> {
    Optional<Parent> findByEmail(String email);
}
