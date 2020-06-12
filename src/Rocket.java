public class Rocket extends Obstacle{
	private int x;
	private int y;
	private int width;
	private int height;
	//private int playerCoordY;
	public Rocket(int id, int y, int width, int height, int maxWidth, int playerCoordY) {
		//rewrite in future for rocket shape
		super(id);
		x = maxWidth - width;;
		this.width = width;
		this.y = y;
		this.height = height;
		updatePlayerCoordY(playerCoordY);
	}
	public int getX() {
		return x;
	}
	@Override
	public boolean contains(int x, int y) {
		//rewrite in future for rocket shape
		if (x > this.x && x < this.x + width && y > this.y && y < this.y + height) {
			return true;
		}
		return false;
	}
	@Override
	public void iterate() {
		//update later so rocket doesn't phase through obstacles
		//update later so it tracks the player
		x -= 8; 
		if ( y < getPlayerCoordY())
			y += 1; 
		else 
			y-= 1;
		
		
	}
	public boolean invalidObstacle() {
		if (x + width < 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean potentialCollison() {
		if (x > 550)
			return false;
		return true;
		
	}
	
	@Override
	public int[] getDefinitions() {
		int[] definitions = {x, y, width, height};
		return definitions;
	}
}

