package com.chokobo.fingerfantasy;

import com.chokobo.fingerfantasy.characters.CharacterManager;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;

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
		TextView lvView = (TextView) findViewById(R.id.lv_text);
		lvView.setText(Integer.toString(CharacterManager.getPlayerLevel()));
		CharacterManager.initEnemy(quest_no);
		CharacterManager.setProgressBar(p_bar, e_bar);
		CharacterManager.setTurn();
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

	public void showContinue() {
		Intent i = new Intent(this,
				com.chokobo.fingerfantasy.ResetActivity.class);
		i.putExtra("quest_no", quest_no);
		startActivity(i);

	}

	public void showDamage(int damage) {
		Toast.makeText(this, damage + "のダメージ", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.battle, menu);
		return true;
	}

}
