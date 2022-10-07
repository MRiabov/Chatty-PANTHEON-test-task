package com.example.chattypantheontesttask.server;

import com.example.chattypantheontesttask.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {ChatServer.class})
@ExtendWith(SpringExtension.class)
class ChatServerTest {

    @Autowired
    private ChatServer chatServer;

    /**
     * Method under test: {@link ChatServer#quit(String)}
     */
    @Test
    void testQuit() {
        // Arrange, Act and Assert
        assertFalse(chatServer.quit("janedoe"));
        assertEquals(0, chatServer.getAllUsers().size());
    }

    /**
     * Method under test: {@link ChatServer#getAllUsers()}
     */
    @Test
    void returnsUsernames() {
        // Arrange
        chatServer.register(new User("123","user 1"));
        chatServer.register(new User("123","user 2"));
        chatServer.register(new User("123","user 3"));
        // Act
        List<String> actualAllUsers = chatServer.getAllUsers();

        // Assert
        assertEquals(3, actualAllUsers.size());
        assertEquals("user 1", actualAllUsers.get(0));
    }

    /**
     * Method under test: {@link ChatServer#getAllUsers()}
     */
    @Test
    void noUsersNothingReturned() {
        // Arrange and Act
        List<String> actualAllUsers = chatServer.getAllUsers();

        // Assert
        assertEquals(0, actualAllUsers.size());
        assertEquals("com.example.chattypantheontesttask.model.User#0", actualAllUsers.get(0));
    }

    /**
     * Method under test: {@link ChatServer#getByUsername(String)}
     */
    @Test
    void incorrectUsernameIsAbsent() {
        // Arrange, Act and Assert
        assertFalse(chatServer.getByUsername("janedoe").isPresent());
    }

    /**
     * Method under test: {@link ChatServer#getByHost(String)}
     */
    @Test
    void testGetByHost() {
        // Arrange
        chatServer.register(new User("localhost", "janedoe"));

        // Act and Assert
        assertTrue(chatServer.getByHost("localhost").isPresent());
        assertEquals("localhost", chatServer.getByHost("localhost").get().getHost());
    }


    /**
     * Method under test: {@link ChatServer#changeTalkingTo(User, User)}
     */
    @Test
    void testChangeTalkingTo() {
        // Arrange
        User user1 = new User("localhost:8080", "User 1");
        User user2 = new User("localhost:1234", "User 2");
        User user3 = new User("localhost:5555", "User 3");
        user1.setTalkingTo(user2);
        // Act
        chatServer.changeTalkingTo(user1, user3);

        // Assert
        assertEquals(user3, user1.getTalkingTo());
    }

    /**
     * Method under test: {@link ChatServer#changeTalkingTo(User, User)}
     */
    @Test
    void cantTalkToYourself() {
        // Arrange
        User user1 = new User("localhost:8080", "User 1");
        User user2 = new User("localhost:1234", "User 2");
        user1.setTalkingTo(user2);
        // Act and Assert
        assertFalse(chatServer.changeTalkingTo(user1, user1));
        assertEquals(user2, user1.getTalkingTo());
    }
}

