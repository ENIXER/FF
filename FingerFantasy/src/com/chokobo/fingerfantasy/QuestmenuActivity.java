package com.chokobo.fingerfantasy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class QuestmenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questmenu);
	}

	public void onQuestButtonClick(View v) {
		Intent i = new Intent(QuestmenuActivity.this,
				com.chokobo.fingerfantasy.BattleActivity.class);
		i.putExtra("quest_no", Integer.parseInt((String) v.getTag()));
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}

	public void onBackButtonClick(View v) {
		finish();
	}
}
