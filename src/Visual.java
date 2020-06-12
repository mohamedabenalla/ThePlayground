import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//Temporary Color Scheme: 
public class Visual {
	enum Page {
		HOME, DIFFICULTY, STORE, RULES;
	}

	private static JFrame frame;
	private static JPanel start;
	private static JPanel difficulty;
	private static JPanel store;
	private static JPanel rules;
	private static JPanel end;
	private static JPanel leader;
	private static JPanel enterLeader;

	private static Color first;
	private static Color second;
	private static Color third;
	private static Color fourth;
	private static Color fifth;
	private static Page currPage;
	public Field game;
	public static Visual program;
	private static Timer progress;
	private JButton back;

	private int highscore;
	private String highscoreName;
	private int coins = 2000;
	private int[] playerUnlocks = new int[6];
	private int[] backgroundUnlocks = new int[8];
	private ArrayList<BufferedImage> playerVisuals = new ArrayList<BufferedImage>();
	private String[] playerNames = { "Erikson", "Brock", "Amelia", "Jefferson", "Appa", "Momo" };
	private int[] playerPrices = { 0, 100, 100, 500, 500, 1000 };
	private JButton[] playerButtons;
	private ArrayList<ImageIcon> backgroundVisuals = new ArrayList<ImageIcon>();
	private String[] backgroundNames = {"The Playground", "The Cave", "The Resort", "The Spooky Woods", "The Castle",
			"The Galaxy" };
	private int[] backgroundPrices = { 0, 100, 100, 500, 500, 1000 };

	public Visual() {
		try {
			Sound music = new Sound("audio\\Off Limits.wav");
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		start = new JPanel();
		difficulty = new JPanel();
		store = new JPanel();
		rules = new JPanel();
		first = new Color(34, 40, 49);
		second = new Color(57, 62, 70);
		third = new Color(41, 161, 156);
		fourth = new Color(250, 250, 250);
		fifth = new Color(163, 247, 191);
		currPage = Page.HOME;
		try {
			for (int i = 1; i <= 6; i++) {
				playerVisuals.add(ImageIO.read(new File("images\\playerArt\\player" + i + "\\rest.png")));
				backgroundVisuals.add(new ImageIcon(new ImageIcon("images\\backgrounds\\background" + i + ".jpg").getImage().getScaledInstance(100, 70, Image.SCALE_DEFAULT)));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		readXML();
		back = new JButton();
		back.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 35));
		back.setText("Home");
		back.setBackground(third);
		back.setForeground(fourth);
		back.setAlignmentX(Component.CENTER_ALIGNMENT);
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(start);
				frame.revalidate();
			}
		});
		makeStart();
		makeDifficulty();
		makeStore();
		makeRules();
		makeEnd();
		makeLeader();

		frame = new JFrame("The Playground");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.black);
		frame.setVisible(true);
		frame.setSize(screenSize.width, screenSize.height - 20);
		frame.setResizable(false);
		frame.setContentPane(start);
		frame.revalidate();

	}

	public static void main(String[] args) {
		program = new Visual();
		// frame.setContentPane(a.getStart());
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// frame.setVisible(true);
		// a.startGame();
	}

	public JPanel getStart() {
		return this.start;
	}

	public JPanel getDifficulty() {
		return this.difficulty;
	}

	public JPanel getStore() {
		return this.store;
	}

	public JFrame getFrame() {
		return this.frame;
	}

	public void makeStart() {
		start.setBackground(first);
		start.setLayout(new BoxLayout(start, BoxLayout.Y_AXIS));
		start.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 10));
		JLabel title = new JLabel("Welcome to The Playground");
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 100));
		title.setForeground(third);
		JButton startButton = new JButton();
		startButton.setText("Start");
		startButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 50));
		startButton.setBackground(third);
		startButton.setForeground(new Color(250, 250, 250));
		JButton Rules = new JButton();
		Rules.setText("Rules");
		Rules.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 50));
		Rules.setBackground(third);
		Rules.setForeground(new Color(250, 250, 250));
		difficulty.add(Box.createVerticalGlue());
		JButton Store = new JButton();
		Store.setText("Store");
		Store.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 50));
		Store.setBackground(third);
		Store.setForeground(new Color(250, 250, 250));
		JButton Leaderboards = new JButton();
		Leaderboards.setText("High Scores");
		Leaderboards.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 50));
		Leaderboards.setBackground(third);
		Leaderboards.setForeground(new Color(250, 250, 250));
		difficulty.add(Box.createVerticalGlue());
		start.add(Box.createVerticalGlue());
		start.add(title);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		start.add(Box.createRigidArea(new Dimension(0, 100)));
		start.add(Box.createRigidArea(new Dimension(0, 50)));
		start.add(startButton);
		start.add(Box.createRigidArea(new Dimension(0, 50)));
		start.add(Rules);
		start.add(Box.createRigidArea(new Dimension(0, 50)));
		start.add(Store);
		start.add(Box.createRigidArea(new Dimension(0, 50)));
		start.add(Leaderboards);
		start.add(Box.createRigidArea(new Dimension(0, 50)));
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		Leaderboards.setAlignmentX(Component.CENTER_ALIGNMENT);
		start.add(Box.createVerticalGlue());
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				program.startGame();
				frame.revalidate();
			}
		});
		Rules.setAlignmentX(Component.CENTER_ALIGNMENT);
		start.add(Box.createVerticalGlue());
		Rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Go to Rules Page
				currPage = Page.RULES;
				frame.setContentPane(rules);
				frame.revalidate();
			}
		});
		Store.setAlignmentX(Component.CENTER_ALIGNMENT);
		start.add(Box.createVerticalGlue());
		Store.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeStore();
				currPage = Page.STORE;
				frame.setContentPane(store);
				frame.revalidate();
			}
		});
		Leaderboards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeLeader();
				frame.setContentPane(leader);
				frame.revalidate();
			}
		});
	}

	public void makeRules() {
		rules = new JPanel();
		rules.setVisible(true);
		rules.setBackground(first);
		rules.setLayout(new BoxLayout(rules, BoxLayout.Y_AXIS));
		rules.setBorder(BorderFactory.createEmptyBorder(100, 10, 100, 10));
		JLabel title = new JLabel("How to Play");
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 80));
		title.setForeground(third);
		JLabel Rule1 = new JLabel("1) Press space to move your character up");
		Rule1.setForeground(fourth);
		Rule1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
		JLabel Rule2 = new JLabel("2) Avoid incoming obstacles");
		Rule2.setForeground(fourth);
		Rule2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
		JLabel Rule3 = new JLabel("3) Collect coins to buy cool items in the shop");
		Rule3.setForeground(fourth);
		Rule3.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
		JLabel Rule4 = new JLabel("4) Have fun! or else.");
		Rule4.setForeground(fourth);
		Rule4.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
		JButton back = new JButton();
		back.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 35));
		back.setText("Home");
		back.setBackground(third);
		back.setForeground(fourth);
		back.setAlignmentX(Component.CENTER_ALIGNMENT);
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(start);
				frame.revalidate();
			}
		});
		rules.add(title);
		rules.add(Rule1);
		rules.add(Rule2);
		rules.add(Rule3);
		rules.add(Rule4);
		rules.add(back);
		rules.add(Box.createVerticalGlue());
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		Rule1.setAlignmentX(Component.CENTER_ALIGNMENT);
		Rule2.setAlignmentX(Component.CENTER_ALIGNMENT);
		Rule3.setAlignmentX(Component.CENTER_ALIGNMENT);
		Rule4.setAlignmentX(Component.CENTER_ALIGNMENT);
		back.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	public void makeLeader() {
		String leader1 = highscoreName + " - " + highscore;

		leader = new JPanel();
		leader.setVisible(true);
		leader.setBackground(first);
		leader.setLayout(new BoxLayout(leader, BoxLayout.Y_AXIS));
		leader.setBorder(BorderFactory.createEmptyBorder(100, 10, 100, 10));
		JLabel title = new JLabel("Leaderboard");
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 80));
		title.setForeground(third);
		JLabel Lead1 = new JLabel("1) " + leader1);
		Lead1.setForeground(fourth);
		Lead1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 50));
		JButton back = new JButton();
		back.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 35));
		back.setText("Home");
		back.setBackground(third);
		back.setForeground(fourth);
		back.setAlignmentX(Component.CENTER_ALIGNMENT);
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(start);
				frame.revalidate();
			}
		});
		leader.add(title);
		leader.add(Box.createVerticalGlue());
		leader.add(Lead1);
		leader.add(Box.createVerticalGlue());
		leader.add(back);
		leader.add(Box.createVerticalGlue());
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		Lead1.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	public void makeEnterLeader() {
		enterLeader = new JPanel();
		enterLeader.setBackground(first);
		enterLeader.setBorder(BorderFactory.createEmptyBorder(200, 400, 200, 400));
		enterLeader.setLayout(new BoxLayout(enterLeader, BoxLayout.Y_AXIS));
		JLabel title = new JLabel("You made the Leaderboard!");
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
		title.setForeground(third);
		JTextField textArea = new JTextField();
		textArea.setText("        ");
		textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
		textArea.setBackground(third);
		textArea.setForeground(new Color(250,250,250));
		textArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		JButton submit = new JButton();
		submit.setText("Submit");
		submit.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 50));
		submit.setBackground(third);
		//submit.setForeground(new Color(250,250,250));
		enterLeader.add(Box.createVerticalGlue());
		enterLeader.add(title);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		enterLeader.add(Box.createRigidArea(new Dimension(0, 100)));
		enterLeader.add(Box.createRigidArea(new Dimension(0, 50)));
		enterLeader.add(textArea);
		enterLeader.add(Box.createRigidArea(new Dimension(0, 50)));
		enterLeader.add(submit);
		enterLeader.add(Box.createRigidArea(new Dimension(0, 50)));
		textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
		enterLeader.add(Box.createVerticalGlue());
		submit.setAlignmentX(Component.CENTER_ALIGNMENT);
		enterLeader.add(Box.createVerticalGlue());
		submit.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			highscoreName = textArea.getText().trim();
			updateXML();
			frame.setContentPane(end);
			frame.revalidate();			
		}
		});
	}

	public void makeStore() {
		store = new JPanel();
		store.setBackground(first);
		store.setLayout(new BoxLayout(store, BoxLayout.Y_AXIS));
		store.setBorder(BorderFactory.createEmptyBorder(100, 10, 100, 10));
		JLabel title = new JLabel("Welcome to the Store!");
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 80));
		title.setForeground(third);
		store.add(title);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel changeContainer = new JPanel();
		changeContainer.setLayout(new BoxLayout(changeContainer, BoxLayout.PAGE_AXIS));
		JButton transfer = new JButton();
		transfer.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 35));
		transfer.setText("Backgrounds");
		transfer.setBackground(third);
		transfer.setForeground(fourth);
		transfer.setAlignmentX(Component.CENTER_ALIGNMENT);
		transfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeBackgroundStore();
				frame.setContentPane(store);
				frame.revalidate();
			}
		});
		changeContainer.add(transfer);
		store.add(changeContainer);
		JPanel gridPane = new JPanel();
		gridPane.setLayout(new GridLayout(3, 2, 30, 30));
		playerButtons = new JButton[6];
		for (int i = 0; i < 6; i++) {
			gridPane.add(makePlayerLabel(i));
		}
		// gridPane.add(Box.createVerticalGlue());
		store.add(gridPane);
		JPanel backContainer = new JPanel();
		backContainer.setLayout(new BoxLayout(backContainer, BoxLayout.PAGE_AXIS));
		JLabel coinAmnts = new JLabel("Coins: $" + coins);
		coinAmnts.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
		coinAmnts.setAlignmentX(Component.CENTER_ALIGNMENT);
		backContainer.add(Box.createHorizontalGlue());
		backContainer.add(coinAmnts);
		backContainer.add(back);
		backContainer.add(Box.createHorizontalGlue());
		backContainer.add(Box.createVerticalGlue());

		store.add(backContainer);
	}

	private JPanel makePlayerLabel(int index) {
		JPanel Player = new JPanel();
		Player.setLayout(new BoxLayout(Player, BoxLayout.Y_AXIS));
		JLabel player = new JLabel(new ImageIcon(playerVisuals.get(index)));
		player.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel playerName = new JLabel(playerNames[index]);
		playerName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 35));
		playerName.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel playerCost = new JLabel("Cost: $" + playerPrices[index]);
		playerCost.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		playerCost.setAlignmentX(Component.CENTER_ALIGNMENT);
		playerButtons[index] = new JButton();
		playerButtons[index].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
		if (playerUnlocks[index] == 2) {
			playerButtons[index].setText("Selected");
			playerButtons[index].setBackground(third);
			playerButtons[index].setForeground(fourth);
		} else if (playerUnlocks[index] == 1) {
			playerButtons[index].setText("Select");
			playerButtons[index].setBackground(second);
			playerButtons[index].setForeground(fourth);
		} else if (playerUnlocks[index] == 0) {
			playerButtons[index].setText("Purchase");
			playerButtons[index].setBackground(fifth);
			playerButtons[index].setForeground(second);
		}
		playerButtons[index].setAlignmentX(Component.CENTER_ALIGNMENT);
		playerButtons[index].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (playerUnlocks[index] == 2) {

				} else if (playerUnlocks[index] == 1) {
					for (int i = 0; i < playerUnlocks.length; i++) {
						if (playerUnlocks[i] == 2) {
							playerUnlocks[i] = 1;
						}
					}
					playerUnlocks[index] = 2;
					makeStore();
					frame.setContentPane(store);
					frame.revalidate();
				} else if (playerUnlocks[index] == 0) {
					if (coins >= playerPrices[index]) {
						coins -= playerPrices[index];
						playerUnlocks[index] = 1;
					}
				}
				updateXML();
				makeStore();
				frame.setContentPane(store);
				frame.revalidate();
			}
		});
		Player.add(Box.createVerticalGlue());
		Player.add(player);
		Player.add(playerName);
		if (playerUnlocks[index] == 0) {
			Player.add(playerCost);
		}
		Player.add(playerButtons[index]);
		return Player;
	}
	public void makeBackgroundStore() {
		store = new JPanel();
		store.setBackground(first);
		store.setLayout(new BoxLayout(store, BoxLayout.Y_AXIS));
		store.setBorder(BorderFactory.createEmptyBorder(100, 10, 100, 10));
		JLabel title = new JLabel("Welcome to the Store!");
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 80));
		title.setForeground(third);
		store.add(title);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel changeContainer = new JPanel();
		changeContainer.setLayout(new BoxLayout(changeContainer, BoxLayout.PAGE_AXIS));
		JButton transfer = new JButton();
		transfer.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 35));
		transfer.setText("Characters");
		transfer.setBackground(third);
		transfer.setForeground(fourth);
		transfer.setAlignmentX(Component.CENTER_ALIGNMENT);
		transfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeStore();
				frame.setContentPane(store);
				frame.revalidate();
			}
		});
		changeContainer.add(transfer);
		store.add(changeContainer);
		JPanel gridPane = new JPanel();
		gridPane.setLayout(new GridLayout(3, 2, 30, 30));
		for (int i = 0; i < 6; i++) {
			gridPane.add(makeBackgroundLabel(i));
		}
		// gridPane.add(Box.createVerticalGlue());
		store.add(gridPane);
		JPanel backContainer = new JPanel();
		backContainer.setLayout(new BoxLayout(backContainer, BoxLayout.PAGE_AXIS));
		JLabel coinAmnts = new JLabel("Coins: $" + coins);
		coinAmnts.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
		coinAmnts.setAlignmentX(Component.CENTER_ALIGNMENT);
		backContainer.add(Box.createHorizontalGlue());
		backContainer.add(coinAmnts);
//		JButton back = new JButton();
//		back.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 35));
//		back.setText("Home");
//		back.setBackground(third);
//		back.setForeground(fourth);
//		back.setAlignmentX(Component.CENTER_ALIGNMENT);
//		back.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				frame.setContentPane(start);
//				frame.revalidate();
//			}
//		});
		backContainer.add(back);
		backContainer.add(Box.createHorizontalGlue());
		backContainer.add(Box.createVerticalGlue());

		store.add(backContainer);
	}
	public JPanel makeBackgroundLabel(int index) {
		JPanel Background = new JPanel();
		Background.setLayout(new BoxLayout(Background, BoxLayout.Y_AXIS));
		JLabel background = new JLabel();
		background.setIcon(backgroundVisuals.get(index));
		background.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel backgroundName = new JLabel(backgroundNames[index]);
		backgroundName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 35));
		backgroundName.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel backgroundCost = new JLabel("Cost: $" + backgroundPrices[index]);
		backgroundCost.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		backgroundCost.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton BackgroundButton = new JButton();
		BackgroundButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
		if (backgroundUnlocks[index] == 2) {
			BackgroundButton.setText("Selected");
			BackgroundButton.setBackground(third);
			BackgroundButton.setForeground(fourth);
		} else if (backgroundUnlocks[index] == 1) {
			BackgroundButton.setText("Select");
			BackgroundButton.setBackground(second);
			BackgroundButton.setForeground(fourth);
		} else if (backgroundUnlocks[index] == 0) {
			BackgroundButton.setText("Purchase");
			BackgroundButton.setBackground(fifth);
			BackgroundButton.setForeground(second);
		}
		BackgroundButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		BackgroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (backgroundUnlocks[index] == 2) {

				} else if (backgroundUnlocks[index] == 1) {
					for (int i = 0; i < backgroundUnlocks.length; i++) {
						if (backgroundUnlocks[i] == 2) {
							backgroundUnlocks[i] = 1;
						}
					}
					backgroundUnlocks[index] = 2;
				} else if (backgroundUnlocks[index] == 0) {
					if (coins >= backgroundPrices[index]) {
						coins -= backgroundPrices[index];
						backgroundUnlocks[index] = 1;
					}
				}
				updateXML();
				makeBackgroundStore();
				frame.setContentPane(store);
				frame.revalidate();
			}
		});
		Background.add(Box.createVerticalGlue());
		Background.add(background);
		Background.add(backgroundName);
		if (backgroundUnlocks[index] == 0) {
			Background.add(backgroundCost);
		}
		Background.add(BackgroundButton);
		return Background;

	}

	public void makeDifficulty() {
		difficulty.setBackground(first);
		difficulty.setLayout(new BoxLayout(difficulty, BoxLayout.Y_AXIS));
		difficulty.setBorder(BorderFactory.createEmptyBorder(100, 10, 100, 10));
		JLabel title = new JLabel("Please Select Your Difficulty!");
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 80));
		title.setForeground(third);
		JButton difficultyButton1 = new JButton();
		difficultyButton1.setText("Easy");
		difficultyButton1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 50));
		difficultyButton1.setBackground(third);
		difficultyButton1.setForeground(new Color(250, 250, 250));
		JButton difficultyButton2 = new JButton();
		difficultyButton2.setText("Medium");
		difficultyButton2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 50));
		difficultyButton2.setBackground(third);
		difficultyButton2.setForeground(new Color(250, 250, 250));
		difficulty.add(Box.createVerticalGlue());
		JButton difficultyButton3 = new JButton();
		difficultyButton3.setText("Hard");
		difficultyButton3.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 50));
		difficultyButton3.setBackground(third);
		difficultyButton3.setForeground(new Color(250, 250, 250));
		difficulty.add(Box.createVerticalGlue());
		difficulty.add(title);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		difficulty.add(Box.createRigidArea(new Dimension(0, 50)));
		difficulty.add(difficultyButton1);
		difficulty.add(Box.createRigidArea(new Dimension(0, 50)));
		difficulty.add(difficultyButton2);
		difficulty.add(Box.createRigidArea(new Dimension(0, 50)));
		difficulty.add(difficultyButton3);
		difficulty.add(Box.createRigidArea(new Dimension(0, 50)));
		difficultyButton1.setAlignmentX(Component.CENTER_ALIGNMENT);
		difficulty.add(Box.createVerticalGlue());
		difficultyButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// DO THE ACTION THAT YOU WANT
				System.out.println("Easy Clicked");
			}
		});
		difficultyButton2.setAlignmentX(Component.CENTER_ALIGNMENT);
		difficulty.add(Box.createVerticalGlue());
		difficultyButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// DO THE ACTION THAT YOU WANT
				System.out.println("Medium Clicked");
			}
		});
		difficultyButton3.setAlignmentX(Component.CENTER_ALIGNMENT);
		difficulty.add(Box.createVerticalGlue());
		difficultyButton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// DO THE ACTION THAT YOU WANT
				System.out.println("Hard Clicked");
			}
		});
	}

	public void makeEnd() {
		end = new JPanel();
		end.setBackground(first);
		end.setLayout(new BoxLayout(end, BoxLayout.Y_AXIS));
		end.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 10));
		JLabel title = new JLabel("You Died!");
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 100));
		title.setForeground(third);
		JButton endButton = new JButton();
		endButton.setText("Home");
		endButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 50));
		endButton.setBackground(third);
		endButton.setForeground(new Color(250, 250, 250));
		JButton Rules = new JButton();
		Rules.setText("Play Again");
		Rules.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 50));
		Rules.setBackground(third);
		Rules.setForeground(new Color(250, 250, 250));
		end.add(Box.createVerticalGlue());
		end.add(title);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		end.add(Box.createRigidArea(new Dimension(0, 100)));
		end.add(Box.createRigidArea(new Dimension(0, 50)));
		end.add(endButton);
		end.add(Box.createRigidArea(new Dimension(0, 50)));
		end.add(Rules);
		end.add(Box.createRigidArea(new Dimension(0, 50)));
		endButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		end.add(Box.createVerticalGlue());
		endButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currPage = Page.HOME;
				frame.setContentPane(start);
				frame.revalidate();
			}
		});
		Rules.setAlignmentX(Component.CENTER_ALIGNMENT);
		end.add(Box.createVerticalGlue());
		Rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startGame();
			}
		});
	}

	public void startGame() {
		game = new Field(getPlayerIndex(), getBackgroundIndex());
		frame.setContentPane(game.getVisual());
		game.getVisual().setVisible(true);
		game.runGame();
		frame.revalidate();
		game.getVisual().setFocusable(true);
		game.getVisual().requestFocus();
		// This checks to see whether the game is over
		// I choose an asynchronous approach to this since it
		// was easiest given a timer being in use
		progress = new javax.swing.Timer(5000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (game.returnStatus()) {
					int[] results = game.returnValues();
					if (results[0] > highscore) {
						makeEnterLeader();
						frame.setContentPane(enterLeader);
						highscore = results[0];
					} else {
						frame.setContentPane(end);
					}
					frame.revalidate();
					coins += results[1];
					progress.stop();
					updateXML();
				}
			}
		});
		progress.start();
	}
	private int getPlayerIndex() {
		for(int i = 0; i < playerUnlocks.length; i++) {
			if (playerUnlocks[i] == 2) {
				return i + 1;
			}
		}
		return 1;
	}
	private int getBackgroundIndex() {
		for(int i = 0; i < backgroundUnlocks.length; i++) {
			if (backgroundUnlocks[i] == 2) {
				return i + 1;
			}
		}
		return 1;
	}

	private void readXML() {
		try {
			File fXmlFile = new File("xml\\data.xml");
			if(fXmlFile.exists()) { 
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			NodeList nodes = doc.getElementsByTagName("information");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node info = nodes.item(i);
				if (info.getNodeType() == Node.ELEMENT_NODE) {
					Element information = (Element) info;
					highscore = Integer.parseInt(information.getElementsByTagName("score").item(0).getTextContent());
					highscoreName = information.getElementsByTagName("highscorer").item(0).getTextContent();
					coins = Integer.parseInt(information.getElementsByTagName("coins").item(0).getTextContent());
					for(int j = 0; j < 6; j++) {
						playerUnlocks[j] = Integer.parseInt(information.getElementsByTagName("playerSelections").item(j).getTextContent());
					}
					for(int j = 0; j < backgroundUnlocks.length; j++)
						backgroundUnlocks[j] = Integer.parseInt(information.getElementsByTagName("backgroundSelections").item(j).getTextContent());
					
//					stateNumber = Integer
//							.parseInt(information.getElementsByTagName("stateNumber").item(0).getTextContent());
//					numberStatesKept = Integer
//							.parseInt(information.getElementsByTagName("numberStatesKept").item(0).getTextContent());

				}
			}
			} else {
				highscore = 0;
				coins = 100;
				playerUnlocks[0] = 2;
				backgroundUnlocks[0] = 2;
				for(int i = 1; i < playerUnlocks.length; i ++) {
					playerUnlocks[i] = 0;
					backgroundUnlocks[i] = 0;
				}
				updateXML();
			}

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateXML() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xml = builder.newDocument();

			Element rootElement = xml.createElement("rootElement");
			xml.appendChild(rootElement);

			Element information = xml.createElement("information");
			rootElement.appendChild(information);

			Element scoreAmount = xml.createElement("score");
			scoreAmount.appendChild(xml.createTextNode("" + highscore));
			information.appendChild(scoreAmount);
			
			Element highscorer = xml.createElement("highscorer");
			highscorer.appendChild(xml.createTextNode("" + highscoreName));
			information.appendChild(highscorer);

			Element coinsAmount = xml.createElement("coins");
			coinsAmount.appendChild(xml.createTextNode("" + coins));
			information.appendChild(coinsAmount);
			for(int i = 0; i < playerUnlocks.length; i++) {
			Element playerSelections = xml.createElement("playerSelections");
			playerSelections.appendChild(xml.createTextNode("" + playerUnlocks[i]));
			information.appendChild(playerSelections);
			}
			for(int i = 0; i < backgroundUnlocks.length; i++) {
				Element backgroundSelections = xml.createElement("backgroundSelections");
				backgroundSelections.appendChild(xml.createTextNode("" + backgroundUnlocks[i]));
				information.appendChild(backgroundSelections);
			}

			TransformerFactory create = TransformerFactory.newInstance();
			Transformer transform = create.newTransformer();
			DOMSource source = new DOMSource(xml);
			StreamResult result = new StreamResult(new File("xml\\data.xml"));
			transform.transform(source, result);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}

	}

}