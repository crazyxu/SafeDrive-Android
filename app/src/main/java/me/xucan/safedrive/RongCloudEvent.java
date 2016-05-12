package me.xucan.safedrive;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import me.xucan.safedrive.message.DriveWarnMessage;
import me.xucan.safedrive.message.MessageEvent;
import me.xucan.safedrive.ui.fragment.SimulationFragment;

/**
 * 融云SDK事件监听处理。
 * 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * <p/>
 * 该类包含的监听事件有：
 * 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。
 * 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。
 * 5、群组信息提供者：GetGroupInfoProvider。
 * 6、会话界面操作的监听器：ConversationBehaviorListener。
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。
 * 8、地理位置提供者：LocationProvider。
 */

public class RongCloudEvent implements RongIMClient.OnReceiveMessageListener{

    private static RongCloudEvent mRongCloudInstance;
    private Context mContext;

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {
        if (mRongCloudInstance == null) {

            synchronized (RongCloudEvent.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongCloudEvent(context);
                }
            }
        }
    }

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private RongCloudEvent(Context context) {
        mContext = context;
    }

    public static RongCloudEvent getInstance() {
        return mRongCloudInstance;
    }

    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    public void setListener() {
        RongIMClient.setOnReceiveMessageListener(this);
    }

    @Override
    public boolean onReceived(Message message, int i) {
        if (message.getObjectName().equals(DriveWarnMessage.tag)){
            DriveWarnMessage warnMessage = (DriveWarnMessage) message.getContent();
            EventBus.getDefault().post(new MessageEvent(SimulationFragment.EVENT_DRIVE_WARN,
                    JSON.parseObject(warnMessage.getContent())));
        }
        return false;
    }

}