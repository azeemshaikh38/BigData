package com.example.smartgrid;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
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

public class ViewActivity extends Activity {

	TextView text = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		
		text = (TextView)findViewById(R.id.editText1);
		Button show = (Button)findViewById(R.id.button1);
		show.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new ViewAppliance().execute();
				
			}
		});
		
		
	}
	private class ViewAppliance extends AsyncTask<Void, Integer, Double> {
		String HadoopoutView;

		protected Double doInBackground(Void... voids) {
			postData();

			return null;
		}

		public void postData() {
			EditText ipAddress = (EditText) findViewById(R.id.editText1);
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://" + ipAddress.getText()
					+ "/viewAppliances.php");

			try {
				
				
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				HadoopoutView = EntityUtils.toString(entity);

			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				return;
			}
		}
		protected void onPostExecute(Double result) {
			text.setText(HadoopoutView);

		}


		

	}
}
