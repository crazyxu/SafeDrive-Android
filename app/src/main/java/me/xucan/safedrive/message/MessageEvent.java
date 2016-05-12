package me.xucan.safedrive.message;

import com.alibaba.fastjson.JSONObject;

/**
 * @author xucan
 * Eventbus事件消息
 */
public class MessageEvent {
    public final String message;
    public JSONObject object;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(String message, JSONObject object) {
        this.message = message;
        this.object = object;
    }
}