import java.awt.Color;

public class Tracker_Bricks {
	
	public int currentRow = 1;
	public int currentBrick = 1;
	public int bricks = 0;
	public UI_Label brickLabel = new UI_Label("Bricks: " + bricks);
	
	public Tracker_Bricks() {
		brickLabel.setColor(Color.BLACK);
		brickLabel.setLocation(10, 20);
	}
	
	public void resetCounters() {
		bricks = 0;
		currentRow = 1;
		currentBrick = 1;
	}
	
	public void addBrick() {
		bricks++;
		brickLabel.setText("Bricks: " + bricks);
	}
	
	public void subtractBrick() {
		bricks--;
		brickLabel.setText("Bricks: " + bricks);
	}
	
	public int getBricks() {
		return bricks;
	}
}
