package com.chokobo.fingerfantasy;

import com.chokobo.fingerfantasy.characters.CharacterManager;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BattleActivity extends ActionBarActivity {

	private int quest_no;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battle);

		ActivityManager.setActivity(this);
		Intent i = getIntent();
		quest_no = i.getIntExtra("quest_no", -1);

		initView();
	}

	private void initView() {
		setEnemy();
		ProgressBar p_bar = (ProgressBar) findViewById(R.id.player_bar);
		ProgressBar e_bar = (ProgressBar) findViewById(R.id.enemy_bar);
		CharacterManager.initCharacter(p_bar, e_bar, quest_no);
	}

	private void setEnemy() {
		Bitmap enemy_image;

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.battle_layout);
		ImageView enemyName = (ImageView)findViewById(R.id.enemy_name);

		switch (quest_no) {
		case 1:
			layout.setBackgroundResource(R.drawable.glass);
			enemyName.setImageResource(R.drawable.ago_name);
			enemy_image = BitmapFactory.decodeResource(getResources(),
					R.drawable.ago);
			break;
		case 2:
			layout.setBackgroundResource(R.drawable.volcano);
			enemyName.setImageResource(R.drawable.dragon_name);
			enemy_image = BitmapFactory.decodeResource(getResources(),
					R.drawable.dragon_light);
			break;
		case 3:
			layout.setBackgroundResource(R.drawable.waterfall);
			enemyName.setImageResource(R.drawable.leviathan_name);
			enemy_image = BitmapFactory.decodeResource(getResources(),
					R.drawable.leviathan);
			break;
		default:
			layout.setBackgroundResource(R.drawable.glass);
			enemy_image = BitmapFactory.decodeResource(getResources(),
					R.drawable.zako);
			break;
		}

		ImageView enemy_imageview = (ImageView) findViewById(R.id.enemy_image);
		enemy_imageview.setImageBitmap(enemy_image);
	}
	
	public void showContinue(){
		Intent i = new Intent(this,
				com.chokobo.fingerfantasy.ResetActivity.class);
		i.putExtra("quest_no", quest_no);
		startActivity(i);
		
//		Toast.makeText(this, "テスト", Toast.LENGTH_LONG).show();
//		
//		ImageView imageview = new ImageView(this);
//		
//		ImageView continue_view = (ImageView)findViewById(R.id.continue_imageview);
//		ImageButton yes_button = (ImageButton)findViewById(R.id.yes_image);
//		//ImageButton no_button = (ImageButton)findViewById(R.id.no_image);
//		
//		continue_view.bringToFront();
//		continue_view.setVisibility(View.VISIBLE);
//		yes_button.setVisibility(View.VISIBLE);
		//no_button.setVisibility(View.VISIBLE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.battle, menu);
		return true;
	}

	public void startMovie(){
		VideoView view = (VideoView) findViewById(R.id.video);
		view.setVisibility(View.VISIBLE);
		view.setVideoPath("assets/ice_efect.mp4");
		view.start();
	}
}
