package com.swyp.playground.domain.child.repository;

import com.swyp.playground.domain.child.domain.Child;
import com.swyp.playground.domain.parent.domain.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child,Long> {
}
