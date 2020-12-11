
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window {
	
	JFrame frame;
	GameControlsWindow control;

	public Window(int w, int h, String title, boolean changed, GameMain game) {
		game.setPreferredSize(new Dimension(w, h));
		game.setMaximumSize(new Dimension(w, h));
		game.setMinimumSize(new Dimension(w, h));
		
		frame = new JFrame(title);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		if (!changed)
			control = new GameControlsWindow(frame.getX(), frame.getY(), w, h);
		
		game.start();
	}
}

