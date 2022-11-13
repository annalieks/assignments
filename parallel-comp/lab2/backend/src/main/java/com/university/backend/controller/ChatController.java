package com.university.backend.controller;

import com.university.backend.dto.EventDto;
import com.university.backend.dto.EventType;
import com.university.backend.model.ChatMessage;
import com.university.backend.model.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Controller
public class ChatController {

    private final SimpMessagingTemplate template;

    public ChatController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/register")
    public void registerUser(@Payload User user, SimpMessageHeaderAccessor header) {
        header.getSessionAttributes().put("username", user.getName());
        ChatMessage message = ChatMessage.builder()
                .content("User registered on the server")
                .sender(user.getName())
                .build();
        handleEvent(EventDto.builder().type(EventType.REGISTER).message(message).build());
    }

    @MessageMapping("/chat")
    public void sendMessage(@Payload ChatMessage message) {
        if (message.getRecipient() == null) {
            template.convertAndSend("/topic/public", message);
            handleEvent(EventDto.builder().type(EventType.PUBLIC_MESSAGE).message(message).build());
        } else {
            template.convertAndSend("/queue/" + message.getRecipient(), message);
            handleEvent(EventDto.builder().type(EventType.PRIVATE_MESSAGE).message(message).build());
        }
    }

    private synchronized void handleEvent(EventDto dto) {
        template.convertAndSend("/topic/events",  dto);
        String log = dto.getType() + ": " + dto.getMessage() + "\n";
        try {
            Files.write(Paths.get("journal.log"), log.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("Could not append message to the log file", e);
        }
    }

}
