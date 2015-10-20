package com.seniorproject.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class LabelHelper {
	
	private final static String FONT_FILE = "play.ttf";
	private final static double POINTS_TO_PIXELS_MULTIPLIER = 1.35;
	
	private Skin skin;
	private Label label;
	private LabelStyle labelStyle;
	private BitmapFont font;
	
	public LabelHelper(String title, int fontSize, Color color) {
		
		// Skin is needed for the label
		skin = new Skin();
		
		setupFont(fontSize);
		
		labelStyle = new LabelStyle(font, color);
		
		skin.add("default-font",  font);
		skin.add("default", labelStyle);
		
		label = new Label(title, skin);
		label.setHeight((float) (fontSize*POINTS_TO_PIXELS_MULTIPLIER));
		
	}
	
	
	public void setColor(Color color) {
		labelStyle.fontColor = color;
	}
	
	
	public Label getLabel() {
		return label;
	}
	
	public void setTitle(String title) {
		label.setText(title);
	}
	
	private void setupFont(int fontSize) {
		// Creates a Bitmap font from the .ttf font file
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_FILE));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = fontSize;

		font = generator.generateFont(parameter);
		generator.dispose();
	}
	
}
