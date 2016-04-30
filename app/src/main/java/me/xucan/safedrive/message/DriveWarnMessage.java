package me.xucan.safedrive.message;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by xcytz on 2016/4/24.
 */

@MessageTag(value = "SD:DriveWarnMsg")
public class DriveWarnMessage extends MessageContent{
    public final static String tag = "SD:DriveWarnMsg";
    private String content;

    public static final Parcelable.Creator<DriveWarnMessage> CREATOR = new Parcelable.Creator() {
        public DriveWarnMessage createFromParcel(Parcel source) {
            return new DriveWarnMessage(source);
        }

        public DriveWarnMessage[] newArray(int size) {
            return new DriveWarnMessage[size];
        }
    };

    public DriveWarnMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            setContent(jsonObj.getString("content"));
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
    }



    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("content",content);
            return jsonObj.toString().getBytes("UTF-8");
        } catch (Exception var4) {
            RLog.e(null, "JSONException", var4.getMessage());
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, content);
    }

    public DriveWarnMessage(Parcel in) {
        this.setContent(ParcelUtils.readFromParcel(in));
    }

    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public static DriveWarnMessage obtain(String content, String extra) {
        DriveWarnMessage msg = new DriveWarnMessage();
        msg.setContent(content);
        return msg;
    }



    public DriveWarnMessage (){}
}
