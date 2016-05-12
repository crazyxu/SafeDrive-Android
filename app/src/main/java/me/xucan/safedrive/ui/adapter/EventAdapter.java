package me.xucan.safedrive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.xucan.safedrive.R;
import me.xucan.safedrive.bean.DriveEvent;
import me.xucan.safedrive.util.DateUtil;
import me.xucan.safedrive.util.EventType;

/**
 * @author xucan
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
	private List<DriveEvent> events;

	public EventAdapter(List<DriveEvent> events) {
		this.events = events;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		private ImageView ivType;
		private TextView tvTime;
		private TextView tvEvents;

		/**
		 * @param itemView
		 */
		public ViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			ivType = (ImageView) itemView.findViewById(R.id.iv_type);
			tvTime = (TextView) itemView.findViewById(R.id.tv_time);
			tvEvents = (TextView) itemView.findViewById(R.id.tv_events);
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
		return events == null ? 0 : events.size();
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
		DriveEvent event = events.get(position);
		if (event != null){
			if (EventType.isWarn(event.getType())){
				viewHolder.ivType.setImageResource(R.mipmap.icon_warn);
			}else{
				viewHolder.ivType.setImageResource(R.mipmap.ic_event);
			}
			viewHolder.tvEvents.setText(EventType.getTip(event));
			viewHolder.tvTime.setText(DateUtil.getSimplifyTime(event.getTime()));
		}

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
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_event_layout, parent, false);
		
		return new ViewHolder(view);
	}


}
