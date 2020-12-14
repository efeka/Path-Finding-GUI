import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class Cell extends GameObject {
	
	public static final int EMPTY = 0;
	public static final int BARRIER = 1;
	public static final int START = 2;
	public static final int EXIT = 3;
	public static final int HIGHLIGHT = 4; 
	
	private int size;
	private int type;
	
	private MouseInput mouse;
	private int mouseX, mouseY;
	
	public static Cell start;
	
	private long pressCooldown = 0L;

	public Cell(int x, int y, int i, int j, int size, int type, MouseInput mouse) {
		super(x, y);
		this.size = size;
		this.type = type;
		this.mouse = mouse;
		start = null;
	}

	public void tick() {
		if (mouse.getPressed()) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - pressCooldown >= 200) {
				pressCooldown = currentTime;
				this.mouseX = mouse.getX();
				this.mouseY = mouse.getY();

				if (x <= mouseX && x + size >= mouseX) {
					if (y <= mouseY && y + size >= mouseY) {
						clearHighlights();
						if (type == EMPTY || type == HIGHLIGHT)
							type = GameControlsWindow.selected_type;
						else
							type = EMPTY;
						if (GameControlsWindow.selected_type == START) {
							type = START;
							if (start == null)
								start = this;
							else {
								start.setType(EMPTY);
								type = START;
								start = this;
							}
						}
					}
				}
			}
		}
	}
	
	private void clearHighlights() {
		for (int i = 0; i < GameMain.grid.length; i++) {
			for (int j = 0; j < GameMain.grid[i].length; j++) {
				if (GameMain.grid[i][j].getType() == HIGHLIGHT)
					GameMain.grid[i][j].setType(EMPTY);
			}
		}
	}

	public void render(Graphics g) {
		switch (type) {
			case EMPTY:
				g.setColor(Color.white);
				g.fillRect(x, y, size, size);
				break;
			case BARRIER:
				g.setColor(Color.black);
				g.fillRect(x, y, size, size);
				break;
			case START:
				g.setColor(new Color(100, 100, 200));
				g.fillRect(x, y, size, size);
				g.setColor(Color.white);
				g.setFont(new Font("", Font.BOLD, 20));
				g.drawString("S", x + size / 4 - 2, y + size - 3);
				break;
			case EXIT:
				g.setColor(new Color(100, 200, 100));
				g.fillRect(x, y, size, size);
				g.setColor(Color.white);
				g.setFont(new Font("", Font.BOLD, 20));
				g.drawString("E", x + size / 4 - 2, y + size - 3);
				break;
			case HIGHLIGHT:
				g.setColor(new Color(0, 255, 0, 150));
				g.fillRect(x, y, size, size);
				break;
		}
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

}
