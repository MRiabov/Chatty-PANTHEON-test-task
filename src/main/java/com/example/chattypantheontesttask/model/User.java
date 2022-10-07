package com.example.chattypantheontesttask.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class User {

    private final String host;
    private final String username;
    private User talkingTo;

}
