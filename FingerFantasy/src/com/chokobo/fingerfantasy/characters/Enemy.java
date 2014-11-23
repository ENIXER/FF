package com.chokobo.fingerfantasy.characters;

import android.widget.ProgressBar;

public class Enemy extends Character {
	protected int enemy_turn;
    protected int max_turn;
	
	public Enemy(int h, int a, ProgressBar b, int e_turn) {
		super(h, a, b);
		enemy_turn = e_turn;
		max_turn = e_turn;
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

	protected void reseteTurn(){
		enemy_turn = max_turn;
	}
}
