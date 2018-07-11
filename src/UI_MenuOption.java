import java.awt.Color;
import java.awt.Font;

public class UI_MenuOption extends UI_Label implements Highlightable, Clickable {
	
	//private boolean active;
	private Runnable command;
	private Color highlightColor;
	private Font highlightFont;
	
	public UI_MenuOption() {
		super();
		highlightColor = getColor();
		highlightFont = getFont();
	}
	
	public UI_MenuOption(String text) {
		super(text);
		highlightColor = getColor();
		highlightFont = getFont();
	}
	
	public UI_MenuOption(String text, int x, int y) {
		super(text, x, y);
		highlightColor = getColor();
		highlightFont = getFont();
	}
	
	public UI_MenuOption(String text, Font font) {
		super(text, font);
		highlightColor = getColor();
		highlightFont = getFont();
	}
	
	public UI_MenuOption(String text, Font font, int x, int y) {
		super(text, font, x, y);
		highlightColor = getColor();
		highlightFont = getFont();
	}
	
	public UI_MenuOption(String text, Font font, Color color) {
		super(text, font, color);
		highlightColor = getColor();
		highlightFont = getFont();
	}
	
	public UI_MenuOption(String text, Font font, Color color, int x, int y) {
		super(text, font, color, x, y);
		highlightColor = getColor();
		highlightFont = getFont();
	}
	
	public void setCommand(Runnable command) {
		this.command = command;
	}
	
	public Runnable getCommand() {
		return command;
	}
	
	public void setHighlightColor(Color color) {
		highlightColor = color;
	}
	
	public Color getHighlightColor() {
		return highlightColor;
	}
	
	public void setHighlightFont(Font font) {
		highlightFont = font;
	}
	
	public Font getHighlightFont() {
		return highlightFont;
	}
	
}
