package com.seniorproject.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.seniorproject.game.levels.Level;

public class LevelBackground extends Actor {

	Sprite bgSprite;
	Texture background;
	float movementSpeedIdle = .0003f;
	float movementSpeedMoving = .0015f;
	boolean upPressed = false;
	float backgroundPosition = 0f;
	Sound wavSound;
	Level level;
	
	long soundID;
	
	public LevelBackground() {
		// background music
		/*wavSound = Gdx.audio.newSound(Gdx.files
				.internal("sounds/action.wav"));
		soundID = wavSound.loop(ShooterGame.MUSIC_VOLUME);// Plays the sound in a infinite loop. @param volume level
*/
		background = new Texture(Gdx.files.internal("space-level1.jpg"));
		background.setWrap(Texture.TextureWrap.Repeat,
				Texture.TextureWrap.Repeat);
		bgSprite = new Sprite(background);
		setBounds(bgSprite.getX(), bgSprite.getY(), bgSprite.getWidth(),
				bgSprite.getHeight());
	
	}

	public LevelBackground(Level level) {
		this();
		
		this.level = level;
	}
	
	public LevelBackground(float movementSpeed) {
		this();
		
		this.movementSpeedIdle = movementSpeed;
	}
	
	public void setBGVolume(float volume) {
		//wavSound.setVolume(soundID, volume);
	}
	
	
	@Override
	public void draw(Batch batch, float alpha) {

		/*
		if (upPressed) {
			backgroundPosition -= movementSpeedMoving;
			bgSprite.setV(backgroundPosition);
			bgSprite.setV2(backgroundPosition + 1);
		} else {
			backgroundPosition -= movementSpeedIdle;
			bgSprite.setV(backgroundPosition);
			bgSprite.setV2(backgroundPosition + 1);
		}
		*/
		
		if(level == null || (level != null && !level.screen.gamePaused)) {
			backgroundPosition -= movementSpeedIdle;
			bgSprite.setV(backgroundPosition);
			bgSprite.setV2(backgroundPosition + 1);
			
			ShooterGame.bgMusic.resume();
			
		}
		else if(level != null && level.screen.gamePaused) {
			ShooterGame.bgMusic.pause();
		}
		
		bgSprite.draw(batch);
		
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void positionChanged() {
		super.positionChanged();
	}

	@Override
	public void setStage(Stage stage) {
		super.setStage(stage);
		if (stage != null) {

			this.getParent().addListener(new InputListener() {

				@Override
				public boolean keyDown(InputEvent event, int keycode) {

					if (keycode == Input.Keys.W) {
						upPressed = true;
					}

					return true;
				}

				@Override
				public boolean keyUp(InputEvent event, int keycode) {

					if (keycode == Input.Keys.W) {
						upPressed = false;
					}

					return true;
				}
			});
		}
	}
	
	public void dispose() {
		//wavSound.dispose();
		background.dispose();
	}
	
	
}
