/*
 * File: Breakout.java
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels.  On some platforms 
  * these may NOT actually be the dimensions of the graphics canvas. */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board.  On some platforms these may NOT actually
  * be the dimensions of the graphics canvas. */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
	private GRect brick;
	private GRect paddle;
	private GOval ball;
	private GObject collider;
	private GLabel terminate;
	
/** time delay to update ball movement*/	
	private static final int GAME_DELAY = 15;

	private double Vx, Vy;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	/* Method: init() */
	/* Initialize the program */
	public void init() {
		// draw the game initial setup
		drawGameBricks();
		
		// draw the paddle
		// locate the initial location of paddle
		double paddle_x = (WIDTH - PADDLE_WIDTH)/ 2;
		double paddle_y = HEIGHT - PADDLE_Y_OFFSET;		
		drawPaddle(paddle_x, paddle_y);
		
		// add listener for mouse movement
		addMouseListeners();
	}
	
	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		
		// draw ball
		ballSetup();
		
		// initialize Vx
		Vx = rgen.nextDouble(1.0, 3.0);
		if(rgen.nextBoolean(0.5)) 
			Vx = -Vx;
		
		// initialize Vy
		Vy = 3.0;		
		
		
		// play game
		while (ball.getX() < WIDTH && ball.getY() < HEIGHT - BALL_RADIUS * 2) {
			moveBall();
			collider = getCollidingObject();
			if (collider == paddle) {
				// collider is paddle, bounce back
				Vy = -Vy;
				ball.move(Vx, Vy);
			} else if (collider == null){
				// collider object is null, do nothing
			} else {
				// collider is brick, bounce back and remove the brick
				remove (collider);
				Vy = -Vy;
				ball.move(Vx, Vy);
			}
			pause(GAME_DELAY);
		}
		
		// terminate game
		terminateGame();
		
	}
	
	// draw the game setup bricks
	private void drawGameBricks() {
		
		double brick_x, brick_y;
		Color brick_color;
		// calculate the location of the first brick in each row
		double brick_x_init = calcFirstBrickLoc();
		
		for (int row_index = 0; row_index < NBRICK_ROWS; row_index ++) {
			// Extrapolate y location of brick
			brick_y = BRICK_Y_OFFSET + row_index * (BRICK_HEIGHT + BRICK_SEP);
			
			// determine the color of bricks in a row
			switch (row_index) {
			case 0: brick_color = Color.red;break;
			case 1: brick_color = Color.red;break;
			case 2: brick_color = Color.orange;break;
			case 3: brick_color = Color.orange;break;
			case 4: brick_color = Color.yellow;break;
			case 5: brick_color = Color.yellow;break;
			case 6: brick_color = Color.green;break;
			case 7: brick_color = Color.green;break;
			case 8: brick_color = Color.cyan;break;
			case 9: brick_color = Color.cyan;break;
			default: brick_color = Color.white;
			}
			
			// draw bricks in a row
			for (int index_column = 0; index_column < NBRICKS_PER_ROW; index_column ++) {
				// Extrapolate x location of brick
				brick_x = brick_x_init + index_column * (BRICK_WIDTH + BRICK_SEP);
				
				// draw the bricks
				drawBrick(brick_x, brick_y, brick_color);
			}
		}
	}
	
	// calculate the location of the first brick
	private double calcFirstBrickLoc () {
		double loc_x;
		loc_x = (WIDTH - BRICK_WIDTH * NBRICKS_PER_ROW - BRICK_SEP * (NBRICKS_PER_ROW -1)) / 2;
		return loc_x;
	}
	
	// draw an individual brick
	private void drawBrick (double x, double y, Color color) {
		brick = new GRect (x, y, BRICK_WIDTH, BRICK_HEIGHT);
		brick.setFilled(true);		// enable fill color
		brick.setFillColor(color);  // fill brick with color
		brick.setColor(color);		// set the boundary color
		add(brick);
	}

	// draw an individual paddle
	private void drawPaddle(double x, double y) {
		// draw the paddle
		paddle = new GRect (x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setFillColor(Color.black);
		add (paddle);
	}

	// define paddle operation when mouse moved
	public void mouseMoved(MouseEvent e) {
		// clean up previous paddle trace
		remove(paddle);
		
		// draw paddle
		if (e.getX() < WIDTH - PADDLE_WIDTH) {
			//paddle.move(e.getX(), HEIGHT - PADDLE_Y_OFFSET);
			drawPaddle(e.getX(), HEIGHT - PADDLE_Y_OFFSET);
		} else 
			drawPaddle(WIDTH- PADDLE_WIDTH - BRICK_SEP, HEIGHT - PADDLE_Y_OFFSET);
			//paddle.move(WIDTH - PADDLE_WIDTH - BRICK_SEP, HEIGHT - PADDLE_Y_OFFSET);
	}
	
	// initialize ball position
	private void ballSetup() {
		double ball_x, ball_y;
		ball_x = (WIDTH - BALL_RADIUS * 2) / 2;
		ball_y = (HEIGHT - BALL_RADIUS * 2) / 2;
		ball = new GOval(ball_x, ball_y, BALL_RADIUS, BALL_RADIUS);
		ball.setFilled(true);
		ball.setFillColor(Color.black);
		add(ball);
	}
	
	// ball movement while in game playing mode
	private void moveBall() { 
		
		// bouncing wall
		if (ball.getY() < HEIGHT - BALL_RADIUS * 2 && ball.getY() > BALL_RADIUS * 2 && ball.getX() < WIDTH - BALL_RADIUS * 2 && ball.getX() > 0 ) { // ball in region
			// move ball
			ball.move(Vx, Vy);
		} else if (ball.getY() <= BALL_RADIUS * 2 || ball.getY() >= HEIGHT - BALL_RADIUS * 2) { 
			// ball touches upper/bottom wall, reverse vertical speed
			Vy = -Vy;
			ball.move(Vx, Vy);
		} else if (ball.getX() >= WIDTH - BALL_RADIUS * 2 || ball.getX() <= 0) {
			// ball touches right/left wall, reverse horizontal speed
			Vx = -Vx;
			ball.move(Vx, Vy);
		} else {
			// exception
			ball.setColor(Color.red);
			ball.setFillColor(Color.red);
			ball.setFilled(true);
			ball.setLocation(WIDTH/3, HEIGHT - BALL_RADIUS * 2);
		}
		
	}

	// retrieve the colliding object
	private GObject getCollidingObject() {
		// ball upper tip
		if (getElementAt(ball.getX() + BALL_RADIUS, ball.getY()) == null) 	
			
			// ball bottom tip
			if(getElementAt(ball.getX() + BALL_RADIUS, (ball.getY() + BALL_RADIUS)) == null)		
						return null;
		
			else 
				return getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + BALL_RADIUS);
		
		else 
			return getElementAt(ball.getX() + BALL_RADIUS, ball.getY());
	}
	
	private void terminateGame(){
		// display 'GAME OVER' signal 
		terminate = new GLabel ("@_@ GAME OVER @_@", WIDTH / 6, HEIGHT / 3);
		terminate.setColor(Color.red);
		terminate.setFont("SansSerif-24");
		add (terminate);
		
		// move ball to origin location
		ball.setLocation(0, 0);
		ball.setColor(Color.red);
		ball.setFillColor(Color.red);
		ball.setFilled(true);
	}
}
