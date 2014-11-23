package com.chokobo.fingerfantasy.characters;

import android.widget.ProgressBar;

public class CharacterManager {

	static Player player;
	static Enemy enemy;

	/*
	 * enemy_id 
	 * 1:あご(HP:200, 攻:10, 1ターン) 
	 * 2:ドラゴン(HP:300, 攻:30, 3ターン) 
	 * 3:雑魚(HP:5000, 攻:1500, 1ターン)
	 */

	public static void initCharacter(ProgressBar player_bar,
			ProgressBar enemy_bar, int enemy_id) {

		player = new Player(100, 10, player_bar);

		int hp, atk, turn;
		switch (enemy_id) {
		case 1:
			hp = 200;
			atk = 10;
			turn = 1;
			break;
		case 2:
			hp = 300;
			atk = 30;
			turn = 3;
			break;
		case 3:
			hp = 5000;
			atk = 1500;
			turn = 1;
			break;
		default:
			hp = 1;
			atk = 1;
			turn = 1;
		}
		enemy = new Enemy(hp, atk, enemy_bar, turn);
	}

	public static void damage(Character charcter, int totalDamage) {
		charcter.damage(totalDamage);
	}

	public static int getPlayerAtk() {
		return player.getAtk();
	}

	public static int getEnemyAtk() {
		return enemy.getAtk();
	}

	public static Player getPlayer() {
		return player;
	}

	public static Enemy getEnemy() {
		return enemy;
	}

	public static void decEnemyturn() {
		enemy.decEnemyTurn();
	}

	public static boolean isEnemyturn() {
		boolean result = false;
		if (enemy.getEnemyTurn() == 0)
			result = true;
		return result;
	}
	
	public static boolean isPlayerDead(){
		return player.isDead();
	}
	
	public static boolean isEnemyDead(){
		return enemy.isDead();
	}

}
