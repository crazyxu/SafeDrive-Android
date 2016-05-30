package me.xucan.safedrive.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.xucan.safedrive.App;
import me.xucan.safedrive.R;
import me.xucan.safedrive.bean.DriveEvent;
import me.xucan.safedrive.bean.DriveRecord;
import me.xucan.safedrive.bean.Location;
import me.xucan.safedrive.bean.User;
import me.xucan.safedrive.db.MyDBManager;
import me.xucan.safedrive.db.MySp;
import me.xucan.safedrive.message.MessageEvent;
import me.xucan.safedrive.net.MJsonRequest;
import me.xucan.safedrive.net.MRequestListener;
import me.xucan.safedrive.net.NetParams;
import me.xucan.safedrive.util.AppParams;
import me.xucan.safedrive.util.DateUtil;
import me.xucan.safedrive.util.EventType;
import me.xucan.safedrive.util.FormatUtil;
import me.xucan.safedrive.util.LocationUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimulationFragment extends Fragment implements MRequestListener{
    //记时
    private final static int MSG_START_COUNT = 0x00;
    //服务器发送的警告信息
    public final static String EVENT_DRIVE_WARN = "SimulationFragment_Drive_Warn";

    //title
    @ViewInject(R.id.tv_cur_speed)
    private TextView tvSpeeds;

    @ViewInject(R.id.tv_cur_time)
    private TextView tvTime;

    @ViewInject(R.id.tv_state)
    private TextView tvState;

    //运行结果
    @ViewInject(R.id.tv_duration)
    private TextView tvDuration;

    @ViewInject(R.id.tv_distance)
    private TextView tvDistance;

    @ViewInject(R.id.tv_total_safety_point)
    private TextView tvSafetyPoint;

    //启动，停止
    @ViewInject(R.id.iv_control)
    private ImageView ivControl;

    //正在获取统计数据
    @ViewInject(R.id.iv_getting_info)
    private ImageView ivGettingResult;


    //container
    @ViewInject(R.id.ll_result)
    private LinearLayout llResult;

    @ViewInject(R.id.ll_get_result)
    private LinearLayout llGetResult;

    //
    boolean cancel = false;

    //当前时间
    private long curTime;

    //当前车速(km/s)
    private int speeds = 60;

    //安全指数(0~100)
    private int safetyIndex;

    //路程(km)
    private float distance;

    //运行状态
    private boolean running = false;

    //最后一次发生事件的时间（疲劳，偏移，急刹，加速）,时间超过十分钟，指数上升
    private long lastEventTime;

    //此次驾驶记录id
    private DriveRecord record = new DriveRecord();

    private MyHandler myHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simulation, container, false);
        x.view().inject(this, view);
        initView();
        clear();
        myHandler = new MyHandler();
        return view;
    }

    private void clear(){
        speeds = 60;
        distance = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        switch (event.message){
            case EVENT_DRIVE_WARN:
                DriveEvent driveEvent = event.object.getObject("event", DriveEvent.class);
                safetyIndex = event.object.getIntValue("safetyIndex");
                if (safetyIndex != 0)
                    tvState.setText("安全指数:" + safetyIndex);
                if (driveEvent != null){
                    String state = EventType.getTip(driveEvent);
                    tvState.setText(state);
                    //疲劳驾驶
                    if(driveEvent.getType() == EventType.WARN_FATIGUE)
                        autoPhone();
                }
                break;
        }
    }

    void autoPhone(){
        User user = MySp.getUser();
        if (TextUtils.isEmpty(user.getUrgentPhone()))
            return;
        final String urgentPhone = user.getUrgentPhone();
        new LocationUtil(getActivity(), new LocationUtil.LocationListener() {
            @Override
            public void onSuccess(final String location) {
                Snackbar.make(tvState,"3秒后自动发送紧急短信",Snackbar.LENGTH_LONG).setAction("取消",new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        cancel = true;
                    }
                }).show();

                if (!cancel){
                    SmsManager sms = SmsManager.getDefault();
                    Location location1 = JSON.parseObject(location, Location.class);
                    sms.sendTextMessage(urgentPhone, null, location1.getCity() + location1.getDistrict()
                            + location1.getStreet(), null, null);
                    //事件
                    sendDriveEvent(EventType.ACTION_URGENT_PHONE);
                }
            }
        }).start();

    }

    void initView(){
        //设置当前时间
        curTime = DateUtil.getTime();
        lastEventTime = curTime;
        tvTime.setText(DateUtil.getSimplifyTime(curTime));
        tvSpeeds.setText(speeds + "km/h");

    }

    @Event(value = {R.id.btn_brakes, R.id.btn_fatigue, R.id.btn_skewing, R.id.btn_speeds_add, R.id.btn_speeds_reduce,
            R.id.iv_control})
    private void OnClick(View view){
        switch (view.getId()){
            case R.id.iv_control:
                if (running){
                    //结束行驶
                    //停止计时
                    running = false;
                    ivControl.setVisibility(View.GONE);
                    llGetResult.setVisibility(View.VISIBLE);
                    //旋转动画
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.loading);
                    animation.setInterpolator( new LinearInterpolator());
                    ivGettingResult.startAnimation(animation);
                    endDrive();
                }else {
                    //发送请求
                    startDrive();
                }
                break;
            case R.id.btn_brakes:
                sendDriveEvent(EventType.EVENT_BRAKES);
                break;
            case R.id.btn_fatigue:
                sendDriveEvent(EventType.EVENT_FATIGUE);
                break;
            case R.id.btn_skewing:
                sendDriveEvent(EventType.EVENT_SKEWING);
                break;
            case R.id.btn_speeds_add:
                changeSpeeds(true);
                sendDriveEvent(EventType.EVENT_ACCELERATION);
                break;
            case R.id.btn_speeds_reduce:
                changeSpeeds(false);
                sendDriveEvent(EventType.EVENT_DECELERATION);
                break;

        }
    }


    /**
     * 开始行驶
     */
    void startDrive(){
        final Map<String,Object> map = new HashMap<>();
        record.setUserId(App.getInstance().getUserId());
        record.setStartTime(new Date().getTime());
        map.put("record", record);
        new LocationUtil(getActivity(), new LocationUtil.LocationListener() {
            @Override
            public void onSuccess(String location) {
                record.setStartPlace(location);
                new MJsonRequest(NetParams.URL_DRIVE_START, map, SimulationFragment.this).startRequest();
            }
        }).start();

    }

    /**
     * 结束行驶
     */
    void endDrive(){
        final Map<String,Object> map = new HashMap<>();
        record.setEndTime(curTime);
        distance =FormatUtil.KeepTwo(distance);
        record.setDistance(distance);
        record.setSafetyIndex(safetyIndex);
        map.put("record", record);
        //发送请求
        new LocationUtil(getActivity(), new LocationUtil.LocationListener() {
            @Override
            public void onSuccess(String location) {
                record.setEndPlace(location);
                new MJsonRequest(NetParams.URL_DRIVE_STOP, map, SimulationFragment.this).startRequest();
            }
        }).start();

    }

    /**
     * 发送行车事件
     * @param type
     */
    void sendDriveEvent(int type){
        DriveEvent event = new DriveEvent();
        event.setRecordId(record.getRecordId());
        event.setType(type);
        if(type == EventType.EVENT_ACCELERATION || type == EventType.EVENT_DECELERATION)
            event.setExtra(String.valueOf(speeds));
        if(type == EventType.EVENT_SKEWING || type == EventType.EVENT_ACCELERATION ||
                type == EventType.EVENT_BRAKES || type == EventType.EVENT_FATIGUE ||
                type == EventType.EVENT_NO){
            lastEventTime = curTime;
        }
        event.setTime(curTime);
        Map<String,Object> map = new HashMap<>();
        map.put("event", event);
        map.put("userId", App.getInstance().getUserId());
        //发送请求
        new MJsonRequest(NetParams.URL_EVENT_SEND, map, this).startRequest();
    }

    void changeSpeeds(boolean add){
        if (add){
            speeds += 10;
        }else {
            speeds -= 10;
        }
        tvSpeeds.setText(speeds + "km/h");
    }

    @Override
    public void onSuccess(String requestUrl, JSONObject response) {
        switch (requestUrl){
            case NetParams.URL_DRIVE_START:
                //开始计时
                //开始行驶
                running = true;
                ivControl.setImageResource(R.mipmap.ic_stop);
                new MyThread(MSG_START_COUNT).start();
                record.setRecordId(response.getInteger("recordId"));
                tvState.setText("正常行驶中");
                break;
            case NetParams.URL_DRIVE_STOP:
                //更新ui，行车数据统计
                ivGettingResult.clearAnimation();
                llGetResult.setVisibility(View.GONE);
                //显示结果
                llResult.setVisibility(View.VISIBLE);
                tvDistance.setText("全程" + record.getDistance() +"km");
                Log.i("duration", ""+(record.getEndTime() - record.getStartTime()));
                tvDuration.setText("时长：" + DateUtil.getDuration(record.getStartTime(),record.getEndTime()));
                tvSafetyPoint.setText("安全指数：" + safetyIndex);

                //保存到本地
                MyDBManager.getInstance().save(record);
                //通知RecordsFragment更新事件
                JSONObject object = new JSONObject();
                object.put("record", record);
                EventBus.getDefault().post(new MessageEvent(RecordsFragment.EVENT_ADD_RECORD,object));
                break;
            case NetParams.URL_EVENT_SEND:

                break;
        }
    }

    @Override
    public void onError(String requestUrl, int errCode, String errMsg) {
        switch (requestUrl){
            case NetParams.URL_DRIVE_STOP:
                ivGettingResult.clearAnimation();
                ivGettingResult.setVisibility(View.GONE);
                break;
        }
    }


    class MyHandler extends Handler{
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what){
                case MSG_START_COUNT:
                    //一次计时
                    curTime += AppParams.zoomMultiple*1000;
                    tvTime.setText(DateUtil.getSimplifyTime(curTime));
                    distance += speeds*(AppParams.zoomMultiple/3600f);
                    //十分钟内没有事件
                    if (curTime - lastEventTime >= 10*60*1000 && safetyIndex < 100){
                        sendDriveEvent(EventType.EVENT_NO);
                        tvState.setText("安全指数:" + safetyIndex);
                    }
                    break;
            }
        }
    }

    class MyThread extends Thread{
        private int action;
        public MyThread(int action){
            this.action = action;
        }

        @Override
        public void run() {
            switch (action){
                //开始计时
                case MSG_START_COUNT:
                    //1000ms发送一次
                    while (running){
                        try {
                            this.sleep(1000);
                            myHandler.sendEmptyMessage(MSG_START_COUNT);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    break;

            }
        }
    }

}
