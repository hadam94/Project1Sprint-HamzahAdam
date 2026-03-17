package speech.gui;

import java.awt.Color;
import java.awt.Dimension;
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
	
	private final TextField roomname;
	private static RoomBookingWindow instance;
		
	public RoomBookingWindow() {
		instance = this;
		setResizable(false);
		setSize(windowDimension);
		setTitle("Server Test");
		addWindowListener(new WindowOnCloseListener());
		
		roomname = ComponentCreator.addTextBoxComponent("Room Name", new Point(50, windowDimension.height - 200), new Dimension(200, 20), null);
		ComponentCreator.addButtonComponent("Add Room", new Point(100, windowDimension.height - 100), new Dimension(100, 50), (e) -> PrintUtil.print("Add Room"));
		
		
		add(new RenderEvent());
		
		setVisible(true);
	}
	
	public static RoomBookingWindow getInstance() {
		return instance;
	}
	
	private void onRender(Gui gui) {
		gui.push();
		gui.graphics().translate(500, 500);
		gui.graphics().drawString("Mallard", 0, 0);
		gui.pop();
		gui.graphics().drawString("Maqeqwllard", 10, 10);
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
