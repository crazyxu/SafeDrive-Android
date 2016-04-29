package me.xucan.safedrive.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.xucan.safedrive.App;
import me.xucan.safedrive.R;
import me.xucan.safedrive.bean.DriveRecord;
import me.xucan.safedrive.message.MessageEvent;
import me.xucan.safedrive.net.MJsonRequest;
import me.xucan.safedrive.net.MRequestListener;
import me.xucan.safedrive.net.NetParams;
import me.xucan.safedrive.ui.activity.DriveRecordActivity;
import me.xucan.safedrive.ui.adapter.RecordAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordsFragment extends Fragment implements MRequestListener{
    public final static String EVENT_ADD_RECORD = "RecordsFragment_Add_Record";
    @ViewInject(R.id.rv_record)
    private RecyclerView rvRecord;
    private RecordAdapter adapter;
    private List<DriveRecord> records;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_records, container, false);
        x.view().inject(this, view);
        initView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getData(0,10);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * EventBus事件处理
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        switch (event.message) {
            //增加record
            case EVENT_ADD_RECORD:
                DriveRecord record = (DriveRecord) event.data;
                if (record != null){
                    records.add(record);
                    adapter.notifyDataSetChanged();
                }
                break;

        }
    }

    void initView(){
        records = new ArrayList<>();
        adapter = new RecordAdapter(records, new RecordAdapter.ClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), DriveRecordActivity.class);
                intent.putExtra("record",records.get(position));
            }
        });
        rvRecord.setAdapter(adapter);
        rvRecord.setHasFixedSize(true);
        rvRecord.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    void getData(int from, int to){
        Map<String, Object> map = new HashMap<>();
        map.put("userId", App.getInstance().getUserId());
        map.put("fromNum",from);
        map.put("toNum", to);
        new MJsonRequest(NetParams.URL_DRIVE_GET, map, this).startRequest();
    }

    @Override
    public void onSuccess(String requestUrl, JSONObject response) {
        switch (requestUrl){
            case NetParams.URL_DRIVE_GET:
                JSONArray array = response.getJSONArray("records");
                for (int i = 0; i < array.size(); i++){
                    JSONObject object = (JSONObject)array.get(i);
                    DriveRecord record = JSON.toJavaObject(object, DriveRecord.class);
                    records.add(record);
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onError(String requestUrl, int errCode, String errMsg) {

    }
}
