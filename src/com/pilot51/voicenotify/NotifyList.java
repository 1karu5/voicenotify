package com.pilot51.voicenotify;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NotifyList extends ListView {
	private static ArrayList<NotifyItem> list = new ArrayList<NotifyItem>();
	private Adapter adapter;
	private static OnListChangeListener listener;

	public NotifyList(Context context) {
		super(context);
		setDivider(context.getResources().getDrawable(R.drawable.divider));
		adapter = new Adapter(getContext(), list);
		setAdapter(adapter);
	}
	
	protected static void addNotification(String title, String message) {
		if (list.size() == 10)
			list.remove(list.size() - 1);
		list.add(0, new NotifyItem(title, message));
		listener.onListChange();
	}
	
	protected static void setLastIgnore(String ignoreReasons) {
		list.get(0).setIgnoreReasons(ignoreReasons);
	}
	
	private static class NotifyItem {
		private String title, message, ignoreReasons, time;
		private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		private NotifyItem(String name, String msg) {
			this.title = name;
			this.message = msg;
			time = sdf.format(Calendar.getInstance().getTime());
		}
		
		private String getTitle() {
			return title;
		}
		
		private String getMessage() {
			return message;
		}
		
		private String getIgnoreReasons() {
			return ignoreReasons;
		}
		protected void setIgnoreReasons(String reasons) {
			ignoreReasons = reasons;
		}
		
		private String getTime() {
			return time;
		}
	}
	
	public static interface OnListChangeListener {
		public void onListChange();
	}
	
	public class Adapter extends BaseAdapter {
		private ArrayList<NotifyItem> data;
		private LayoutInflater mInflater;

		public Adapter(Context context, ArrayList<NotifyItem> list) {
			data = list;
			mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			listener = new OnListChangeListener() {
				@Override
				public void onListChange() {
					notifyDataSetChanged();
				}
			};
		}
		
		@Override
		public boolean isEnabled(int position) {
		    return false; // Disable list item selector
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null)
				view = mInflater.inflate(R.layout.notify_log_item, parent, false);
			else view = convertView;
			NotifyItem item = data.get(position);
			TextView textView;
			((TextView)view.findViewById(R.id.time)).setText(item.getTime());
			((TextView)view.findViewById(R.id.title)).setText(item.getTitle());
			textView = (TextView)view.findViewById(R.id.message);
			if (item.getMessage().length() != 0) {
				textView.setText(item.getMessage());
				textView.setVisibility(TextView.VISIBLE);
			} else textView.setVisibility(TextView.GONE);
			textView = (TextView)view.findViewById(R.id.ignore_reasons);
			if (item.getIgnoreReasons() != null && item.getIgnoreReasons().length() != 0) {
				textView.setText(item.getIgnoreReasons());
				textView.setVisibility(TextView.VISIBLE);
			} else textView.setVisibility(TextView.GONE);
			return view;
		}
	}
}