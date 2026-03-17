package speech.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Stack;

/**
 * OpenGL Inspired class.
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
