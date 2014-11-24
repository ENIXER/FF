package com.chokobo.fingerfantasy.characters;

import android.widget.ProgressBar;

public class Player extends Character {
	protected int max_hp;
	
	public Player(int h, int a, ProgressBar b) {
		super(h, a, b);
		max_hp = h;
	}
	
	@Override
	public void attack() {
		// TODO Auto-generated method stub
		
	}
	
	protected int getMaxHp(){
		return max_hp;
	}
	
	protected int getHp() {
		return hp;
	}

}
