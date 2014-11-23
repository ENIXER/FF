package com.chokobo.fingerfantasy.characters;

import android.widget.ProgressBar;

import com.chokobo.fingerfantasy.R;

public class CharacterManager {

	static Player player;
	static Enemy enemy;
	
	public static void initCharacter(ProgressBar player_bar, ProgressBar enemy_bar){
		player = new Player(100, 10, player_bar);
		enemy = new Enemy(200, 20, enemy_bar);
	}

	public static void damage(int totalDamage){
		enemy.damage(totalDamage);
	}
	
	public static int getAtk(){
		return player.getAtk();
	}
	
	
}
