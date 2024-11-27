package com.swyp.playground.domain.note.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swyp.playground.domain.note.domain.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
    
}
