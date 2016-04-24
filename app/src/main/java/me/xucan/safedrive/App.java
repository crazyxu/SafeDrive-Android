package me.xucan.safedrive;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import org.xutils.x;

import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;
import me.xucan.safedrive.message.DriveWarnMessage;

/**
 * Created by xcytz on 2016/4/22.
 */
public class App extends Application {
    private static App app;
    private Context context;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;
    private int userId;



    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = this.getApplicationContext();
        x.Ext.init(this);
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIMClient.init(this);
            try {
                //注册自定义消息
                RongIMClient.registerMessageType(DriveWarnMessage.class);
            } catch (AnnotationNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Context getContext(){
        return context;
    }

    public static App getInstance(){
        return app;
    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }


}
