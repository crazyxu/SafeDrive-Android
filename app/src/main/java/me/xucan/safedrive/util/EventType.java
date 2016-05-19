package me.xucan.safedrive.util;

import me.xucan.safedrive.bean.DriveEvent;

/**
 * Created by xcytz on 2016/4/23.
 * 行车事件类型
 */
public class EventType {
    //急刹
    public final static int EVENT_BRAKES = 0x00;

    //疲劳
    public final static int EVENT_FATIGUE = 0x01;

    //偏移
    public final static int EVENT_SKEWING = 0x02;

    //加速
    public final static int EVENT_ACCELERATION = 0x03;

    //减速
    public final static int EVENT_DECELERATION = 0x04;

    //交通良好
    public final static int ENVIR_NORMAL = 0x05;

    //交通拥堵
    public final static int ENVIR_JAM = 0x06;

    //交通雨天
    public final static int ENVIR_RAIN = 0x07;

    //交通大雾
    public final static int ENVIR_FOG = 0x08;

    //时间流逝
    public final static int EVENT_NO = 0x09;


    //超速警告
    public final static int WARN_OVER_SPEED = 0x10;

    //追尾警告
    public final static int WARN_CRASH = 0x20;

    //擦蹭警告
    public final static int WARN_FRACTION = 0x30;

    //疲劳驾驶
    public final static int WARN_FATIGUE = 0x40;

    public static String  getTip(DriveEvent event){
        if (event.getType() == -1){
            return event.getExtra();
        }
        switch (event.getType()){
            case EVENT_BRAKES:
                return "急刹";

            case EVENT_FATIGUE:
                return "疲劳";

            case EVENT_SKEWING:
                return "偏移";

            case EVENT_ACCELERATION:
                return "加速";

            case EVENT_DECELERATION:
                return "减速";

            case WARN_OVER_SPEED:
                return "超速风险";
            case WARN_CRASH:
                return "追尾风险";
            case WARN_FRACTION:
                return "擦蹭风险";
            case WARN_FATIGUE:
                return "疲劳驾驶";
            case EVENT_NO:
                return "时间流逝";
        }
        return "未定义";
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
