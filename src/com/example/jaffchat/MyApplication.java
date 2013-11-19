package com.example.jaffchat;


import java.util.Map;

import android.app.Application;
import android.app.Notification;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class MyApplication extends Application {

	SharedPreferences prefs;
	ChatData chatData;

	@Override
	public void onCreate() {

		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		chatData=new ChatData(this);
		

	}
}
