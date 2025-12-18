package com.yya.service;

import com.yya.bean.ChatEntity;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;

import java.util.List;

public interface DocumentService {

    public List<Document> loadText(Resource resource, String fileName);

    public List<Document> doSearch(String question);
}
