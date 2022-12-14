package com.example.chattypantheontesttask.controller;

import com.example.chattypantheontesttask.model.User;
import com.example.chattypantheontesttask.server.ChatServer;
import com.example.chattypantheontesttask.service.SenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
@RequiredArgsConstructor
public class RestController {

    private final SenderService senderService;
    private final ChatServer chatServer;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam("username") String username, @RequestHeader("Host") String host) {
        chatServer.register(new User(host, username));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Successfully registered user with username " + username);
    }

    @DeleteMapping("/quit")
    public ResponseEntity<String> deleteUser(@RequestParam(name = "username") String username) {
        if (chatServer.quit(username))
            return ResponseEntity.ok("User " + username + " successfully quit!");
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("User with username " + username + "not found!");
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<String>> getAllUsers() {
        List<String> users = chatServer.getAllUsers();
        if (users.isEmpty())
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestParam("text") String text, @RequestHeader("Host") String host) {
        Optional<User> sender = chatServer.getByHost(host);
        if (sender.isEmpty()) return ResponseEntity.badRequest().body("Please register first.");
        User receiver = sender.get().getTalkingTo();
        boolean successfullySent = senderService.sendMessage(sender.get(), receiver, text);
        return ResponseEntity
                .status(successfullySent? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @PutMapping("/talkTo")
    public ResponseEntity<String> talkTo(@RequestParam(name = "recipientName") String recipientName, @RequestHeader("Host") String host) {
        Optional<User> sender = chatServer.getByHost(host);
        if (sender.isEmpty()) return ResponseEntity.badRequest().body("Please register first.");
        Optional<User> receiver = chatServer.getByUsername(recipientName);
        if (receiver.isEmpty()) return ResponseEntity.notFound().build();
        boolean successfullyChanged = chatServer.changeTalkingTo(sender.get(), receiver.get());
        return ResponseEntity
                .status(successfullyChanged?HttpStatus.OK:HttpStatus.BAD_REQUEST)
                .build();
    }

}
