package com.yya.enums;

public enum SSEMsgType {
    MESSAGE("message","单次发送的普通消息类型"),
    ADD("add","消息追加"),
    FINISH("finish","消息完成"),
    CUSTOM_EVENT("custom_event","用户自定义事件"),
    done("done","单次发送的普通消息类型");

    public final String type;
    public final String value;

    SSEMsgType(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
