package com.vyorkin.game.core.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.vyorkin.game.core.entities.Entity;
import com.vyorkin.game.core.entities.EntityFactory;

public class LevelFactory {

	private static final String[] COLOR_NAMES = {
		"RED", "BLUE", "GREEN", "YELLOW", "PURPLE", "ORANGE", "PINK", "WHITE"
	};

	private static final Color[] COLORS = {
		Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
		new Color(0.6f, 0.2f, 0.8f, 1f),
		Color.ORANGE, Color.PINK, Color.WHITE
	};

	private final EntityFactory entityFactory;
	private final LevelView view;

	public LevelFactory(LevelView view) {
		this.entityFactory = new EntityFactory();
		this.view = view;
	}

	public Level create(LevelMetadata metadata) {
		int pairCount = metadata.entityCount;
		int totalCards = pairCount * 2;

		List<Vector2> cells = createCells(totalCards);
		List<Entity> entities = new ArrayList<Entity>(totalCards);

		List<Integer> colorIndices = pickColorIndices(pairCount);

		int cardIndex = 0;
		for (int pair = 0; pair < pairCount; pair++) {
			int nameIndex = colorIndices.get(pair);
			String colorName = COLOR_NAMES[nameIndex];

			Color displayColor1 = pickDifferentColor(nameIndex);
			Color displayColor2 = pickDifferentColor(nameIndex, displayColor1);

			Circle shape1 = createCircle(cells.get(cardIndex));
			entities.add(entityFactory.create(cells.get(cardIndex), shape1, pair + 1, colorName, displayColor1));
			cardIndex++;

			Circle shape2 = createCircle(cells.get(cardIndex));
			entities.add(entityFactory.create(cells.get(cardIndex), shape2, pair + 1, colorName, displayColor2));
			cardIndex++;
		}

		return new Level(metadata, entities);
	}

	private List<Integer> pickColorIndices(int count) {
		List<Integer> available = new ArrayList<Integer>();
		for (int i = 0; i < COLOR_NAMES.length; i++) {
			available.add(i);
		}
		Collections.shuffle(available);
		List<Integer> picked = new ArrayList<Integer>();
		for (int i = 0; i < count && i < available.size(); i++) {
			picked.add(available.get(i));
		}
		return picked;
	}

	private Color pickDifferentColor(int nameIndex) {
		int colorIndex;
		do {
			colorIndex = MathUtils.random(COLORS.length - 1);
		} while (colorIndex == nameIndex);
		return COLORS[colorIndex];
	}

	private Color pickDifferentColor(int nameIndex, Color exclude) {
		int colorIndex;
		do {
			colorIndex = MathUtils.random(COLORS.length - 1);
		} while (colorIndex == nameIndex || COLORS[colorIndex].equals(exclude));
		return COLORS[colorIndex];
	}

	public Circle createCircle(Vector2 cell) {
		return new Circle(getPosition(cell), view.radius - view.padding);
	}

	public List<Vector2> createCells(int count) {
		List<Vector2> cells = new ArrayList<Vector2>(count);

		while (cells.size() < count) {
			Vector2 cell = createCell();
			if (!cells.contains(cell))
				cells.add(cell);
		}

		Collections.shuffle(cells);
		return cells;
	}

	private Vector2 createCell() {
		return new Vector2(
			(int)MathUtils.random(1, view.cols),
			(int)MathUtils.random(1, view.rows)
		);
	}

	public Vector2 getPosition(Vector2 cell) {
		return new Vector2(
			view.offsetX + cell.x*view.diameter - view.radius,
			view.offsetY + cell.y*view.diameter - view.radius
		);
	}
}
