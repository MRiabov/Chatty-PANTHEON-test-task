package com.example.chattypantheontesttask.model;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class Message {
    @NonNull
    private final String text;
    private final User sentBy;

    public Message(@NonNull String text, User sentBy) {
        this.text = text;
        this.sentBy = new User(null, sentBy.getUsername());
    }
}
