package com.yya.service.impl;

import com.yya.service.DocumentService;
import com.yya.utils.CustomTextSplitter;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentImpl implements DocumentService {

    private final RedisVectorStore redisVectorStore;

    @Override
    public List<Document> loadText(org.springframework.core.io.Resource resource, String fileName) {
        TextReader textReader = new TextReader(resource);
        textReader.getCustomMetadata().put("fileName", fileName);
        List<Document> documentList = textReader.get();

//        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
//        List<Document> list = tokenTextSplitter.apply(documentList);

        CustomTextSplitter tokenTextSplitter = new CustomTextSplitter();
        List<Document> list = tokenTextSplitter.apply(documentList);
        System.out.println("documentList = " + list);
        redisVectorStore.add(list);
        return documentList;
    }

    @Override
    public List<Document> doSearch(String question) {
        return redisVectorStore.similaritySearch(question);
    }
}
