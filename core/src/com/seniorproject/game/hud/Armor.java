package com.seniorproject.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Armor extends BasicHealthBar {

	Texture armorBarTex = new Texture(
			Gdx.files.internal("hud/armor/armorbar-left.png"));
	Texture wingTex = new Texture(
			Gdx.files.internal("hud/armor/toprightwing.png"));

	Sprite armorbar = new Sprite(armorBarTex);
	Sprite wings = new Sprite(wingTex);

	// sentinel value to keep the position from reassigning
	int i = 0;

	public Armor() {
		setHealthBarSprite(armorbar);
	}

	public void drawArmor(Batch batch, float alpha) {
		super.draw(batch, alpha);

		if (!isPositionSet() && getParent().getStage() != null
				&& positionYOffsetHeight != 0 && positionXOffsetWidth != 0) {

			parentWidth = getParent().getStage().getWidth();
			parentHeight = getParent().getStage().getHeight();

			/*
			armorbar.setPosition(parentWidth / 2
					+ (positionXOffsetWidth / 3.5f),
					parentHeight - armorbar.getHeight()
							- (positionYOffsetHeight / 4.33f));
			*/
			/*
			healthBarX = halfParentWidth
					- (healthBarSprite.getWidth() + (positionXOffsetWidth / 3.4f));
			healthBarY = (parentHeight - healthBarSprite.getHeight())
					- (positionYOffsetHeight / 4.1f);
			*/
			armorbar.setPosition((parentWidth/2)-(armorbar.getWidth()+(positionXOffsetWidth/3.4f)), (parentHeight - armorbar.getHeight())-(positionYOffsetHeight/4.1f));
			/*
			wings.setPosition(parentWidth / 2 + (positionXOffsetWidth / 4),
					parentHeight - wings.getHeight()
							- (positionYOffsetHeight / 5));
			 */
			setPositionSet(true);
			i++;// makes sure that it does not reassign the positions in the
				// following iterations
		}

		//wings.draw(batch);
		armorbar.draw(batch);

	}

}
