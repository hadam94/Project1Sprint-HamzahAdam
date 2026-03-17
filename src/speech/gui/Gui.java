package speech.gui;

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
	
	public Graphics2D graphics() {
		return graphicsStack.peek();
	}
	
	public void pop() {
		if(graphicsStack.size() <= 1)
			return;
		graphicsStack.pop();
	}
}
