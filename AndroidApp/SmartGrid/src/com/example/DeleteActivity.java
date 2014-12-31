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

public class DeleteActivity extends Activity {
	EditText ip = null;
	EditText AppName = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete);
		
		AppName = (EditText)findViewById(R.id.editText2);
		
		Button delete = (Button)findViewById(R.id.button1);
		delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new DeleteTask().execute();
				
			}
		});
	}
	
	private class DeleteTask extends AsyncTask<Void, Integer, Double> {
		String HadoopoutDelete;

		protected Double doInBackground(Void... voids) {
			postData();

			return null;
		}

		public void postData() {
			EditText ipAddress = (EditText) findViewById(R.id.editText1);
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://" + ipAddress.getText()
					+ "/deleteAppliance.php");

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						1);
				nameValuePairs.add(new BasicNameValuePair("Name", AppName
						.getText().toString()));
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				HadoopoutDelete = EntityUtils.toString(entity);

			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				return;
			}
		}

		protected void onPostExecute(Double result) {
			AppName.setText("");
			

		}

	}
}
