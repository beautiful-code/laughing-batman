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
import android.widget.RadioButton;
import android.widget.Toast;

public class RegistrationActivity extends Activity {
	static final String TAG = "locationbasedactivity";
	public static SharedPreferences prefs;

	private static EditText emailID,  password;
	public static String emailIDValue, passwordValue;
	private static Button buttonSignup;
	String gender;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "on created MainActivity");
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		setContentView(R.layout.activity_registration);
		password = (EditText) findViewById(R.id.password);
		emailID = (EditText) findViewById(R.id.email);
		buttonSignup = (Button) findViewById(R.id.sign_up_button);
		buttonSignup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				emailIDValue = emailID.getText().toString();
				passwordValue = password.getText().toString();
				new SignUp().execute();
			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "On Paused MainActivity");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "On Resumed MainActivity");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "On Stopped MainActivity");
	}

	public class SignUp extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {

			HttpClient client = new DefaultHttpClient();
			List<NameValuePair> pair = new ArrayList<NameValuePair>();
			pair.add(new BasicNameValuePair("user[email]", emailIDValue));
			pair.add(new BasicNameValuePair("user[password]", passwordValue));
			HttpPost httpPost = new HttpPost(UserData.HOST+"/users.json");

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(pair));
				HttpResponse httpResponse = null;
				httpResponse = client.execute(httpPost);
				int responseCode = httpResponse.getStatusLine().getStatusCode();
				HttpEntity entity = httpResponse.getEntity();
				InputStream is = entity.getContent();
				JSONObject jObj;
				switch (responseCode) {

				case 201:
					 jObj = new JSONObject(
							NetworkHelper.convertStreamToString(is));
					jObj.get("auth_token").toString();
					String authentication_token = jObj.get("authentication_token").toString();
					//TODO Have to extend Devises Registrations controller to return "authentication_token" ...as of now ...not returning anything I think
					Log.d(TAG, authentication_token);
					UserData.authentication_token = authentication_token;
					Log.d(TAG, authentication_token);
					Editor editor = prefs.edit();
					editor.putString("authentication_token",authentication_token);
					editor.commit();
					break;
				default:
					jObj = new JSONObject(NetworkHelper.convertStreamToString(is));
					Log.d(TAG, jObj.toString());
					break;

				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(RegistrationActivity.this, result, Toast.LENGTH_LONG)
					.show();
			Intent intent = new Intent(RegistrationActivity.this,
					JaffaMainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();

		}

	}

}
