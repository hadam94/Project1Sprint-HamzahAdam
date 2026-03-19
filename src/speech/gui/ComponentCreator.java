package speech.gui;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.ActionListener;

public class ComponentCreator {
	
	/**
	 * Add a button component to the GUI.
	 * @param name The name of the button displayed on it.
	 * @param position where the button is located in the GUI.
	 * @param size the width and height of the button.
	 * @param action What action should be performed upon clicking on it.
	 * @return The button component you created.
	*/
	public static Button addButtonComponent(String name, Point position, Dimension size, ActionListener action) {
		Button button = new Button();
		button.setLabel(name);
		button.setBounds(position.x, position.y, size.width, size.height);
		if(action != null) {
			button.addActionListener(action);
		}
		button.setVisible(true);
		RoomBookingWindow.getInstance().add(button);
		return button;
	}
	
	/**
	 * Add a text field component to the GUI.
	 * @param position where the text field is located in the GUI.
	 * @param size the width and height of the text field.
	 * @return The text field component you created.
	*/
	public static TextField addTextBoxComponent(Point position, Dimension size) {
		TextField field = new TextField();
		field.setBounds(position.x, position.y, size.width, size.height);
		field.setVisible(true);
		RoomBookingWindow.getInstance().add(field);
		return field;
	}

}
