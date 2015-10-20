package com.seniorproject.game;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class EnemySpawner extends Spawner {

	public EnemySpawner(World world, String enemySpriteFile) {
		super(world, enemySpriteFile);
		this.world = world;
		this.enemySpriteFile = enemySpriteFile;
		
		
		maxEnemies = 30;
		totalEnemies = 0;
		spawnRate = 3;
		lastSpawnTime = 0;
		enemiesToSpawnEachInterval = 2;
		enemySpawnIntervalCount = 0;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		
		// Reset the amount spawned this interval if enough time has passed
		if((getSeconds()-lastSpawnTime) > spawnRate) {
			enemySpawnIntervalCount = 0;
		}
		
		
		// Adds enemies to the stage
		if(getStage() != null && totalEnemies < maxEnemies && enemiesToSpawnEachInterval > enemySpawnIntervalCount) {
			level = (Level) getStage();
			Enemy enemy = new Enemy(world, enemySpriteFile);
			level.addGameObject(enemy);
			totalEnemies++;
			enemySpawnIntervalCount++;
			lastSpawnTime = getSeconds();
		}
		
	}
	
}
