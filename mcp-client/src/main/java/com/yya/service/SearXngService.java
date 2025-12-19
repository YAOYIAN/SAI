package com.yya.service;

import com.yya.bean.SearchResult;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;

import java.util.List;

public interface SearXngService {

    public List<SearchResult> search(String query);
}
