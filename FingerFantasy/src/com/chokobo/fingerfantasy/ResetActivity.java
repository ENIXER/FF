package com.chokobo.fingerfantasy;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class ResetActivity extends ActionBarActivity implements View.OnClickListener{

	private int quest_no;
	private MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset);
		
		Intent i = getIntent();
		quest_no = i.getIntExtra("quest_no", -1);
		setBackGround();

		ImageButton nobtn = (ImageButton)findViewById(R.id.noBtn);
		nobtn.setOnClickListener(this);
	}

	@Override
    public void onClick(View v) {
		Intent i = new Intent(this,com.chokobo.fingerfantasy.QuestmenuActivity.class);
		this.startActivityForResult(i, 1);
		
		mMediaPlayer = MediaPlayer.create(this, R.raw.push_bigger);
		mMediaPlayer.setLooping(false);
		mMediaPlayer.start();
		mMediaPlayer.stop();
		mMediaPlayer.release();
	}

	public void setBackGround(){
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.resetLayout);

		switch (quest_no) {
		case 1:
			layout.setBackgroundResource(R.drawable.glass);
			break;
		case 2:
			layout.setBackgroundResource(R.drawable.volcano);
			break;
		case 3:
			layout.setBackgroundResource(R.drawable.waterfall);
			break;
		default:
			layout.setBackgroundResource(R.drawable.glass);
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reset, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
