package com.chokobo.fingerfantasy;

import com.chokobo.fingerfantasy.characters.CharacterManager;
import com.chokobo.fingerfantasy.characters.Enemy;
import com.chokobo.fingerfantasy.characters.Player;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BattleActivity extends ActionBarActivity {

	private Player player;
	private Enemy enemy;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_battle);
		
		Intent i = getIntent();
		int quest_no = i.getIntExtra("quest_no",-1);
		
		initView();
	}

	private void initView(){
		//Resources r = getResources();
		//Bitmap enemy_image = BitmapFactory.decodeResource(r,R.drawable.dragon);
		//ImageView enemy_imageview = (ImageView)findViewById(R.id.enemy_image);
		//enemy_imageview.setImageBitmap(enemy_image);
		
        // ビットマップ作成オブジェクトの設定
        BitmapFactory.Options bmfOptions = new BitmapFactory.Options();
        // ARGBでそれぞれ0～127段階の色を使用（メモリ対策）
        bmfOptions.inPreferredConfig = Config.ARGB_4444;
        // 画像を1/20サイズに縮小（メモリ対策）
        bmfOptions.inSampleSize = 20;
        // システムメモリ上に再利用性の無いオブジェクトがある場合に勝手に解放（メモリ対策）
        bmfOptions.inPurgeable = true;
        // 現在の表示メトリクスの取得
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        // ビットマップのサイズを現在の表示メトリクスに合わせる（メモリ対策）
        bmfOptions.inDensity = dm.densityDpi;
        // 画像ファイルオブジェクトとビットマップ作成オブジェクトから、ビットマップオブジェクト作成
        Bitmap enemy_image = BitmapFactory.decodeResource(getResources(),R.drawable.dragon_light);
        
		ImageView enemy_imageview = (ImageView)findViewById(R.id.enemy_image);
		enemy_imageview.setImageBitmap(enemy_image);
		
		ProgressBar p_bar = (ProgressBar)findViewById(R.id.player_bar);
		ProgressBar e_bar = (ProgressBar)findViewById(R.id.enemy_bar);
		CharacterManager.initCharacter(p_bar, e_bar);
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
