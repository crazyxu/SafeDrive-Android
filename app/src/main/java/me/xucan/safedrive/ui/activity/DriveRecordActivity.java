package me.xucan.safedrive.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xucan.safedrive.R;
import me.xucan.safedrive.bean.DriveEvent;
import me.xucan.safedrive.bean.DriveRecord;
import me.xucan.safedrive.net.MJsonRequest;
import me.xucan.safedrive.net.MRequestListener;
import me.xucan.safedrive.net.NetParams;
import me.xucan.safedrive.ui.adapter.EventAdapter;
import me.xucan.safedrive.ui.widget.DividerItemDecoration;
import me.xucan.safedrive.util.DateUtil;

public class DriveRecordActivity extends AppCompatActivity implements MRequestListener {
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.tv_start_time)
    private TextView tvStartTime;

    @ViewInject(R.id.tv_end_time)
    private TextView tvEndTime;

    @ViewInject(R.id.tv_start_place)
    private TextView tvStartPlace;

    @ViewInject(R.id.tv_end_place)
    private TextView tvEndPlace;

    @ViewInject(R.id.tv_duration)
    private TextView tvDuration;

    @ViewInject(R.id.tv_distance)
    private TextView tvDistance;

    @ViewInject(R.id.tv_safety_index)
    private TextView tvSafetyIndex;

    @ViewInject(R.id.rv_event_warn)
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<DriveEvent> eventList;
    private int recordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_record);
        x.view().inject(this);
        extraIntent();
        initData();
    }

    void initData(){
        eventList = new ArrayList<>();
        adapter = new EventAdapter(eventList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        Map<String,Object> map = new HashMap<>();
        map.put("recordId", recordId);
        new MJsonRequest(NetParams.URL_EVENT_GET, map, this).startRequest();
    }

    void extraIntent(){
        DriveRecord record = (DriveRecord) getIntent().getSerializableExtra("record");
        recordId = record.getRecordId();
        tvStartPlace.setText(record.getStartPlace() + "");
        tvEndPlace.setText(record.getEndPlace() + "");
        tvStartTime.setText(DateUtil.getSimplifyTime(record.getStartTime()));
        tvEndTime.setText(DateUtil.getSimplifyTime(record.getEndTime()));
        tvDuration.setText(DateUtil.getDuration(record.getStartTime(), record.getEndTime()));
        tvDistance.setText(record.getDistance()+"公里");
        tvSafetyIndex.setText("安全指数:" + record.getSafetyIndex());
        toolbar.setTitle("湖北荆州 " + DateUtil.getSimplifyDate(record.getStartTime()));
        setSupportActionBar(toolbar);

    }

    @Override
    public void onSuccess(String requestUrl, JSONObject response) {
        switch (requestUrl){
            case NetParams.URL_EVENT_GET:
                JSONArray jsonArray = response.getJSONArray("events");
                for (int i = 0; i < jsonArray.size(); i++){
                    JSONObject object = (JSONObject)jsonArray.get(i);
                    DriveEvent event = JSON.toJavaObject(object, DriveEvent.class);
                    eventList.add(event);
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onError(String requestUrl, int errCode, String errMsg) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
