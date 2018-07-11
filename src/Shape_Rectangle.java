import java.awt.Color;

import java.awt.Rectangle;

public class Shape_Rectangle extends Rectangle implements Colorable, Collideable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean collisionEnabled = true;
	private Color color;
	
	public Shape_Rectangle() {
		super();
		color = Color.BLACK;
	}
	
	public Shape_Rectangle(int x, int y) {
		super(x, y);
		color = Color.BLACK;
	}
	
	public Shape_Rectangle(int x, int y, int width, int height) {
		super(x, y, width, height);
		color = Color.BLACK;
	}
	
	public Shape_Rectangle(Color color) {
		super();
		this.color = color;
	}
	
	public Shape_Rectangle(int x, int y, Color color) {
		super(x, y);
		this.color = color;
	}
	
	public Shape_Rectangle(int x, int y, int width, int height, Color color) {
		super(x, y, width, height);
		this.color = color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public boolean isCollisionEnabled() {
		return collisionEnabled;
	}
	
	public void setCollisionEnabled(boolean collisionEnabled) {
		this.collisionEnabled = collisionEnabled;
	}

}
