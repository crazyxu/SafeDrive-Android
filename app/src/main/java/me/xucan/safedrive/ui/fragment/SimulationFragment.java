package me.xucan.safedrive.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.Timer;

import me.xucan.safedrive.R;
import me.xucan.safedrive.util.AppParams;
import me.xucan.safedrive.util.DateUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimulationFragment extends Fragment {
    //记时
    private final static int MSG_START_COUNT = 0x00;

    @ViewInject(R.id.tv_cur_speed)
    private TextView tvSpeeds;

    @ViewInject(R.id.tv_cur_time)
    private TextView tvTime;

    @ViewInject(R.id.tv_state)
    private TextView tvState;

    @ViewInject(R.id.iv_control)
    private ImageView ivControl;

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

    private Timer timer;

    private MyHandler myHandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simulation, container, false);
        x.view().inject(this, view);
        initView();
        timer = new Timer();
        myHandler = new MyHandler();
        return view;
    }

    void initView(){
        //设置当前时间
        tiem = new Date().getTime();
        tvTime.setText(DateUtil.parseMillis(tiem));


    }

    @Event(value = {R.id.btn_brakes, R.id.btn_cancel, R.id.btn_fatigue, R.id.btn_isOK, R.id.btn_Jitter,
        R.id.btn_overspeed, R.id.btn_skewing, R.id.btn_speeds_add, R.id.btn_speeds_reduce,
            R.id.btn_train, R.id.iv_control})
    private void OnClick(View view){
        switch (view.getId()){
            case R.id.iv_control:
                if (running){
                    //结束行驶
                    running = false;
                    ivControl.setVisibility(View.GONE);
                    llGetResult.setVisibility(View.VISIBLE);
                }else {
                    //开始行驶
                    running = true;
                    ivControl.setImageResource(R.mipmap.ic_stop);
                    //开始计时
                    new MyThread(MSG_START_COUNT).start();
                }
                break;
            case R.id.btn_brakes:
                break;
            case R.id.btn_cancel:
                break;
            case R.id.btn_fatigue:
                break;
            case R.id.btn_isOK:
                break;
            case R.id.btn_Jitter:
                break;
            case R.id.btn_overspeed:
                break;
            case R.id.btn_skewing:
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

    void changeSpeeds(boolean add){
        if (add){
            speeds += 10;
        }else {
            speeds -= 10;
        }
        tvSpeeds.setText(speeds + "km/h");
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
