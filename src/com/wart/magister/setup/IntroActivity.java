package com.wart.magister.setup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.wart.magister.R;

public class IntroActivity extends Activity {

	private Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		mButton = (Button) findViewById(R.id.intro_button);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				IntroActivity.this.startActivity(new Intent(IntroActivity.this, SelectSchoolActivity.class));
			}
		});
	}
}