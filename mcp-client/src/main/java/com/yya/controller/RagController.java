package com.yya.controller;

import com.yya.bean.ChatEntity;
import com.yya.service.ChatService;
import com.yya.service.DocumentService;
import com.yya.utils.LeeResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("rag")
public class RagController {
    // http://localhost:8080/chat

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ChatService chatService;

    @PostMapping("uploadRagDoc")
    public LeeResult uploadRagDoc(@RequestParam("file") MultipartFile file) {
        List<Document> documentList =  documentService.loadText( file.getResource(), file.getOriginalFilename());
        return LeeResult.ok(documentList);
    }

    @GetMapping("doSearch")
    public LeeResult doSearch(@RequestParam String question) {
        return LeeResult.ok(documentService.doSearch(question));
    }

    // http://172.17.13.225:9090/rag/search
    @PostMapping("search")
    public void Search(@RequestBody ChatEntity chatEntity, HttpServletResponse response) {
        List<Document> list = documentService.doSearch(chatEntity.getMessage());
        response.setCharacterEncoding("UTF-8");
        chatService.doChatRagSearch(chatEntity,list);
    }
}
