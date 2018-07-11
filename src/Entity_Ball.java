import java.awt.Color;
import java.awt.Point;
import java.util.TreeMap;
import java.awt.Shape;
import java.awt.Rectangle;

public class Entity_Ball extends Shape_Ellipse implements Updateable, Collideable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double xVelocity;
	private double yVelocity;
	
	private boolean collisionEnabled = true;
	
	public Entity_Ball() {
		super();
		setColor(Color.BLACK);
	}
	
	public Entity_Ball(Color color) {
		super();
		setColor(color);
	}
	
	public Entity_Ball(double x, double y, int width, int height) {
		super(x, y, width, height);
		setColor(Color.BLACK);
	}
	
	public Entity_Ball(double x, double y, int width, int height, Color color) {
		super(x, y, width, height);
		setColor(color);
	}
	
	public void setVelocity(int x, int y) {
		this.xVelocity = x;
		this.yVelocity = y;
	}
	
	public void setVelocity(double x, double y) {
		this.xVelocity = x;
		this.yVelocity = y;
	}
	
	public void addVelocity(int x, int y) {
		this.xVelocity += x;
		this.yVelocity += y;
	}
	
	public void addVelocity(double x, double y) {
		this.xVelocity += x;
		this.yVelocity += y;
	}
	
	public void update() {
		double dX = xVelocity * Game.instance.timeElapsed;
		double dY = yVelocity * Game.instance.timeElapsed;
		if (dX < 0 && x + dX < 0) {
			dX = -dX;
			xVelocity = -xVelocity;
		} else if (dX > 0 && x + width >= Game.WINDOW_WIDTH) {
			dX = -dX;
			xVelocity = -xVelocity;
		}
		if (dY < 0 && getY() + dY < 0) {
			dY = -dY;
			yVelocity = -yVelocity;
		} else if (dY > 0 && y >= Game.WINDOW_HEIGHT) {
			Game.instance.removeElement(this);
			Game.instance.statTracker.ballsInPlay--;
		}
		if (collisionEnabled) {
			//Cast a Rectangle forward in the direction the ball is heading to check for collision
			Rectangle cast = new Rectangle(getBounds());
			cast.translate((int)dX, (int)dY);
			//Run through each collideable object on-screen and check if the casted Rectangle intersects
			TreeMap<java.lang.Double, Shape> colliders = new TreeMap<java.lang.Double, Shape>();
			for (Shape element : Game.instance.elements) {
				if (element instanceof Collideable && element != this) {
					if (((Collideable) element).isCollisionEnabled()) {
						Rectangle rect = element.getBounds();
						if (cast.intersects(rect)) {
							Point p1 = new Point((int)(getX() + (getWidth() / 2)),(int) (getY() + (getHeight() / 2)));
							Point p2 = new Point((int)(rect.getX() + (rect.getWidth() / 2)), (int)(rect.getY() + (rect.getHeight() / 2)));
							java.lang.Double distance = Math.sqrt((Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2)));
							colliders.put(distance, element);
						}
					}
				}
			}
			for (Shape element : colliders.values()) {
				Rectangle collider = element.getBounds();
				if (cast.intersects(collider)) 
				{
					if ((int)collider.getMinY() + 1 >= (int)getMaxY()) 
					{
						dY = -(collider.getMinY() - getMaxY()) / 2;
						dX = dX * (Math.abs(dY) / Math.abs(yVelocity));
						yVelocity = -(Math.abs(yVelocity));
						cast.setBounds(getBounds());
						cast.translate((int)dX, (int)dY);
					} 
					else if ((int)collider.getMaxY() - 1 <= (int)getMinY()) 
					{
						dY = (collider.getMaxY() - getMinY()) / 2;
						dX = dX * (Math.abs(dY) / Math.abs(yVelocity));
						yVelocity = Math.abs(yVelocity);
						cast.setBounds(getBounds());
						cast.translate((int)dX, (int)dY);
					} 

					if ((int)collider.getMinX() + 1 >= (int)getMaxX()) 
					{
						dX = -(collider.getMinX() - getMaxX()) / 2;
						dY = dY * (Math.abs(dX) / Math.abs(xVelocity));
						xVelocity = -(Math.abs(xVelocity));
						cast.setBounds(getBounds());
						cast.translate((int)dX, (int)dY);
					} 
					else if ((int)collider.getMaxX() - 1 <= (int)getMinX()) 
					{
						dX = (collider.getMaxX() - getMinX()) / 2;
						dY = dY * (Math.abs(dX) / Math.abs(xVelocity));
						xVelocity = Math.abs(xVelocity);
						cast.setBounds(getBounds());
						cast.translate((int)dX, (int)dY);
					}
					if (element instanceof Damageable) {
						((Damageable)element).damage(5);
					}
					if (element instanceof Entity_Paddle) {
						addVelocity(xVelocity * Game.BALL_ACCELERATION, yVelocity * Game.BALL_ACCELERATION);
						((Entity_Paddle)element).accelerate();
						Game.instance.statTracker.resetCombo();
					}
				}
			}
		}
		translate(dX, dY);
	}
	
	public void translate(double x, double y) {
		this.x += x;
		this.y += y;
	}
	
	public boolean isCollisionEnabled() {
		return collisionEnabled;
	}
	
	public void setCollisionEnabled(boolean collisionEnabled) {
		this.collisionEnabled = collisionEnabled;
	}
}
