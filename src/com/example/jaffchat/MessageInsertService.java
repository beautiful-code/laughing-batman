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
		String emailId = intent.getStringExtra("email_id_receiver");
		String firstName = intent.getStringExtra("first_name");
		String message = intent.getStringExtra("message");
		String messageUrl = intent.getStringExtra("message_url");
		long timeOfMessage = intent.getLongExtra("time_of_message", 1);
		String direction=intent.getStringExtra("direction");
		((MyApplication) getApplication()).chatData.insert(emailId, firstName,message,messageUrl, timeOfMessage,direction);		
		Log.d(TAG, "Have finished inserting data");		
		sendBroadcast(new Intent("com.example.locationbased.action.updateui"));

	}

}
