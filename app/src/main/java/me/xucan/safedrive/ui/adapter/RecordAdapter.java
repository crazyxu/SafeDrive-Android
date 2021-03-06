package me.xucan.safedrive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import me.xucan.safedrive.R;
import me.xucan.safedrive.bean.DriveRecord;
import me.xucan.safedrive.bean.Location;
import me.xucan.safedrive.util.DateUtil;

/**
 * @author xucan
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
	private List<DriveRecord> records;
	private ClickListener listener;


	public RecordAdapter(List<DriveRecord> records, ClickListener listener) {
		this.records = records;
		this.listener = listener;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		private TextView tvStartTime;
		private TextView tvSafetyPoint;
		private TextView tvStartPlace;
		private TextView tvEndPlace;
		private TextView tvDuration;
		private TextView tvDistance;

		/**
		 * @param itemView
		 */
		public ViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			tvStartTime = (TextView) itemView.findViewById(R.id.tv_start_time);
			tvSafetyPoint = (TextView) itemView.findViewById(R.id.tv_safety_index);
			tvStartPlace = (TextView) itemView.findViewById(R.id.tv_start_place);
			tvEndPlace = (TextView) itemView.findViewById(R.id.tv_end_place);
			tvDuration = (TextView) itemView.findViewById(R.id.tv_duration);
			tvDistance = (TextView) itemView.findViewById(R.id.tv_distance);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.widget.RecyclerView.Adapter#getItemCount()
	 */
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return records == null ? 0 : records.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder(android.
	 * support.v7.widget.RecyclerView.ViewHolder, int)
	 */
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		// TODO Auto-generated method stub
		DriveRecord record = records.get(position);
		if (record == null)
			return;
		viewHolder.tvStartTime.setText(DateUtil.getDate(record.getStartTime()));
		viewHolder.tvSafetyPoint.setText("安全指数:" + record.getSafetyIndex());
		Location startLoc = JSON.parseObject(record.getStartPlace(), Location.class);
		Location endLoc = JSON.parseObject(record.getEndPlace(), Location.class);
		viewHolder.tvEndPlace.setText(endLoc.getDistrict() + endLoc.getStreet());
		viewHolder.tvStartPlace.setText(startLoc.getDistrict() + startLoc.getStreet());
		viewHolder.tvDistance.setText(record.getDistance() + "km");
		viewHolder.tvDuration.setText(DateUtil.getDuration(
				record.getStartTime(), record.getEndTime()));
		viewHolder.itemView.setTag(position);
		viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.onClick((int)v.getTag());
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v7.widget.RecyclerView.Adapter#onCreateViewHolder(android
	 * .view.ViewGroup, int)
	 */
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_records_layout, parent, false);
		
		return new ViewHolder(view);
	}

	public interface ClickListener{
		void onClick(int position);
	}


}
