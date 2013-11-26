package com.example.jaffchat;

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


public class CustomAdapterNew extends ArrayAdapter<Map>{
    private List<Map> entries;
    private Activity activity;
 
    public CustomAdapterNew(Activity a, int textViewResourceId, List<Map> entries) {
        super(a, textViewResourceId, entries);
        this.entries = entries;
        this.activity = a;
    }
 
    public static class ViewHolder{
        public TextView message;
        public TextView email;
        public WebView gif;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
        	
            LayoutInflater vi =
                (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            if(position%2==0){
    			v = vi.inflate(R.layout.row_message_new, null);
   			
    		}else{
    			v = vi.inflate(R.layout.row_message_new_even,null);
    		}
            holder = new ViewHolder();
            holder.message = (TextView) v.findViewById(R.id.chat_message);
            holder.email = (TextView) v.findViewById(R.id.email);
            holder.gif = (WebView) v.findViewById(R.id.webview);
           
            
            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();
 
        final Map map = entries.get(position);
        
        
        if (map != null) {
            Log.d("custom_adapter",map.toString()); 
        	holder.message.setText(map.get("message").toString());
        	Log.d("custom_adapter",map.get("email").toString());
            holder.email.setText(map.get("email").toString());
            String url = "http://www.google.com";
            String full_url = "";
            if(map.get("clipUrl")==null)
            {	//Log.d("custom_adapter", "Clip url is null for "+map.get("message").toString());
            
           // holder.gif.setVisibility(4);	
            }
            else{
             url = map.get("clipUrl").toString();
        
              full_url = UserData.HOST+url; 
            holder.gif.loadUrl(full_url);
            Log.d("custom_adapter","Have loaded gif");
            }
        }

        return v;
    }
 
}