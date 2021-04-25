import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.Test;

public class MyTests {

	//A Brick is properly inserted into the Collection of bricks
	//A Brick is properly removed from the Collection of bricks
	//A MovingBrick is properly inserted into the Collection of bricks
	//A MovingBreak is properly removed from the Collection of bricks
	//A MovingBrick changes its velocity when it hits a wall
	//A MultipleHitBrick is properly inserted into the Collection of bricks
	//A MultipleHitBrick isn't removed when hit once but changes color
	//A MultipleHitBrick isn't removed when hit twice but changes color
	//A MultipleHitBrick is properly removed from the Collection of bricks when hit three times

	@Test public void testBricksAdded() {
		ArrayList<Brick> bricks = new ArrayList<Brick>();
		bricks.add(new Brick(0, 0, 0, 0));
		assertEquals("Brick added to Collection", bricks.size(), 1);
		bricks.add(new MovingBrick(0, 0, 0, 0));
		assertEquals("MovingBrick added to Collection", bricks.size(), 2);
		bricks.add(new MultipleHitBrick(0, 0, 0, 0));
		assertEquals("MultipleHitBrick added to Collection", bricks.size(), 3);
	}

	@Test public void testBricksRemoved() {
		ArrayList<Brick> bricks = new ArrayList<Brick>();
		bricks.add(new Brick(0, 0, 0, 0));
		bricks.add(new MovingBrick(0, 0, 0, 0));
		bricks.add(new MultipleHitBrick(0, 0, 0, 0));
		bricks.remove(0);
		assertEquals("Brick removed from Collection", bricks.size(), 2);
		bricks.remove(0);
		assertEquals("MovingBrick removed from Collection", bricks.size(), 1);
		bricks.remove(0);
		assertEquals("MultipleHitBrick removed from Collection", bricks.size(), 0);
	}

	@Test public void testMultipleHitBrickColorChanges() {
		MultipleHitBrick brick = new MultipleHitBrick(0, 0, 0, 0);
		assertTrue("MultipleHitBrick is Green at 0 hits", brick.getColor().equals(Color.GREEN));
		brick.isHit();
		assertTrue("MultipleHitBrick is Yellow at 1 hits", brick.getColor() == Color.YELLOW);
		brick.isHit();
		assertTrue("MultipleHitBrick is Pink at 2 hits", brick.getColor() == Color.PINK);
	}

	@Test public void testMovingBrickHitsWall() {
		MovingBrick brick = new MovingBrick(0, 0, 0, 0);
		assertTrue("MovingBrick velocity is positive", brick.getVx() > 0);
		brick.bounce(brick.hitWall());
		assertTrue("MovingBrick velocity is negative", brick.getVx() < 0);
	}

	@Test public void testGameClassComponents() {
		JFrame frame = new JFrame("Tests");
		frame.pack();
		frame.setVisible(true);
		assertTrue("Frame is visible", frame.isVisible());
		assertTrue("Frame is packed", frame.isValid());
	}

	@Test public void testGameObjComponents() {
		Brick brick = new Brick(500, 500, 50, 50);
		brick.setPx(-5);
		brick.setPy(1000);
		brick.clip();
		assertTrue("Brick placed properly in limits x", brick.getPx() == 0);
		assertTrue("Brick placed properly in limits y", brick.getPy() == 470);
	}

	@Test public void testIntersects() {
		Brick brick = new Brick(500, 500, 50, 50);
		brick.setPx(-5);
		brick.setPy(1000);
		brick.clip();
		MovingBrick movingBrick = new MovingBrick(500, 500, 50, 50);
		movingBrick.move();
		assertTrue("MovingBrick moves properly", movingBrick.getPx() == 53);
		movingBrick.setPy(500);
		assertTrue("Intersection is correct", brick.intersects(movingBrick));
	}

	@Test public void testWillIntersect() {
		Brick brick = new Brick(500, 500, 101, 50);
		MovingBrick movingBrick = new MovingBrick(500, 500, 0, 50);
		movingBrick.setVx(10);
		assertTrue("Brick and MovingBrick do not initially intersect", !brick.intersects(movingBrick));
		assertTrue("Brick and MovingBrick will intersect", brick.willIntersect(movingBrick));
	}	
}
