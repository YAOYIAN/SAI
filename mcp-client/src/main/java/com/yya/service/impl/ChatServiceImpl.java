package com.yya.service.impl;

import cn.hutool.json.JSONUtil;
import com.yya.bean.ChatResponseEntity;
import com.yya.enums.SSEMsgType;
import com.yya.service.ChatService;
import com.yya.utils.SSEServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
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

    @Override
    public void doChat(com.yya.bean.ChatEntity chatEntity) {
        String  userId = chatEntity.getCurrentUserName();
        String  prompt = chatEntity.getMessage();
        String  botMsgId = chatEntity.getBotMsgId();
        Flux<String> stringFlux = chatClient.prompt(prompt).stream().content();
        List<String> list =  stringFlux.toStream().map(s -> {
            String content = s.toString();
            SSEServer.sendMsg(userId,content, SSEMsgType.ADD);
            log.info("发送给用户:{} 内容:{}",userId,content);
            return content;
        }).collect(Collectors.toList());
        String fulContent = list.stream().collect(Collectors.joining());
        ChatResponseEntity chatResponseEntity = new ChatResponseEntity(fulContent, botMsgId);
        SSEServer.sendMsg(userId, JSONUtil.toJsonStr(chatResponseEntity), SSEMsgType.FINISH);
    }
}










