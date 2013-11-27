package com.example.jaffchat;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapterNew extends ArrayAdapter<Map> {
	private List<Map> entries;
	private Activity activity;

	public CustomAdapterNew(Activity a, int textViewResourceId,
			List<Map> entries) {
		super(a, textViewResourceId, entries);
		this.entries = entries;
		this.activity = a;
	}

	public static class ViewHolder {
		public TextView message;
		public TextView email;
		public WebView gif;
	}

	public static class ViewHolder1 {
		public TextView message;
		public TextView email;
		public WebView gif;
	}

	@Override
	public int getViewTypeCount() {

		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		Map map = entries.get(position);
		if (map.get("email").toString().equals(MyApplication.email())) {
			Log.d("custom_adapter", "It is my message"
					+ map.get("message").toString());
			return 0;
		}
		// if something else
		return 1;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		int viewType = this.getItemViewType(position);
		ViewHolder holder = null;
		ViewHolder1 holder1 = null;
		Map map = entries.get(position);
		View v = convertView;


		switch (viewType) {
		

		case 0:
			if (v == null) {
				Log.d("custom_adapter", "Case is 0");
				LayoutInflater vi = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_message_self, parent,false);
				Log.d("custom_adapter", "Other message");
				holder = new ViewHolder();
				holder.message = (TextView) v.findViewById(R.id.chat_message);
				// holder.email = (TextView) v.findViewById(R.id.email);
				holder.gif = (WebView) v.findViewById(R.id.webview);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}

			if (map != null) {
				holder.message.setText(map.get("message").toString());
				String url = "http://www.google.com";
				String full_url = "";
				if (map.get("clipUrl") == null) { // holder.gif.setVisibility(4);
				} else {
					url = map.get("clipUrl").toString();
					full_url = UserData.HOST + url;
					holder.gif.loadUrl(full_url);
					holder.gif.setBackgroundColor(Color.parseColor("#E6E6E6"));
					Log.d("custom_adapter", "Have loaded gif");
				}
			}

			return v;
		case 1:
			if (v == null) {
				Log.d("custom_adapter", "Case is 1");
				LayoutInflater vi = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_message_other, parent,false);
				Log.d("custom_adapter", "My message");
				holder1 = new ViewHolder1();
				holder1.message = (TextView) v.findViewById(R.id.chat_message);
				// holder.email = (TextView) v.findViewById(R.id.email);
				holder1.gif = (WebView) v.findViewById(R.id.webview);
				v.setTag(holder1);
			} else {
				holder1 = (ViewHolder1) v.getTag();
			}


			if (map != null) {
				holder1.message.setText(map.get("message").toString());
				String url = "http://www.google.com";
				String full_url = "";
				if (map.get("clipUrl") == null) { // holder.gif.setVisibility(4);
				} else {
					url = map.get("clipUrl").toString();
					full_url = UserData.HOST + url;
					holder1.gif.setBackgroundColor(Color.parseColor("#3385D6"));
					holder1.gif.loadUrl(full_url);					
					Log.d("custom_adapter", "Have loaded gif");
				}
			}
			
			
			return v;

		}
		return v;
	}

}