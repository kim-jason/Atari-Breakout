import java.awt.Color;
import java.awt.Graphics;

public class Brick extends GameObj {
	public static final int X_LENGTH = 100;
	public static final int Y_LENGTH = 30;
	public static int px;
	public static int py;
	public static int vx;
	public static int vy;
	private Color color;

	public Brick(int courtWidth, int courtHeight, int positionX, int positionY) {
		super(vx, vy, positionX, positionY, X_LENGTH, Y_LENGTH, courtWidth, courtHeight);
		this.px = positionX;
		this.py = positionY;
		color = Color.BLUE;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(this.color);
		g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
	}
}