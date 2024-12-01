package com.swyp.playground.domain.note.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swyp.playground.domain.note.domain.Note;
import com.swyp.playground.domain.note.dto.WriteNoteDto;
import com.swyp.playground.domain.note.repository.NoteRepository;

@Service
public class NoteService {
    
    @Autowired
    private NoteRepository noteRepository;
    
    /* -- POST -- */
    public Note sendNote(Note note) {
        return noteRepository.save(note);
    }

    /* -- GET -- */
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(Long id) {
        
        return noteRepository.findById(id)
        .orElseThrow(() -> new NullPointerException("해당 쪽지가 존재하지 않습니다."));
    }

    /* -- UPDATE -- */
    public void patchNoteById(Long id, WriteNoteDto newNote) {
        try {
            Note prevNote = noteRepository.findById(id)
                            .orElseThrow(() -> new NullPointerException("해당 쪽지가 존재하지 않습니다."));
            
            if (prevNote.getContent() != newNote.getContent()) {
                prevNote.setContent(newNote.getContent());
            }
            
            noteRepository.save(prevNote);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /* -- DELETE -- */
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
}
