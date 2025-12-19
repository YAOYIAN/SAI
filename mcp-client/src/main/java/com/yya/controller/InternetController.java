package com.yya.controller;

import com.yya.bean.ChatEntity;
import com.yya.service.ChatService;
import com.yya.service.DocumentService;
import com.yya.service.SearXngService;
import com.yya.utils.LeeResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("internet")
@Slf4j
public class InternetController {
    // http://localhost:8080/chat

    @Autowired
    private SearXngService searXngService;

    @Autowired
    private ChatService chatService;

    // http://172.17.13.225:9090/rag/search
    @GetMapping("test")
    public Object test(@RequestParam("query") String query) {
        return searXngService.search(query);
    }

    @PostMapping("search")
    public void search(@RequestBody ChatEntity chatEntity, HttpServletResponse response) {
        log.info("InternetController search called with message: {}", chatEntity.getMessage());
        response.setCharacterEncoding("UTF-8");
        chatService.doInternetSearch(chatEntity);
    }
}
