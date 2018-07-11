import java.awt.Point;
import java.awt.Color;
import java.awt.Font;

public class UI_Label {

	private int x;
	private int y;
	
	private String text;
	
	private Color color;
	private Font font;
	
	public UI_Label() {
		x = 0;
		y = 0;
		text = "";
		color = Color.BLACK;
		font = new Font("Helvetica", Font.PLAIN, 12);
	}
	
	public UI_Label(int x, int y) {
		this.x = x;
		this.y = y;
		text = "";
		color = Color.BLACK;
		font = new Font("Helvetica", Font.PLAIN, 12);
	}
	
	public UI_Label(String text) {
		x = 0;
		y = 0;
		this.text = text;
		color = Color.BLACK;
		font = new Font("Helvetica", Font.PLAIN, 12);
	}
	
	public UI_Label(String text, int x, int y) {
		this.x = x;
		this.y = y;
		this.text = text;
		color = Color.BLACK;
		font = new Font("Helvetica", Font.PLAIN, 12);
	}
	
	public UI_Label(String text, Font font) {
		x = 0;
		y = 0;
		this.text = text;
		color = Color.BLACK;
		this.font = font;
	}
	
	public UI_Label(String text, Font font, int x, int y) {
		this.x = x;
		this.y = y;
		this.text = text;
		color = Color.BLACK;
		this.font = font;
	}
	
	public UI_Label(String text, Font font, Color color) {
		x = 0;
		y = 0;
		this.text = text;
		this.color = color;
		this.font = font;
	}
	
	public UI_Label(String text, Font font, Color color, int x, int y) {
		this.x = x;
		this.y = y;
		this.text = text;
		this.color = color;
		this.font = font;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Font getFont() {
		return font;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Point getLocation() {
		return new Point(x, y);
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setLocation(Point p) {
		x = p.x;
		y = p.y;
	}
	
}
