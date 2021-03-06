package com.seniorproject.game.hud;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class BaseHUD extends Actor {
	
	// Positioning Variables
	protected float parentWidth;
	protected float parentHeight;
	protected float positionYOffsetHeight = 0;
	protected float positionXOffsetWidth = 0;
	
	public void setPositionYOffsetHeight(float offset) {
		positionYOffsetHeight = offset;
	}
	
	public void setPositionXOffsetWidth(float offset) {
		positionXOffsetWidth = offset;
	}
	
}
