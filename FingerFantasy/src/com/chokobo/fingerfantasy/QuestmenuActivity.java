package com.chokobo.fingerfantasy;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class QuestmenuActivity extends Activity {

	private MediaPlayer mMediaPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questmenu);
		
		mMediaPlayer = MediaPlayer.create(this, R.raw.questchoice);
		mMediaPlayer.setLooping(true);
		mMediaPlayer.seekTo(0);
		mMediaPlayer.start();
	}

	public void onQuestButtonClick(View v) {
		Intent i = new Intent(QuestmenuActivity.this,
				com.chokobo.fingerfantasy.BattleActivity.class);
		i.putExtra("quest_no", Integer.parseInt((String) v.getTag()));
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		mMediaPlayer.stop();
		mMediaPlayer.release();
		mMediaPlayer = MediaPlayer.create(this, R.raw.push_bigger);
		mMediaPlayer.setLooping(false);
		mMediaPlayer.start();
		mMediaPlayer.stop();
		mMediaPlayer.release();
		finish();
	}

	public void onBackButtonClick(View v) {
		mMediaPlayer.stop();
		mMediaPlayer.release();
		finish();
	}
}
