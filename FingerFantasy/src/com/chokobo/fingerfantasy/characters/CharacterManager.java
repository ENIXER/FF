package com.chokobo.fingerfantasy.characters;

import java.util.Random;

import com.chokobo.fingerfantasy.ActivityManager;

import android.widget.ProgressBar;

public class CharacterManager {

	static Player player;
	static Enemy enemy;

	/*
	 * enemy_id 1:あご(HP:200, 攻:10, 1ターン) 2:ドラゴン(HP:300, 攻:30, 3ターン) enemy_id
	 * 1:あご(HP:120, 攻:50×ランダム（0.9〜1.1）, 2ターン) 2:ドラゴン(HP:300, 攻:30, 3ターン)
	 * 3:雑魚(HP:5000, 攻:1500, 1ターン)
	 */

	public static void initEnemy(int enemy_id) {

		int hp, atk, turn, exp, crystal;
		switch (enemy_id) {
		case 1:
			hp = 120;
			atk = 10;
			exp = 60;
			crystal = 2;
			turn = 3;
			break;
		case 2:
			hp = 1000;
			atk = 30;
			exp = 1000;
			crystal = 4;
			turn = 3;
			break;
		case 3:
			hp = 2600;
			atk = 1500;
			exp = 1500;
			crystal = 4;
			turn = 2;
			break;
		default:
			hp = 1;
			atk = 1;
			exp = 1;
			crystal = 1;
			turn = 1;
		}
		enemy = new Enemy(hp, atk, turn, exp, crystal);
	}

	public static void setProgressBar(ProgressBar playerBar,
			ProgressBar enemyBar) {
		player.setProgressBar(playerBar);
		enemy.setProgressBar(enemyBar);
	}

	public static void damage(Character charcter, int totalDamage) {
		charcter.damage(totalDamage);
	}

	public static int getPlayerAtk() {
		return player.getAtk();
	}

	public static int getEnemyAtk() {
		return (int) (enemy.getAtk() * getRate());
	}

	public static Player getPlayer() {
		return player;
	}

	public static Enemy getEnemy() {
		return enemy;
	}

	public static void decEnemyturn() {
		enemy.decEnemyTurn();
		setTurn();
	}

	public static int getExp() {
		return enemy.getExp();
	}

	public static int getCrystal() {
		return enemy.getCrystal();
	}

	public static void setTurn() {
		ActivityManager.setTurn(enemy.getEnemyTurn());
	}

	public static boolean isEnemyturn() {
		boolean result = false;
		if (enemy.getEnemyTurn() == 0) {
			enemy.resetTurn();
			setTurn();
			result = true;
		}
		return result;
	}

	public static boolean isPlayerDead() {
		return player.isDead();
	}

	public static boolean isEnemyDead() {
		return enemy.isDead();
	}

	public static int getPlayerHp() {
		return player.getHp();
	}

	public static int getPlayerMaxHp() {
		return player.getMaxHp();
	}

	public static void resetEnemyTurn() {
		enemy.resetTurn();
	}

	public static double getRate() {
		Random rand = new Random(System.currentTimeMillis());
		double rate = (double) (90 + rand.nextInt(21)) / 100;
		return rate;
	}

	public static void initPlayer() {
		player = new Player();
	}

	public static void earnExp() {
		player.earnExp(enemy.getExp());
	}

	public static int getPlayerLevel() {
		return player.getLevel();
	}
}
