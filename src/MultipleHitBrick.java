import java.awt.Color;
import java.awt.Graphics;

public class MultipleHitBrick extends Brick {
	public static final int X_LENGTH = 100;
	public static final int Y_LENGTH = 30;
	public static int px;
	public static int py;
	public static int vx;
	public static int vy;
	private Color color;
	private int hits = 3;

	public MultipleHitBrick(int courtWidth, int courtHeight, int positionX, int positionY) {
		super(courtWidth, courtHeight, positionX, positionY);
		px = positionX;
		py = positionY;
		setVx(vx);
		color = Color.GREEN;
	}

	public int getHits() {
		return hits;
	}

	public void isHit() {
		hits = hits - 1;
		if (hits == 2) {
			color = Color.YELLOW;
		}
		else {
			color = Color.PINK;
		}
	}

	public Color getColor() {
		return color;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
	}
}