package com.example.chattypantheontesttask;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.Console;

public class OnStartup implements ApplicationListener<ContextRefreshedEvent> {

    Console console = System.console();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        String choice = console.readLine("Would you like to be a server or a client?" +
                "\n[S/C]: ");
        boolean isServer = false;
        if (choice.equalsIgnoreCase("S")) {
            chooseServer();
        } else chooseClient();

    }

    private void chooseClient(){
        console.readLine("Fine, choose your port and name. ([port] [name]): ");
    }

    private void chooseServer(){
        console.readLine("Fine, type your port: ");
    }

}
