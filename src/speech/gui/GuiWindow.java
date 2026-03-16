package speech.gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import speech.util.PrintUtil;


public class GuiWindow extends JFrame {
	
	private final Dimension windowDimension = new Dimension(1000, 1000);
	
	public GuiWindow() {
		setResizable(false);
		setSize(windowDimension);
		setTitle("Server Test");
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.exit(-1);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		getContentPane().setBackground(Color.GRAY);
		addButtonComponent("Test", new Point(10, 10), new Dimension(50, 50), (e) -> PrintUtil.print("Hello World"));
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		add(panel);
		setVisible(true);
	}
	
	private void addButtonComponent(String name, Point position, Dimension size, ActionListener action) {
		Button button = new Button();
		button.setLabel(name);
		button.setBounds(position.x, position.y, size.width, size.height);
		button.addActionListener(action);
		button.setVisible(true);
		add(button);
	}
}
