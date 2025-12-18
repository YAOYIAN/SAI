package com.yya.controller;

import com.yya.service.ChatService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("hello")
public class HelloController {
    // http://localhost:8080/hello/world

    @Autowired
    private ChatService chatService;

    @GetMapping("world")
    public String hello() {
        return "Hello, test!";
    }

    @GetMapping("chat")
    public String chat(String msg) {
        return chatService.chatTest(msg);
    }

    @GetMapping("chat/stream/reponse")
    public Flux<ChatResponse> chatStreamResponse(String msg, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return chatService.streamResponse(msg);
    }

    // http://127.0.0.1:9090/hello/chat/stream/str?msg=你是谁？
    @GetMapping("chat/stream/str")
    public Flux<String> chatStreamStr(String msg, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return chatService.streamStr(msg);
    }
}
