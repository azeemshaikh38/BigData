package com.example.smartgrid;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleActivity extends Activity {
	String HadoopoutSchedule = null;
	TextView scheduleText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		scheduleText = (TextView) findViewById(R.id.textView2);
		Button home = (Button) findViewById(R.id.button1);
		Button force = (Button)findViewById(R.id.button4);
		Button getSchedule = (Button) findViewById(R.id.button2);
		Button commit = (Button) findViewById(R.id.button3);
		home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		getSchedule.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new ScheduleTask().execute();

			}
		});
		commit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new CommitTask().execute();

			}
		});
		force.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new ForceTask().execute();

			}
		});
	}

	private class ScheduleTask extends AsyncTask<Void, Integer, Double> {

		protected Double doInBackground(Void... voids) {
			postData();

			return null;
		}

		public void postData() {
			EditText ipAddress = (EditText) findViewById(R.id.editText1);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://" + ipAddress.getText()
					+ "/Schedule.php");

			try {

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				HadoopoutSchedule = EntityUtils.toString(entity);
				//Log.d("tagg", HadoopoutSchedule);

			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				return;
			}
		}

		protected void onPostExecute(Double result) {
			//Log.d("tagg1", HadoopoutSchedule);
			scheduleText.setText(HadoopoutSchedule);

		}

	}

	private class CommitTask extends AsyncTask<Void, Integer, Double> {
		String HadoopoutCommit;

		protected Double doInBackground(Void... voids) {
			postData();

			return null;
		}

		public void postData() {
			EditText ipAddress = (EditText) findViewById(R.id.editText1);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://" + ipAddress.getText()
					+ "/commit.php");

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						1);
				nameValuePairs.add(new BasicNameValuePair("Schedule",
						HadoopoutSchedule.toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				HadoopoutCommit = EntityUtils.toString(entity);

			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				return;
			}
		}
		protected void onPostExecute(Double result) {
			
			Toast.makeText(getBaseContext(),HadoopoutCommit,Toast.LENGTH_LONG ).show();
		}

	}
	
	private class ForceTask extends AsyncTask<Void, Integer, Double> {
		String HadoopoutForce;

		protected Double doInBackground(Void... voids) {
			postData();

			return null;
		}

		public void postData() {
			EditText ipAddress = (EditText) findViewById(R.id.editText1);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://" + ipAddress.getText()
					+ "/commit.php");

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						1);
				nameValuePairs.add(new BasicNameValuePair("Schedule",
						HadoopoutSchedule.toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				HadoopoutForce = EntityUtils.toString(entity);

			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				return;
			}
		}
		protected void onPostExecute(Double result) {
			
			Toast.makeText(getBaseContext(),HadoopoutForce,Toast.LENGTH_LONG ).show();
		}

	}
}
