package speech.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.TextField;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import speech.util.PrintUtil;


public class RoomBookingWindow extends JFrame {
	
	private final Dimension windowDimension = new Dimension(1000, 1000);
	
	private final TextField roomnameAdd;
	private final TextField capacityAdd;
	
	private final TextField roomIdRemove;
	
	private final TextField roomIdChange;
	private final TextField capacityChange;

	private static RoomBookingWindow instance;
		
	public RoomBookingWindow() {
		instance = this;
		setResizable(false);
		setSize(windowDimension);
		setTitle("Server Test");
		addWindowListener(new WindowOnCloseListener());
		
		Dimension textBoxDimension = new Dimension(200, 20);
		Dimension buttonDimensions = new Dimension(100, 50);
		
		int buttonY = 175;

		roomnameAdd = ComponentCreator.addTextBoxComponent(new Point(50, buttonY - 100), textBoxDimension, null);
		capacityAdd = ComponentCreator.addTextBoxComponent(new Point(50, buttonY - 50), textBoxDimension, null);
		ComponentCreator.addButtonComponent("Add Room", new Point(100, buttonY), buttonDimensions, (e) -> PrintUtil.print("Add Room"));
	
		
		roomIdRemove = ComponentCreator.addTextBoxComponent(new Point((int) (windowDimension.width / 2) - (textBoxDimension.width / 2), buttonY - 50), textBoxDimension, null);
		ComponentCreator.addButtonComponent("Remove Room", new Point((int) (windowDimension.width / 2) - (buttonDimensions.width / 2), buttonY), buttonDimensions, (e) -> PrintUtil.print("Add Room"));

		
		ComponentCreator.addButtonComponent("Change Room", new Point(windowDimension.width - (buttonDimensions.width + 100), buttonY), buttonDimensions, (e) -> PrintUtil.print("Add Room"));
		roomIdChange = ComponentCreator.addTextBoxComponent(new Point(windowDimension.width - (int)(textBoxDimension.width + 50), buttonY - 100), textBoxDimension, (e) -> PrintUtil.print("Add Room"));
		capacityChange = ComponentCreator.addTextBoxComponent(new Point(windowDimension.width - (int)(textBoxDimension.width + 50), buttonY - 50), textBoxDimension, (e) -> PrintUtil.print("Add Room"));

		add(new RenderEvent());
		
		setVisible(true);
	}
	
	public static RoomBookingWindow getInstance() {
		return instance;
	}
	
	private void onRender(Gui gui) {
		int topY = 50;
		gui.graphics().drawLine(0, topY, (int) windowDimension.getWidth(), topY);
		
		gui.push();
		gui.graphics().translate(windowDimension.getWidth() / 2, 40);
		gui.graphics().scale(3, 3);
		gui.drawCenteredString(getFont(), "Room Booking Panel", 0, 0, Color.BLACK);
		gui.pop();
		
		gui.graphics().drawString("Room Name", roomnameAdd.getX(), roomnameAdd.getY() - 1);
		gui.graphics().drawString("Capacity", capacityAdd.getX(), capacityAdd.getY() - 1);
		gui.graphics().drawString("Room ID", roomIdRemove.getX(), roomIdRemove.getY() - 1);
		gui.graphics().drawString("Room ID", roomIdChange.getX(), roomIdChange.getY() - 1);
		gui.graphics().drawString("New Capacity", capacityChange.getX(), capacityChange.getY() - 1);
		
		gui.graphics().drawLine(325, topY, 325, topY + 225);
		gui.graphics().drawLine(675, topY, 675, topY + 225);
		
		gui.graphics().drawLine(0, topY + 225, (int) windowDimension.getWidth(), topY + 225);
		
		gui.push();
		gui.graphics().translate(5, topY + 250);
		//gui.graphics().scale(2, 2);
		gui.drawString(new Font("segoe ui", 1, 24), "Current Database", 0, 0, Color.BLACK);
		gui.pop();
	}
	
	private class RenderEvent extends JPanel {
		
		private final Timer timer = new Timer(16, (action) -> {
			repaint();
		});
		
		private RenderEvent() {
			setBackground(Color.GRAY);
			timer.start();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			Gui gui = new Gui((Graphics2D) g);
			onRender(gui);
		}
	}
}
