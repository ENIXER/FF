package com.chokobo.fingerfantasy;

import android.app.Activity;
import android.content.Intent;

public class ActivityManager {
	static Activity origin_activity;
	
	static public void setActivity(Activity activity){
		origin_activity = activity;
	}
	
	static public void intentActivity(){
		Intent i = new Intent(origin_activity, com.chokobo.fingerfantasy.ReesultActivity.class);
		origin_activity.startActivityForResult(i, 1);
	}
}
