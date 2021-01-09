package com.mygdx.game;

//IMPORT ALL THE NECESSARY CLASSES
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class BrickBreaker extends Game {
	SpriteBatch batch;
	Circle mouse_pointer;
	BitmapFont font;
	Rectangle paddle;
	Circle ball;
	Rectangle Brick;
	Rectangle play_button;
	Rectangle exit_button;
	ShapeRenderer shapeRenderer;
	
	//MULTIPLE SCREENS
	enum Screen{
		MAIN_MENU, MAIN_GAME, GAME_OVER;
	}
	
	//PROGRAM STARTS WITH MAIN MENU SCREEN
	Screen currentScreen = Screen.MAIN_MENU;
	
	float play_button_X = 290f; //POSITION OF PLAY BUTTON
	float play_button_Y = 360f;
	
	float exit_button_X = 290f; //POSITION OF EXIT BUTTON
	float exit_button_Y = 260f;
	
	int playerX = 200, playerY = 5; //INITIAL POSITION OF PADDLE/PLAYER
	int ballX = 300, ballY = 250, ballY_dir = -1, ballY_speed = 2; //INITIAL POSITION, DIRECTION, SPEED OF BALL
	double ballX_dir = -1 + Math.random()*1 , ballX_speed = 5.5d;
	boolean won = false; //A BOOLEAN VARIABLE TO CHECK IF PLAYER WON THE GAME OR NOT

	int score = 0; //INITIAL SCORE 0
	
	//CREATING A LIST OF CO-ORDINATES OF BRICKS IN ORDER TO DISPLAY THE BRICKS AT THAT SPECIFIED CO-ORDINATE
	float[] BrickX = new float[20];
	float[] BrickY = new float[20];
	
	//IN CREATE METHOD YOU DECLARE THE OBJECT OF THE IMPORTED CLASSES
	@Override
	public void create () {
		
		//DECLARING THE OBJECT OF THE RESPECTIVE CLASSES WITH SUITABLE VARIABLE NAME
		batch = new SpriteBatch();
		mouse_pointer = new Circle();
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		paddle = new Rectangle();
		ball = new Circle();
		Brick = new Rectangle();
		play_button = new Rectangle();
		exit_button = new Rectangle();
		
		//DECLARING THE CO-ORDINATES OF THE BRICKS AND ASSIGNING THEM TO THE LIST CREATED ABOVE
		float x = 90.5f, y = 500f;
		
		for(int i = 0; i < 20; i++) {
			if(i == 5 || i == 10 || i == 15) {
				x = 90.5f;
				y -= 70f;
			}
			else if(i == 0)
				x = 90.5f;
			else 
				x += 140f;
			
			BrickX[i] = x;
			BrickY[i] = y;
		}
		
	}
	
	//RENDER METHOD KEEPS DISPLAYING THE SCREEN EVERY SINGLE SECOND
	@Override
	public void render () {
		
		//MAIN MENU
		if(currentScreen == Screen.MAIN_MENU) {
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			Gdx.graphics.setResizable(false); //USER CANNOT RESIZE THE SCREEN(OTHERWISE CO-ORDINATES WILL BE MESSED UP)
			
			//BRICK BREAKER TITLE
			batch.begin();
			font.setColor(Color.GOLD);
			font.getData().setScale(4f, 3f);
			font.draw(batch, "B R I C K  B R E A K E R", 80, 550);
			batch.end();
			
			//MOUSE POINTER IS A CIRCLE OF RADIUS 1 PIXEL
			mouse_pointer.set(800f - Gdx.input.getX(), 600f - Gdx.input.getY(), 1);
			//PLAY BUTTON IS A RECTANGLE OF WIDTH 210 PIXEL AND HEIGHT 50 PIXEL
			play_button.set(play_button_X, play_button_Y , 210, 50);
			//EXIT BUTTON IS A RECTANGLE OF WIDTH 210 PIXEL AND HEIGHT 50 PIXEL
			exit_button.set(exit_button_X, exit_button_Y, 210, 50);
			
			//HOVERING EFFECT OF MOUSE POINTER AND PLAY BUTTON
			if(play_button.contains(mouse_pointer.x, mouse_pointer.y)) { //IF PLAY BUTTON INTERSECTS WITH MOUSE POINTER
				
				shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				shapeRenderer.setColor(Color.GREEN);
				shapeRenderer.rect(play_button_X, play_button_Y, 210, 50);
				shapeRenderer.end();
				
				shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				shapeRenderer.setColor(Color.BLACK);
				shapeRenderer.triangle(300, 400, 340, 385, 300, 370);
				shapeRenderer.end();
				
				batch.begin();
				font.setColor(Color.BLACK);
				font.getData().setScale(3f, 2f);
				font.draw(batch, "P L A Y", 350, 397);
				batch.end();
				
				
			}else {
				shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				shapeRenderer.setColor(Color.WHITE);
				shapeRenderer.rect(play_button_X, play_button_Y, 210, 50);
				shapeRenderer.end();
				
				shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				shapeRenderer.setColor(Color.BLACK);
				shapeRenderer.triangle(300, 400, 340, 385, 300, 370);
				shapeRenderer.end();
				
				batch.begin();
				font.setColor(Color.BLACK);
				font.getData().setScale(3f, 2f);
				font.draw(batch, "P L A Y", 350, 397);
				batch.end();
				
			}
			
			//HOVERING EFFECT OF MOUSE POINTER AND EXIT BUTTON
			if(exit_button.contains(mouse_pointer.x, mouse_pointer.y)) { //IF MOUSE POINTER INTERSECTS WITH EXIT BUTTON
				shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				shapeRenderer.setColor(Color.RED);
				shapeRenderer.rect(exit_button_X, exit_button_Y, 210, 50);
				shapeRenderer.end();
				
				batch.begin();
				font.setColor(Color.BLACK);
				font.getData().setScale(3.5f, 2f);
				font.draw(batch, "E  X  I  T", 300, 297);
				batch.end();
			}
			else {
				shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				shapeRenderer.setColor(Color.WHITE);
				shapeRenderer.rect(exit_button_X, exit_button_Y, 210, 50);
				shapeRenderer.end();
				
				batch.begin();
				font.setColor(Color.BLACK);
				font.getData().setScale(3.5f, 2f);
				font.draw(batch, "E  X  I  T", 300, 297);
				batch.end();
			}
			
			//IF PLAYER CLICKS ON THE PLAY BUTTON AND EXIT BUTTON. INPUT ADAPTER CLASS CHECKS IF USER PRESSED MOUSE PAD OR KEYBOARD BUTTON
			Gdx.input.setInputProcessor(new InputAdapter() {
				@Override
				public boolean touchDown(int x, int y, int pointer, int button) {
					if(button == Input.Buttons.LEFT) {
						if(play_button.contains(mouse_pointer.x, mouse_pointer.y)) {
							currentScreen = Screen.MAIN_GAME;
						}
						else if(exit_button.contains(mouse_pointer.x, mouse_pointer.y)) {
							Gdx.app.exit(); //EXIT THE GAME
						}
					}
					
					return true;
				}
			});
			
		}else if(currentScreen == Screen.MAIN_GAME) { //GAME LOGIC AND MAIN GAME
			
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			Gdx.graphics.setTitle("Brick Breaker"); //TITLE OF THE GAME(TOP LEFT CORNER)
			
			//DISPLAY SCORE
			batch.begin();
			font.setColor(Color.GOLD);
			font.getData().setScale(1.5f, 1.5f);
			font.draw(batch, "S C O R E : " + score, 10, 590);
			batch.end();
			
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(Color.SALMON);
			shapeRenderer.rect(playerX, playerY, 80, 6);
			shapeRenderer.end();
			
			//PADDLE
			paddle.set(playerX, playerY, 80, 6);
			
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(Color.ORANGE);
			shapeRenderer.circle(ballX, ballY, 8);
			shapeRenderer.end();
			
			//BALL
			ball.set(ballX, ballY, 8);
			
			//DISPLAYING THE BRICKS FROM THE LIST OF BRICKS
			for(int i = 0; i < 20; i++) {
				
				shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				shapeRenderer.setColor(Color.LIME);
				shapeRenderer.rect(BrickX[i], BrickY[i], 70, 30);
				shapeRenderer.end();
				
				//EACH BRICK IS A RECTANGLE AND THE CO-ORDINATES ARE FETCHED FROM THE LIST OF BRICKS
				Brick.set(BrickX[i], BrickY[i], 70, 30);
				
				if(Intersector.overlaps(ball, Brick)) { //IF BALL HITS THE BRICK
					
					if(ballX <= BrickX[i] + 70 && ballX >= BrickX[i]) //IF BALL HITS THE WIDTH CHANGE Y DIRECTION OF THE BALL
						ballY_dir = -ballY_dir;
					else if(ballY >= BrickY[i] && ballY <= BrickY[i] + 30) //IF BALL HITS THE HEIGHT CHANGE X DIRCTION OF THE BALL
						ballX_dir = -ballX_dir;
					else { //ELSE CHANGE BOTH X AND Y DIRECTION OF THE BALL
						ballY_dir = -ballY_dir;
						ballX_dir = -ballX_dir;
					}
					
					ballY += ballY_dir*ballY_speed; //VELOCITY ALONG THE Y AXIS
					ballX += ballX_dir*ballX_speed; //VELOCITY ALONG THE X AXIS
					
					BrickX[i] = 1000f; //IF BALL HITS THE BRICK THEN MOVE THE BRICK OUT OF THE SCREEN SO THAT IT IS NO MORE VISIBLE TO USER
					BrickY[i] = 1000f; // ' ' '
					
					score += 1; //INCREASE THE SCORE BY 1
					
				}
				
			}
			
			//CONTROLLING THE PADDLE THROUGH KEYBOARD
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				if(playerX <= 0)
					playerX = 0;
				else
					playerX -= 10;
			}
			else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				if(playerX >= 720)
					playerX = 720;
				else
					playerX += 10;
			}
			
			if(Intersector.overlaps(ball, paddle)) { //IF BALL HITS THE PADDLE
				ballY_dir = -ballY_dir; //CHANGE Y DIRECTION OF THE BALL
				ballY += ballY_dir*ballY_speed;
				if(ballX >= playerX+40) { //IF IT HITS THE RIGHT SIDE OF THE PADDLE THEN THE BALL GOES TOWARDS RIGHT
					ballX_dir = Math.random();
					ballX += ballX_dir*ballX_speed;
				}
				else if(ballX < playerX+40) { //IF IT HITS THE LEFT SIDE OF THE PADDLE THEN THE BALL GOES TOWARDS LEFT
					ballX_dir = -1 + Math.random()*0;
					ballX += ballX_dir*ballX_speed;
				}
			}
			
			else if(ballX <= 0) { //IF BALL HITS THE LEFT SIDE OF THE SCREEN THEN BOUNCE IT BACK
				ballX_dir = -ballX_dir;
				ballY += ballY_dir*ballY_speed;
				ballX += ballX_dir*ballX_speed;
			}
			else if(ballX >= 792) { //IF BALL HITS THE RIGHT SIDE OF THE SCREEN THEN BOUNCE IT BACK
				ballX_dir = -ballX_dir;
				ballY += ballY_dir*ballY_speed;
				ballX += ballX_dir*ballX_speed;
			}
			else if(ballY >= 592) { //IF BALL HITS TOP OF THE SCREEN THEN BOUNCE IT BACK
				ballY_dir = -ballY_dir;
				ballY += ballY_dir*ballY_speed;
				ballX += ballX_dir*ballX_speed;
			}
			else if(ballY <= 0) { //IF BALL HITS BOTTOM OF THE SCREEN THEN GAME OVER
				won = false; //LOST
				
				//DELAY OF .5 SECONDS
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//RESET THE BALL POSITION, BALL DIRECTION, AND BRICK CO-ORDINATES FOR THE NEXT GAME(IF USER WANTS TO PLAY AGAIN)
				ballY = 250;
				ballY_dir = -1;
				score = 0;
				float x = 90.5f, y = 500f;
				
				for(int i = 0; i < 20; i++) {
					if(i == 5 || i == 10 || i == 15) {
						x = 90.5f;
						y -= 70f;
					}
					else if(i == 0)
						x = 90.5f;
					else 
						x += 140f;
					
					BrickX[i] = x;
					BrickY[i] = y;
				}
				//SWITCH TO THE GAME OVER SCREEN
				currentScreen = Screen.GAME_OVER;
;
			}
			else { 
				ballY += ballY_dir*ballY_speed;
				ballX += ballX_dir*ballX_speed;
			}
			
			if(score == 20) { //WON THE GAME
				won = true;
				
				//RESET THE BALL POSITION, BALL DIRECTION, SCORE AND BRICK CO-ORDINATES FOR THE NEXT GAME(IF USER WANTS TO PLAY AGAIN)
				ballY = 250;
				ballY_dir = -1;
				score = 0;
				float x = 90.5f, y = 500f;
				for(int i = 0; i < 20; i++) {
					if(i == 5 || i == 10 || i == 15) {
						x = 90.5f;
						y -= 70f;
					}
					else if(i == 0)
						x = 90.5f;
					else 
						x += 140f;
					
					BrickX[i] = x;
					BrickY[i] = y;
				}
				
				//SWITCH TO GAME OVER(ACTUALLY GAME WON) SCREEN
				currentScreen = Screen.GAME_OVER;
			}
		}
		else if(currentScreen == Screen.GAME_OVER) { //GAME OVER OR GAME WON
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			Gdx.graphics.setResizable(false);
			
			if(!won) { //GAME LOST
				batch.begin();
				font.getData().setScale(4f, 4f);
				font.setColor(Color.SCARLET);
				font.draw(batch, "G A M E  O V E R", 180, 450);
				batch.end();
				
				batch.begin();
				font.getData().setScale(2.25f, 3f);
				font.setColor(Color.LIME);
				font.draw(batch, "P R E S S 'R' T O R E T U R N TO M A I N M E N U", 20, 300);
				batch.end();
			}
			else { //GAME WON
				batch.begin();
				font.getData().setScale(4f, 4f);
				font.setColor(Color.GOLD);
				font.draw(batch, "Y O U  W O N !", 180, 450);
				batch.end();
				
				batch.begin();
				font.getData().setScale(2.25f, 3f);
				font.setColor(Color.LIME);
				font.draw(batch, "P R E S S 'R' T O R E T U R N TO M A I N M E N U", 20, 300);
				batch.end();
			}
			
			if(Gdx.input.isKeyPressed(Input.Keys.R)) { //IF USER PRESSES 'R' THEN SWITCH TO MAIN MENU
				currentScreen = Screen.MAIN_MENU;
			}
		}
		}
	
	//DISPOSE THE GAME OBJECTS AFTER USER QUITS THE GAME(NOT NECESSARY)
	@Override
	public void dispose () {
		font.dispose();
		batch.dispose();
	}
	
}