package com.seniorproject.game.levels;

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.seniorproject.game.CollisionData;
import com.seniorproject.game.GameActor;
import com.seniorproject.game.GameScreen;
import com.seniorproject.game.LevelBackground;
import com.seniorproject.game.Ship;
import com.seniorproject.game.ShooterGame;
import com.seniorproject.game.enemies.AsteroidSpawner;
import com.seniorproject.game.enemies.EnemySpawner;
import com.seniorproject.game.helpers.CollisionHelper;
import com.seniorproject.game.hud.*;
import com.seniorproject.game.particles.FireExplosion;

public class Level extends Stage {

	public static float WORLD_STEP = (1/300f);
	
	public Ship ship;
	public LevelBackground background;
	public EnemySpawner enemySpawner;
	public AsteroidSpawner asteroidSpawner;
	private World world;
	private float accumulator = 0f;
	private Box2DDebugRenderer renderer;
	private OrthographicCamera camera;
	private ArrayList<GameActor> collisionList;
	private LevelTitle levelTitle;
	private Group gameObjects;
	protected String enemySpriteFile = "enemy2.png";
	protected String asteroidSpriteFile = "asteroid.png";
	
	public GameScreen screen;
	
	public Score score;
	public Health healthBar;
	public Armor armorBar;
	public EnemyHealth enemyHealthBar;
	
	private Group hudObjects;
	
	public boolean levelFinished = false;
	
	public Level(ShooterGame g) {
		super(new ScreenViewport());	

		// world is used for collisions, I believe its constructor sets the gravity in our game using the Vector2 object
		// Since we don't need actual gravity in the game, its set to 0 here
		world = new World(new Vector2(0,0), true);	
		
		// The camera is used mostly for debugging the world to help show the boxes around the sprites
		camera = new OrthographicCamera(ShooterGame.GAME_WIDTH, ShooterGame.GAME_HEIGHT);
		camera.position.set(ShooterGame.GAME_WIDTH/2, ShooterGame.GAME_HEIGHT/2, 0f);

		/* 
		 * Group of actors for the stage
		 * They are grouped because you can only set one actor to take in keyboard input
		 * By grouping them, it lets you allow all the actors in the group to take keyboard input
		 */
		gameObjects = new Group();
		
		// Used to generate enemies onto the screen
		enemySpawner = new EnemySpawner(world, enemySpriteFile);
		enemySpawner.setMaxEnemies(ShooterGame.CURRENT_LEVEL*ShooterGame.STARTING_ENEMY_COUNT);
		
		
		asteroidSpawner = new AsteroidSpawner(world, asteroidSpriteFile);
		
		// The background
		background = new LevelBackground(this);
		
		// The player's ship
		ship = new Ship(world);
		ShooterGame.PLAYER_SHIP = ship;
		
		levelTitle = new LevelTitle("Level "+ShooterGame.CURRENT_LEVEL);

		// Used for debugging, shows the boxes around the sprites
		renderer = new Box2DDebugRenderer();
		
		healthBar = new Health(g);
		armorBar = new Armor();
		score = new Score();
		enemyHealthBar = new EnemyHealth();
		
		healthBar.setPositionXOffsetWidth(score.getBGWidth());
		healthBar.setPositionYOffsetHeight(score.getBGHeight());
		
		armorBar.setPositionXOffsetWidth(score.getBGWidth());
		armorBar.setPositionYOffsetHeight(score.getBGHeight());
		
		enemyHealthBar.setPositionXOffsetWidth(score.getBGWidth());
		enemyHealthBar.setPositionYOffsetHeight(score.getBGHeight());
		
		levelTitle.setPositionXOffsetWidth(score.getBGWidth());
		levelTitle.setPositionYOffsetHeight(score.getBGHeight());
		
		hudObjects = new Group();
		hudObjects.addActor(healthBar);
		hudObjects.addActor(armorBar);
		hudObjects.addActor(enemyHealthBar);
		hudObjects.addActor(levelTitle);
		hudObjects.addActor(score);
		hudObjects.addActor(levelTitle.getLabel());
		
		
		// Add the actors to the group
		gameObjects.addActor(enemySpawner);
		gameObjects.addActor(asteroidSpawner);
		gameObjects.addActor(background);
		gameObjects.addActor(ship);
		gameObjects.addActor(hudObjects);

		// Add the group to the stage
		addActor(gameObjects);

		// This allows the actors in the gameObjects group to take keyboard input
		setKeyboardFocus(gameObjects);
		
		// Used to keep track of the bullets that have hit enemies
		collisionList = new ArrayList<GameActor>();
		
		// Sets up the collision detection
		collisionDetection();
		
		pauseListener();

		
	}
	
	public void addGameObject(Actor actor) {
		gameObjects.addActor(actor);
	}
	
	public void addHUDObject(Actor actor) {
		hudObjects.addActor(actor);
	}
	
	public Ship getShip() {
		return ship;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		// Bring the level title to the top so there is no overlap
		hudObjects.toFront();
		
		// World stepper?
		// Not sure exactly what this does, but it has something to do with how the engine deals with collisions
		accumulator += delta;
		while(accumulator >= delta) {
			world.step(WORLD_STEP, 6, 2);
			accumulator -= WORLD_STEP;
		}
		
		// Sets bullets to being "dead" that collided into enemies
		if(!collisionList.isEmpty()) {
			for(GameActor actor : collisionList) {
				if(!actor.isDead()) {
					actor.setDead(true);
				}
			}
		}
		
	}
	
	
	@Override
	public void draw() {
		super.draw();
		
		// Comment this out to remove the boxes around the sprites
		//renderer.render(world, camera.combined);
		
	}
	
	@Override
	public void dispose() {
		super.dispose();
		background.dispose();
	}
	
	public void setScreen(GameScreen screen) {
		this.screen = screen;
	}
	
	public void pauseListener() {
		
		this.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				
				if(keycode == Input.Keys.P) {
					screen.pause();
				}
				
				return true;
			}
		});
		
	}
	
	/*
	public void collisionDetection() {
		
		world.setContactListener(new PlayerCollision("Ship", "Enemy"));
		//world.setContactListener(new PlayerCollision("Ship", "Asteroid"));
		//world.setContactListener(new PlayerCollision("Ship", "Boss"));
		//world.setContactListener(new BulletCollision("Enemy", collisionList));
		//world.setContactListener(new BulletCollision("Bullet", "Boss", collisionList));
		//world.setContactListener(new BulletCollision("EnemyBullet", "Ship", collisionList));
		//world.setContactListener(new RepositionCollision("Enemy", "Enemy"));
		
		
	}
	*/
	
	public void collisionDetection() {
		
		world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				CollisionHelper.repositionCollision(contact, "Enemy", "Enemy");
			}

			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
				
				// CollisionData contains the actual object that was collided.
				CollisionData collisionDataA = (CollisionData) contact.getFixtureA().getUserData();
				CollisionData collisionDataB = (CollisionData) contact.getFixtureB().getUserData();
				
				
				GameActor gameActorA = collisionDataA.getActor();
				GameActor gameActorB = collisionDataB.getActor();
				
				
				CollisionHelper.bulletCollision(contact, collisionList, "Enemy", "Bullet");
				CollisionHelper.bulletCollision(contact, collisionList, "Asteroid", "Bullet");
				CollisionHelper.bulletCollision(contact, collisionList, "Boss", "Bullet");
				CollisionHelper.bulletCollision(contact, collisionList, "Ship", "EnemyBullet");
				CollisionHelper.playerCollision(contact, "Enemy", true);
				CollisionHelper.playerCollision(contact, "Asteroid", true);
				CollisionHelper.playerCollision(contact, "Boss", false);
				
				
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	
}
