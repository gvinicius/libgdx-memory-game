package com.vyorkin.game.core.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

import com.vyorkin.engine.E;
import com.vyorkin.engine.base.Renderer;

import com.vyorkin.game.core.level.Level;
import com.vyorkin.game.core.level.LevelState;

public class EntityRenderer implements Renderer<Entity> {
	private static final int CIRCLE_SEGMENTS = 32;
	private static final Color CARD_BACK_COLOR = new Color(0.2f, 0.2f, 0.3f, 1f);
	private static final Color CARD_BACK_BORDER = new Color(0.4f, 0.4f, 0.5f, 1f);
	private static final Color MATCHED_BG = new Color(0.15f, 0.4f, 0.15f, 1f);

	private final ShapeRenderer shapeRenderer;
	private Level level;

	public EntityRenderer(ShapeRenderer shapeRenderer) {
		this.shapeRenderer = shapeRenderer;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	@Override
	public void render(Entity model, float delta) {
		Circle shape = model.getShape();
		boolean showFace = model.isFaceUp() || model.isMatched()
			|| level.getState() == LevelState.Memorization;

		if (model.isMatched()) {
			shapeRenderer.setColor(MATCHED_BG);
		} else if (showFace) {
			shapeRenderer.setColor(0.15f, 0.15f, 0.2f, 1f);
		} else {
			shapeRenderer.setColor(CARD_BACK_COLOR);
		}
		shapeRenderer.circle(shape.x, shape.y, shape.radius, CIRCLE_SEGMENTS);

		shapeRenderer.setColor(CARD_BACK_BORDER);
		shapeRenderer.circle(shape.x, shape.y, shape.radius * 0.85f, CIRCLE_SEGMENTS);

		if (showFace) {
			shapeRenderer.setColor(model.getDisplayColor());
			shapeRenderer.circle(shape.x, shape.y, shape.radius * 0.7f, CIRCLE_SEGMENTS);
		} else {
			shapeRenderer.setColor(0.25f, 0.25f, 0.4f, 1f);
			shapeRenderer.circle(shape.x, shape.y, shape.radius * 0.7f, CIRCLE_SEGMENTS);
			shapeRenderer.setColor(0.3f, 0.3f, 0.5f, 1f);
			shapeRenderer.circle(shape.x, shape.y, shape.radius * 0.15f, CIRCLE_SEGMENTS);
		}
	}

	public void renderText(Entity model) {
		boolean showFace = model.isFaceUp() || model.isMatched()
			|| level.getState() == LevelState.Memorization;

		if (showFace) {
			Circle shape = model.getShape();
			String colorName = model.getColorName();
			TextBounds bounds = E.font.getBounds(colorName);

			float posx = shape.x - bounds.width / 2;
			float posy = shape.y + bounds.height / 2;

			Color prevColor = E.font.getColor().cpy();
			E.font.setColor(Color.WHITE);
			E.font.draw(E.batch, colorName, posx, posy);
			E.font.setColor(prevColor);
		}
	}
}
