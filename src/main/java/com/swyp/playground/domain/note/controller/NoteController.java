package com.swyp.playground.domain.note.controller;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.playground.domain.note.domain.Note;
import com.swyp.playground.domain.note.dto.WriteNoteDto;
import com.swyp.playground.common.redis.RedisService;

import com.swyp.playground.domain.note.service.NoteService;


@RestController
@RequestMapping("/note")
public class NoteController {
    
    @Autowired
    private NoteService noteService;

    private RedisService redisService;

    // -- GET --
    @GetMapping("/all")
    public ResponseEntity<List<Note>> getAllNotes(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<List<Note>>(noteService.getAllNotes(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Note> getNote(@RequestParam Long id) {
        return new ResponseEntity<Note>(noteService.getNoteById(id), HttpStatus.OK);
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
    @PatchMapping
    public ResponseEntity<String> updateNote(@RequestParam Long id, @RequestBody WriteNoteDto newNote) {
        try {
            noteService.patchNoteById(id, newNote);
        } catch (Exception e) {
            // 디버깅 처리
            System.err.println(e);
        }
        return ResponseEntity.ok("쪽지가 정상적으로 삭제되었습니다.");
    }

    // -- DELETE --
    @DeleteMapping
    public ResponseEntity<String> deleteNote(@RequestParam Long id) {
        try {
            noteService.deleteNote(id);
        } catch (Exception e) {
            // 디버깅 처리
            System.err.println(e);
        }
        return ResponseEntity.ok("쪽지가 정상적으로 삭제되었습니다.");
    }

    // -- Other Methods --
    public Date getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return calendar.getTime();
    }
}
