package com.example.chattypantheontesttask.service;

import com.example.chattypantheontesttask.model.Message;
import com.example.chattypantheontesttask.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SenderService {

    public boolean sendMessage(User sentBy, User sentTo, String messageText){
        Message message = new Message(messageText,sentBy);
        new RestTemplate().put("http://"+sentTo.getHost()+"/recieveMessage",null,
                sentBy.getUsername(),messageText);
        ResponseEntity<Message> sentMessage = new RestTemplate().
                postForEntity("http://"+sentTo.getHost()+"/recieveMessage", message, Message.class);
        return (sentMessage.getStatusCode()==HttpStatus.OK);
    }

}
