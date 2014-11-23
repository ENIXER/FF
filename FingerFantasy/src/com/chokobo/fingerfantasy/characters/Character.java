package com.chokobo.fingerfantasy.characters;

public abstract class Character {

	protected int hp;
	protected int atk;

	protected Character(int h, int a) {
		hp = h;
		atk = a;
	}

	public abstract void attack();

	protected void damage(int d) {
		hp -= d;
	}

	public boolean isDead() {
		return hp <= 0;
	}
}
