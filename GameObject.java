
import java.awt.Graphics;

public abstract class GameObject  {
	
	protected int x, y;
	
	public GameObject(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public int getX() { return x; }
	public int getY() { return y; }

}