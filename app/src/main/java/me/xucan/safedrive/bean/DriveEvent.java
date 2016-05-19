package me.xucan.safedrive.bean;

/**
 * Created by xcytz on 2016/4/23.
 * 行车过程中的事件
 */
public class DriveEvent {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    //事件类型
    private int type;
    //一次出行DriveRecord Id
    private int recordId;
    //发生时间
    private long time;
    //extra
    private String extra;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
