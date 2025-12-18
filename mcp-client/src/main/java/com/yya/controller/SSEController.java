package com.yya.controller;

import com.yya.enums.SSEMsgType;
import com.yya.service.ChatService;
import com.yya.utils.SSEServer;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.awt.*;

@RestController
@RequestMapping("sse")
public class SSEController {

    @GetMapping(path = "connect", produces =  {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter connect(@RequestParam String userId) {
        return SSEServer.connect(userId);
    }

    // SSE 发送单个消息
    @GetMapping("sendMessage")
    public Object sendMessage(@RequestParam String userId, @RequestParam String message) {
        SSEServer.sendMsg(userId, message, SSEMsgType.MESSAGE);
        return "消息发送成功";
    }

}
