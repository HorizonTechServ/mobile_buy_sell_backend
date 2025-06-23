package com.one.societyAPI.controller;

import com.one.societyAPI.dto.ChatMessageDTO;
import com.one.societyAPI.entity.ChatMessage;
import com.one.societyAPI.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageRepository chatRepo;

    @MessageMapping("/chat") // listens to /app/chat
    public void handleMessage(@Payload ChatMessageDTO message) {
        // Save message to DB
        ChatMessage entity = new ChatMessage();
        entity.setComplaintId(message.getComplaintId());
        entity.setSender(message.getSender());
        entity.setContent(message.getContent());
        entity.setTimestamp(LocalDateTime.now());
        chatRepo.save(entity);

        // Broadcast to /topic/complaint/{id}
        messagingTemplate.convertAndSend("/topic/complaint/" + message.getComplaintId(), message);
    }
}