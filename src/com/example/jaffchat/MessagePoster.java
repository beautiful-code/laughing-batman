package com.example.jaffchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MessagePoster extends BroadcastReceiver {

	static String TAG="MessagePoster";
	@Override
	public void onReceive(Context context, Intent intent) {
      
		Log.d(TAG, "OnReceive");
		
		Intent intent1=new Intent("com.example.locationbased.action.insertmessage");
		intent1.putExtras(intent);
		context.startService(intent1);		
	}
	

}