package com.chokobo.fingerfantasy.characters;

import android.widget.ProgressBar;

public class Enemy extends Character {
	protected int enemy_turn;
    protected int max_turn;
	protected int exp;
    protected int crystal;
	
	public Enemy(int h, int a, ProgressBar b, int e_turn, int e, int cry) {
		super(h, a, b);
		enemy_turn = e_turn;
		max_turn = e_turn;
		exp = e;
		crystal = cry;
	}

	@Override
	public void attack() {
		// TODO Auto-generated method stub
	}
	
	protected int getEnemyTurn(){
		return enemy_turn;
	}
	
	protected void decEnemyTurn() {
		enemy_turn--;
	}

	protected void resetTurn(){
		enemy_turn = max_turn;
	}
}
