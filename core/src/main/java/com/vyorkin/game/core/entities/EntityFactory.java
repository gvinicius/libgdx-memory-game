package com.vyorkin.game.core.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class EntityFactory {
	public Entity create(Vector2 cell, Circle shape, int number, String colorName, Color displayColor) {
		return new Bird(cell, shape, number, colorName, displayColor);
	}
}
