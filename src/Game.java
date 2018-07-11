import java.awt.*;
import java.util.*;
import java.util.stream.DoubleStream;

import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

public class Game extends Component implements MouseMotionListener, MouseListener, KeyListener {
	
	public static Game instance;
	
	public double timeElapsed;
	
	//Debug label displaying the elapsed time in nanos
	UI_Label timeLabel = new UI_Label("FPS: ");
	
	private enum GameState {PAUSED, AT_MAIN_MENU, RUNNING, END_OF_GAME, INITIALIZING};
	private enum StateCommand {NONE, PAUSE, UNPAUSE, MAIN_MENU, INIT_GAME, END_GAME};
	
	// CONSTANTS
	private static final long serialVersionUID = 1L;
	
	// Dimensions of the game window
	public static final int WINDOW_WIDTH = 400;
	public static final int WINDOW_HEIGHT = 600;
	
	// Stats for the paddle
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 12;
	private static final int PADDLE_Y_OFFSET = 50; // Offset of the paddle up from the bottom
	public static final double PADDLE_INITIAL_SPEED = 3.5; //Initial speed of the paddle, in pixels per frame
	public static final double PADDLE_ACCELERATION = 0.025;
	
	// Stats for the bricks in the game
	private static final int BRICK_SEP = 4; // Separation between bricks
	private static final int BRICKS_PER_ROW = 10;
	private static final int BRICK_ROWS = 10; // Number of rows of bricks
	private static final int BRICK_WIDTH = (WINDOW_WIDTH - (BRICKS_PER_ROW - 1) * BRICK_SEP) / BRICKS_PER_ROW;
	private static final int BRICK_HEIGHT = 10;
	private static final int BRICK_Y_OFFSET = 70; // Offset of the top brick row from the top of the screen
	
	// Stats for the ball
	public static final int BALL_RADIUS = 7;
	public static final double BALL_INITIAL_X = (WINDOW_WIDTH / 2) - BALL_RADIUS;
	public static final double BALL_INITIAL_Y = (WINDOW_HEIGHT / 2)  - BALL_RADIUS;
	public static final double BALL_INITIAL_MAX_SPEED = 2.5; //Initial speed of the ball, in pixels per frame
	public static final double BALL_ACCELERATION = 0.025; //% In decimal form that the ball should accelerate when it bounces
	
	// Misc. constants
	public static final int NUM_LIVES = 3; // Number of lives granted at the start of a game.
	public static final double TARGET_MS_PER_FRAME = 17;
	//private static final int SLEEP_TIME = 8; // Number of milliseconds to delay after each frame.
	
	// Contains the game's current state. Should only be modified within the run() method.
	private GameState state = GameState.PAUSED;
	// Contains the current command being issued to modify the game's state. Handled within the run() method.
	private StateCommand command = StateCommand.NONE;
	
	// Objects for tracking game stats such as lives, bricks remaining, etc
	public Tracker_Bricks brickTracker = new Tracker_Bricks();
	public Tracker_Stats statTracker = new Tracker_Stats();
	
	// This should contain all elements currently being rendered on screen.
	public ArrayList<Shape> elements = new ArrayList<Shape>();
	// Any elements in this list will be removed from the elements array each time paint() is called.
	public ArrayList<Shape> cleanup = new ArrayList<Shape>();
	// Contains any textual elements that need to be rendered on screen
	public ArrayList<UI_Label> labels = new ArrayList<UI_Label>();
	// Contains any KeyStroke objects that need to be handled by handleKeyEvent()
	private HashMap<String, KeyStroke> keys = new HashMap<String, KeyStroke>();
	
	// Point object to track the mouse's position. Initial position of the mouse is set to the middle of the screen
	public Point mousePos = new Point(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
	
	// Objects needed to enable random number generation for the speed of the ball
	private DoubleStream randomDoubles;
	private PrimitiveIterator.OfDouble doubleIterator;
	private Random rgen;
	
	// Swing elements and menu options
	private JFrame frame;
	private UI_MenuOption resumeOption;
	private UI_MenuOption quitOption;
	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		instance = new Game();
		instance.start();
	}
	
	public void start() throws InvocationTargetException, InterruptedException {
		initWindow();
		loadElements();
		command = StateCommand.MAIN_MENU;
		run();
	}
	
	private void initWindow() {
		// initialize the JFrame
		frame = new JFrame("Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.setResizable(false);
		// set up the drawing surface
		setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.add(this);
		// display the frame
		frame.pack();
		frame.setVisible(true);
		// register the drawing surface as a mouse and key listener
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);
		// make sure the drawing surface itself properly has focus
		setFocusable(true);
		requestFocus();
		requestFocusInWindow();
	}
	
	private void loadElements() {
		Graphics2D g = (Graphics2D)frame.getGraphics();
		resumeOption = new UI_MenuOption("Continue (Spacebar)");
		resumeOption.setColor(Color.BLACK);
		resumeOption.setHighlightColor(Color.RED);
		resumeOption.setFont(new Font("Helvetica", Font.BOLD, 16));
		resumeOption.setHighlightFont(new Font("Helvetica", Font.BOLD, 16));
		Rectangle bounds = resumeOption.getFont().getStringBounds(resumeOption.getText(), g.getFontRenderContext()).getBounds();
		resumeOption.setLocation((WINDOW_WIDTH / 2) - (int)(bounds.getWidth() / 2), (WINDOW_HEIGHT / 2) + 50);
		resumeOption.setCommand(new Runnable() {
			public void run() {
				command = StateCommand.UNPAUSE;
			}
		});
		quitOption = new UI_MenuOption("Return to menu (Q)");
		quitOption.setColor(Color.BLACK);
		quitOption.setHighlightColor(Color.RED);
		quitOption.setFont(new Font("Helvetica", Font.BOLD, 16));
		quitOption.setHighlightFont(new Font("Helvetica", Font.BOLD, 16));
		bounds = quitOption.getFont().getStringBounds(quitOption.getText(), g.getFontRenderContext()).getBounds();
		quitOption.setLocation((WINDOW_WIDTH / 2) - (int)(bounds.getWidth() / 2), (WINDOW_HEIGHT / 2) + 75);
		quitOption.setCommand(new Runnable() {
			public void run() {
				command = StateCommand.MAIN_MENU;
			}
		});
		rgen = new Random();
		randomDoubles = rgen.doubles(BALL_INITIAL_MAX_SPEED / 2, BALL_INITIAL_MAX_SPEED);
		doubleIterator = randomDoubles.iterator();
		timeLabel.setLocation(WINDOW_WIDTH - 100, 20);
	}
	
	private void run() throws InvocationTargetException, InterruptedException {
		long startTime;
		long endTime;
		startTime = System.nanoTime();
		while (true) {
			// Handle all key events registered between this frame and the last
			for (KeyStroke key : keys.values()) {
				handleKeyEvent(key);
			} 
			/* Process the stored StateCommand, if a command has been issued between this frame and the last.
			 * command and state are private variables-- commands should only be modified by code within this class,
			 * and states should be modified and/or handled within this method only. Modifying the game's state
			 * outside this method (in particular, during the render loop) may cause unexpected behavior.
			 * 
			 * Each case must always set the command to NONE after handling the given command. */
			switch(command) {
			// command == MAIN_MENU
			case MAIN_MENU:
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						elements.clear();
						labels.clear();
						UI_MenuOption start = new UI_MenuOption("Start!");
						start.setLocation(175, 450);
						start.setColor(Color.BLUE);
						start.setHighlightColor(Color.RED);
						start.setFont(new Font("Helvetica", Font.BOLD, 16));
						start.setHighlightFont(new Font("Helvetica", Font.BOLD, 16));
						start.setCommand(new Runnable() {
							public void run() {
								command = StateCommand.INIT_GAME;
							}
						});
						labels.add(start);
						state = GameState.AT_MAIN_MENU;
						command = StateCommand.NONE;
					}
				});
				break;
			// command == INIT_GAME
			case INIT_GAME:
				/* TODO: find a better way to handle this.
				 * PAUSED has special handling which expects the game to be initialized */
				state = GameState.PAUSED;
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						statTracker.resetCounters();
						brickTracker.resetCounters();
						elements.clear();
						labels.clear();
						labels.add(statTracker.scoreLabel);
						labels.add(brickTracker.brickLabel);
						labels.add(statTracker.comboLabel);
						labels.add(timeLabel);
						state = GameState.INITIALIZING;
						command = StateCommand.NONE;
					}
				});
				break;
			// command == PAUSE
			case PAUSE:
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						labels.add(resumeOption);
						labels.add(quitOption);
					}
				});
				state = GameState.PAUSED;
				command = StateCommand.NONE;
				break;
			// command == UNPAUSE
			case UNPAUSE:
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						if (labels.contains(resumeOption)) {
							labels.remove(resumeOption);
						}
						if (labels.contains(quitOption)) {
							labels.remove(quitOption);
						}
					}
				});
				state = GameState.RUNNING;
				command = StateCommand.NONE;
				break;
			// command == END_GAME
			case END_GAME:
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						labels.add(quitOption);
						state = GameState.END_OF_GAME;
						command = StateCommand.NONE;
					}
				});
				break;
			// command == NONE
			case NONE:
				break;
			}
			
			// If the game is running, check the terminating conditions of the round.
			if (state == GameState.RUNNING) {
				if (brickTracker.getBricks() <= 0) {
					command = StateCommand.END_GAME;
				}
				if (statTracker.ballsInPlay <= 0) {
					statTracker.lives--;
					if (statTracker.lives > 0) {
						SwingUtilities.invokeAndWait(new Runnable() {
							public void run() {
								for (Shape element : elements) {
									if (element instanceof Entity_Paddle) {
										cleanup.add(element);
									}
								}
								addBall();
								addPaddle();
								command = StateCommand.PAUSE;
								//TODO: Is there a better way to do this?
								state = GameState.PAUSED;
								statTracker.resetStreak();
							}
						});
					} else {
						command = StateCommand.END_GAME;
					}
				}
			}
			
			// If the game is initializing, add the elements one by one to the screen each frame
			if (state == GameState.INITIALIZING) {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						addNextGameElement();
					}
				});
			}
			
			// After all state handling is done, render the screen and sleep a specified amount of milliseconds
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					repaint();
				}
			});
			
			// Calculations to determine FPS
			endTime = System.nanoTime();
			double frameTimeElapsed = Math.abs(endTime - startTime) / (double)1000000;
			int sleepTime = (int)(TARGET_MS_PER_FRAME - frameTimeElapsed);
			assert(sleepTime > 0);
			sleep(sleepTime);
			startTime = System.nanoTime();
			timeElapsed = (frameTimeElapsed + sleepTime) / TARGET_MS_PER_FRAME;
			int fps = (int)((1 / (frameTimeElapsed + sleepTime)) * 1000);
			timeLabel.setText("FPS: " + fps);
		}
	}
	
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void addNextGameElement() {
		if (brickTracker.currentRow <= BRICK_ROWS) {
			if (brickTracker.currentBrick <= BRICKS_PER_ROW) {
				int rowIndex = Math.round((brickTracker.currentRow + 0.1f) / 2);
				int brickHealth = getBrickHealth(rowIndex);
				int brickX = (BRICK_SEP * (brickTracker.currentBrick - 1)
						+ ((brickTracker.currentBrick - 1) * BRICK_WIDTH)
						+ ((WINDOW_WIDTH - (BRICK_WIDTH * BRICKS_PER_ROW 
						+ (BRICK_SEP * (BRICKS_PER_ROW - 1)))) / 2));
				int brickY = (BRICK_Y_OFFSET + (brickTracker.currentRow * BRICK_HEIGHT) 
						+ (BRICK_SEP * brickTracker.currentRow));
				Entity_Brick brick = new Entity_Brick(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT, brickHealth);
				elements.add(brick);
				brickTracker.addBrick();
				brickTracker.currentBrick++;
			} else {
				brickTracker.currentBrick = 1;
				brickTracker.currentRow++;
			}
		} else {
			addBall();
			addPaddle();
			command = StateCommand.PAUSE;
		}
	}
	
	private int getBrickHealth(int index) {
		int health;
		switch(index) {
		case 1:
			health = 25;
			break;
		case 2:
			health = 20;
			break;
		case 3:
			health = 15;
			break;
		case 4:
			health = 10;
			break;
		case 5:
			health = 5;
			break;
		default:
			health = 0;
			break;
		}
		return health;
	}
	
	private void addBall() {
		Entity_Ball ball = new Entity_Ball(BALL_INITIAL_X, BALL_INITIAL_Y, 
				(BALL_RADIUS * 2), (BALL_RADIUS * 2), Color.BLUE);
		double xVelocity = (rgen.nextBoolean()) ? doubleIterator.next() : -doubleIterator.next();
		ball.setVelocity(xVelocity, BALL_INITIAL_MAX_SPEED);
		elements.add(ball);
		statTracker.ballsInPlay++;
	}
	
	private void addPaddle() {
		Entity_Paddle paddle = new Entity_Paddle(((WINDOW_WIDTH / 2) - (PADDLE_WIDTH / 2)), 
				(WINDOW_HEIGHT - PADDLE_Y_OFFSET),
				PADDLE_WIDTH, PADDLE_HEIGHT, 
				elements, mousePos, Color.BLACK);
		paddle.setSpeed(PADDLE_INITIAL_SPEED);
		elements.add(paddle);
	}
	
	
	public void paint(Graphics g) {
		try {
			// cast the given graphics context to a Graphics2D object for enhanced drawing functionality
			Graphics2D draw2D = (Graphics2D) g;
			for (Shape element : elements) {
				// if the element is Updatable, call its update() method
				if (state == GameState.RUNNING) {
					if (element instanceof Updateable) {
						((Updateable) element).update();
					}
				}
				// if the element is Colorable, set our graphics context's color to that object's color property
				if (element instanceof Colorable) {
					draw2D.setColor(((Colorable) element).getColor());
				} else {
					// if the element is not Colorable, it is black by default
					draw2D.setColor(Color.BLACK);
				}
				// if the element is Hideable, check that it's visible before rendering
				if (element instanceof Hideable) {
					if (((Hideable) element).isVisible()) {
						draw2D.fill(element);
					}
				} else {
					// if it's not Hideable, just render it
					draw2D.fill(element);
				}
			}
			// Clean up any elements that need to be removed from the screen
			if (!cleanup.isEmpty()) {
				for (Shape clean : cleanup) {
					if (elements.contains(clean)) {
						elements.remove(clean);
					}
				}
				cleanup.clear();
			}
			for (UI_Label label : labels) {
				if (label instanceof Highlightable) {
					Rectangle bounds = label.getFont().getStringBounds(label.getText(), draw2D.getFontRenderContext()).getBounds();
					bounds.setLocation(label.getX(), label.getY() - bounds.height);
					if (bounds.contains(mousePos)) {
						draw2D.setColor(((Highlightable) label).getHighlightColor());
						draw2D.setFont(((Highlightable) label).getHighlightFont());
					} else {
						draw2D.setColor(label.getColor());
						draw2D.setFont(label.getFont());
					}
				} else {
					draw2D.setColor(label.getColor());
					draw2D.setFont(label.getFont());
				}
				draw2D.drawString(label.getText(), label.getX(), label.getY());
			}
		} catch (ConcurrentModificationException e) {
			e.printStackTrace();
		}
	}
	
	public void removeElement(Shape element) {
		cleanup.add(element);
	}

	private void handleKeyEvent(KeyStroke key) {
		int keyCode = key.getKeyCode();
		if (keyCode == KeyEvent.VK_SPACE) {
			if (state == GameState.PAUSED) {
				command = StateCommand.UNPAUSE;
			} else if (state == GameState.RUNNING) {
				command = StateCommand.PAUSE;
			}
		}
		if (keyCode == KeyEvent.VK_Q) {
			if (state == GameState.PAUSED || state == GameState.END_OF_GAME) {
				command = StateCommand.MAIN_MENU;
			}
		}
		String keyString = String.valueOf(keyCode);
		keys.remove(keyString);
	}
	
	public void mouseMoved(MouseEvent e) {
		mousePos.x = e.getX();
		mousePos.y = e.getY();
	}
	
	public void mouseDragged(MouseEvent e) {
		mousePos.x = e.getX();
		mousePos.y = e.getY();
	}

	public void mouseClicked(MouseEvent e) {
		// NOTE: Do nothing
	}

	public void mouseEntered(MouseEvent e) {
		// NOTE: Do nothing
	}

	public void mouseExited(MouseEvent e) {
		// NOTE: Do nothing
	}

	public void mousePressed(MouseEvent e) {
		// NOTE: Do nothing
	}

	public void mouseReleased(MouseEvent e) {
		for (UI_Label label : labels) {
			if (label instanceof Clickable) {
				Graphics2D g = (Graphics2D)frame.getGraphics();
				Rectangle bounds = label.getFont().getStringBounds(label.getText(), g.getFontRenderContext()).getBounds();
				bounds.setLocation(label.getX(), label.getY() - bounds.height);
				if (bounds.contains(mousePos)) {
					((Clickable) label).getCommand().run();
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		KeyStroke key = KeyStroke.getKeyStrokeForEvent(e);
		String keyCode = String.valueOf(key.getKeyCode());
		if (!keys.containsKey(keyCode)) {
			keys.put(keyCode, key);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		KeyStroke key = KeyStroke.getKeyStrokeForEvent(e);
		String keyCode = String.valueOf(key.getKeyCode());
		if (keys.containsKey(keyCode)) {
			keys.remove(keyCode);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public Dimension getPreferredSize() {
		return (new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
	}
	
}
