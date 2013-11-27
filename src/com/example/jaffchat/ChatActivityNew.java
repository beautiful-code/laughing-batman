package com.example.jaffchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ChatActivityNew extends Activity {

	public static String TAG = "ChatActivityNew";
	Button sendMessage;
	Button getImages;
	String clipId;
	WebView w1, w2;
	EditText message;
	static List<Map> list = new ArrayList<Map>();
	static List<Map> imagesList = new ArrayList<Map>();
	CustomAdapterNew adapter;
	String[] from = { "message", "message_url", "time" };
	int[] to = { R.id.chat_message, R.id.time_of_chat_message, R.id.webview };
	ListView listView;
	static String ACTION_SEND_MESSAGE = "com.example.locationbased.action.sendmessage"; // intent
																						// action
	String roomId; // when
	// message
	// is
	// sent
	// by
	// user

	Map lastMessage;
	String timeOfMostRecentMessage;
	IntentFilter notificationFilter = new IntentFilter(
			"com.example.locationbased.action.updateui");
	IntentFilter imagesFilter = new IntentFilter(
			"com.example.locationbased.action.showimages");

	IntentFilter initializationFilter = new IntentFilter(
			"com.example.locationbased.action.initializeui");

	// To update ui after getting initial messages
	BroadcastReceiver initializationReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG,
					"received broadcast to initialize chat screen messages ..going to notify adapter");
			// list = (List<Map>) intent.getSerializableExtra("messages");
			Log.d(TAG, list.toString());
			adapter = new CustomAdapterNew(ChatActivityNew.this,
					R.layout.row_message, list);
			listView.setAdapter(adapter);
			Handler handler = new Handler(); // This is to delay the scroll till
												// after the web images
												// load..else it wont scroll to
												// the bottom...bcos it scrolls
												// before image gets expanded
			handler.postDelayed(new Runnable() {

				public void run() {
					listView.setSelection(adapter.getCount() - 1);
					listView.smoothScrollToPosition(listView.getCount() - 1);

				}

			}, 1700);
			// adapter.notifyDataSetChanged();
			new GetMessages().execute("lnr@gmail.com", "lnr");
		}
	};

	// To update ui after sending msg
	BroadcastReceiver imagesReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "received broadcast for images");
			WebView[] views = { w1, w2 };

			for (int i = 0; i < Math.min(imagesList.size(), 2); i++) {
				Map map = imagesList.get(i);
				String full_url = UserData.HOST + map.get("clipUrl").toString();
				views[i].loadUrl(full_url);
				Log.d(TAG, "Creating image ");
			}

		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(imagesReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();

		registerReceiver(imagesReceiver, imagesFilter);
		w1.setVisibility(4);
		w2.setVisibility(4);
//		 //Hiding the image views
//		 RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)
//		 w1.getLayoutParams(); //190 , 130 width,height
//		 RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams)
//		 w1.getLayoutParams(); //190 , 130 width,height
//		 LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams)
//		 listView.getLayoutParams(); //190 , 130 width,height
//		
//		 params1.height = 1;
//		 params2.height = 1;
//		 params1.width = 1;
//		 params2.width = 1;
//		 params3.height=600;
//		
//		 w1.setLayoutParams(params1);
//		 w2.setLayoutParams(params2);
//		 listView.setLayoutParams(params3);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(initializationReceiver);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		roomId = getIntent().getStringExtra("roomId");
		sendMessage = (Button) findViewById(R.id.buttonSend);
		getImages = (Button) findViewById(R.id.getImagesButton);
		message = (EditText) findViewById(R.id.message);
		listView = (ListView) findViewById(R.id.list_messages);
		notificationFilter.addAction(ChatActivity.ACTION_SEND_MESSAGE);
		notificationFilter.setPriority(1);
		w1 = (WebView) findViewById(R.id.webView1);
		w2 = (WebView) findViewById(R.id.webView2);

		Log.d(TAG, "Going to start GetMessage service");
		Intent intent1 = new Intent(
				"com.example.locationbased.action.getmessages");
		intent1.putExtra("roomId", roomId);
		startService(intent1);
		registerReceiver(initializationReceiver, initializationFilter);

		getImages.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.d(TAG, "OnButtonClicked");
				String messageValue = message.getText().toString();
				Intent intent = new Intent(
						"com.example.locationbased.action.getimages"); // I
																		// added
																		// postmessage
																		// just
																		// to
																		// differentiate
																		// from
																		// the
																		// old
																		// sendmessage
																		// which
																		// inserts
																		// messages
																		// into
																		// the
																		// db...since
																		// i
																		// dont
																		// wanna
																		// do
																		// that
																		// right
																		// now ,
																		// i'm
																		// using
																		// a
																		// separate
																		// service.
				intent.putExtra("message", messageValue);
				startService(intent);

				w1.setVisibility(0);
				w2.setVisibility(0);
				// //Unhide the images
				// RelativeLayout.LayoutParams params1 =
				// (RelativeLayout.LayoutParams) w1.getLayoutParams(); //190 ,
				// 130 width,height
				// RelativeLayout.LayoutParams params2 =
				// (RelativeLayout.LayoutParams) w1.getLayoutParams(); //190 ,
				// 130 width,height
				// LinearLayout.LayoutParams params3 =
				// (LinearLayout.LayoutParams) listView.getLayoutParams(); //190
				// , 130 width,height
				//
				// params1.height = 130;
				// params2.height = 130;
				// params1.width = 190;
				// params2.width = 190;
				// params3.height = 300;
				// w1.setLayoutParams(params1);
				// w2.setLayoutParams(params2);
				// listView.setLayoutParams(params3);

			}
		});

		sendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "OnButtonClicked");

				w1.loadUrl("about:blank");
				w2.loadUrl("about:blank");
				w2.setVisibility(0);
				w1.setVisibility(4);

				w2.setVisibility(4);
				String messageValue = message.getText().toString();
				Intent intent = new Intent(
						"com.example.locationbased.action.postmessage"); // I
																			// added
																			// postmessage
																			// just
																			// to
																			// differentiate
																			// from
																			// the
																			// old
																			// sendmessage
																			// which
																			// inserts
																			// messages
																			// into
																			// the
																			// db...since
																			// i
																			// dont
																			// wanna
																			// do
																			// that
																			// right
																			// now
																			// ,
																			// i'm
																			// using
																			// a
																			// separate
																			// service.
				intent.putExtra("message", messageValue);
				intent.putExtra("message_url",
						"http://imageshack.us/a/img706/8427/brahmi.gif"); // TODO
																			// This
																			// is
																			// actually
																			// not
																			// relevant...because
																			// we
																			// are
																			// sending
																			// a
																			// predefined
																			// clip
																			// id
																			// ...and
																			// not
																			// a
																			// url....Change
																			// this
																			// when
																			// u
																			// add
																			// a
																			// search
																			// endpoint
																			// ot
																			// add
																			// gif
																			// dynamically

				Log.d(TAG, "Room id is " + roomId);
				intent.putExtra("roomId", roomId);
				intent.putExtra("clipId", clipId);

				intent.putExtra("direction", "out");
				clipId = null; //clear clipId after message sent
				startService(intent);
			}
		});

		w1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				clipId = imagesList.get(0).get("id").toString();
				Log.d(TAG, "Clip Id selected is " + clipId);

				return true;
			}
		});

		w2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				clipId = imagesList.get(1).get("id").toString();
				Log.d(TAG, "Clip Id selected is " + clipId);

				return true;
			}
		});

	}

	public static List<Map> addToList(Map map) {
		list.add(map);
		Log.d(TAG, "Have added map to list");

		Log.d(TAG, "Size of list is" + list.size());
		return list;
	}

	public static Map<String, String> createRoomMap(String email,
			String message, String clipUrl, String time, int[] image_dimensions) {
		if (image_dimensions == null) {
			image_dimensions[0] = 0;
			image_dimensions[1] = 0;
		}

		Map map;
		map = new HashMap();
		map.put("email", email);
		map.put("message", message);
		map.put("clipUrl", clipUrl);
		map.put("time", time);
		map.put("width", image_dimensions[0]);
		map.put("height", image_dimensions[1]);
		Log.d(TAG, "Converting JSOn to map");
		return map;
	}

	public static Map<String, String> createImageMap(String id, String clipUrl) {
		Map map;
		map = new HashMap();
		map.put("id", id);
		map.put("clipUrl", clipUrl);
		Log.d(TAG, "Converting JSOn image to map");
		return map;
	}

	public static List<Map> addToImageList(Map map) {
		imagesList.add(map);
		Log.d(TAG, "Have added map to list");
		Log.d(TAG, "Size of list is" + list.size());
		return list;
	}

	public class GetMessages extends AsyncTask<String, String, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			String authentication_token = MyApplication.prefs.getString(
					"authentication_token", null);
			Log.d(TAG, roomId);
			Log.d(TAG, authentication_token);
			HttpClient httpClient = new DefaultHttpClient();
			Log.d(TAG, new Integer(list.size()).toString());
			lastMessage = list.get(list.size() - 1);
			Log.d(TAG, "Last message is " + lastMessage.toString());

			timeOfMostRecentMessage = (String) lastMessage.get("time");
			Log.d(TAG, "Most recent message is " + timeOfMostRecentMessage);
			HttpGet httpGet = new HttpGet(UserData.HOST + "/rooms/" + roomId
					+ "/messages.json?" + "auth_token=" + authentication_token
					+ "&since=" + timeOfMostRecentMessage);
			HttpResponse httpResponse = null;
			String response = null;
			InputStream is;

			try {
				httpResponse = httpClient.execute(httpGet);
				int responseCode = httpResponse.getStatusLine().getStatusCode();
				Log.d(TAG, "Response is" + httpResponse.toString());
				Log.d(TAG,
						"Response code is"
								+ new Integer(responseCode).toString());
				HttpEntity entity = httpResponse.getEntity();
				is = entity.getContent();

				JSONArray messagesArray;
				JSONArray dimensions;
				int[] image_dimensions = new int[2];

				switch (responseCode) {

				case 200:
					messagesArray = new JSONArray(
							NetworkHelper.convertStreamToString(is));

					for (int i = 0; i < messagesArray.length(); i++) {
						Log.d(TAG,
								"Response is ChatActivity Polling TASK is iterating for "
										+ i + "th time");

						JSONObject c = messagesArray.getJSONObject(i);
						String email = c.getString("user_email");
						String message = c.getString("content");
						String time = c.getString("created_at");
						String clipUrl = null;
						if (c.getString("clip") == null
								|| c.getString("clip") == "null") {
							// Log.d(TAG, "Clip is "+c.getString("clip"));
						} else {
							Log.d(TAG, c.getString("clip").getClass().getName());
							JSONObject clip = c.getJSONObject("clip");
							Log.d(TAG, clip.toString());
							clipUrl = clip.get("file_path").toString();
							Log.d(TAG, clipUrl.toString());
							dimensions = clip.getJSONArray("dimensions");

							image_dimensions[0] = Integer.parseInt(dimensions
									.get(0).toString());
							image_dimensions[1] = Integer.parseInt(dimensions
									.get(1).toString());

						}
						Map map = ChatActivityNew.createRoomMap(email, message,
								clipUrl, time, image_dimensions);
						Log.d(TAG, "Email is " + map.get("email").toString());
						list.add(map);
						Log.d(TAG, "Added to list");
					}

					if (messagesArray.length() > 0) {
						Log.d(TAG, "Messages Array Length is > 0");
						return true;
					}
					break;

				default:
					messagesArray = new JSONArray(
							NetworkHelper.convertStreamToString(is));
					Log.d(TAG, messagesArray.toString());
					// TODO Write error handling code here ... like checking for
					// n/w connection.
					break;
				}
				return false; // When no response is returned .

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result) {
				Log.d(TAG, "Result is true");
				Log.d(TAG, adapter.toString());
				adapter.notifyDataSetChanged();
				// Select the last row so it will scroll into view...

				Handler handler = new Handler(); // This is to delay the scroll
													// till after the web images
													// load..else it wont scroll
													// to the bottom...bcos it
													// scrolls before image gets
													// expanded
				handler.postDelayed(new Runnable() {

					public void run() {
						listView.setSelection(adapter.getCount() - 1);
						// listView.smoothScrollToPosition(listView.getCount());

					}

				}, 1700);

			}

			new GetMessages().execute("repeat");

		}

	}

}
