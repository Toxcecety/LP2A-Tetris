package application;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Shapes {
	public static final int SIZE = Tetris.SIZE;
	public static int XMAX = Tetris.XMAX;
	public static int YMAX = Tetris.YMAX;
	Rectangle a;
	Rectangle b;
	Rectangle c;
	Rectangle d;
	Color color;
	public int form = 1;
	
	//Constructors
	public Shapes (Rectangle a, Rectangle b, Rectangle c, Rectangle d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	public Shapes (Rectangle a, Rectangle b, Rectangle c, Rectangle d, Color color) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		
		this.a.setFill(color);
		this.b.setFill(color);
		this.c.setFill(color);
		this.d.setFill(color);
	}
	
	/**
	 * Changes the number of form to indicate which position the shape is on
	 */
	public void changeShapes() {
		if (form != 4) {
			form++;
		} else {
			form = 1;
		}
	}
	
	/*
	 * Takes care of turning the piece to the proper position [the position the piece is on is kept track of by using "form"]
	 */
	public abstract void turnAround(Shapes shape);
	
}