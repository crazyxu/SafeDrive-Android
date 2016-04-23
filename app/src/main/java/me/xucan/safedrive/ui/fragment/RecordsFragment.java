package me.xucan.safedrive.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.xucan.safedrive.R;
import me.xucan.safedrive.bean.DriveRecord;
import me.xucan.safedrive.bean.MessageEvent;
import me.xucan.safedrive.ui.adapter.RecordAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordsFragment extends Fragment {
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
        EventBus.getDefault().register(this);
        initView();
        return view;
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
        adapter = new RecordAdapter(records);
        rvRecord.setAdapter(adapter);
        rvRecord.setHasFixedSize(true);
        rvRecord.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
