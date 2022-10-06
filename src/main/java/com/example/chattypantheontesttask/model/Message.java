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

}
