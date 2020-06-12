
public class Laser extends Obstacle {
	private int xStart;
	private int yStart;
	private int xEnd;
	private int yEnd;
	// buffer determines the distance for it to be considered a hit
	// low buffer increases probability of fading through
	private int buffer = 3;

	public Laser(int id, int xStart, int yStart, int xEnd, int yEnd) {
		super(id);
		this.xStart = xStart;
		this.yStart = yStart;
		this.xEnd = xEnd;
		this.yEnd = yEnd;
	}

	public boolean contains(int x, int y) {
		boolean contains = false;
		float d1 = dist(x, y, xStart, yStart);
		float d2 = dist(x, y, xEnd, yEnd);
		float sum = d1 + d2;
		float length = dist(xStart, yStart, xEnd, yEnd);
		if (sum > length - buffer && sum < length + buffer) {
			contains = true;
		}
		return contains;
	}

	private float dist(int x, int y, int x1, int y1) {
		float distance = (float) (Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2)));
		return distance;

	}

	@Override
	public void iterate() {
		xStart -= 5;
		xEnd -= 5;
	}

	public boolean invalidObstacle() {
		if (xStart < 0 && xEnd < 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean potentialCollison() {
		if(xStart > 500 && xEnd > 500) {
			return false;
		}
		return true;
	}




	@Override
	public int[] getDefinitions() {
		int[] definitions = {xStart, yStart, xEnd, yEnd};
		return definitions;
	}
}
