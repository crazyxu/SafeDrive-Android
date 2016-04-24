package me.xucan.safedrive.message;

/**
 * @author xucan
 * Eventbus事件消息
 */
public class MessageEvent {
    public final String message;
    public Object data;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}