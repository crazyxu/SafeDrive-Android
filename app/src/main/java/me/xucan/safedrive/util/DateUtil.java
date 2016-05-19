package me.xucan.safedrive.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xcytz on 2016/4/22.
 */
public class DateUtil {

    /**
     * 获取时间差
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getDuration(long startTime, long endTime){
        int second = (int) ((endTime -  startTime)/1000);
        int hour = second/3600;
        int minute = (second - hour*3600)/60 +1;
        String res = "";
        if (hour != 0)
            res += hour + "小时";
        res += minute + "分钟";
        return res;
    }

    /**
     * 获取简化时间
     * @param time
     * @return
     */
    public static String getDate(long time){
        SimpleDateFormat newSdf = new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);
        try {
            Date date = new Date(time);
            return newSdf.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSimplifyDate(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
        return sdf.format(time);
    }

    public static String getSimplifyTime(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdf.format(time);
    }


    public static long getTime(){
        return new Date().getTime();
    }
}
