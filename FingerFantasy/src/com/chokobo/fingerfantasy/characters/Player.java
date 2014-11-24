package com.chokobo.fingerfantasy.characters;

import android.widget.ProgressBar;

public class Player extends Character {
	private int level;
	private int exp;

	private int[] expTable = { 0, 100, 300, 600, 1000, 1500, 2100, 2800, 3600,
			4500, 5500, 6600, 7800, 9100, 10500, 12000 };

	public Player() {
		super(150, 20);
		level = 1;
		exp = 0;
	}

	public void setProgressBar(ProgressBar b) {
		super.setProgressBar(b);
	}

	@Override
	public void attack() {
	}

	public void earnExp(int value) {
		exp += value;
		while (exp >= expTable[level]) {
			level++;
			hp += 150;
			atk++;
		}
	}

	public int getLevel() {
		return level;
	}
}
