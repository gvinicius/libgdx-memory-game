package com.vyorkin.game.core.level;

import java.util.List;

import com.vyorkin.game.core.entities.Entity;

public class Level {
	private final LevelMetadata metadata;

	private List<Entity> entities;
	private LevelState state;
	private LevelResult result;

	public int index;
	public int errors;
	public int matchedPairs;
	public int totalPairs;

	private Entity firstFlipped;
	private Entity secondFlipped;
	private float flipTimer;
	private boolean waitingForFlipBack;

	private static final float FLIP_BACK_DELAY = 1.0f;

	public Level(LevelMetadata metadata, List<Entity> entities) {
		this.metadata = metadata;
		this.entities = entities;

		index = 1;
		errors = 0;
		matchedPairs = 0;
		totalPairs = metadata.entityCount;
		state = LevelState.Countdown;
		result = new LevelResult();

		firstFlipped = null;
		secondFlipped = null;
		flipTimer = 0;
		waitingForFlipBack = false;
	}

	public LevelResult getResult() {
		return result;
	}

	public LevelMetadata getMetadata() {
		return metadata;
	}

	public LevelState getState() {
		return state;
	}

	public void setState(LevelState state) {
		this.state = state;
		if (state == LevelState.Playing) {
			for (Entity entity : entities) {
				entity.setFaceUp(false);
			}
		}
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public boolean isWaitingForFlipBack() {
		return waitingForFlipBack;
	}

	public boolean flipCard(Entity card) {
		if (waitingForFlipBack || card.isFaceUp() || card.isMatched()) {
			return false;
		}

		card.setFaceUp(true);

		if (firstFlipped == null) {
			firstFlipped = card;
			return true;
		}

		secondFlipped = card;

		if (firstFlipped.getColorName().equals(secondFlipped.getColorName())) {
			firstFlipped.setMatched(true);
			secondFlipped.setMatched(true);
			matchedPairs++;
			firstFlipped = null;
			secondFlipped = null;
			return true;
		}

		errors++;
		waitingForFlipBack = true;
		flipTimer = FLIP_BACK_DELAY;
		return false;
	}

	public void update(float delta) {
		if (waitingForFlipBack) {
			flipTimer -= delta;
			if (flipTimer <= 0) {
				if (firstFlipped != null) firstFlipped.setFaceUp(false);
				if (secondFlipped != null) secondFlipped.setFaceUp(false);
				firstFlipped = null;
				secondFlipped = null;
				waitingForFlipBack = false;
			}
		}
	}

	public void updateState() {
		if (errors > metadata.lives) {
			state = LevelState.Lose;
		} else if (matchedPairs >= totalPairs) {
			state = LevelState.Win;
		}
	}

	public void nextState() {
		if (state == LevelState.Countdown) {
			state = LevelState.Memorization;
		} else if (state == LevelState.Memorization) {
			state = LevelState.Playing;
		}
	}

	public boolean isDone() {
		return state == LevelState.Win || state == LevelState.Lose;
	}
}
