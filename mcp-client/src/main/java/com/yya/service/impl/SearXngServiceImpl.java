package com.yya.service.impl;

import cn.hutool.json.JSONUtil;
import com.yya.bean.SearXNGResponse;
import com.yya.bean.SearchResult;
import com.yya.service.SearXngService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearXngServiceImpl implements SearXngService {

    @Value("${internet.websearch.searxng.url}")
    private String SEARXNG_URL;

    @Value("${internet.websearch.searxng.counts}")
    private Integer COUNTS;

    private final OkHttpClient okHttpClient;

    @Override
    public List<SearchResult> search(String query) {
        HttpUrl url = HttpUrl.get(SEARXNG_URL).newBuilder()
                    .addQueryParameter("q",query)
                    .addQueryParameter("format","json").build();
        log.info("SearXng搜索URL：{}",url.url());
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(!response.isSuccessful()){
                throw new RuntimeException("请求失败 - 状态码：" + response.code());
            }
            if(response.body() != null){
                String responseBody = response.body().string();
                log.info("SearXng搜索响应：{}",responseBody);
                SearXNGResponse searXNGResponse = JSONUtil.toBean(responseBody, SearXNGResponse.class);
                return dealResults(searXNGResponse.getResults());
            }
            log.info("搜索失败:{}",response.message());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return List.of();
    }

    private List<SearchResult> dealResults(List<SearchResult> results){

        return results.subList(0,Math.min(COUNTS,results.size()))
                    .parallelStream()
                    .sorted(Comparator.comparingDouble(SearchResult::getScore)
                            .reversed())
                    .toList();
    }
}
