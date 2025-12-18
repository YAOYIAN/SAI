package com.yya.service.impl;

import com.yya.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
public class ChatServiceImpl implements ChatService {

    private ChatClient chatClient;

    public ChatServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.defaultSystem("你是一个聪明的人工智能助手，可以解决很多问题，你的名字叫‘Bill’").build();
    }
    @Override
    public String chatTest(String prompt) {
        return chatClient.prompt(prompt).call().content();
    }


    @Override
    public Flux<ChatResponse> streamResponse(String prompt) {
        return chatClient.prompt(prompt).stream().chatResponse();
    }

    @Override
    public Flux<String> streamStr(String prompt) {
        return chatClient.prompt(prompt).stream().content();
    }
}










