package me.xucan.safedrive.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.xucan.safedrive.R;
import me.xucan.safedrive.bean.DriveRecord;
import me.xucan.safedrive.ui.adapter.RecordAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordsFragment extends Fragment {
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

    void initView(){
        records = new ArrayList<>();
        adapter = new RecordAdapter(records);
        rvRecord.setAdapter(adapter);
        rvRecord.setHasFixedSize(true);
        rvRecord.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
