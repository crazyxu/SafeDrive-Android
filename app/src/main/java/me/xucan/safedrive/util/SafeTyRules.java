package me.xucan.safedrive.util;

import me.xucan.safedrive.bean.DriveWarn;

/**
 * Created by xcytz on 2016/4/24.
 */
public class SafeTyRules {
    public final static String NORMAL = "正常驾驶";
    public final static String VIGILANT = "警惕驾驶";
    public final static String DANGEROUS = "危险驾驶";

    public final static String OVERSPEEND = "超速风险";
    public final static String CRASH = "追尾风险";

    public  static String getState(DriveWarn warn){
        return NORMAL;
    }
}
