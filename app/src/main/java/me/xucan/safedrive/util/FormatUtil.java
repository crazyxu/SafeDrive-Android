package me.xucan.safedrive.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by xucan on 2016/4/28.
 */
public class FormatUtil {
    public static String keepTwo(float value){
        DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(value);//format 返回的是字符串
    }

    public static float KeepTwo(float value){
        BigDecimal bg = new BigDecimal(value);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
