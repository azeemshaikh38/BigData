package com.example.smartgrid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button create = (Button) findViewById(R.id.button1);
		Button delete = (Button) findViewById(R.id.button2);
		Button edit = (Button) findViewById(R.id.button3);
		Button schedule = (Button) findViewById(R.id.button4);
		Button view = (Button) findViewById(R.id.button5);

		create.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(MainActivity.this,
						CreateActivity.class);
				startActivity(intent1);

			}
		});

		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(MainActivity.this,
						DeleteActivity.class);
				startActivity(intent2);

			}
		});

		edit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent3 = new Intent(MainActivity.this,
						EditActivity.class);
				startActivity(intent3);

			}
		});

		schedule.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent4 = new Intent(MainActivity.this,
						ScheduleActivity.class);
				startActivity(intent4);

			}
		});

		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent5 = new Intent(MainActivity.this,
						ViewActivity.class);
				startActivity(intent5);

			}
		});

	}
}
