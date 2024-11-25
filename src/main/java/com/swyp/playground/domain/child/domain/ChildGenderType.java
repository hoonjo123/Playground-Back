package com.swyp.playground.domain.child.domain;

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
