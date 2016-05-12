package me.xucan.safedrive.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        SimpleDateFormat sdf = new SimpleDateFormat("HH时mm分", Locale.CHINA);
        return sdf.format(endTime - startTime);
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

    /**
     * 获取当前时分
     * @param mss
     * @return
     */
    public static String parseMillis(long mss){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mss);
        return calendar.get(calendar.HOUR_OF_DAY) + ":" + calendar.get(calendar.MINUTE);
    }

    public static long getTime(){
        return new Date().getTime();
    }
}
