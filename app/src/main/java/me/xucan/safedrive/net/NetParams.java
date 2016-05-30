package me.xucan.safedrive.net;

/**
 * Created by xcytz on 2016/4/23.
 */
public class NetParams {
    //public final static String HOST = "http://115.28.21.170:8080/SafeDrive/";
    public final static String HOST = "http://192.168.1.105:8080/SafeDrive/";
    public final static String URL_DRIVE_START = HOST+ "record/start";
    public final static String URL_DRIVE_STOP = HOST+ "record/stop";
    public final static String URL_DRIVE_GET = HOST+ "record/get";

    public final static String URL_EVENT_SEND = HOST+ "event/send";
    public final static String URL_EVENT_GET = HOST+ "event/get";

    public final static String URL_USER_REGISTER = HOST+ "user/register";
    public final static String URL_USER_LOGIN = HOST+ "user/login";
    public final static String URL_USER_UPDATE = HOST+ "user/update";


}
