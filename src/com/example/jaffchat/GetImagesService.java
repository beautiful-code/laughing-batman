package com.example.jaffchat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
import android.net.Uri;
import android.util.Log;

public class GetImagesService extends IntentService {
	String message;
	List list = new ArrayList();

	
	public static String TAG = "GetImagesService";
	public GetImagesService() {
		super(TAG);

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "On Handle intent");

		String authentication_token = MyApplication.prefs.getString("authentication_token", null);
		message = intent.getStringExtra("message");
        Log.d(TAG, authentication_token);			
        HttpClient httpClient = new DefaultHttpClient();
        //String full_url = UserData.HOST+"/search.json?"+"auth_token="+authentication_token+"&q="+message;  
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").authority("54.254.137.190").appendPath("search.json").appendQueryParameter("auth_token", authentication_token).appendQueryParameter("q", message);

        String myUrl = builder.build().toString();

		try {
			Log.d(TAG, myUrl);
			HttpGet httpGet = new HttpGet(myUrl);
			
			HttpResponse httpResponse = null;
			String response = null;
			InputStream is;
			httpResponse = httpClient.execute(httpGet);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			Log.d(TAG, httpResponse.toString());
			Log.d(TAG, new Integer(responseCode).toString());
			HttpEntity entity = httpResponse.getEntity();
			is = entity.getContent();
			JSONArray imagesArray;
			
			switch(responseCode) {
			
			case 200: 
				  imagesArray = new JSONArray(NetworkHelper.convertStreamToString(is));	
				  ChatActivityNew.imagesList.clear();
				  for (int i = 0; i < imagesArray.length(); i++) {
					  JSONArray a = imagesArray.getJSONArray(i);
					  JSONObject c =  a.getJSONObject(0);
					  String clipId = c.getString("_id");
					  String file_path = c.getString("file_path");					  
					  Log.d(TAG, "Going to add to images list");
					  ChatActivityNew.addToImageList(ChatActivityNew.createImageMap(clipId, file_path));					  
				  }
				  Intent intent1 = new Intent("com.example.locationbased.action.showimages");
                  sendBroadcast(intent1);				  
			}
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
