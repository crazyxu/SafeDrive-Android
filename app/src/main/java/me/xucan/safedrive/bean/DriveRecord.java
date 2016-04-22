package me.xucan.safedrive.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by xcytz on 2016/4/22.
 * 一个行车记录
 */
@Table(name = "tb_drive_record")
public class DriveRecord {

    @Column(name = "recordId", isId = true)
    private int recordId;

    @Column(name = "userId")
    private int userId;

    @Column(name = "startTime")
    private String startTime;

    @Column(name = "endTime")
    private String endTime;

    @Column(name = "startPlace")
    private String startPlace;

    @Column(name = "endPlace")
    private String endPlace;

    @Column(name = "distance")
    private int distance;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
