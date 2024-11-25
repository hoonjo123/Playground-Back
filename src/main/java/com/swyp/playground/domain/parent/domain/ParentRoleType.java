package com.swyp.playground.domain.parent.domain;


public enum ParentRoleType {
    FATHER("아빠"),
    MOTHER("엄마");

    private final String description;

    ParentRoleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

