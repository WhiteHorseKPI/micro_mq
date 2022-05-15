package com.microservices.messagesservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MessagesController {

    private final MessagesService messagesService;


    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
        Thread t1 = new Thread(messagesService);
        t1.start();
    }


    @GetMapping("/message")
    public String getMessages() {
        return messagesService.getMessages();
    }
}
