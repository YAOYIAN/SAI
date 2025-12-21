package com.yya.service.impl;

import cn.hutool.json.JSONUtil;
import com.yya.bean.ChatEntity;
import com.yya.bean.ChatResponseEntity;
import com.yya.bean.SearchResult;
import com.yya.enums.SSEMsgType;
import com.yya.service.ChatService;
import com.yya.service.SearXngService;
import com.yya.utils.SSEServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    private ChatClient chatClient;

    @Autowired
    private SearXngService searXngService;

    private ChatMemory chatMemory;

    public ChatServiceImpl(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder.defaultToolCallbacks(tools).defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
             //   .defaultSystem("你的名字叫‘Bill’")
                .build();
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

    private static final String ragPROMPT = """
                                              基于上下文的知识库内容回答问题：
                                              【上下文】
                                              {context}
                                              
                                              【问题】
                                              {question}
                                              
                                              【输出】
                                              如果没有查到，请回复：不知道。
                                              如果查到，请回复具体的内容。不相关的近似内容不必提到。
                                              """;
    @Override
    public void doChatRagSearch(ChatEntity chatEntity, List<Document> ragContext) {
        String  userId = chatEntity.getCurrentUserName();
        String  question = chatEntity.getMessage();
        String  botMsgId = chatEntity.getBotMsgId();

        String context = null;
        if(ragContext != null && ragContext.size() >0){
            context = ragContext.stream()
                    .map(Document::getText)
                    .collect(Collectors.joining("\n"));
        }
        Prompt prompt = new Prompt(ragPROMPT.replace("{context}",context)
                .replace("{question}",question));

        log.info("收到prompt如下:");
        log.info(prompt.toString());

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

    private static final String searXngPROMPT = """
                                              你是一个互联网搜索大师，请基于以下互联网返回的结果，并综合你的理解和用户的问题，生成专业的回答：
                                              【上下文】
                                              {context}
                                              
                                              【问题】
                                              {question}
                                              
                                              【输出】
                                              如果没有查到，请回复：不知道。
                                              如果查到，请回复具体的内容。
                                              """;

    @Override
    public void doInternetSearch(ChatEntity chatEntity) {
        String  userId = chatEntity.getCurrentUserName();
        String  question = chatEntity.getMessage();
        String  botMsgId = chatEntity.getBotMsgId();
        List<SearchResult> searchResults = searXngService.search(question);

        String finalPrompt = buildSearXngPrompt(question,searchResults);

        Prompt prompt = new Prompt(finalPrompt);

        log.info("收到prompt如下:");
        log.info(prompt.toString());

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

    private static String buildSearXngPrompt(String question, List<SearchResult> searchResults){
        StringBuilder contextBuilder = new StringBuilder();

        searchResults.forEach(searchResult -> {
            contextBuilder.append(
                            String.format("<context>\n[来源] %s \n [摘要] %s \n </context>\n",
                            searchResult.getUrl(),
                            searchResult.getContent()));
        });
        return searXngPROMPT.replace("{context}",contextBuilder.toString())
                            .replace("{question}",question);
    }
}










