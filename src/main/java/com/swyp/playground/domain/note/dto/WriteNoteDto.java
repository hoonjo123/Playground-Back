package com.swyp.playground.domain.note.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WriteNoteDto {
    
    private Long noteId;

    private Long targetId;

    private String content;

    private String writtenBy;
    
}
