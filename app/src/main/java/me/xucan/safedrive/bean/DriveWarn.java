package me.xucan.safedrive.bean;

/**
 * Created by xcytz on 2016/4/24.
 * 服务器返回的警告提示
 */
public class DriveWarn {
    private int safetyPoint;
    private int[] warns;

    public int getSafetyPoint() {
        return safetyPoint;
    }

    public void setSafetyPoint(int safetyPoint) {
        this.safetyPoint = safetyPoint;
    }

    public int[] getWarns() {
        return warns;
    }

    public void setWarns(int[] warns) {
        this.warns = warns;
    }
}
