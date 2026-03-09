package com.vyorkin.game.core.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import com.vyorkin.engine.base.Updatable;

public abstract class Entity implements Updatable {
	private final int number;
	private final String colorName;
	private final Color displayColor;
	private final Vector2 cell;
	private final Circle shape;

	private boolean faceUp;
	private boolean matched;

	protected Entity(Vector2 cell, Circle shape, int number, String colorName, Color displayColor) {
		this.number = number;
		this.colorName = colorName;
		this.displayColor = displayColor;
		this.cell = cell;
		this.shape = shape;
		this.faceUp = false;
		this.matched = false;
	}

	public boolean isClicked(Vector2 position) {
		return !matched && shape.contains(position.x, position.y);
	}

	public Circle getShape() {
		return shape;
	}

	public boolean isFaceUp() {
		return faceUp;
	}

	public void setFaceUp(boolean faceUp) {
		this.faceUp = faceUp;
	}

	public boolean isMatched() {
		return matched;
	}

	public void setMatched(boolean matched) {
		this.matched = matched;
	}

	public boolean isMarked() {
		return matched;
	}

	public void mark() {
		this.matched = true;
	}

	public int getNumber() {
		return number;
	}

	public String getColorName() {
		return colorName;
	}

	public Color getDisplayColor() {
		return displayColor;
	}

	public Vector2 getCell() {
		return cell;
	}

	@Override
	public void update(float delta) {
	}
}
