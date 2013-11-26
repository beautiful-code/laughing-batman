package com.example.jaffchat;


import java.util.Map;

import android.app.Application;
import android.app.Notification;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class MyApplication extends Application {

	public static SharedPreferences prefs;
	ChatData chatData;

	@Override
	public void onCreate() {

		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		chatData=new ChatData(this);
	}
	
	
	public static String authentication_token() {
		return prefs.getString("authentication_token", null);
	}
	
	
	public static String email() {
		return prefs.getString("email", null);
	}
	
	public static SharedPreferences getPrefs()
	{
		return prefs;
	}
	
}
