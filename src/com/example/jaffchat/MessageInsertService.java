package com.example.jaffchat;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MessageInsertService extends IntentService {

	static String TAG = "MessageInsertService";

	public MessageInsertService() {
		super(TAG);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "OnHandleIntent");
		String email = intent.getStringExtra("email_id_receiver"); //TODO have to change this..get email of the user from message
		String message = intent.getStringExtra("message");
		String clipUrl = intent.getStringExtra("clipUrl");
		long timeOfMessage = intent.getLongExtra("time_of_message", 1);
		String direction=intent.getStringExtra("direction");
		((MyApplication) getApplication()).chatData.insert(email, message,clipUrl, timeOfMessage,direction);		
		Log.d(TAG, "Have finished inserting data");		
		sendBroadcast(new Intent("com.example.locationbased.action.updateui"));

	}

}
