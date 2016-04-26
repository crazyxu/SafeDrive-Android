package me.xucan.safedrive.bean;

/**
 * Created by xucan on 2016/4/25.
 * 用于drive record detail 中显示行车事件和系统警告
 */
public class EventWarn {
    //0表示event，1表示warn
    private int type;
    private String msg;
    private long time;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
