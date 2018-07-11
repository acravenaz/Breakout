import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.awt.Point;

public class Entity_Paddle extends Shape_Rectangle implements Updateable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double speed = 0;
	
	private ArrayList<Shape> elements;
	private Point mousePos;
	
	public Entity_Paddle(ArrayList<Shape> elements, Point mousePos) {
		super();
		setColor(Color.BLACK);
		this.elements = elements;
		this.mousePos = mousePos;
	}
	
	public Entity_Paddle(int x, int y, ArrayList<Shape> elements, Point mousePos) {
		super(x, y);
		setColor(Color.BLACK);
		this.elements = elements;
		this.mousePos = mousePos;
	}
	
	public Entity_Paddle(int x, int y, int width, int height,ArrayList<Shape> elements, Point mousePos) {
		super(x, y, width, height);
		setColor(Color.BLACK);
		this.elements = elements;
		this.mousePos = mousePos;
	}
	
	public Entity_Paddle(ArrayList<Shape> elements, Point mousePos, Color color) {
		super();
		setColor(color);
		this.elements = elements;
		this.mousePos = mousePos;
	}
	
	public Entity_Paddle(int x, int y, ArrayList<Shape> elements, Point mousePos, Color color) {
		super(x, y);
		setColor(color);
		this.elements = elements;
		this.mousePos = mousePos;
	}
	
	public Entity_Paddle(int x, int y, int width, int height, ArrayList<Shape> elements, Point mousePos, Color color) {
		super(x, y, width, height);
		setColor(color);
		this.elements = elements;
		this.mousePos = mousePos;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void accelerate() {
		setSpeed(speed + (speed * Game.PADDLE_ACCELERATION));
	}
	
	public void update() {
		double dX;
		double center = (getX() + (getWidth() / 2));
		if (mousePos.getX() > center) {
			dX = mousePos.getX() - center;
			assert dX > 0;
			if (dX > speed * Game.instance.timeElapsed) {
				dX = speed * Game.instance.timeElapsed;
			}
		} else if (mousePos.getX() < center) {
			dX = mousePos.getX() - center;
			assert dX < 0;
			if (-dX > speed * Game.instance.timeElapsed) {
				dX = -speed * Game.instance.timeElapsed;
			}
		} else {
			dX = 0;
		}
		if (dX < 0 && getX() + dX < 0) {
			dX = -getX();
		} else if (dX > 0 && getMaxX() + dX >= Game.WINDOW_WIDTH) {
			dX = -(getMaxX() - Game.WINDOW_WIDTH);
		}
		
		Rectangle cast = new Rectangle(getBounds());
		cast.translate((int)dX, 0);
		
		for (Shape element : elements) {
			Rectangle collider = element.getBounds();
			if (cast.intersects(collider) && element != this) {
				if ((int)collider.getMinX() + 1 >= (int)getMaxX()) {
					dX = -(collider.getMinX() - getMaxX()) / 2;
				} else if ((int)collider.getMaxX() - 1 <= (int)getMinX()) {
					dX = (collider.getMaxX() - getMinX()) / 2;
				}
			}
		}
		translate((int)dX, 0);
	}
	
	public void setCollisionArray(ArrayList<Shape> elements) {
		this.elements = elements;
	}
	
}
