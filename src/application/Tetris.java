package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Tetris extends Application {
	// The variables
	public static final int MOVE = 25;
	public static int movespeed;
	public static final int SIZE = 25;
	public static int XMAX = SIZE * 10;
	public static int YMAX = SIZE * 20;
	public static int[][] GRID = new int[XMAX / SIZE][YMAX / SIZE];
	private static Pane group = new Pane();
	private static Shapes object;
	private static Scene scene = new Scene(group, XMAX + 150, YMAX);
	public static int score = 0;
	private static int top = 0;
	private static boolean game = true;
	private static Shapes nextObj = Controller.newShape();
	private static int linesNo = 0;
	private static boolean paused = false;
	
	/**
	 * Starts the game. It handles the display of the different pieces and running the game by calling different methods.
	 * @param stage - the window
	 */
	public static void startGame(Stage stage){
		game = true;
		score = 0;
		top = 0;
		linesNo = 0;
		GRID = new int[XMAX / SIZE][YMAX / SIZE];
		group = new Pane();
		scene = new Scene(group, XMAX + 150, YMAX);
		stage.setScene(scene);
		
		for (int[] a : GRID) {
			Arrays.fill(a, 0);
		}

		Line line = new Line(XMAX, 0, XMAX, YMAX);
		Text scoretext = new Text("Score: ");
		scoretext.setStyle("-fx-font: 20 arial;");
		scoretext.setY(50);
		scoretext.setX(XMAX + 5);
		scoretext.isFocused();
		
		Text level = new Text("Lines: ");
		level.setStyle("-fx-font: 20 arial;");
		level.setY(100);
		level.setX(XMAX + 5);
		level.setFill(Color.GREEN);
		
		
		Button pause = new Button("Pause"){
			public void requestFocus() {}
		};
		pause.setStyle("-fx-font: 20 arial;");
		pause.setLayoutY(150);
		pause.setLayoutX(XMAX + 5);
		
		group.getChildren().addAll(scoretext, line, level, pause);
		
		pause.setOnAction(value -> {
			if(paused == false) {
				paused = true;
			}else {
				paused = false;
			}		
		});
		
		Shapes a = nextObj;
		group.getChildren().addAll(a.a, a.b, a.c, a.d);
		
		moveOnKeyPress(a);
		object = a;
		nextObj = Controller.newShape();
		stage.setScene(scene);
		stage.setTitle("TETRIS");
		stage.show();

		switch(Main.difficulty) {
		case EASY:
			movespeed = 300;
			break;
		case NORMAL:
			movespeed = 250;
			break;
		case EXPERT:
			movespeed = 200;
			break;
		case HARDCORE:
			movespeed = 150;
			break;
		case LEGEND:
			movespeed = 100;
		}

		TimerTask task = new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						if (object.a.getY() == 0 || object.b.getY() == 0 || object.c.getY() == 0
								|| object.d.getY() == 0)
							top++;
						else
							top = 0;

						if (top == 2) {
							// GAME OVER
							Text over = new Text("GAME OVER");
							over.setFill(Color.RED);
							over.setStyle("-fx-font: 60 arial;");
							over.setY(250);
							over.setX(10);
							group.getChildren().add(over);
							game = false;
							File doc = new File("highscore.txt");
							Scanner highscore;
							try {
								highscore = new Scanner(doc);
								if (highscore.nextInt() < score) {
									FileWriter fw = new FileWriter(doc.getAbsoluteFile());
									BufferedWriter newscore = new BufferedWriter(fw);
									newscore.write(""+ score);
									newscore.close();
								}
								highscore.close();
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						// Exit
						if (top == 15) {
							stage.close();
						}

						if (game && paused == false) {
							moveDown(object);
							scoretext.setText("Score: " + Integer.toString(score));
							level.setText("Lines: " + Integer.toString(linesNo));
						}
					}
				});
			}
		};
		new Reminder(movespeed, task);

		stage.setOnHidden( e -> {
			File doc = new File("highscore.txt");
			Scanner highscore;
			try {
				highscore = new Scanner(doc);
				if (highscore.nextInt() < score) {
					FileWriter fw = new FileWriter(doc.getAbsoluteFile());
					BufferedWriter newscore = new BufferedWriter(fw);
					newscore.write(""+ score);
					newscore.close();
				}
				highscore.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Reminder.stopIt();
			score = 0;
			top = 0;
			linesNo = 0;
			GRID = new int[XMAX / SIZE][YMAX / SIZE];
			group = new Pane();
			scene = new Scene(group, XMAX + 150, YMAX);
			stage.setScene(scene);
			nextObj = Controller.newShape();
		});
	}

	/**
	 * Handles the movement of the piece. Calls the different methods to move the tetromino
	 * @param shape - the tetromino we wish to move
	 */
	private static void moveOnKeyPress(Shapes shape) {
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (!paused) {
						switch (event.getCode()) {
						case RIGHT:
							Controller.moveRight(shape);
							break;
						case DOWN:
							moveDown(shape);
							break;
						case LEFT:
							Controller.moveLeft(shape);
							break;
						case UP:
							shape.turnAround(shape);
							//MoveTurn(shape);
							break;
						default:
							break;
						}
					}
				}
			});
	}

	/**
	 * Removes the rows that are full and changes the score accordingly
	 * @param pane - The list of the elements that are displayed on the scene
	 */
	private static void removeRows(Pane pane) {
		ArrayList<Node> rects = new ArrayList<Node>();
		ArrayList<Integer> lines = new ArrayList<Integer>();
		ArrayList<Node> newrects = new ArrayList<Node>();
		int full_lines;
		int last_full_lines = 0;
		int full = 0;
		for (int i = 0; i < GRID[0].length; i++) {
			for (int j = 0; j < GRID.length; j++) {
				if (GRID[j][i] == 1)
					full++;
			}
			if (full == GRID.length)
			lines.add(i);
			full = 0;
		}
		full_lines = lines.size();
		if (full_lines > 0) {
			do {
				for (Node node : pane.getChildren()) {
					if (node instanceof Rectangle)
						rects.add(node);
				}
				score += 100;
				linesNo++;

				for (Node node : rects) {
					Rectangle a = (Rectangle) node;
					if (a.getY() == lines.get(0) * SIZE) {
						GRID[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
						pane.getChildren().remove(node);
					} else
						newrects.add(node);
				}

				for (Node node : newrects) {
					Rectangle a = (Rectangle) node;
					if (a.getY() < lines.get(0) * SIZE) {
						GRID[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
						a.setY(a.getY() + SIZE);
					}
				}
				lines.remove(0);
				rects.clear();
				newrects.clear();
				for (Node node : pane.getChildren()) {
					if (node instanceof Rectangle)
						rects.add(node);
				}
				for (Node node : rects) {
					Rectangle a = (Rectangle) node;
					try {
						GRID[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 1;
					} catch (ArrayIndexOutOfBoundsException e) {
					}
				}
				rects.clear();
			} while (lines.size() > 0);
			if (full_lines == 4 && last_full_lines == 4) {
				score += 800;
			} else if (full_lines == 4) {
				score += 400;
			}
			
			last_full_lines = full_lines;
		}
	}

	/**
	 * Moves down the individual rectangle [a piece of the tetromino]
	 * @param rect - the piece to move
	 */
	static void moveDown(Rectangle rect) {
		if (rect.getY() + MOVE < YMAX)
			rect.setY(rect.getY() + MOVE);
	}

	/**
	 * Moves to the right the individual rectangle [a piece of the tetromino]
	 * @param rect - the piece to move
	 */
	static void moveRight(Rectangle rect) {
		if (rect.getX() + MOVE <= XMAX - SIZE)
			rect.setX(rect.getX() + MOVE);
	}

	/**
	 * Moves to the left the individual rectangle [a piece of the tetromino]
	 * @param rect - the piece to move
	 */
	static void moveLeft(Rectangle rect) {
		if (rect.getX() - MOVE >= 0)
			rect.setX(rect.getX() - MOVE);
	}

	/**
	 * Moves up the individual rectangle [a piece of the tetromino]
	 * @param rect - the piece to move
	 */
	static void moveUp(Rectangle rect) {
		if (rect.getY() - MOVE > 0)
			rect.setY(rect.getY() - MOVE);
	}

	/**
	 * Moves down the entire tetromino piece
	 * @param shape - the tetromino piece
	 */
	private static void moveDown(Shapes shape) {
		if (shape.a.getY() == YMAX - SIZE || shape.b.getY() == YMAX - SIZE || shape.c.getY() == YMAX - SIZE
				|| shape.d.getY() == YMAX - SIZE || moveA(shape) || moveB(shape) || moveC(shape) || moveD(shape)) {
			GRID[(int) shape.a.getX() / SIZE][(int) shape.a.getY() / SIZE] = 1;
			GRID[(int) shape.b.getX() / SIZE][(int) shape.b.getY() / SIZE] = 1;
			GRID[(int) shape.c.getX() / SIZE][(int) shape.c.getY() / SIZE] = 1;
			GRID[(int) shape.d.getX() / SIZE][(int) shape.d.getY() / SIZE] = 1;
			removeRows(group);

			Shapes a = nextObj;
			nextObj = Controller.newShape();
			object = a;
			group.getChildren().addAll(a.a, a.b, a.c, a.d);
			moveOnKeyPress(a);
		}

		if (shape.a.getY() + MOVE < YMAX && shape.b.getY() + MOVE < YMAX && shape.c.getY() + MOVE < YMAX
				&& shape.d.getY() + MOVE < YMAX) {
			int movea = GRID[(int) shape.a.getX() / SIZE][((int) shape.a.getY() / SIZE) + 1];
			int moveb = GRID[(int) shape.b.getX() / SIZE][((int) shape.b.getY() / SIZE) + 1];
			int movec = GRID[(int) shape.c.getX() / SIZE][((int) shape.c.getY() / SIZE) + 1];
			int moved = GRID[(int) shape.d.getX() / SIZE][((int) shape.d.getY() / SIZE) + 1];
			if (movea == 0 && movea == moveb && moveb == movec && movec == moved) {
				shape.a.setY(shape.a.getY() + MOVE);
				shape.b.setY(shape.b.getY() + MOVE);
				shape.c.setY(shape.c.getY() + MOVE);
				shape.d.setY(shape.d.getY() + MOVE);
			}
		}
	}

	/**
	 * Indicates if there is space next to the rectangle of the tetromino piece
	 * @param shape - the tetromino piece
	 * @return boolean
	 */
	private static boolean moveA(Shapes shape) {
		return (GRID[(int) shape.a.getX() / SIZE][((int) shape.a.getY() / SIZE) + 1] == 1);
	}

	private static boolean moveB(Shapes shape) {
		return (GRID[(int) shape.b.getX() / SIZE][((int) shape.b.getY() / SIZE) + 1] == 1);
	}

	private static boolean moveC(Shapes shape) {
		return (GRID[(int) shape.c.getX() / SIZE][((int) shape.c.getY() / SIZE) + 1] == 1);
	}

	private static boolean moveD(Shapes shape) {
		return (GRID[(int) shape.d.getX() / SIZE][((int) shape.d.getY() / SIZE) + 1] == 1);
	}

	/**
	 * Checks if the piece of the tetromino is too close to the border
	 * @param rect - a piece that forms the tetromino
	 * @param x - the movement that will take place on the x axis
	 * @param y - the movement that will take place on the y axis
	 * @return boolean - returns true if the border is far enough, false if not
	 */
	static boolean checkBorder(Rectangle rect, int x, int y) {
		boolean xb = false;
		boolean yb = false;
		if (x >= 0)
			xb = rect.getX() + x * MOVE <= XMAX - SIZE;
		if (x < 0)
			xb = rect.getX() + x * MOVE >= 0;
		if (y >= 0)
			yb = rect.getY() - y * MOVE > 0;
		if (y < 0)
			yb = rect.getY() + y * MOVE < YMAX;
		return xb && yb && GRID[((int) rect.getX() / SIZE) + x][((int) rect.getY() / SIZE) - y] == 0;
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}