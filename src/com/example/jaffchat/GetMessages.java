package com.example.jaffchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class GetMessages extends IntentService {

	public static String TAG = "GetMesssages";
	String message;
	String clipId;
	String roomId;
	List list = new ArrayList();
	CustomAdapterNew adapter;

	public GetMessages() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String authentication_token = MyApplication.prefs.getString(
				"authentication_token", null);
		roomId = intent.getStringExtra("roomId");
		Log.d(TAG, roomId);
		Log.d(TAG, authentication_token);
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(UserData.HOST + "/rooms/" + roomId
				+ ".json?" + "auth_token=" + authentication_token);
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
			JSONObject jsonObject;
			JSONArray messagesArray;
			JSONArray dimensions;
			int[] image_dimensions = new int[2];

			switch (responseCode) {

			case 200:
				messagesArray = new JSONArray();
				jsonObject = new JSONObject(
						NetworkHelper.convertStreamToString(is));
				messagesArray = jsonObject.getJSONArray("messages");
				Log.d(TAG, jsonObject.toString());
				Log.d(TAG, messagesArray.toString());

				for (int i = 0; i < messagesArray.length(); i++) {
					JSONObject c = messagesArray.getJSONObject(i);
					String email = c.getString("user_email");
					String message = c.getString("content");
					String time = c.getString("created_at");
					String clipUrl = null;
					if (c.getString("clip") == null
							|| c.getString("clip") == "null") {
						Log.d(TAG, "Clip is " + c.getString("clip"));
					} else {
						Log.d(TAG, c.getString("clip").getClass().getName());
						JSONObject clip = c.getJSONObject("clip");
						Log.d(TAG, clip.toString());
						clipUrl = clip.get("file_path").toString();
						dimensions = clip.getJSONArray("dimensions");

						image_dimensions[0] = Integer.parseInt(dimensions
								.get(0).toString());
						image_dimensions[1] = Integer.parseInt(dimensions
								.get(1).toString());
						Log.d(TAG, clipUrl.toString());
					}
					Map map = ChatActivityNew.createRoomMap(email, message,
							clipUrl, time, image_dimensions);
					Log.d(TAG, "Email is " + map.get("email").toString());
					ChatActivityNew.addToList(map);
					Log.d(TAG, "Added to list");
				}
				Log.d(TAG, list.toString());
				Intent intent1 = new Intent(
						"com.example.locationbased.action.initializeui");
				intent1.putExtra("messages", (ArrayList) list);
				sendBroadcast(intent1);
				break;

			default:
				messagesArray = new JSONArray(
						NetworkHelper.convertStreamToString(is));
				Log.d(TAG, messagesArray.toString());
				// TODO Write error handling code here ... like checking for
				// n/w connection.
				break;
			}

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

	}

}
