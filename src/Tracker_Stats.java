import java.awt.Color;
public class Tracker_Stats {

	int lives = Game.NUM_LIVES;
	int ballsInPlay = 0;
	int score = 0;
	double comboModifier = 1;
	int streak = 0;
	UI_Label scoreLabel = new UI_Label("Score: " + score);
	UI_Label comboLabel = new UI_Label("Combo Bonus: " + ((comboModifier * 100) - 100) + "%");
	
	public Tracker_Stats() {
		scoreLabel.setColor(Color.BLACK);
		scoreLabel.setLocation(10, Game.WINDOW_HEIGHT - 10);
		comboLabel.setColor(Color.BLACK);
		comboLabel.setLocation(Game.WINDOW_WIDTH - 130, Game.WINDOW_HEIGHT - 10);
	}
	
	public void resetCounters() {
		lives = Game.NUM_LIVES;
		ballsInPlay = 0;
		score = 0;
		comboModifier = 1;
		scoreLabel.setText("Score: " + score);
	}
	
	public void incrementScore(int brickScore) {
		score = (int)(score + brickScore + streak * comboModifier);
		comboModifier += 0.2;
		streak++;
		comboLabel.setText("Combo Bonus: " + ((comboModifier * 100) - 100) + "%");
		scoreLabel.setText("Score: " + score);
	}
	
	public void resetCombo() {
		comboModifier = 1;
		comboLabel.setText("Combo Bonus: " + ((comboModifier * 100) - 100) + "%");
	}
	
	public void resetStreak() {
		streak = 0;
	}
	
}
