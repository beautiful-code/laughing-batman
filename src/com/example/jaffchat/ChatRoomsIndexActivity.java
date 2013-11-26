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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ChatRoomsIndexActivity extends Activity {

	static String TAG = "ChatRoomActivity";
	static List<Map> list = new ArrayList<Map>();
	SimpleAdapter adapter;
	String[] from = { "roomId" };
	int[] to = { R.id.room_id };
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_rooms_index);
		listView = (ListView) findViewById(R.id.list_rooms);
		new GetRooms().execute(null, null, null);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Log.d(TAG, ((HashMap) parent.getItemAtPosition(position)).get("roomId").toString());
				Intent intent = new Intent(ChatRoomsIndexActivity.this, ChatActivityNew.class);
			    intent.putExtra("roomId", ((HashMap) parent.getItemAtPosition(position)).get("roomId").toString());   //this is the room id
			    startActivity(intent);
			    
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_rooms_index, menu);
		return true;
	}

	public static List<Map> addToList(Map map) {
		list.add(map);
		Log.d(TAG, "Size of list is" + list.size());
		return list;
	}

	public static Map<String, String> createRoomMap(String roomId) {
		Map map;
		map = new HashMap();
		Log.d(TAG, "Converting JSOn to map");
		map.put("roomId", roomId);
		return map;
	}

	public class GetRooms extends AsyncTask<String, String, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {

			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(UserData.HOST+"/rooms.json");
			HttpResponse httpResponse = null;
			String response = null;
			InputStream is;

			try {
				httpResponse = httpClient.execute(httpGet);
				int responseCode = httpResponse.getStatusLine().getStatusCode();
				Log.d(TAG, httpResponse.toString());
				Log.d(TAG, new Integer(responseCode).toString());
				HttpEntity entity = httpResponse.getEntity();
				is = entity.getContent();
				JSONArray roomsArray;

				switch (responseCode) {

				case 200:
					new JSONArray();
					roomsArray = new JSONArray(
							NetworkHelper.convertStreamToString(is));
					for (int i = 0; i < roomsArray.length(); i++) {
						JSONObject c = roomsArray.getJSONObject(i);
						String roomId = c.getString("id");
						Log.d(TAG, "Room Id is " + roomId);						
						ChatRoomsIndexActivity.addToList(ChatRoomsIndexActivity.createRoomMap(roomId));
					}
					Log.d(TAG, list.toString());					
					adapter = new SimpleAdapter(ChatRoomsIndexActivity.this, (List<? extends Map<String, ?>>) list, R.layout.row_room, from, to);
					return true;

				default:
					roomsArray = new JSONArray(
							NetworkHelper.convertStreamToString(is));
					Log.d(TAG, roomsArray.toString());
					// TODO Write error handling code here ... like checking for
					// n/w connection.
					break;
				}
				return false;   //When no response is returned . 

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
			
			if(result)
			{
				listView.setAdapter(adapter);
			}			
			
			
		}

	
	
	}

}
