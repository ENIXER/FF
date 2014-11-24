package com.chokobo.fingerfantasy;
import com.chokobo.fingerfantasy.characters.CharacterManager;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ReesultActivity extends Activity implements View.OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_reesult);
		
		ImageButton btn = (ImageButton)findViewById(R.id.backBtn);
		btn.setOnClickListener(this);
		
		setExp();
	}
	
	public void setExp(){
		TextView exp_view = (TextView)findViewById(R.id.getExp_text);
		int exp_value = CharacterManager.getExp();
		String exp_string = String.valueOf(exp_value);
		exp_view.setText(exp_string);
	}

	@Override
    public void onClick(View v) {
		Intent i = new Intent(this,com.chokobo.fingerfantasy.QuestmenuActivity.class);
		this.startActivityForResult(i, 1);
	}
	
}
