package com.yya.controller;

import com.yya.bean.ChatEntity;
import com.yya.service.ChatService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("chat")
public class ChatController {
    // http://localhost:8080/chat

    @Autowired
    private ChatService chatService;

    @PostMapping("doChat")
    public void chat(@RequestBody ChatEntity chatEntity) {
        chatService.doChat(chatEntity);
    }
}
