package com.example.jaffchat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CustomAdapter extends ArrayAdapter<Map>{
    private List<Map> entries;
    private Activity activity;
 
    public CustomAdapter(Activity a, int textViewResourceId, List<Map> entries) {
        super(a, textViewResourceId, entries);
        this.entries = entries;
        this.activity = a;
    }
 
    public static class ViewHolder{
        public TextView message;
        public TextView time;
        public WebView gif;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi =
                (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_message, null);
            holder = new ViewHolder();
            holder.message = (TextView) v.findViewById(R.id.chat_message);
            holder.time = (TextView) v.findViewById(R.id.time_of_chat_message);
            holder.gif = (WebView) v.findViewById(R.id.webview);
           
            
            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();
 
        final Map map = entries.get(position);
        
        
        if (map != null) {
            Log.d("custom_adapter",map.toString()); 
        	holder.message.setText(map.get("message").toString());
            holder.time.setText(map.get("time").toString());
            holder.gif.loadUrl("http://stream1.gifsoup.com/view7/2895029/brahmi-thinking-o.gif");
            Log.d("custom_adapter","Have loaded gif");
        }
        return v;
    }
 
}