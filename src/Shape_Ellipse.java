import java.awt.geom.Ellipse2D;
import java.awt.Color;

public class Shape_Ellipse extends Ellipse2D.Double implements Colorable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Color color;
	
	public Shape_Ellipse() {
		super();
		color = Color.BLACK;
	}
	
	public Shape_Ellipse(Color color) {
		super();
		this.color = color;
	}
	
	public Shape_Ellipse(double x, double y, int width, int height) {
		super(x, y, width, height);
		color = Color.BLACK;
	}
	
	public Shape_Ellipse(double x, double y, int width, int height, Color color) {
		super(x, y, width, height);
		this.color = color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

}
