package me.xucan.safedrive.message;

import android.util.Log;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import me.xucan.safedrive.App;
import me.xucan.safedrive.RongCloudEvent;

/**
 * Created by crazyxu on 2015/11/11.
 */
public class RongManager {
    /**
     * 连接融云
     * @param callback
     */
    public static void connect(RongIMClient.ConnectCallback callback){
        RongIMClient.connect(App.getInstance().getToken(), callback);
    }

    /**
     * 连接融云
     */
    public static void connect(){
        connect(new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.i("RongCloud", "token error");
            }

            @Override
            public void onSuccess(String s) {
                Log.i("RongCloud", "connect success");
                RongCloudEvent.getInstance().setListener();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.i("RongCloud", "connect error");
            }
        });
    }

    /**
     * 断开连接
     * @param receivePushMsg 断开后是否接收推送消息
     */
    public static void disConnect(boolean receivePushMsg){
        if (RongIMClient.getInstance() != null){
            if (receivePushMsg){
                RongIMClient.getInstance().disconnect();
            }else {
                RongIMClient.getInstance().logout();
            }
        }
    }

    /**
     * 发送消息
     * @param conversationType
     * @param targetId
     * @param content
     * @param callback
     */
    public static void sendMessage(Conversation.ConversationType conversationType, String targetId, final MessageContent content, final RongSendMessageCallBack callback){
        RongIMClient.getInstance().sendMessage(conversationType, targetId, content, "", "", new RongIMClient.SendMessageCallback() {
            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                Log.i("sendMessage", "消息发送失败：" + errorCode.getMessage());
                if (callback != null)
                    callback.onError(integer, errorCode);
            }

            @Override
            public void onSuccess(Integer integer) {
                Log.i("sendMessage", content.toString());
                if (callback != null)
                    callback.onSuccess(integer);

            }
        });
    }

    /**
     * 融云连接回调
     */
    public interface RongConnectCallBack{
        void onSuccess(String s);
        void onError(RongIMClient.ErrorCode errorCode);
    }

    /**
     * 消息发送回调
     */
    public interface RongSendMessageCallBack{
        void onError(Integer integer, RongIMClient.ErrorCode errorCode);
        void onSuccess(Integer integer);
    }
}
