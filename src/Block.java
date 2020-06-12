
public class Block extends Obstacle{
	private int x;
	private int y;
	private int width;
	private int height;
	public Block(int id, int y, int width, int height, int maxWidth) {
		super(id);
		x = maxWidth;
		this.width = width;
		this.y = y;
		this.height = height;

	};
	
	//prevents unnecessary iteration through points by deeming if collision is even possible
	//this is efficient but not needed, can make only one method call viable
	public boolean potentialCollison() {
		if (x > 550)
			return false;
		return true;
		
	}
	@Override
	public boolean contains(int x, int y) {
		if (x > this.x && x < this.x + width && y > this.y && y < this.y + height) {
			return true;
		}
		return false;
	}
	@Override
	public void iterate() {
		x -= 5;
	}
	@Override
	public boolean invalidObstacle() {
		if (x + width < 0) {
			return true;
		}
		return false;
	}


	@Override
	public int[] getDefinitions() {
		int[] definition = {x, y ,width, height};
		return definition;
	}
	 
	
}