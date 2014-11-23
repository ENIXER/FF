package com.chokobo.fingerfantasy;

import android.content.Intent;

public class ActivityManager {
	static BattleActivity origin_activity;

	static public void setActivity(BattleActivity activity) {
		origin_activity = activity;
	}

	static public void intentActivity() {
		Intent i = new Intent(origin_activity,
				com.chokobo.fingerfantasy.ReesultActivity.class);
		origin_activity.startActivityForResult(i, 1);
	}

	static public void startMovie() {
		origin_activity.startMovie();
	}
}
