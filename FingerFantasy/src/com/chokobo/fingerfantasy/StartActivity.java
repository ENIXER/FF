package com.chokobo.fingerfantasy;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class StartActivity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_main);
		
		Button btn = (Button)findViewById(R.id.startBtn);
		btn.setOnClickListener(this);
	}

	@Override
    public void onClick(View v) {
		Intent i = new Intent(this,com.chokobo.fingerfantasy.QuestmenuActivity.class);
		this.startActivityForResult(i, 1);
	}
}
