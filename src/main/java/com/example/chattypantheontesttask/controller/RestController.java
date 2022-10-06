package com.example.chattypantheontesttask.controller;

import com.example.chattypantheontesttask.model.User;
import com.example.chattypantheontesttask.server.ChatServer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
public class RestController {

    private final ChatServer chatServer;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam(name = "username") String username) {
        chatServer.getConnectedUsers().put(username, new User(123, username));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Successfully registered user with username " + username);
    }

    @DeleteMapping("/quit")
    public ResponseEntity<String> deleteUser(@RequestParam(name = "username") String username) {
        if (chatServer.getConnectedUsers().remove(username) != null)
            return ResponseEntity.ok("User " + username + " successfully quit!");
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("User with username " + username + "not found!");
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<String>> getAllUsers() {
        if (chatServer.getConnectedUsers().isEmpty())
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        return ResponseEntity.ok(chatServer.getConnectedUsers().keySet().stream().toList());
    }


}
