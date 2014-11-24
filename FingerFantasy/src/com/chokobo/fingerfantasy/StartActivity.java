package com.chokobo.fingerfantasy;

import com.chokobo.fingerfantasy.characters.CharacterManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class StartActivity extends Activity implements View.OnClickListener {

	private MediaPlayer mMediaPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_main);
		
		ImageButton start_btn = (ImageButton)findViewById(R.id.startBtn);
		start_btn.setOnClickListener(this);
		CharacterManager.initPlayer();
		
		mMediaPlayer = MediaPlayer.create(this, R.raw.startscene);
		mMediaPlayer.setLooping(true);
		mMediaPlayer.seekTo(0);
		mMediaPlayer.start();
	}
	

	@Override
    public void onClick(View v) {
		mMediaPlayer.stop();
		mMediaPlayer.release();
		
		mMediaPlayer = MediaPlayer.create(this, R.raw.push_bigger);
		mMediaPlayer.setLooping(false);
		mMediaPlayer.start();
		mMediaPlayer.stop();
		mMediaPlayer.release();
		Intent i = new Intent(this,com.chokobo.fingerfantasy.QuestmenuActivity.class);
		this.startActivityForResult(i, 1);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
	    if (event.getAction()==KeyEvent.ACTION_DOWN) {
	        switch (event.getKeyCode()) {
	        case KeyEvent.KEYCODE_BACK:
	            // ダイアログ表示など特定の処理を行いたい場合はここに記述
	            // 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
	            return true;
	        }
	    }
	    return super.dispatchKeyEvent(event);
	}
}
