package com.swyp.playground.domain.child.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ChildGenderType {
    MALE("남자"),
    FEMALE("여자");

    private final String description;

    ChildGenderType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
