
import java.awt.*;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.*;

@SuppressWarnings("serial")
public class GameCourt extends JPanel {
	private ArrayList<Brick> bricks = new ArrayList<Brick>();

	private Circle circle;
	private Slider slider;
	public boolean playing = false; 
	private JLabel status;
	private JFrame frame;

	public static final int COURT_WIDTH = 600;
	public static final int COURT_HEIGHT = 500;
	public static final int CIRCLE_VELOCITY = 20;
	public static final int SLIDER_VELOCITY = 8;
	public static final int INTERVAL = 15;

	private int score = 0;

	public GameCourt(JLabel status, JFrame f) {
		frame = f;
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					tick();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		timer.start();

		setFocusable(true);

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					slider.setVx(-SLIDER_VELOCITY);
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					slider.setVx(SLIDER_VELOCITY);
				}
			}

			public void keyReleased(KeyEvent e) {
				slider.setVx(0);
				slider.setVy(0);
			}
		});

		this.status = status;
	}

	/**
	 * (Re-)set the game to its initial state.
	 */
	public void reset() {
		slider = new Slider(COURT_WIDTH, COURT_HEIGHT, Color.BLACK, 80);
		circle = new Circle(COURT_WIDTH, COURT_HEIGHT, Color.CYAN);

		bricks = new ArrayList<Brick>();
		bricks.add(new MultipleHitBrick(COURT_WIDTH, COURT_HEIGHT, 40, 40));
		bricks.add(new MultipleHitBrick(COURT_WIDTH, COURT_HEIGHT, 180, 40));
		bricks.add(new MultipleHitBrick(COURT_WIDTH, COURT_HEIGHT, 320, 40));
		bricks.add(new MultipleHitBrick(COURT_WIDTH, COURT_HEIGHT, 460, 40));

		bricks.add(new MovingBrick(COURT_WIDTH, COURT_HEIGHT, 0, 90));

		bricks.add(new Brick(COURT_WIDTH, COURT_HEIGHT, 40, 140));
		bricks.add(new MultipleHitBrick(COURT_WIDTH, COURT_HEIGHT, 180, 140));
		bricks.add(new MultipleHitBrick(COURT_WIDTH, COURT_HEIGHT, 320, 140));
		bricks.add(new Brick(COURT_WIDTH, COURT_HEIGHT, 460, 140));

		bricks.add(new MovingBrick(COURT_WIDTH, COURT_HEIGHT, 1000, 190));

		bricks.add(new Brick(COURT_WIDTH, COURT_HEIGHT, 40, 240));
		bricks.add(new Brick(COURT_WIDTH, COURT_HEIGHT, 180, 240));
		bricks.add(new Brick(COURT_WIDTH, COURT_HEIGHT, 320, 240));
		bricks.add(new Brick(COURT_WIDTH, COURT_HEIGHT, 460, 240));

		score = 0;
		status.setText("SCORE: 0");
		requestFocusInWindow();
		playing = false;

		// Shows the instructions
		String instructions = "ATARI BREAKOUT: \nJason Kim - CIS120 \n \nINSTRUCTIONS: \nMove the slider "
				+ "with the left and right keys. Bounce the ball and hit the bricks to remove them and get points!"
				+ "\nSome of the bricks move left and right; others need to be hit multiple times. Your goal is to "
				+ "hit all the bricks and get on the high score list.";
		JOptionPane.showMessageDialog(frame, instructions);

		playing = true;
	}

	/**
	 * This method is called every time the timer defined in the constructor triggers.
	 * @throws IOException 
	 */
	void tick() throws IOException {
		if (playing) {
			slider.move();
			circle.move();

			for (Brick b : bricks) {
				if (b instanceof MovingBrick) {
					((MovingBrick) b).shift();
				}
			}

			if (circle.getPy() + circle.getVy() > circle.getMaxY()) {
				playing = false;
				status.setText("GAME OVER! FINAL SCORE: " + score);

				checkHighScore();
				return;
			} 

			circle.bounce(circle.hitWall());

			if (circle.willIntersect(slider)) {
				if (score > 0) {
					score = score - 10;
				}
			}
			circle.bounce(circle.hitObj(slider));

			for (int a = 0; a < bricks.size(); a++) {
				if (circle.willIntersect(bricks.get(a))) {
					circle.bounce(circle.hitObj(bricks.get(a)));
					score = score + 50;

					if (bricks.get(a) instanceof MultipleHitBrick) {
						((MultipleHitBrick) bricks.get(a)).isHit();
						if (((MultipleHitBrick) bricks.get(a)).getHits() == 0) {
							bricks.remove(a);
							if (bricks.size() == 0) {
								break;
							}
						}
					}
					else {
						bricks.remove(a);
						if (bricks.size() == 0) {
							break;
						}
					}
				}
				else {
					bricks.get(a).move();
				}
			}

			// check for the game end conditions
			if (bricks.size() == 0) {
				playing = false;
				status.setText("YOU WIN! FINAL SCORE: " + score);
				checkHighScore();
				return;
			}

			// update the display
			status.setText("SCORE: " + score);

			repaint();
		}
	}

	private void checkHighScore() throws IOException {
		TreeMap<String, Integer> highscores = new TreeMap<String, Integer>();
		Reader r = new FileReader("highscores.txt");
		BufferedReader reader = new BufferedReader(r);
		String line = reader.readLine();
		while (line != null) {
			String[] split = line.split(" ");
			highscores.put(split[0], Integer.valueOf(split[1]));
			line = reader.readLine();
		}

		Collection<String> highscoreNames = highscores.keySet();
		boolean getsOnBoard = false;
		String keyToRemove = "";
		for (String s : highscoreNames) {
			if (getsOnBoard) {
				if (highscores.get(keyToRemove) >= highscores.get(s)) {
					keyToRemove = s;
				}
			}
			else if (score > highscores.get(s)) {
				getsOnBoard = true;
				keyToRemove = s;

				break;
			}
		}

		if (getsOnBoard) {
			String name = JOptionPane.showInputDialog("NEW HIGH SCORE! \nWhat is your name? No spaces please.");
			while (name == null || highscores.containsKey(name)) {
				name = JOptionPane.showInputDialog("Name is already taken. What is your name? No spaces please.");
			}
			while (name == null || name.contains(" ") || highscores.containsKey(name) || name.equals("")) {
				name = JOptionPane.showInputDialog("Invalid name. What is your name? No spaces please.");	
			}
			highscores.remove(keyToRemove);
			highscores.put(name, score);
		}

		ArrayList<String> keys = new ArrayList<String>();
		Set<String> temp = highscores.keySet();
		for (String s : temp) {
			keys.add(s);
		}
		String[][] scoresOrdered = new String[10][2];		

		for (int x = 0; x < scoresOrdered.length; x++) {
			String highestKey = keys.get(0);
			for (int a = 0; a < keys.size(); a++) {
				if (highscores.get(keys.get(a)) > highscores.get(highestKey)) {
					highestKey = keys.get(a);
				}
			}
			scoresOrdered[x][0] = highestKey;
			scoresOrdered[x][1] = String.valueOf(highscores.get(highestKey));
			keys.remove(highestKey);
		}

		String output = "HIGH SCORES: \n";
		for (int a = 0; a < scoresOrdered.length; a++) {
			output = output + "\n" + scoresOrdered[a][1] + " | " + scoresOrdered[a][0];
		}

		JOptionPane.showMessageDialog(frame, output);

		FileWriter writer = new FileWriter("highscores.txt");		
		output = "";
		for (int a = 0; a < scoresOrdered.length; a++) {
			output = output + scoresOrdered[a][0] + " " + scoresOrdered[a][1] + "\n";
		}

		writer.write(output);
		writer.close();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		slider.draw(g);
		circle.draw(g);

		for (Brick b : bricks) {
			b.draw(g);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}

	public static int getCourtWidth() {
		return COURT_WIDTH;
	}

	public static int getCourtHeight() {
		return COURT_HEIGHT;
	}

	public Graphics getGraphic() {
		return this.getGraphic();
	}
}