package com.chokobo.fingerfantasy.characters;

import android.widget.ProgressBar;

public abstract class Character {

	protected int hp;
	protected int atk;
	protected ProgressBar bar;
	
	protected Character(int h, int a, ProgressBar b) {
		hp = h;
		atk = a;
		bar = b;
		init();
	}
	
	public abstract void attack();

	protected void damage(int d) {
		hp -= d;
		bar.setProgress(hp);
	}

	protected int getAtk(){
		return atk;
	}
	
	public boolean isDead() {
		return hp <= 0;
	}

	protected void init(){
		bar.setMax(hp);
		bar.setProgress(hp);
		bar.bringToFront();
	}
	
}
