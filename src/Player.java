import java.util.ArrayList;

import javax.swing.*;

public class Player {
	//Make x and y coord top left
	private final double xCoord = 300;
	private double yCoord;
	private int coins;
	private boolean alive;
	private double height;
	private double width;
	//gravity variables
	private final double acceleration;
	private double velocity;
	private double jumpVelocity; //will be a negative value and will change 
	private final double maxHeight;
	private int animateID; //know what image to draw
	private int animateTimer = 0;
	
	
	
	Player(double width, double height, double maxHeight){
		this.coins = 0;
		this.alive = true;
		this.yCoord = maxHeight * 0.3;
		this.height = height;
		this.width = width;
		this.maxHeight = maxHeight;
		jumpVelocity = -1 * maxHeight * 0.006;
		acceleration = .000098*2/3 * maxHeight;
	}
	
	public void up() {
		velocity = jumpVelocity;
	}
	
	public int getCoins() {
		return this.coins;
	}
	
	public double getYCoord() {
		return this.yCoord;
	}
	
	public boolean getAlive() {
		return this.alive;
	}
	
	
	//
	public boolean contains(int x, int y) {
		if(this.xCoord <= x && x <= (this.xCoord + width) && this.yCoord <= y && y <= (this.yCoord + height)) {
			return true;
		}else {
			return false;
		}
	}
	
	
	//this gets all the points that player contains to use for determining collisions -- makes the hitbox
	public ArrayList<Integer[]> getPoints() {
		ArrayList<Integer[]> points = new ArrayList<Integer[]>();
		//TO increase efficiency I determined the hitbox points through keypoints - corner points in addition to  other spaced out points
		// Imagine a connect the dots scenario
		//I use psuedo coords and dimensions so that the hitbox is a little bit smaller than the actual player
		for (int i = (int) xCoord; i < xCoord + width; i+= width / 5){
			for (int j = (int) yCoord; j < yCoord + height; j+= height / 5) {
				Integer[] coords = {i, j};
				points.add(coords);
			}
		}
		return points;
	}
	public void iterate() {
		if (yCoord + velocity + height > maxHeight) {
			yCoord = maxHeight - height;
			velocity = 0;
		} else if (yCoord + velocity < 0) {
			yCoord = 0;
			velocity = 0;
		} else {
			yCoord += velocity;
		}
		if(animateTimer == 0) {
			animate();
			animateTimer = 40;
		} else {
			animateTimer-=1;
		}
		if( yCoord != maxHeight) {
			velocity += acceleration;
		}

	}
	
	public int[] getDefinitions() {
		int[] definition = {(int) xCoord - 5, (int) yCoord - 5 ,(int) width + 10, (int) height + 10};
		return definition;
	}
	private void animate() {
		
		if(velocity == 0) {
			if(animateID == 3) {
			animateID = 0;
			}else {
			animateID+= 1;
			}
		}else {
			animateID = 0;
		}
	}
	public int getAnimateID() {
		return animateID;
	}
	 

}

