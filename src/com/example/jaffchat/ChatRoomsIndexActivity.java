package com.example.jaffchat;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ChatRoomsIndexActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_rooms_index);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_rooms_index, menu);
		return true;
	}

}
