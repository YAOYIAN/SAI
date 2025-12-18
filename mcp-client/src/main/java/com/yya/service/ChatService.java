package com.yya.service;

import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

public interface ChatService {
    public String chatTest(String prompt);

    public Flux<ChatResponse> streamResponse(String prompt);

    public Flux<String> streamStr(String prompt);
}
