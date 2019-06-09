package com.waes.domain;

public enum Side {

	LEFT,
	RIGHT;
	
	public boolean isLeft() {
		return this.equals(LEFT);
	}
	
	public boolean isRight() {
		return this.equals(RIGHT);
	}
}
