package me.xucan.safedrive;

import android.app.Application;
import android.content.Context;

/**
 * Created by xcytz on 2016/4/22.
 */
public class App extends Application {
    private static App app;
    private Context context;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int userId;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = this.getApplicationContext();
    }

    public Context getContext(){
        return context;
    }

    public static App getInstance(){
        return app;
    }
}
