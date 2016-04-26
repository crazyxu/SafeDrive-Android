package me.xucan.safedrive.bean;

/**
 * Created by xcytz on 2016/4/24.
 * 服务器返回的警告提示
 */
public class DriveWarn {
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    private int recordId;
    private int safetyPoint;
    private int[] warns;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }



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
