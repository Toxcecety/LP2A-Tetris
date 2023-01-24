package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class IShape extends Shapes{
	
	public IShape(Rectangle a, Rectangle b, Rectangle c, Rectangle d) {
		super(a, b, c, d);
	}
	
	public IShape(Rectangle a, Rectangle b, Rectangle c, Rectangle d, Color color) {
		super(a, b, c, d, color);
	}

	public static Shapes createShape() {
		Rectangle a = new Rectangle(SIZE-1, SIZE-1);
		Rectangle b = new Rectangle(SIZE-1, SIZE-1);
		Rectangle c = new Rectangle(SIZE-1, SIZE-1);
		Rectangle d = new Rectangle(SIZE-1, SIZE-1);
		
		a.setX(XMAX / 2 - 2 * SIZE);
		b.setX(XMAX / 2 - SIZE);
		c.setX(XMAX / 2);
		d.setX(XMAX / 2 + SIZE);
		return new IShape(a,b,c,d, Color.CORNFLOWERBLUE);
	}
	
	public void turnAround(Shapes shape) {
		if (shape.form == 1 && Tetris.checkBorder(shape.a, 1, 1) && Tetris.checkBorder(shape.c, -1, -1) && Tetris.checkBorder(shape.d, -2, -2)) {
			Tetris.moveRight(shape.a);
			Tetris.moveUp(shape.a);
			Tetris.moveLeft(shape.c);
			Tetris.moveDown(shape.c);
			Tetris.moveLeft(shape.d);
			Tetris.moveLeft(shape.d);
			Tetris.moveDown(shape.d);
			Tetris.moveDown(shape.d);
			changeShapes();
		} else if (shape.form == 2 && Tetris.checkBorder(shape.a, 1, -1) && Tetris.checkBorder(shape.c, -1, 1) && Tetris.checkBorder(shape.d, -2, 2)) {
			Tetris.moveRight(shape.a);
			Tetris.moveDown(shape.a);
			Tetris.moveLeft(shape.c);
			Tetris.moveUp(shape.c);
			Tetris.moveLeft(shape.d);
			Tetris.moveLeft(shape.d);
			Tetris.moveUp(shape.d);
			Tetris.moveUp(shape.d);
			changeShapes();
		} else if (shape.form == 3 && Tetris.checkBorder(shape.a, -1, -1) && Tetris.checkBorder(shape.c, 1, 1) && Tetris.checkBorder(shape.d, 2, 2)) {
			Tetris.moveLeft(shape.a);
			Tetris.moveDown(shape.a);
			Tetris.moveRight(shape.c);
			Tetris.moveUp(shape.c);
			Tetris.moveRight(shape.d);
			Tetris.moveRight(shape.d);
			Tetris.moveUp(shape.d);
			Tetris.moveUp(shape.d);
			changeShapes();
		} else if (shape.form == 4 && Tetris.checkBorder(shape.a, -1, 1) && Tetris.checkBorder(shape.c, 1, -1) && Tetris.checkBorder(shape.d, 2, -2)) {
			Tetris.moveLeft(shape.a);
			Tetris.moveUp(shape.a);
			Tetris.moveRight(shape.c);
			Tetris.moveDown(shape.c);
			Tetris.moveRight(shape.d);
			Tetris.moveRight(shape.d);
			Tetris.moveDown(shape.d);
			Tetris.moveDown(shape.d);
			changeShapes();
		}
	}
}
