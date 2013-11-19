package com.example.jaffchat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ChatActivity extends Activity {

	public static String TAG = "ChatActivity";
	Button sendMessage;
	EditText message;
	static Cursor messagesCursor;
	static List<Map> list;
	Map dataOfChat;
	SimpleAdapter adapter;
	String[] from = { "message", "message_url","time" };
	int[] to = { R.id.chat_message, R.id.time_of_chat_message,R.id.webview };
	ListView listView;
	static String ACTION_SEND_MESSAGE = "com.example.locationbased.action.sendmessage"; // intent
																						// action
																						// when
																						// message
																						// is
																						// sent
																						// by
																						// user

	IntentFilter notificationFilter = new IntentFilter(
			"com.example.locationbased.action.updateui");

	// To update ui after sending msg
	BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			// Log.d(TAG,
			// "Testing notification service in LocationEnterActivity "
			// + intent.getStringExtra("first_name"));
			Log.d(TAG, "received broadcast ..going to notify adapter");
			new GetMessages().execute("lnr@gmail.com", "lnr");
			// playSound(); // To play a sound similar to notification sound
			// because we have disabled notifications
			// addButton(intent.getStringExtra("first_name"),
			// intent.getStringExtra("message"));
		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(notificationReceiver);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(notificationReceiver, notificationFilter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		sendMessage = (Button) findViewById(R.id.buttonSend);
		message = (EditText) findViewById(R.id.message);
		listView = (ListView) findViewById(R.id.list_messages);
		notificationFilter.addAction(ChatActivity.ACTION_SEND_MESSAGE);
		notificationFilter.setPriority(1);
		sendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "OnButtonClicked");
				String messageValue = message.getText().toString();
				Intent intent = new Intent(
						"com.example.locationbased.action.sendmessage");
				intent.putExtra("email_id_receiver", "lnr@gmail.com");
				intent.putExtra("message", messageValue);
				intent.putExtra("message_url",
						"http://imageshack.us/a/img706/8427/brahmi.gif");
				intent.putExtra("time_of_message",
						NetworkHelper.getCurrentTimeStamp());
				intent.putExtra("direction", "out");
				intent.putExtra("first_name", "lnr");
				intent.putExtra("action", "inviteforride");
				sendBroadcast(intent);
			}
		});
		new GetMessages().execute("lnr@gmail.com", "lnr");

	}

	public static List<Map> getList(Cursor c) {
		list = new ArrayList<Map>();
		Map map;
		messagesCursor.moveToPosition(-1);
		while (messagesCursor.moveToNext()) {
			map = new HashMap();
			Log.d(TAG, "Converting cursor to list");
			map.put("message", messagesCursor.getString(0));
			map.put("message_url", messagesCursor.getString(messagesCursor.getColumnIndex(ChatData.MESSAGE_URL)));
			map.put("time", messagesCursor.getString(messagesCursor
					.getColumnIndex(ChatData.TIME_OF_MESSAGE)));
			map.put("directtion", messagesCursor.getString(messagesCursor
					.getColumnIndex(ChatData.DIRECTION)));
			list.add(map);

		}
		Log.d(TAG, "Size of list is" + list.size());
		return list;

	}

	class GetMessages extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			Log.d(TAG, "DoinBackground of GetUsers");
			if (messagesCursor != null)
				messagesCursor = null;
			messagesCursor = ((MyApplication) getApplication()).chatData
					.getMessagesOfUser(params[0]);
			List result = ChatActivity.getList(messagesCursor);
			if (result == null)
				return null;
			else
				return "present";

		}

		@Override
		protected void onPostExecute(String result) {
			Log.d(TAG, "OnPostExecute");
			super.onPostExecute(result);

			if (result != null) {
				Log.d(TAG, list.toString());
				adapter = new SimpleAdapter(ChatActivity.this,
						(List<? extends Map<String, ?>>) list,
						R.layout.row_message, from, to);
				Log.d(TAG, "Before setting View Binder");
				adapter.setViewBinder(VIEW_BINDER);
				Log.d(TAG, "After setting View Binder");
				listView.setAdapter(adapter);
				listView.setSelection(adapter.getCount() - 1);

				//WebView webview = (WebView) findViewById(1);
				// Log.d(TAG, webview.toString());
				 //webview.loadUrl("http://www.google.com");

			}

		}

	}

	static final SimpleAdapter.ViewBinder VIEW_BINDER = new SimpleAdapter.ViewBinder() {

		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {

			if (view.getId() == R.id.webview) {
				// we are dealing with the ProgressBar so set the progress and
				// return true(to let the adapter know you binded the data)
				// set the progress(the data parameter, I don't know what you
				// actually store in the progress column(integer, string etc)).

				// WebView webview = new WebView(view.getA);
				Log.d(TAG, "Setting id ");
				 ((WebView)view).loadUrl("http://www.google.com");

				return false;
			}

			return false; // we are dealing with the TextView so return false
							// and let the adapter bind the data
		}

	};

}
