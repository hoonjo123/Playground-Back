package com.swyp.playground.domain.note.controller;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.playground.domain.comment.domain.Comment;
import com.swyp.playground.domain.note.domain.Note;
import com.swyp.playground.domain.note.dto.WriteNoteDto;

import com.swyp.playground.domain.note.service.NoteService;

@RestController
@RequestMapping("/note")
public class NoteController {
    
    @Autowired
    private NoteService noteService;

    // -- GET --
    @GetMapping("/all")
    public ResponseEntity<List<Note>> getAllNotes() {
        return new ResponseEntity<List<Note>>(noteService.getAllNotes(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Optional<Note>> getNote(@RequestParam Long id) {
        return new ResponseEntity<Optional<Note>>(noteService.getNoteById(id), HttpStatus.OK);
    }

    // -- POST --
    @PostMapping
    public ResponseEntity<Note> sendNote(@RequestBody WriteNoteDto writeNoteDto) {
        Note targetNote = new Note();

        targetNote.setNoteId(writeNoteDto.getNoteId());
        targetNote.setTargetId(writeNoteDto.getTargetId());
        targetNote.setContent(writeNoteDto.getContent());
        targetNote.setWrittenBy(writeNoteDto.getWrittenBy());
        targetNote.setSentAt(getCurrentDateTime());

        targetNote = noteService.sendNote(targetNote);
        
        return new ResponseEntity<>(targetNote, HttpStatus.CREATED);
    }

    // -- UPDATE --

    // -- DELETE --

    // -- Other Methods --
    public Date getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return calendar.getTime();
    }
}
