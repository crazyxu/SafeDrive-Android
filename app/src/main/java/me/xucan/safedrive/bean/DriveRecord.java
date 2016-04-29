package me.xucan.safedrive.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by xcytz on 2016/4/22.
 * 一个行车记录
 */
@Table(name = "tb_drive_record")
public class DriveRecord implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L;

    @Column(name = "recordId", isId = true)
    private int recordId;

    @Column(name = "userId")
    private int userId;

    @Column(name = "startTime")
    private long startTime;

    @Column(name = "endTime")
    private long endTime;

    @Column(name = "startPlace")
    private String startPlace;

    @Column(name = "endPlace")
    private String endPlace;

    @Column(name = "distance")
    private float distance;

    @Column(name = "safetyIndex")
    private int safetyIndex;

    public int getSafetyIndex() {
        return safetyIndex;
    }

    public void setSafetyIndex(int safetyIndex) {
        this.safetyIndex = safetyIndex;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
