import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

@SuppressWarnings("serial")
public class GameMain extends Canvas implements Runnable {
	/*
	 * TODO: Kullanici giris ve cikis noktalarini secebilecek
	 * TODO: Kullanici istedigi yere engel koyup silebilecek
	 * TODO: Kullanici rastgele labirent olusturtabilecek
	 */
	private Thread thread;
	public static boolean running = false;

	private static MouseInput mouse;

	private static int cellSize = 20;
	private static int borderSize = 1;

	//this is changed from the controls window
	public static int rows, cols;
	private static Window window;
	private static int width, height; //calculated using rows, columns, borderSize and cellSize
	public static Cell[][] grid;

	public GameMain(int row, int col) {
		requestFocus();

		rows = row;
		cols = col;
		width = (borderSize + cellSize) * cols + borderSize;
		height = (borderSize + cellSize) * rows + borderSize;
		grid = new Cell[cols][rows];

		boolean changed = false;
		if (window != null) {
			window.frame.dispose();
			changed = true;
			window = null;
		}

		mouse = new MouseInput();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);

		window = new Window(width, height, "Labyrinth Solver", changed, this);

		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++)
				grid[i][j] = new Cell((borderSize + cellSize) * i + borderSize, (borderSize + cellSize) + (borderSize + cellSize) * (j - 1) + borderSize, cellSize, Cell.EMPTY, mouse);
	}
	
	public static void randomLabyrinth() {
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++)
				grid[i][j].setType(Cell.EMPTY);
		int count = rows * cols / 3;
		while (count-- != 0) {
			int row = (int)(Math.random() * cols);
			int col = (int)(Math.random() * rows);
			while (grid[row][col].getType() != Cell.EMPTY) {
				row = (int)(Math.random() * cols);
				col = (int)(Math.random() * rows);
			}
			grid[row][col].setType(Cell.BARRIER);
		}
	}

	//updates entities in the game
	public void tick() {
		try {
			for (int i = 0; i < grid.length; i++)
				for (int j = 0; j < grid[i].length; j++)
					grid[i][j].tick();
		} catch(NullPointerException e) {
			//this only throws an exception when threads get desynchronized
			//e.g when the user changes row/column numbers
			//so this exception does not matter
		}
	}

	//method for drawing on the screen
	private void render() {
		BufferStrategy bs=this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		//---draw begin---

		//background
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, width, height);

		//render cells
		try {
			for (int i = 0; i < grid.length; i++)
				for (int j = 0; j < grid[i].length; j++)
					if (grid[i][j] != null) 
						grid[i][j].render(g);

		} catch(NullPointerException e) {
			//this only throws an exception when threads get desynchronized
			//e.g when the user changes row/column numbers
			//so this exception does not matter
		}
		
		//render grid
		g.setColor(new Color(200, 200, 200));
		for (int i = 0; i < height; i += (borderSize + cellSize)) 
			g.fillRect(0, i, width, borderSize);
		for (int i = 0; i < width; i += (borderSize + cellSize)) 
			g.fillRect(i, 0, borderSize, height);



		//---draw end---	
		g.dispose();
		bs.show();
	}

	//this game loop and parts of the game engine is taken from this tutorial
	//https://www.youtube.com/watch?v=1gir2R7G9ws&t=1s&ab_channel=RealTutsGML
	public void run(){
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
			}
			if(running) 
				render();

			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
			}
		}
		stop();
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch(InterruptedException e) {}
	}

	public static void main(String[] args) {
		new GameMain(20, 20);	
	}

}