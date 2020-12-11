import java.awt.Color;
import java.awt.Graphics;

public class Cell extends GameObject {
	
	public static final int EMPTY = 0;
	public static final int BARRIER = 1;
	public static final int ENTRANCE = 2;
	public static final int EXIT = 3;
	
	private int size;
	private int type;
	
	private MouseInput mouse;
	private int mouseX, mouseY;
	
	private long pressCooldown = 0L;

	public Cell(int x, int y, int size, int type, MouseInput mouse) {
		super(x, y);
		this.size = size;
		this.type = type;
		this.mouse = mouse;
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
						if (type == EMPTY)
							type = GameControlsWindow.selected_type;
						else
							type = EMPTY;
					}
				}
			}
		}
	}

	public void render(Graphics g) {
		switch (type) {
			case EMPTY:
				g.setColor(Color.white);
				break;
			case BARRIER:
				g.setColor(Color.black);
				break;
			case ENTRANCE:
				g.setColor(Color.blue);
				break;
			case EXIT:
				g.setColor(Color.green);
				break;
		}
		g.fillRect(x, y, size, size);
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

}
