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
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class SendMessageService extends IntentService {

	public static String TAG="SendMessageService";
	String message;
	String clipId;
	String roomId;

	public SendMessageService() {
		super(TAG);

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Log.d(TAG, "OnHandleIntent");
message=intent.getStringExtra("message");
clipId = intent.getStringExtra("clipId");
roomId = intent.getStringExtra("roomId");

List<NameValuePair> values =new ArrayList<NameValuePair>();
values.add(new BasicNameValuePair("clip[id]",clipId));
values.add(new BasicNameValuePair("content",message));
values.add(new BasicNameValuePair("auth_token",MyApplication.authentication_token()));

String url = UserData.HOST+"/rooms/"+intent.getStringExtra("roomId")+"/add_message.json";
Log.d(TAG, "Room is "+roomId);
Log.d(TAG, "Clip Id is "+clipId);
Log.d(TAG, "URL is "+url);


HttpClient httpClient =new DefaultHttpClient();
HttpPost httpPost = new HttpPost(url);
HttpResponse httpResponse=null;
	InputStream is;
	try {
		httpPost.setEntity(new UrlEncodedFormEntity(values));
		httpResponse = httpClient.execute(httpPost);
		int responseCode = httpResponse.getStatusLine().getStatusCode();
		Log.d(TAG, httpResponse.toString());
		Log.d(TAG, new Integer(responseCode).toString());
		HttpEntity entity = httpResponse.getEntity();
	    is = entity.getContent();
		JSONObject jsonObject;
		JSONArray messagesArray;

    
		switch (responseCode) {
		
		case 200: 
			Log.d(TAG, "Posted message to add_message endpoint");
			messagesArray =  new JSONArray();
			jsonObject = new JSONObject(NetworkHelper.convertStreamToString(is));
			String email = jsonObject.getString("user_email");
			String message = jsonObject.getString("content");
			String time = jsonObject.getString("created_at");
			String clipUrl = null;
			if(jsonObject.getString("clip")==null || jsonObject.getString("clip")=="null"){
				Log.d(TAG, "Clip is "+jsonObject.getString("clip"));
			}
			else {	
				Log.d(TAG, jsonObject.getString("clip").getClass().getName());
			JSONObject clip = jsonObject.getJSONObject("clip");
			Log.d(TAG, clip.toString());
			 clipUrl = clip.get("file_path").toString();
			Log.d(TAG, clipUrl.toString());
			}
			Map map = createRoomMap(email,message,clipUrl);			
			
			
//			Intent intent1 = new Intent("com.example.locationbased.action.updateui");
//			intent1.putExtra("email", email);
//			intent1.putExtra("message", message);
//			intent1.putExtra("clipUrl", clipUrl);
//			intent1.putExtra("time", time);
//
//			//sendBroadcast(intent1);   //For updating ui
			        break;
		
		  default:
				
				Log.d(TAG, httpResponse.toString());
				//TODO Write error handling code here ... like checking for n/w connection.
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
	
	
	public  Map<String, String> createRoomMap(String email,String message,String clipUrl) {
		Map map;
		map = new HashMap();
		map.put("email", email);
		map.put("message",message);
		map.put("clipUrl", clipUrl);
		Log.d(TAG, "Converting JSOn to map");
		return map;
	}
	
	
//	public  Boolean getMessages() {
//		String authentication_token = MyApplication.prefs.getString("authentication_token", null);
//        Log.d(TAG, roomId);
//        Log.d(TAG, authentication_token);			
//		HttpClient httpClient = new DefaultHttpClient();			
//		HttpGet httpGet = new HttpGet("http://10.0.2.2:3000/rooms/"+roomId+".json?"+"auth_token="+authentication_token); 
//		HttpResponse httpResponse = null;
//		String response = null;
//		InputStream is;
//
//		try {
//			httpResponse = httpClient.execute(httpGet);
//			int responseCode = httpResponse.getStatusLine().getStatusCode();
//			Log.d(TAG, httpResponse.toString());
//			Log.d(TAG, new Integer(responseCode).toString());
//			HttpEntity entity = httpResponse.getEntity();
//			is = entity.getContent();
//			JSONObject jsonObject;
//			JSONArray messagesArray;
//
//			switch (responseCode) {
//
//			case 200:
//				messagesArray =  new JSONArray();
//				jsonObject = new JSONObject(
//						NetworkHelper.convertStreamToString(is));
//				messagesArray = jsonObject.getJSONArray("messages");
//				Log.d(TAG, jsonObject.toString());
//				Log.d(TAG, messagesArray.toString());
//				
//				for (int i = 0; i < messagesArray.length(); i++) {
//					JSONObject c = messagesArray.getJSONObject(i);
//					String email = c.getString("user_email");
//					String message = c.getString("content");
//					String clipUrl = null;
//					if(c.getString("clip")==null || c.getString("clip")=="null"){
//						Log.d(TAG, "Clip is "+c.getString("clip"));
//					}
//					else {	
//						Log.d(TAG, c.getString("clip").getClass().getName());
//					JSONObject clip = c.getJSONObject("clip");
//					Log.d(TAG, clip.toString());
//					 clipUrl = clip.get("file_path").toString();
//					Log.d(TAG, clipUrl.toString());
//					}
//					Map map = ChatActivityNew.createRoomMap(email,message,clipUrl);
//					Log.d(TAG, "Email is "+map.get("email").toString());
//					ChatActivityNew.addToList(map);
//					Log.d(TAG,"Added to list");
//				}
//				Log.d(TAG, list.toString());
//				 adapter = new CustomAdapterNew(ChatActivityNew.this, R.layout.row_message, list);
//				return true;
//
//			default:
//				messagesArray = new JSONArray(NetworkHelper.convertStreamToString(is));
//				Log.d(TAG, messagesArray.toString());
//				// TODO Write error handling code here ... like checking for
//				// n/w connection.
//				break;
//			}
//			return false; // When no response is returned .
//
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//return null;
//		}
//
//		
//	}
//

}
