package com.abhiroop.chatbackend.lib.enums;

public enum ParticipantRole {
    OWNER,
    ADMIN,
    MEMBER;

    public boolean hasEditAccess() {
        return this == ADMIN || this == OWNER;
    }
}
