
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game implements Runnable {
	public void run() {
		// Frame
		final JFrame frame = new JFrame("ATARI BREAKOUT");
		frame.setLocation(200, 200);

		// Status panel
		final JPanel status_panel = new JPanel();
		frame.add(status_panel, BorderLayout.NORTH);
		final JLabel status = new JLabel("Running...");
		status_panel.add(status);

		// Main playing area
		final GameCourt court = new GameCourt(status, frame);
		frame.add(court, BorderLayout.CENTER);

		// Reset button
		final JPanel control_panel = new JPanel();
		frame.add(control_panel, BorderLayout.SOUTH);
		final JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.reset();
			}
		});
		control_panel.add(reset);

		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Start game
		court.reset();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}