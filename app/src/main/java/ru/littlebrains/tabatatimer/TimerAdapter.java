package ru.littlebrains.tabatatimer;

import java.util.List;

import ru.littlebrains.tabatatimer.api.TimerModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TimerAdapter extends BaseAdapter{
	
	private List<TimerModel> list;
	private Context mContext;

	public TimerAdapter(Context mContext, List<TimerModel> list){
		this.list = list;
		this.mContext = mContext;
	}
	
	public void setList(List<TimerModel> list){
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size()+1;
	}

	@Override
	public TimerModel getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View container = convertView;
		if(container == null){
			container = LayoutInflater.from(mContext).inflate(R.layout.item_timer_list, null);
		}

		View root = container.findViewById(R.id.root);
		if(position == getCount()-1){
			root.setVisibility(View.GONE);
			ViewGroup.LayoutParams lp = container.getLayoutParams();

			return container;
		}
		root.setVisibility(View.VISIBLE);
		TimerModel item = list.get(position);

		TextView textRun = (TextView) container.findViewById(R.id.time_run);
		TextView title = (TextView) container.findViewById(R.id.title);
		textRun.setText(item.timeRun + " " + getNumEnding(item.timeRun));
		if(item.timerCount != TimerModel.COUNT_SINGLE_TIMER) {
			title.setText(item.name);
			TextView textPause = (TextView) container.findViewById(R.id.time_pause);
			textPause.setText(item.timePause + " " + getNumEnding(item.timePause));
			TextView textCount = (TextView) container.findViewById(R.id.timer_count);
			textCount.setText("" + item.timerCount);
		}	else{
			title.setText(item.name);
			container.findViewById(R.id.timer_count).setVisibility(View.GONE);
			container.findViewById(R.id.text_timer_count).setVisibility(View.GONE);
			container.findViewById(R.id.text_time_pause).setVisibility(View.GONE);
			container.findViewById(R.id.time_pause).setVisibility(View.GONE);
		}

		return container;
	}

	private String getNumEnding(int t)
	{
		t = t % 100;
		if (t>=11 && t<=19) {
			return mContext.getString(R.string.second);
		}

		int i = t % 10;
		switch (i)
		{
			case 1: return mContext.getString(R.string.seconda);
			case 2:
			case 3:
			case 4: return mContext.getString(R.string.seconds);
			default: return mContext.getString(R.string.second);
		}
	}
}
