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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateActivity extends Activity {
	
	EditText AppName = null;
	EditText wattage = null;
	EditText startTime = null;
	EditText endTime = null;
	EditText runTime = null;
	EditText constraint = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);
		
		AppName = (EditText)findViewById(R.id.editText2);
		wattage = (EditText)findViewById(R.id.editText3);
		startTime = (EditText)findViewById(R.id.editText4);
		endTime = (EditText)findViewById(R.id.editText5);
		runTime = (EditText)findViewById(R.id.editText6);
		constraint = (EditText)findViewById(R.id.editText7);
		
		Button add = (Button)findViewById(R.id.button1);
		add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AddTask().execute();
				
			}
		});
	}
	private class AddTask extends AsyncTask<Void, Integer, Double> {
		String HadoopoutAdd;

		protected Double doInBackground(Void... voids) {
			postData();

			return null;
		}

		public void postData() {
			EditText ipAddress = (EditText) findViewById(R.id.editText1);
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://" + ipAddress.getText()
					+ "/createAppliances.php");

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						1);
				nameValuePairs.add(new BasicNameValuePair("Name", AppName
						.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("StartTime",
						startTime.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("EndTime", endTime
						.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("Wattage", wattage
						.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("RunTime", runTime
						.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("Constraint",
						constraint.getText().toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				HadoopoutAdd = EntityUtils.toString(entity);
				

			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				return;
			}
		}

		protected void onPostExecute(Double result) {
			AppName.setText("");
			wattage.setText("");
			startTime.setText("");
			endTime.setText("");
			runTime.setText("");
			constraint.setText("");
			Toast.makeText(CreateActivity.this, HadoopoutAdd, Toast.LENGTH_LONG).show();

		}

	}

}
