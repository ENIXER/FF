package com.chokobo.fingerfantasy;

import com.chokobo.fingerfantasy.characters.CharacterManager;
import com.chokobo.fingerfantasy.customviews.TestView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;

public class BattleActivity extends Activity {

	private int quest_no;
	private MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.setActivity(this);
		setContentView(R.layout.activity_battle);
		Intent i = getIntent();
		quest_no = i.getIntExtra("quest_no", -1);
		initView();
		TestView testview = (TestView)findViewById(R.id.testview);
		testview.init(CharacterManager.getCrystal());
	}

	private void initView() {
		setEnemy();
		ProgressBar p_bar = (ProgressBar) findViewById(R.id.player_bar);
		ProgressBar e_bar = (ProgressBar) findViewById(R.id.enemy_bar);
		TextView lvView = (TextView) findViewById(R.id.lv_text);
		lvView.setText(Integer.toString(CharacterManager.getPlayerLevel()));
		CharacterManager.initEnemy(quest_no);
		CharacterManager.setProgressBar(p_bar, e_bar);
		CharacterManager.setTurn();
		setPlayerHp();
	}

	private void setEnemy() {
		Bitmap enemy_image;

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.battle_layout);
		ImageView enemyName = (ImageView) findViewById(R.id.enemy_name);

		switch (quest_no) {
		case 1:
			layout.setBackgroundResource(R.drawable.glass);
			enemyName.setImageResource(R.drawable.ago_name);
			enemy_image = BitmapFactory.decodeResource(getResources(),
					R.drawable.ago);
			mMediaPlayer = MediaPlayer.create(this, R.raw.zakobattle);
			break;
		case 2:
			layout.setBackgroundResource(R.drawable.volcano);
			enemyName.setImageResource(R.drawable.dragon_name);
			enemy_image = BitmapFactory.decodeResource(getResources(),
					R.drawable.dragon_light);
			mMediaPlayer = MediaPlayer.create(this, R.raw.dragonbattle);
			break;
		case 3:
			layout.setBackgroundResource(R.drawable.waterfall);
			enemyName.setImageResource(R.drawable.leviathan_name);
			enemy_image = BitmapFactory.decodeResource(getResources(),
					R.drawable.leviathan_kai);
			mMediaPlayer = MediaPlayer.create(this, R.raw.leviazanbattle);
			break;
		default:
			layout.setBackgroundResource(R.drawable.glass);
			enemy_image = BitmapFactory.decodeResource(getResources(),
					R.drawable.zako);
			break;
		}

		ImageView enemy_imageview = (ImageView) findViewById(R.id.enemy_image);
		enemy_imageview.setImageBitmap(enemy_image);
		mMediaPlayer.setLooping(true);
		mMediaPlayer.seekTo(0);
		mMediaPlayer.start();
	}

	public void showContinue() {
		resetMedia();
		Intent i = new Intent(this,
				com.chokobo.fingerfantasy.ResetActivity.class);
		i.putExtra("quest_no", quest_no);
		startActivity(i);
	}
	
	public void resetMedia(){
		mMediaPlayer.stop();
		mMediaPlayer.release();
	}

	public void showDamage(int damage) {
		Toast.makeText(this, damage + "のダメージ", Toast.LENGTH_SHORT).show();
	}
	
	public void setPlayerHp(){
		int player_hp_value = CharacterManager.getPlayerHp();
		String player_hp_string = String.valueOf(player_hp_value);
		int player_maxhp_value = CharacterManager.getPlayerMaxHp();
		String player_maxhp_string = String.valueOf(player_maxhp_value);
		
		TextView player_hp_textview = (TextView)findViewById(R.id.hp_text);
		player_hp_textview.setText(player_hp_string);
		TextView player_maxhp_textview = (TextView)findViewById(R.id.maxhp_text);
		player_maxhp_textview.setText(player_maxhp_string);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.battle, menu);
		return true;
	}

}
