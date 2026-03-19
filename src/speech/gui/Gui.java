package speech.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Stack;

/**
 * OpenGL Inspired class. Uses 3x3 matrix for 2d rendering. 4x4 typically in 3d rendering, but not needed
 * This is mainly so you can save the translation, rotation, and scaling states of graphics elements, and 
 * you can revert it to the previous state (matrix) by calling pop()
 * 
 * push()
 * transformation/scaling/rotation code (alters the matrix)
 * render code here (influenced by altered matrix operations)
 * pop() reverts matrix back to previous state
 */
public class Gui {
	
	private final Stack<Graphics2D> graphicsStack;
	
	public Gui(Graphics2D graphics) {
		graphicsStack = new Stack<>();
		graphicsStack.push(graphics);
	}
	
	public void push() {
		Graphics2D current = (Graphics2D) graphicsStack.peek().create();
		graphicsStack.push(current);
	}
	
	public void pop() {
		if(graphicsStack.size() <= 1)
			return;
		graphicsStack.pop();
	}
	
	public Graphics2D graphics() {
		return graphicsStack.peek();
	}
	
	public void drawCenteredString(Font font, String text, int x, int y, Color color) {
		graphics().setFont(font);
		graphics().setColor(color);
		int stringWidth = graphics().getFontMetrics().stringWidth(text);
		graphics().drawString(text, x - (stringWidth / 2), y);
	}
	
	public void drawString(Font font, String text, int x, int y, Color color) {
		graphics().setFont(font);
		graphics().setColor(color);
		graphics().drawString(text, x, y);
	}
}
