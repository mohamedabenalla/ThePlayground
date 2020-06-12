import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

//I grouped the classes by method purpose
// Top methods are for the main infrastructure of the code
// Middle methods are used as calculations/ to progress data
//Bottom methods are for getters/setters as needed
//bottom is draw jpanel extension
// These are camel case and are named by 
public class Field implements KeyListener {
	//rand variable
	Random rand = new Random();
	// timer variable
	private javax.swing.Timer t;
	// screen variables
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public final int maxWidth = (int) screenSize.getWidth(); // 1980
	public final int maxHeight = (int) screenSize.getHeight() - 210; // 1000
	private draw display;
	// obstacle variables
	private ArrayList<Obstacle> obstacles;
	private int obstacleCheckpoint = (int) screenSize.getWidth() / 2; // once obstacle passes this then new obstacles are
																	// spawned
	// player variables
	private Player player; //100, 50
	final private int playerHeight;
	final private int playerWidth;
	private int score;
	private int coins;
	// variable IDS
	private int BLOCK = 0;
	private int LASER = 1;
	private int ROCKET = 2;
	private int COIN = 3;
	private int[] ids = { BLOCK, LASER, ROCKET, COIN };
	private int[] boxDimension = { (int) (maxHeight * .1), (int) (maxHeight * .125), (int) (maxHeight * .2),
			(int) (maxHeight * .25), (int) (maxHeight * .325) };
	private int[] laserDimension = { (int) (maxHeight * .285), (int) (maxHeight * 0.5) };
	private boolean gameOver;
	private int difficulty = 1;
	private int cap = 900;
	// draw variables
	BufferedImage background;
	BufferedImage block;;
	BufferedImage rocket;
	BufferedImage laser;
	BufferedImage coin;
	ArrayList<BufferedImage> playerAnimations = new ArrayList<BufferedImage>();

	// constructor for field
	// in the future this will have more variables since there will be customization
	// of the screen and etc
	public Field(int playerIndex, int backgroundIndex) {
		// is the new game method
		// variable declarations
		obstacles = new ArrayList<Obstacle>();
		score = 0;
		coins = 0;
		display = new draw();
		display.addKeyListener(this);
		display.setFocusable(true);
		try {
			background = ImageIO.read(new File("images\\backgrounds\\background" + backgroundIndex + ".jpg"));
			block = ImageIO.read(new File("images\\smallBlock.png"));
			coin = ImageIO.read(new File("images\\coin.png"));
			rocket = ImageIO.read(new File("images\\rocket.png"));
			String playerAnimationPath ="images\\playerArt\\player" + playerIndex;
			playerAnimations.add(ImageIO.read(new File(playerAnimationPath + "\\stride1.png")));
			playerAnimations.add(ImageIO.read(new File(playerAnimationPath + "\\rest.png")));
			playerAnimations.add(ImageIO.read(new File(playerAnimationPath + "\\stride2.png")));
			playerAnimations.add(ImageIO.read(new File(playerAnimationPath + "\\rest.png")));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		playerWidth = (int) (playerAnimations.get(0).getWidth() * 1.8) - 10;
		playerHeight = (int) (playerAnimations.get(0).getHeight() * 1.8) - 10;
		// this is up to be determined, the game will always start with the same
		// obstacle but then would be randomized as time goes on
		obstacles.add(new Block(BLOCK, 0, 0, 0, maxWidth));
		player = new Player(playerWidth, playerHeight, maxHeight);
		gameOver = false;
	}

	public void runGame() {
		// returns score - equates to coins earned
		// return value can be changed at a later date to include distance or to
		// differentiate score and coins
			t = new javax.swing.Timer(5, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					display.repaint();
					player.iterate();
					// advancement of time dependent variables - score
					score += 1;
					if(difficulty < 6 & score > cap) {
						difficultyProgress();
					}
					// functions
					updateObstacles();
					detectCollison();
				}
			});
			t.start();

	}
	private void difficultyProgress() {
		difficulty += 1;
		cap += 2000;
	}

	// potential to have a tracking variable which will only create certain
	// obstacles depending on progress in game - TBD (ie will restrict part of the
	// id array)
	// this method is made to iterate through all the obstacles
	private void updateObstacles() {
		int[] lastObstacleDefinitions = obstacles.get(obstacles.size() - 1).getDefinitions();
		if (lastObstacleDefinitions[0] + lastObstacleDefinitions[0] < obstacleCheckpoint +(difficulty * 0.2 * obstacleCheckpoint)) {
			// I found just spawning boxes to be boring and not engaging enough
			// as solution I have created obstacle groups
			// a random integer is created and from there each random integer triggers a
			// certain group
			// GENERATE NEW OBJECT HERE
			int id = rand.nextInt(difficulty);
			int coinChance = rand.nextInt(3);
			if (id == 0) {
				//int formationId = rand.nextInt(difficulty);
				// Two blocks on top and bottom
				int dimensionId = rand.nextInt(3);
				int dimension = boxDimension[dimensionId + 2];
				// int yId = rand.nextInt(4);
				// int y = maxHeight - dimension;
				// if (yId == 0) {
				// y = 0;
				// } else if (yId == 1) {
				// y *= 2 / 3;
				// } else if (yId == 2) {
				// y *= 1 / 3;
				// }
				obstacles.add(new Block(ids[0], 0, dimension, dimension, maxWidth));
				obstacles.add(new Block(ids[0], maxHeight - dimension, dimension, dimension, maxWidth));
				int rocket = rand.nextInt(difficulty);
				if (dimensionId == 0) {
					dimensionId += 1;
				}
				if (rocket >= 3) {
					obstacles.add(new Rocket(ROCKET, maxHeight / 2, 100, 50, maxWidth + 200, (int) player.getYCoord()));
				} else {
					for(int i = 0; i < dimensionId+1; i++) {
						for(int j= 0; j < dimensionId+1; j++) {
						obstacles.add(new Coin(COIN, maxWidth + 10+  + (int) (maxWidth*0.05 * j) , (int) (maxHeight / 2 - (maxHeight * .1) + (maxHeight * 0.05 * i)), 40));
						}
					}
				}
			} else if (id == 1) {
				// 1 == 4 blocks in diamond formation
				// 2 == stacked blocks
				int formationId= rand.nextInt(3);
				int dimensionId = rand.nextInt(2);
				int dimension = boxDimension[dimensionId];
				int centerHeight = (maxHeight / 2);
				// seperation value is how spread out the boxes are
				int seperationValue = boxDimension[rand.nextInt(3) + 2];
				// no objects should be placed automatically in the screen so I use maxHeight +
				// value instead of - value
				if(formationId <= 1) {
				obstacles.add(new Block(BLOCK, centerHeight - (dimension / 2), dimension, dimension,
						maxWidth + (seperationValue * 2)));
				obstacles.add(new Block(BLOCK, centerHeight - (dimension / 2), dimension, dimension, maxWidth));
				obstacles.add(new Block(BLOCK, centerHeight + seperationValue, dimension, dimension,
						maxWidth + seperationValue));
				obstacles.add(new Block(BLOCK, centerHeight - seperationValue - dimension, dimension, dimension,
						maxWidth + seperationValue));
				} else if (formationId == 2){
					for(int i = 1; i < 8 - dimensionId; i++) {
						for(int j = 8 - dimensionId; j > i; j--) {
							obstacles.add(new Block(BLOCK, maxHeight- (dimension * i), dimension, dimension, maxWidth + (j*dimension)));
						}
					}
				}
			} else if (id >= 2) {
				int formationId = rand.nextInt(difficulty);
				int dimensionId = rand.nextInt(2);
				int dimension = laserDimension[dimensionId];

				if (formationId == 0) {
					// Parallel vertical
					obstacles.add(new Laser(LASER, maxWidth, 80, maxWidth, 80 + dimension));
					obstacles.add(new Laser(LASER, maxWidth + 300, 80, maxWidth + 300, 80 + dimension));
					if (dimensionId == 0) {
						obstacles.add(
								new Laser(LASER, maxWidth, maxHeight - 80, maxWidth, maxHeight - 80 - dimension));
						obstacles.add(new Laser(LASER, maxWidth + 300, maxHeight - 80, maxWidth + 300,
								maxHeight - 80 - dimension));
						for(int i = 0; i < 4; i++) {
							for(int j= 0; j < 4; j++) {
							obstacles.add(new Coin(COIN, maxWidth + (int) (maxWidth*0.05 * j - 1) , (int) (maxHeight / 2 - (maxHeight * .1) + (maxHeight * 0.05 * i)), 40));
							}
						}
					} else {
						for(int i = 0; i < 4; i++) {
							for(int j= 0; j < 4; j++) {
							obstacles.add(new Coin(COIN, maxWidth + (int) (maxWidth*0.05 * j) , (int) (maxHeight * 3 / 4 - (maxHeight * .1) + (maxHeight * 0.05 * i)), 40));
							}
						}
					}
				} else if (formationId <= 2 ) {
					// Parallel horizontal
					int yId = rand.nextInt(4);
					if (yId == 0) {
						// top
						obstacles.add(new Laser(LASER, maxWidth, (int) (maxHeight * 0.1), maxWidth + dimension, (int) (maxHeight * 0.1)));
						obstacles.add(new Laser(LASER, maxWidth, (int) (maxHeight * 0.2), maxWidth + dimension, (int) (maxHeight * 0.2)));
						obstacles.add(new Laser(LASER, maxWidth, maxHeight - (int) (maxHeight * 0.1), maxWidth + dimension, maxHeight - (int) (maxHeight * 0.1)));
						obstacles.add(new Laser(LASER, maxWidth, maxHeight - (int) (maxHeight * 0.2), maxWidth + dimension, maxHeight - (int) (maxHeight * 0.2)));
					} else if (yId == 1) {
						// bottom
						obstacles.add(new Laser(LASER, maxWidth, maxHeight - (int) (maxHeight * 0.1), maxWidth + dimension, maxHeight - (int) (maxHeight * 0.1)));
						obstacles.add(new Laser(LASER, maxWidth, maxHeight - (int) (maxHeight * 0.2), maxWidth + dimension, maxHeight - (int) (maxHeight * 0.2)));
						obstacles.add(new Laser(LASER, maxWidth, maxHeight - (int) (maxHeight * 0.45), maxWidth + dimension, maxHeight - (int) (maxHeight * 0.45)));
						obstacles.add(new Laser(LASER, maxWidth, maxHeight - (int) (maxHeight * 0.55), maxWidth + dimension, maxHeight - (int) (maxHeight * 0.55)));
					} else {
						obstacles.add(new Laser(LASER, maxWidth, (int) (maxHeight * 0.1), maxWidth + dimension, (int) (maxHeight * 0.1)));
						obstacles.add(new Laser(LASER, maxWidth, (int) (maxHeight * 0.2), maxWidth + dimension, (int) (maxHeight * 0.2)));
						obstacles.add(new Laser(LASER, maxWidth, maxHeight - (int) (maxHeight * 0.45), maxWidth + dimension, maxHeight - (int) (maxHeight * 0.45)));
						obstacles.add(new Laser(LASER, maxWidth, maxHeight - (int) (maxHeight * 0.55), maxWidth + dimension, maxHeight - (int) (maxHeight * 0.55)));
					}
				} else if (formationId > 2) {
					// Parallel at an angle
					obstacles.add(new Laser(LASER, maxWidth + 300, 100, maxWidth + 400, 100 + dimension));
					obstacles.add(new Laser(LASER, maxWidth, 100, maxWidth + 100, 100 + dimension));
				}
				// obstackes.add(new Laser(LASER, ))
			}
			for (int i = 0; i < obstacles.size(); i++) {
				if (obstacles.get(i).invalidObstacle()) {
					obstacles.remove(i);
				}

			}

		}
		for (Obstacle o : obstacles) {
			o.iterate();
			if (o.getId() == ROCKET) {
				o.updatePlayerCoordY((int) player.getYCoord());
				// add collisions with other objects later but make sure to make efficient
			}
		}

	}

	// method to detect collision with player, works by taking all the players
	// coordinates and iterating them through the obstacles searching for
	// a line up of coordinates
	private boolean detectCollison() {
		ArrayList<Integer[]> points = player.getPoints();
		for (int i = 0; i < obstacles.size(); i++) {
			if (obstacles.get(i).potentialCollison()) { 
				// this line makes it so it won't check for collisions when the	object is too far away
				for (Integer[] coord : points) {
					if (obstacles.get(i).contains(coord[0], coord[1])) {
						if (obstacles.get(i).getId() == COIN) {
							coins += 1;
							obstacles.remove(i);
							// i-- insures nothing is skipped in array when there is a deletion
//							i--;
						} else {
							// game ends here
							t.stop();
							gameOver = true;
						}
					}
				}
			}
		}
		return false;

	}

	//
	// Getters and Setters
	//
	public boolean returnStatus() {
		return gameOver;
	}

	public int[] returnValues() {
		int[] returnValues = { score, coins };
		return returnValues;
	}

	public JPanel getVisual() {
		return display;
	}

	// draw class
	class draw extends JPanel {
		public void paintComponent(Graphics g) {
			g.drawImage(background, 0, 0, maxWidth, maxHeight + 200, null);
			g.setColor(Color.BLACK);
			g.fillRect(0, maxHeight, maxWidth, 200);
			for (int i = 0; i < obstacles.size(); i++) {
				if (obstacles.get(i).getId() == BLOCK) {
					// BLOCK DESIGN GOES HERE
					g.setColor(Color.BLUE);
					int[] definitions = obstacles.get(i).getDefinitions();
					// g.fillRect(definitions[0], definitions[1], definitions[2], definitions[3]);
					g.drawImage(block, definitions[0], definitions[1], definitions[2], definitions[3], null);
				} else if (obstacles.get(i).getId() == LASER) {
					// LASER DESIGN GOES HERE
					g.setColor(Color.RED);
					int[] definitions = obstacles.get(i).getDefinitions();
					for (int j = -5; j <= 5; j++)
						g.drawLine(definitions[0] + j, definitions[1] + j, definitions[2] + j, definitions[3] + j);
					g.setColor(new Color(255,160,122));
//					g.fillOval(definitions[0], definitions[1], definitions[2] - definitions[0], definitions[3] - definitions[1]);
					g.setColor(Color.BLACK);
					g.fillOval(definitions[0] - 10, definitions[1] - 10, 20, 20);
					g.fillOval(definitions[2] - 10, definitions[3] - 10, 20, 20);
				} else if (obstacles.get(i).getId() == ROCKET) {
					// ROCKET DESIGN GOES HERE
					g.setColor(Color.RED);
					int[] definitions = obstacles.get(i).getDefinitions();
					// g.fillRect(definitions[0], definitions[1], definitions[2], definitions[3]);
					g.drawImage(rocket, definitions[0], definitions[1], definitions[2], definitions[3], null);

				} else if (obstacles.get(i).getId() == COIN) {
					// COIN Design GOES HERE
					g.setColor(Color.YELLOW);
					int[] definitions = obstacles.get(i).getDefinitions();
					// g.fillOval(definitions[0], definitions[1], definitions[2], definitions[3]);
					g.drawImage(coin, definitions[0], definitions[1], definitions[2], definitions[3], null);
				}

			}
			// PLAYER DESIGN HERE
			g.setColor(Color.WHITE);
			int[] playerDefinitions = player.getDefinitions();
			//g.drawRect(playerDefinitions[0], playerDefinitions[1], playerDefinitions[2], playerDefinitions[3]);
			g.drawImage(playerAnimations.get(player.getAnimateID()) , playerDefinitions[0], playerDefinitions[1], playerDefinitions[2], playerDefinitions[3], null);
			// playerDefinitions[2], playerDefinitions[3], null);
			// TEXT FOR SCORE AND COIN
			g.setColor(Color.white);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 40)); 
			g.drawString("Score " + score + "       Coins: " + coins, 20, maxHeight + 50);
		}

	}

	// Key Listeners
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 32) {
			// spacebar key code
			player.up();

		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
