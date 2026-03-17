package speech.gui;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.ActionListener;

public class ComponentCreator {
	
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
	
	public static TextField addTextBoxComponent(Point position, Dimension size, ActionListener action) {
		TextField field = new TextField();
		field.setBounds(position.x, position.y, size.width, size.height);
		if(action != null) {
			field.addActionListener(action);
		}
		field.setVisible(true);
		RoomBookingWindow.getInstance().add(field);
		return field;
	}

}
