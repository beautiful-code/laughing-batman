package com.example.jaffchat;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;

public class JaffaMainActivity extends Activity {
	
	
	String TAG = "JaffaMainActivity";
	SharedPreferences prefs;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String authentication_token = prefs.getString("authentication_token",null);
		
		if (authentication_token == null) {
			Intent intent = new Intent(JaffaMainActivity.this,
					LoginActivity.class);
			startActivity(intent);
			Log.d(TAG, "going to finish JaffaMain");
			finish();
			Log.d(TAG, "have finished the mainenteractivity");
		} else {
			
			setContentView(R.layout.activity_jaffa_main);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.jaffa_main, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_logout:
			Editor editor = prefs.edit();
			editor.clear();
			editor.commit();
			UserData.authentication_token = null;
			
			Intent intent = new Intent(JaffaMainActivity.this,
					LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;

		}
		return true;
	}
	
	

}
