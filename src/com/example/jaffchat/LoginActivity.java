package com.example.jaffchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	static final String TAG = "locationbasedactivity";
	public static SharedPreferences prefs;
	EditText emailIdET;
	EditText passwordET;
	Button loginButton;
	Button signupButton;
	String emailId,password;
	String action="login";
	String apid;
	String gcm_regid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Log.d(TAG, "Oncreated FirstActivity");
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		emailIdET = (EditText) findViewById(R.id.email);
		passwordET = (EditText) findViewById(R.id.password);
		loginButton = (Button) findViewById(R.id.sign_in_button);
		signupButton = (Button) findViewById(R.id.sign_up_button);
        
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	emailId=emailIdET.getText().toString();
	password=passwordET.getText().toString();
	Log.d(TAG, "Emailid and passwrd are "+emailId+" and "+password);
	new ValidateLogin().execute(emailId,password,action);
			
				
			}
		});
		
		
		signupButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class); 
			//	intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);	
				finish();
	
			}
		});
		
		
		
		
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "On Paused FirstActivity");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "On Resumed FirstActivity");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d(TAG, "On Stopped FirstActivity");
	}
	
	
	class ValidateLogin extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
//			while(apid==null)
//				{apid = PushManager.shared().getAPID();
//				gcm_regid = PushManager.shared().getGcmId();
//			Log.d(TAG, "APID is "+apid);
//			Log.d(TAG, "GCM id is"+gcm_regid);
//				}
		List<NameValuePair> values=new ArrayList<NameValuePair>();
		values.add(new BasicNameValuePair("user[email]", params[0]));
		values.add(new BasicNameValuePair("user[password]",params[1]));
		//values.add(new BasicNameValuePair("action",action));
		//values.add(new BasicNameValuePair("uairship_id", apid));
		//values.add(new BasicNameValuePair("gcm_reg_id", gcm_regid));
	
			
	HttpClient httpClient =new DefaultHttpClient();
	HttpPost httpPost = new HttpPost("http://10.0.2.2:3000/users/sign_in.json");
	HttpResponse httpResponse=null;
	String response=null;
	
		InputStream is;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(values));
			httpResponse = httpClient.execute(httpPost);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			Log.d(TAG, httpResponse.toString());
			Log.d(TAG, new Integer(responseCode).toString());
			
			switch (responseCode) {
			
			case 200: HttpEntity entity = httpResponse.getEntity();
				       is = entity.getContent();
				      JSONObject jObj = new JSONObject(convertStreamToString(is));
				      jObj.get("auth_token").toString();
				       String authentication_token =  jObj.get("auth_token").toString();
				       Log.d(TAG,authentication_token);
				       Editor editor = prefs.edit();
				       editor.putString("authentication_token", authentication_token);
				       editor.commit();	
				       return authentication_token;
			
			case 201:
//				HttpEntity entity = httpResponse.getEntity();
//				if (entity != null) {
//					InputStream is = entity.getContent();
//					String responseString = convertStreamToString(is);
//					Editor editor = prefs.edit();
//					if (responseString.trim().equalsIgnoreCase(
//							"Succesfully Registered")) {
//						
//						editor.putBoolean("registered", true);  // Indicates that we have registered at least one time
//						editor.putBoolean("loggedin", true);  //Indicates that user is signed in
//						editor.putString("uairship_id", apid);
//						editor.putString("emailId", emailIDValue);
//						editor.putString("password", passwordValue);
//						editor.putString("phone", phoneValue);
//						editor.putString("gcm_regid", gcm_regid);
//						editor.putString("firstName", firstNameValue);
//						editor.putString("lastName", lastNameValue);
//						editor.putString("gender",gender);
//						editor.commit();
//						return "You have successfully Registered";
//
//					} else if (responseString.trim().equalsIgnoreCase(
//							"Registration Failed"))
//						return "Registration Failed";
//
//				}
//				break;
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

		return null;

	}


		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result!=null)
			{
		    Log.d(TAG, result);
			Editor editor =prefs.edit();
			editor.putBoolean("loggedin", true);
			editor.putString("authentication_token", result);
			editor.commit();
			UserData.authentication_token = result;
			Log.d(TAG,result);
			Intent intent = new Intent(LoginActivity.this, JaffaMainActivity.class); 
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);	
			finish();			
			}
			else
				Toast.makeText(LoginActivity.this, "Invalid username/password", Toast.LENGTH_LONG).show();
		}
		
	}
	
	
	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append((line + "\n"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	

	

}
