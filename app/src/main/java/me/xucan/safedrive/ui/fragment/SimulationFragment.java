package me.xucan.safedrive.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
    private ImageView ivGettingResult;


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
    private float distance;
    //运行状态
    private boolean running = false;

    //此次驾驶记录id
    private DriveRecord record = new DriveRecord();

    private MyHandler myHandler;
    private RequestManager requestManager = RequestManager.getInstance();

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

        }
    }


    /**
     * 开始行驶
     */
    void startDrive(){
        Map<String,Object> map = new HashMap<>();
        record.setUserId(App.getInstance().getUserId());
        record.setStartPlace(PositionUtil.getPosition());
        record.setStartTime(new Date().getTime());
        map.put("record", record);
        new MJsonRequest(NetParams.URL_DRIVE_START, map, this).startRequest();
    }

    /**
     * 结束行驶
     */
    void endDrive(){
        Map<String,Object> map = new HashMap<>();
        record.setEndTime(new Date().getTime());
        record.setEndPlace(PositionUtil.getPosition());
        record.setDistance(distance);
        map.put("record", record);

        //发送请求
        new MJsonRequest(NetParams.URL_DRIVE_STOP, map, this).startRequest();

    }

    /**
     * 发送行车事件
     * @param type
     */
    void sendDriveEvent(int type){
        DriveEvent event = new DriveEvent();
        event.setRecordId(record.getRecordId());
        event.setType(type);
        event.setTime(DateUtil.getTime());
        Map<String,Object> map = new HashMap<>();
        map.put("userId", App.getInstance().getUserId());
        map.put("event", JSON.toJSONString(event));
        //发送请求
        new MJsonRequest(NetParams.URL_DRIVE_EVENT, map, this).startRequest();
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
                break;
            case NetParams.URL_DRIVE_STOP:
                int safetyIndex = response.getIntValue("safetyIndex");
                record.setSafetyIndex(safetyIndex);

                //更新ui，行车数据统计
                ivGettingResult.clearAnimation();
                llGetResult.setVisibility(View.GONE);
                //显示结果
                llResult.setVisibility(View.VISIBLE);
                tvDistance.setText(record.getDistance()+"km");
                tvDuration.setText(DateUtil.getDuration(record.getEndTime(),record.getStartTime()));
                tvSafetyPoint.setText(record.getSafetyIndex()+"");
                //保存到本地
                MyDBManager.getInstance().save(record);
                //通知RecordsFragment更新事件
                EventBus.getDefault().post(new MessageEvent(RecordsFragment.EVENT_ADD_RECORD,record));
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
