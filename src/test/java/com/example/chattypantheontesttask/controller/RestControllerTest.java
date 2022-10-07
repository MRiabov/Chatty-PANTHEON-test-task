package com.example.chattypantheontesttask.controller;

import com.example.chattypantheontesttask.model.User;
import com.example.chattypantheontesttask.server.ChatServer;
import com.example.chattypantheontesttask.service.SenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RestController.class})
@ExtendWith(SpringExtension.class)
class RestControllerTest {
    @MockBean
    private ChatServer chatServer;

    @Autowired
    private RestController restController;

    @MockBean
    private SenderService senderService;

    /**
     * Method under test: {@link RestController#deleteUser(String)}
     */
    @Test
    void testDeleteUser() throws Exception {
        // Arrange
        when(chatServer.quit(any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/quit").param("username", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(restController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User foo successfully quit!"));
    }

    /**
     * Method under test: {@link RestController#getAllUsers()}
     */
    @Test
    void noUsersNothingReturned() throws Exception {
        // Arrange
        when(chatServer.getAllUsers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAllUsers");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(restController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    /**
     * Method under test: {@link RestController#getAllUsers()}
     */
    @Test
    void returnsValue() throws Exception {
        // Arrange
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("?");
        when(chatServer.getAllUsers()).thenReturn(stringList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAllUsers");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(restController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[\"?\"]"));
    }

    /**
     * Method under test: {@link RestController#sendMessage(String, String)}
     */
    @Test
    void testSendMessage() throws Exception {
        // Arrange
        when(senderService.sendMessage(any(), any(), any())).thenReturn(true);
        when(chatServer.getByHost(any())).thenReturn(Optional.of(new User("localhost", "janedoe")));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/sendMessage")
                .param("text", "foo")
                .header("Host", "www.example.com:8080");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(restController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link RestController#sendMessage(String, String)}
     */
    @Test
    void messageFailedToSend() throws Exception {
        // Arrange
        when(senderService.sendMessage(any(), any(), any())).thenReturn(false);
        when(chatServer.getByHost(any())).thenReturn(Optional.of(new User("localhost", "janedoe")));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/sendMessage")
                .param("text", "foo")
                .header("Host", "www.example.com:8080");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(restController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500));
    }

    /**
     * Method under test: {@link RestController#sendMessage(String, String)}
     */
    @Test
    void notRegisteredNoMessageSent() throws Exception {
        // Arrange
        when(senderService.sendMessage(any(), any(), any())).thenReturn(true);
        when(chatServer.getByHost(any())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/sendMessage")
                .param("text", "foo")
                .header("Host", "www.example.com:8080");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(restController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().string("Please register first."));
    }

    /**
     * Method under test: {@link RestController#deleteUser(String)}
     */
    @Test
    void noUserFoundReturnedError() throws Exception {
        // Arrange
        when(chatServer.quit(any())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/quit").param("username", "foo");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(restController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(406))
                .andExpect(MockMvcResultMatchers.content().string("User with username foonot found!"));
    }

    /**
     * Method under test: {@link RestController#registerUser(String, String)}
     */
    @Test
    void testRegisterUser() throws Exception {
        // Arrange
        doNothing().when(chatServer).register(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register")
                .param("username", "foo")
                .header("Host", "www.example.com:8080");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(restController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Successfully registered user with username foo"));
    }

    /**
     * Method under test: {@link RestController#talkTo(String, String)}
     */
    @Test
    void testTalkTo() throws Exception {
        // Arrange
        when(chatServer.changeTalkingTo(any(), any())).thenReturn(true);
        when(chatServer.getByUsername(any())).thenReturn(Optional.of(new User("localhost", "janedoe")));
        when(chatServer.getByHost(any())).thenReturn(Optional.of(new User("localhost", "janedoe")));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/talkTo")
                .param("recipientName", "foo")
                .header("Host", "www.example.com:8080");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(restController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link RestController#talkTo(String, String)}
     */
    @Test
    void cantAssignToYourself() throws Exception {
        // Arrange
        when(chatServer.changeTalkingTo(any(), any())).thenReturn(false);
        when(chatServer.getByUsername(any())).thenReturn(Optional.of(new User("localhost", "janedoe")));
        when(chatServer.getByHost(any())).thenReturn(Optional.of(new User("localhost", "janedoe")));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/talkTo")
                .param("recipientName", "foo")
                .header("Host", "www.example.com:8080");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(restController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link RestController#talkTo(String, String)}
     */
    @Test
    void noOtherUserNotReturned() throws Exception {
        // Arrange
        when(chatServer.changeTalkingTo(any(), any())).thenReturn(true);
        when(chatServer.getByUsername(any())).thenReturn(Optional.empty());
        when(chatServer.getByHost(any())).thenReturn(Optional.of(new User("localhost", "janedoe")));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/talkTo")
                .param("recipientName", "foo")
                .header("Host", "www.example.com:8080");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(restController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link RestController#talkTo(String, String)}
     */
    @Test
    void notRegisteredNothingReturned() throws Exception {
        // Arrange
        when(chatServer.changeTalkingTo(any(), any())).thenReturn(true);
        when(chatServer.getByUsername(any())).thenReturn(Optional.of(new User("localhost", "janedoe")));
        when(chatServer.getByHost(any())).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/talkTo")
                .param("recipientName", "foo")
                .header("Host", "www.example.com:8080");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(restController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().string("Please register first."));
    }
}

