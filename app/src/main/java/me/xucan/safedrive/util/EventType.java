package me.xucan.safedrive.util;

import me.xucan.safedrive.bean.DriveEvent;

/**
 * Created by xcytz on 2016/4/23.
 * 行车事件类型
 */
public class EventType {
    //急刹
    public final static int BRAKES = 0x00;

    //疲劳
    public final static int FATIGUE = 0x01;

    //抖动
    public final static int JITTER = 0x02;

    //超速
    public final static int OVERSPEEDS = 0x03;

    //偏移
    public final static int SKEWING = 0x04;

    //急刹
    public final static int CRASH = 0x05;


    //超速警告
    public final static int WARN_OVERSPEEDS = 0x10;

    //追尾警告
    public final static int WARN_CRASH = 0x20;

    public static String  getTip(DriveEvent event){
        if (event.getType() == -1){
            return event.getExtra();
        }
        switch (event.getType()){
            case WARN_OVERSPEEDS:
                return "超速警告";
            case WARN_CRASH:
                return "追尾警告";
        }
        return "";
    }

    /**
     * 区别类型
     * @param type
     * @return
     */
    public static boolean isWarn(int type){
        if (type >= 0x10)
            return true;
        else
            return false;
    }

}
