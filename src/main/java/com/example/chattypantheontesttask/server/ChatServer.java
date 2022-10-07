package com.example.chattypantheontesttask.server;

import com.example.chattypantheontesttask.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ChatServer {
    private final Map<String, User> connectedUsers;

    public boolean quit(String username){
        return connectedUsers.remove(username) != null;
    }

    public void register(User user){
        connectedUsers.put(user.getUsername(),user);
    }

    public List<String> getAllUsers(){
        return connectedUsers.keySet().stream().toList();
    }

    public Optional<User> getByUsername(String username){
        return Optional.ofNullable(connectedUsers.get(username));
    }

    public Optional<User> getByHost(String host){
        return connectedUsers.values().stream()
                .filter(u -> u.getHost().equals(host))
                .findAny();
    }

    public void changeTalkingTo(User changedUser, User nowTalksTo){
        changedUser.setTalkingTo(nowTalksTo);
    }
}
