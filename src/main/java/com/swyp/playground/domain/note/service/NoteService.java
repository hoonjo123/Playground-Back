package com.swyp.playground.domain.note.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swyp.playground.domain.note.domain.Note;
import com.swyp.playground.domain.note.dto.WriteNoteDto;
import com.swyp.playground.domain.note.repository.NoteRepository;
import com.swyp.playground.domain.parent.repository.ParentRepository;

@Service
public class NoteService {
    
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ParentRepository parentRepository;

    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);
    
    public Note sendNote(Note note) {
        Long parentId = parentRepository.findByNickname(note.getWrittenBy()).get().getParentId();
        note.setWriterId(parentId);
        note.setRead(false);
        return noteRepository.save(note);
    }

    public List<Note> getAllNotes(String email) {
        String targetNickname = parentRepository.findByEmail(email).get().getNickname();

        List<Note> receivedNotes = noteRepository.findAllByTargetNickname(targetNickname);

        List<Note> sentNotes = noteRepository.findAllByWrittenBy(targetNickname);

        for (Note note : receivedNotes) {
            if (!note.isRead()) {
                note.setRead(true);
                noteRepository.save(note);
            }
        }
        
        receivedNotes.addAll(sentNotes);
        receivedNotes.sort(Comparator.comparing(Note::getSentAt));

        return receivedNotes;
    }

    public Note getNoteById(Long id) {
        return noteRepository.findById(id)
        .orElseThrow(() -> new NullPointerException("해당 쪽지가 존재하지 않습니다."));
    }

    public void patchNoteById(Long id, WriteNoteDto newNote, String email) {
        try {
            Note prevNote = noteRepository.findById(id)
                            .orElseThrow(() -> new NullPointerException("해당 쪽지가 존재하지 않습니다."));
            // 실제 작성자 검증 로직
            if (prevNote.getWrittenBy() == parentRepository.findByEmail(email).get().getNickname()) {
                if (prevNote.getContent() != newNote.getContent()) { // 변경사항 있을 시에만 업데이트
                    prevNote.setContent(newNote.getContent());
                    noteRepository.save(prevNote);
                }
            } else {
                throw new IllegalAccessError("작성자만 수정할 수 있습니다.");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void deleteNote(Long id, String email) {
        String nickname = parentRepository.findByEmail(email).get().getNickname();
        String writtenBy = noteRepository.findById(id).get().getWrittenBy();
        if (nickname.equals(writtenBy)) {
            noteRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
    }
}
