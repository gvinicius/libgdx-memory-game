package com.vyorkin.game.core.level;

import com.vyorkin.engine.E;
import com.vyorkin.engine.base.Renderer;

public class HudRenderer implements Renderer<Level> {

	@Override
	public void render(Level model, float delta) {
		LevelMetadata metadata = model.getMetadata();

		E.font.draw(E.batch,
			String.format("STROOP  Level: %s  Pairs: %d/%d  Lives: %d  Errors: %d",
				metadata.getNumberString(),
				model.matchedPairs, model.totalPairs,
				metadata.lives - model.errors, model.errors),
			20, 50
		);
	}
}
