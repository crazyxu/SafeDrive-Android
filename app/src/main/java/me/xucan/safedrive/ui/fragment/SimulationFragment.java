package me.xucan.safedrive.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import me.xucan.safedrive.bean.DriveWarn;
import me.xucan.safedrive.message.MessageEvent;
import me.xucan.safedrive.db.MyDBManager;
import me.xucan.safedrive.net.MJsonRequest;
import me.xucan.safedrive.net.MRequestListener;
import me.xucan.safedrive.net.NetParams;
import me.xucan.safedrive.net.RequestManager;
import me.xucan.safedrive.util.AppParams;
import me.xucan.safedrive.util.DateUtil;
import me.xucan.safedrive.util.EventType;
import me.xucan.safedrive.util.PositionUtil;
import me.xucan.safedrive.util.SafeTyRules;

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
    private LinearLayout ivGettingResult;

    //container
    @ViewInject(R.id.ll_result)
    private LinearLayout llResult;

    @ViewInject(R.id.ll_get_result)
    private LinearLayout llGetResult;

    //当前车速(km/s)
    private int speeds;
    //当前时间（毫秒）
    private long tiem;
    //安全指数(0~100)
    private int safetyPoint;
    //路程(km)
    private long distance;
    //运行状态
    private boolean running = false;

    //此次驾驶记录id
    private int recordId;

    private MyHandler myHandler;
    private RequestManager requestManager = RequestManager.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simulation, container, false);
        x.view().inject(this, view);
        EventBus.getDefault().register(this);
        initView();
        myHandler = new MyHandler();
        return view;
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
                DriveWarn warn = (DriveWarn) event.data;
                String state = SafeTyRules.getState(warn);
                tvState.setText(state);
                break;
        }
    }

    void initView(){
        //设置当前时间
        tvTime.setText(DateUtil.parseMillis(DateUtil.getTime()));

    }

    @Event(value = {R.id.btn_brakes, R.id.btn_fatigue, R.id.btn_Jitter,
        R.id.btn_overspeed, R.id.btn_skewing, R.id.btn_speeds_add, R.id.btn_speeds_reduce,
            R.id.btn_crash, R.id.iv_control})
    private void OnClick(View view){
        switch (view.getId()){
            case R.id.iv_control:
                if (running){
                    //结束行驶
                    running = false;
                    ivControl.setVisibility(View.GONE);
                    llGetResult.setVisibility(View.VISIBLE);
                    endDrive();
                }else {
                    //开始行驶
                    running = true;
                    ivControl.setImageResource(R.mipmap.ic_stop);
                    //发送请求
                    startDrive();
                }
                break;
            case R.id.btn_brakes:
                sendDriveEvent(EventType.BRAKES);
                break;
            case R.id.btn_fatigue:
                sendDriveEvent(EventType.FATIGUE);
                break;
            case R.id.btn_Jitter:
                sendDriveEvent(EventType.JITTER);
                break;
            case R.id.btn_overspeed:
                sendDriveEvent(EventType.OVERSPEED);
                break;
            case R.id.btn_skewing:
                sendDriveEvent(EventType.SKEWING);
                break;
            case R.id.btn_crash:
                sendDriveEvent(EventType.CRASH);
                break;
            case R.id.btn_speeds_add:
                changeSpeeds(true);
                break;
            case R.id.btn_speeds_reduce:
                changeSpeeds(false);
                break;
            case R.id.btn_train:
                break;

        }
    }


    /**
     * 开始行驶
     */
    void startDrive(){
        Map<String,Object> map = new HashMap<>();
        map.put("userId", App.getInstance().getUserId());
        map.put("startTime", new Date().getTime());
        map.put("startPosition", PositionUtil.getPosition());
        requestManager.startRequest(new MJsonRequest(NetParams.URL_DRIVE_START, map, this));
    }

    /**
     * 结束行驶
     */
    void endDrive(){
        Map<String,Object> map = new HashMap<>();
        map.put("userId", App.getInstance().getUserId());
        map.put("endTime", new Date().getTime());
        map.put("startPosition", PositionUtil.getPosition());
        map.put("distance", distance);
        //旋转动画
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.loading);
        animation.setInterpolator( new LinearInterpolator());
        ivGettingResult.startAnimation(animation);
        //发送请求
        requestManager.startRequest(new MJsonRequest(NetParams.URL_DRIVE_STOP, map, this));

    }

    /**
     * 发送行车事件
     * @param type
     */
    void sendDriveEvent(int type){
        DriveEvent event = new DriveEvent();
        event.setRecordId(recordId);
        event.setType(type);
        event.setTime(DateUtil.getTime());
        Map<String,Object> map = new HashMap<>();
        map.put("userId", App.getInstance().getUserId());
        map.put("event", JSON.toJSONString(event));
        //发送请求
        requestManager.startRequest(new MJsonRequest(NetParams.URL_DRIVE_EVENT, map, this));
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
                new MyThread(MSG_START_COUNT).start();
                recordId = response.getInteger("recordId");
                break;
            case NetParams.URL_DRIVE_STOP:
                DriveRecord driveRecord = response.getObject("record", DriveRecord.class);
                //更新ui，行车数据统计
                ivGettingResult.clearAnimation();
                ivGettingResult.setVisibility(View.GONE);
                //显示结果
                llResult.setVisibility(View.VISIBLE);
                if (driveRecord != null){
                    tvDistance.setText(driveRecord.getDistance());
                    tvDuration.setText(DateUtil.getDuration(driveRecord.getEndTime(),
                            driveRecord.getStartTime()));
                    tvSafetyPoint.setText(driveRecord.getSafetyIndex());
                }
                //保存到本地
                MyDBManager.getInstance().save(driveRecord);
                //通知RecordsFragment更新事件
                EventBus.getDefault().post(new MessageEvent(RecordsFragment.EVENT_ADD_RECORD,driveRecord));
                break;
            case NetParams.URL_DRIVE_EVENT:
                DriveWarn warn = response.getObject("warn", DriveWarn.class);
                String state = SafeTyRules.getState(warn);
                tvState.setText(state);
                break;
        }
    }

    @Override
    public void onError(String requestUrl, int errCode, String errMsg) {

    }


    class MyHandler extends Handler{
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what){
                case MSG_START_COUNT:
                    //一次计时
                    tiem += AppParams.zoomMultiple*1000;
                    tvTime.setText(DateUtil.parseMillis(tiem));
                    distance += speeds*(AppParams.zoomMultiple/3600.0);
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
                        myHandler.sendEmptyMessageDelayed(MSG_START_COUNT, 1000);
                    }
                    break;

            }
        }
    }

}
