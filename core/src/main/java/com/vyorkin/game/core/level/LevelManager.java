package com.vyorkin.game.core.level;

import com.vyorkin.game.core.domain.PlayerProfile;

public class LevelManager {
	public static final int MAX_SEASONS = 3;
	public static final int MAX_NUMBERS = 9;

	private final LevelMetadata[][] levels;

	public LevelManager(PlayerProfile profile) {
		levels = new LevelMetadata[MAX_SEASONS][MAX_NUMBERS];
		for (int i = 0; i < MAX_SEASONS; i++) {
			for (int j = 0; j < MAX_NUMBERS; j++) {
				LevelMetadata l = new LevelMetadata(i + 1, j + 1);
				l.countdownTime = 3;
				l.memorizationTime = Math.max(2, 4 - i);
				l.lives = Math.max(3, 6 - i);
				l.entityCount = 3 + j + (i * 2);
				if (l.entityCount > 8) l.entityCount = 8;
				levels[i][j] = l;
			}
		}
	}

	public LevelMetadata get(int season, int number) {
		int s = Math.max(0, Math.min(season - 1, MAX_SEASONS - 1));
		int n = Math.max(0, Math.min(number - 1, MAX_NUMBERS - 1));
		return levels[s][n];
	}
}
