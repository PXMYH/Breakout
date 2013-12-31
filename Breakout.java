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
	
	private static final int GAME_DELAY = 50;

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
		drawBall();
		
		// initialize Vx
//		Vx = rgen.nextDouble(1.0, 3.0);
//		if(rgen.nextBoolean(0.5)) 
//			Vx = -Vx;
		Vx = 0.0;
		
		// initialize Vy
		Vy = 3.0;		
		
		while (ball.getX() < WIDTH) {
			moveBall();
			//checkForCollision();
			//pause(GAME_DELAY);
		}
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
	
	private void drawBall() {
		double ball_x, ball_y;
		ball_x = (WIDTH - BALL_RADIUS * 2) / 2;
		ball_y = (HEIGHT - BALL_RADIUS * 2) / 2;
		ball = new GOval(ball_x, ball_y, BALL_RADIUS, BALL_RADIUS);
		ball.setFilled(true);
		ball.setFillColor(Color.black);
		add(ball);
	}
	
	private void moveBall() { 
		

		
		// move ball
		ball.move(Vx, Vy);
		
		
		// bouncing wall
		
	}
}
