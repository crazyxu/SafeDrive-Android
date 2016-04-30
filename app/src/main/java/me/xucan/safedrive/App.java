package me.xucan.safedrive;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import org.xutils.x;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.ipc.RongExceptionHandler;
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
    private int userId = 1;



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
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            RongIMClient.init(this);

            /**
             * 融云SDK事件监听处理
             *
             * 注册相关代码，只需要在主进程里做。
             */
            if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

                RongCloudEvent.init(this);

                Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));

                try {
                    RongIMClient.registerMessageType(DriveWarnMessage.class);

                } catch (Exception e) {
                    e.printStackTrace();
                }
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
