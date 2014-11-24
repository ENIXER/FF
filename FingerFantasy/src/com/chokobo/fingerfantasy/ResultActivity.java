package com.chokobo.fingerfantasy;

import com.chokobo.fingerfantasy.characters.CharacterManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity implements View.OnClickListener {
	private TextView levelText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reesult);

		ImageButton btn = (ImageButton) findViewById(R.id.backBtn);
		btn.setOnClickListener(this);

		levelText = (TextView) findViewById(R.id.level_text);
		levelText.setText(Integer.toString(CharacterManager.getPlayerLevel()));

		CharacterManager.earnExp();
		setExp();
		setNextExp();
	}

	private void setExp() {
		TextView exp_view = (TextView) findViewById(R.id.getExp_text);
		int exp_value = CharacterManager.getExp();
		String exp_string = String.valueOf(exp_value);
		exp_view.setText(exp_string);
	}

	private void setNextExp() {
		TextView nextExpView = (TextView) findViewById(R.id.nextExp_text);
		int nextExp = CharacterManager.getNextExp();
		String nextExpString = String.valueOf(nextExp);
		nextExpView.setText(nextExpString);
	}

	@Override
	public void onClick(View v) {
		if (CharacterManager.isPlayerLevelUp()) {
			findViewById(R.id.level_up).setVisibility(View.VISIBLE);
			levelText.setText(Integer.toString(CharacterManager
					.getPlayerLevel()));
			levelText.setTextColor(Color.RED);
			CharacterManager.resetPlayerLevelUp();
		} else {
			Intent i = new Intent(this,
					com.chokobo.fingerfantasy.QuestmenuActivity.class);
			this.startActivityForResult(i, 1);
			finish();
		}
	}

}
