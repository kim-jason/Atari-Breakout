import java.awt.Color;
import java.awt.Graphics;

public class MovingBrick extends Brick {
	public static final int X_LENGTH = 100;
	public static final int Y_LENGTH = 30;
	public static int px;
	public static int py;
	public static int vx = 3;
	public static int vy;
	private Color color;

	public MovingBrick(int courtWidth, int courtHeight, int positionX, int positionY) {
		super(courtWidth, courtHeight, positionX, positionY);
		px = positionX;
		py = positionY;
		setVx(vx);
		color = Color.RED;
	}

	public void shift() {
		this.bounce(this.hitWall());
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(this.color);
		g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
	}
}
