package com.chokobo.fingerfantasy;

import com.chokobo.fingerfantasy.characters.CharacterManager;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class BattleActivity extends ActionBarActivity {

	private int quest_no;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_battle);
		
		ActivityManager.setActivity(this);
		Intent i = getIntent();
		quest_no = i.getIntExtra("quest_no",-1);
		
		initView();
	}

	private void initView(){
		setEnemy();
		ProgressBar p_bar = (ProgressBar)findViewById(R.id.player_bar);
		ProgressBar e_bar = (ProgressBar)findViewById(R.id.enemy_bar);
		CharacterManager.initCharacter(p_bar, e_bar, quest_no);
	}
	
	private void setEnemy(){
		Bitmap enemy_image;
		
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.battle_layout);

		switch (quest_no) {
		case 1:
			layout.setBackgroundResource(R.drawable.glass);
	        enemy_image = BitmapFactory.decodeResource(getResources(),R.drawable.ago);
			break;
		case 2:
			layout.setBackgroundResource(R.drawable.volcano);
	        enemy_image = BitmapFactory.decodeResource(getResources(),R.drawable.dragon_light);
	        break;
		default:
			layout.setBackgroundResource(R.drawable.glass);
			enemy_image = BitmapFactory.decodeResource(getResources(),R.drawable.zako);
			break;
		}
		
		ImageView enemy_imageview = (ImageView)findViewById(R.id.enemy_image);
		enemy_imageview.setImageBitmap(enemy_image);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.battle, menu);
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
