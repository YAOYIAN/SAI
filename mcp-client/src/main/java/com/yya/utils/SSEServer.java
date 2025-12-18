package com.yya.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.yya.enums.SSEMsgType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
public class SSEServer {
    private static final Map<String, SseEmitter> sseClients = new ConcurrentHashMap<>();

    public static SseEmitter connect(String userId){
        SseEmitter sseEmitter = new SseEmitter(0L);

        sseEmitter.onTimeout(timeoutCallback(userId));
        sseEmitter.onCompletion(completionCallback(userId));
        sseEmitter.onError(errorCallback(userId));

        sseClients.put(userId,sseEmitter);
        log.info("SSE连接创建成功，用户ID: {}",userId);
        System.out.println("SSE连接创建成功，用户ID: " + userId);
        return sseEmitter;
    }

    public static void sendMsg(String userId,
                               String message,
                               SSEMsgType msgType){
        if(CollectionUtils.isEmpty(sseClients)){
            return;
        }
        if(sseClients.containsKey(userId)){
            SseEmitter sseEmitter = sseClients.get(userId);
            sendEmitterMessage(sseEmitter,userId,message,msgType);
        }

    }

    private static void sendEmitterMessage(SseEmitter sseEmitter,
                                          String userId,
                                          String message,
                                          SSEMsgType msgType){
        try {
            SseEmitter.SseEventBuilder msgEvent = SseEmitter.event()
                    .id(userId)
                    .name(msgType.type)
                    .data(message);
            sseEmitter.send(msgEvent);
        } catch (IOException e) {
            log.error("SSE发送消息失败，用户ID: {}, 错误信息: {}",userId,e.getMessage());
            remove(userId);
        }
    }
    public static Runnable timeoutCallback(String userId){
        return () -> {
            log.info("SSE超时，用户ID: {}",userId);
            remove(userId);
        };
    }
    public static Runnable completionCallback(String userId){
        return () -> {
            log.info("SSE完成，用户ID: {}",userId);
            remove(userId);
        };
    }
    public static Consumer<Throwable> errorCallback(String userId){
        return  Throwable -> {
            log.info("SSE错误，用户ID: {}",userId);
            remove(userId);
        };
    }


    public static void remove(String userId){
        sseClients.remove(userId);
        log.info("SSE断开，用户ID: {}",userId);
    }
}
