package com.example.chattypantheontesttask.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Message {
    @NonNull
    private final String text;
    private final User sentTo;
    private final User sentBy;

    public Message(@NonNull String text, User sentBy) {
        this.text = text;
        this.sentBy = new User(null, sentBy.getUsername());
        this.sentTo=null;
    }
}
