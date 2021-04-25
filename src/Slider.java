import java.awt.Color;
import java.awt.Graphics;

public class Slider extends GameObj {
	public static int xLength;
	public static final int Y_LENGTH = 10;
	public static final int INIT_POS_X = GameCourt.getCourtWidth() / 2;
	public static final int INIT_POS_Y = GameCourt.getCourtHeight() - 50;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	private Color color;

	public Slider(int courtWidth, int courtHeight, Color color, int xLength) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, xLength, Y_LENGTH, courtWidth, courtHeight);
		this.color = color;
		this.xLength = xLength;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(this.color);
		g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
	}
}
