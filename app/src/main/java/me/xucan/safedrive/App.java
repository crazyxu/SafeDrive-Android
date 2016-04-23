package me.xucan.safedrive;

import android.app.Application;
import android.content.Context;

/**
 * Created by xcytz on 2016/4/22.
 */
public class App extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
