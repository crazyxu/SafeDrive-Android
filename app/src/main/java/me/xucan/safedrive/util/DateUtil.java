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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date startDate = new Date(startTime);
            Date endDate = new Date(endTime);
            long mss = endDate.getTime() - startDate.getTime();
            long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
            String distance = "";
            if (hours != 0){
                distance += hours + "小时";
            }
            if (minutes != 0){
                distance += minutes + "分钟";
            }
            return distance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取简化时间
     * @param time
     * @return
     */
    public static String getSimplifyDate(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        SimpleDateFormat newSdf = new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);
        try {
            Date date = new Date(time);
            return newSdf.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前时分
     * @param mss
     * @return
     */
    public static String parseMillis(long mss){
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        String distance = "";
        if (hours != 0){
            distance += hours + ":";
        }
        if (minutes != 0){
            distance += minutes;
        }
        return distance;
    }

    public static long getTime(){
        return new Date().getTime();
    }
}
