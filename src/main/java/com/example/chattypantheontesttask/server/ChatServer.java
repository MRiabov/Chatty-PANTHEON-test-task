package com.example.chattypantheontesttask.server;

import com.example.chattypantheontesttask.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Getter
@Component
public class ChatServer {
//    private final int port;
    private final Map<String, User> connectedUsers;


}
