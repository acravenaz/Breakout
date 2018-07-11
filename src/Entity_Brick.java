import java.awt.Color;

public class Entity_Brick extends Shape_Rectangle implements Damageable, Updateable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean damageable = true;
	private int maxHealth = 1;
	private int health = 1;
	
	public Entity_Brick() {
		super();
		updateColor();
	}
	
	public Entity_Brick(int x, int y) {
		super(x, y);
		updateColor();
	}
	
	public Entity_Brick(int x, int y, int width, int height) {
		super(x, y, width, height);
		updateColor();
	}
	
	public Entity_Brick(int maxHealth) {
		super();
		setMaxHealth(maxHealth);
		updateColor();
	}
	
	public Entity_Brick(int x, int y, int maxHealth) {
		super(x, y);
		setMaxHealth(maxHealth);
		updateColor();
	}
	
	public Entity_Brick(int x, int y, int width, int height, int maxHealth) {
		super(x, y, width, height);
		setMaxHealth(maxHealth);
		updateColor();
	}
	
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
		health = maxHealth;
		updateColor();
	}
	
	public void damage(int damage) {
		if (damageable) {
			health -= damage;
			if (health > maxHealth) {
				health = maxHealth;
			}
			updateColor();
			Game.instance.statTracker.incrementScore(1);
		}
	}
	
	public void updateColor() {
		switch(health) {
		case 0:
			setColor(Color.CYAN);
			break;
		case 5:
			setColor(Color.CYAN);
			break;
		case 10:
			setColor(Color.GREEN);
			break;
		case 15:
			setColor(Color.YELLOW);
			break;
		case 20:
			setColor(Color.ORANGE);
			break;
		case 25:
			setColor(Color.RED);
			break;
		default:
			setColor(Color.BLACK);
			break;
		}
	}
	
	public void update() {
		if (health <= 0) {
			destroy();
		}
	}
	
	public void destroy() {
		Game.instance.removeElement(this);
		Game.instance.brickTracker.subtractBrick();
	}

	public boolean isDamageable() {
		return damageable;
	}

	public void setDamageable(boolean damageable) {
		this.damageable = damageable;
	}
	
}
