package me.xucan.safedrive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.xucan.safedrive.R;
import me.xucan.safedrive.bean.EventWarn;
import me.xucan.safedrive.util.DateUtil;

/**
 * @author xucan
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
	private List<EventWarn> eventWarns;

	public EventAdapter(List<EventWarn> eventWarns) {
		this.eventWarns = eventWarns;
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
		return eventWarns == null ? 0 : eventWarns.size();
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
		EventWarn eventWarn = eventWarns.get(position);
		if (eventWarn != null){
			if (eventWarn.getType() == 0){
				viewHolder.ivType.setImageResource(R.mipmap.ic_event);
			}else if(eventWarn.getType() == 1){
				viewHolder.ivType.setImageResource(R.mipmap.icon_warn);
			}
			viewHolder.tvEvents.setText(eventWarn.getMsg());
			viewHolder.tvTime.setText(DateUtil.getSimplifyDate(eventWarn.getTime()));
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
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_records_layout, parent, false);
		
		return new ViewHolder(view);
	}


}
