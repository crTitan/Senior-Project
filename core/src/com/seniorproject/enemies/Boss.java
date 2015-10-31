package com.seniorproject.enemies;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;

public class Boss extends BaseEnemy {

	
	float lastShipCollision = -1;
	
	public Boss(World world, String spriteFile) {
		super(world, spriteFile);

		collisionData.setActorType("Boss");
	
		this.health = 300;
		
		this.hoverOffset = 0;
		this.shootInterval = .5f;
		this.diveRadius = 50;
		
		
		this.collisionDamage = 35;
		this.hitAwardPoints = 15;
		this.killAwardPoints = 50;
	}


	public float getLastShipCollision() {
		return lastShipCollision;
	}
	
	public void setLastShipCollision() {
		lastShipCollision = Spawner.getSeconds();
	}
	
	public void setLastShipCollision(float time) {
		lastShipCollision = time;
	}
	
	
}