package speech.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.Point;
import java.awt.TextField;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import speech.booking.RoomBookingRequests;
import speech.util.JsonObject;
import speech.util.PrintUtil;


public class RoomBookingWindow extends JFrame {
	
	private final Dimension windowDimension = new Dimension(1000, 1000);
	
	//Was planning for java table to display the rooms list more nicely, but due to time constraints I decided to cut it from final build.
	
//	private JTable roomsTable = new JTable(new DefaultTableModel(new String[][]{{"a", "b", "c"},{"d", "e", "f"}}, new String[]{"id", "name", "capacity"}) {
//		public boolean isCellEditable(int row, int column) {
//			return false;
//		};
//	});
	
	private java.awt.List roomsList = new List();
	
	private final TextField roomnameAdd;
	private final TextField capacityAdd;
	
	private final TextField roomIdRemove;
	
	private final TextField roomIdChange;
	private final TextField capacityChange;

	private static RoomBookingWindow instance;
	
	private RoomBookingRequests bookingSession = new RoomBookingRequests();
	
	private String status = "Idling...";
		
	//passing in false is meant for tests... so window doesn't pop up.
	public RoomBookingWindow(boolean window) {
		instance = this;		
		PrintUtil.print("Logging in...");
		//dont worry, cs490 isn't my actual password!
		boolean success = bookingSession.login("hadam@student.bridgew.edu", "cs490");
		if(!success) {
			PrintUtil.print("Login session failed! exiting");
			System.exit(-1);
		}
		if(!window) {
			PrintUtil.print("Starting up gui...");
		}
		updateAvaliableMeetingRooms();
		setResizable(false);
		setSize(windowDimension);
		setTitle("Server Test");
		addWindowListener(new WindowOnCloseListener());
		
		Dimension textBoxDimension = new Dimension(200, 20);
		Dimension buttonDimensions = new Dimension(100, 50);
		int buttonY = 175;

		roomnameAdd = ComponentCreator.addTextBoxComponent(new Point(50, buttonY - 100), textBoxDimension);
		capacityAdd = ComponentCreator.addTextBoxComponent(new Point(50, buttonY - 50), textBoxDimension);
		ComponentCreator.addButtonComponent("Add Room", new Point(100, buttonY), buttonDimensions, (action) -> {
			String roomName = roomnameAdd.getText();
			int capacity = -1;
			try {
				capacity = Integer.parseInt(capacityAdd.getText());
			} catch(Exception e) {
				
			}
			if(capacity < 0 || roomName.isEmpty()) {
				status = ("Invalid capacity/room name!");
				return;
			}
			boolean added = bookingSession.addMeetingRoom(roomName, capacity);
			if(added) {
				updateAvaliableMeetingRooms();
				status = "Added new room.";
			} else {
				status = "Failed to add new room.";	
			}
		});
		
		roomIdRemove = ComponentCreator.addTextBoxComponent(new Point((int) (windowDimension.width / 2) - (textBoxDimension.width / 2), buttonY - 50), textBoxDimension);
		ComponentCreator.addButtonComponent("Remove Room", new Point((int) (windowDimension.width / 2) - (buttonDimensions.width / 2), buttonY), buttonDimensions, (action) -> {
			try {
				int id = Integer.parseInt(roomIdRemove.getText());
				boolean removed = bookingSession.removeMeetingRoom(id);
				if(removed) {
					if(getRoomById(id) == null) {
						status = "Room " + id + " currently doesn't exist on the server.";
						return;
					} else {
						status = "Removed room " + id + ".";
					}
					java.util.List<JsonObject> currentBookings = bookingSession.listMyBookings();
					java.util.List<JsonObject> cancelledBookings = new ArrayList<>();
					BufferedWriter writer = new BufferedWriter(new FileWriter("assets/report/report.txt"));
					for(JsonObject object: currentBookings) {
						if(object.getProperty("id").equals(id)) {
							cancelledBookings.add(object);
							bookingSession.cancelMeetingRoom(id);
						}
					}
					writer.write("Reservations cancelled: " + cancelledBookings.size());
					writer.newLine();
					for(JsonObject cancelledBooking: cancelledBookings) {
						writer.write(cancelledBooking.toString());
						writer.newLine();
					}
					writer.close();
					updateAvaliableMeetingRooms();
				} else {
					status = "This meeting room id doesn't exist.";
				}
			} catch(Exception e) {
				status = "Please enter a valid meeting room id number.";
			}
		});

		roomIdChange = ComponentCreator.addTextBoxComponent(new Point(windowDimension.width - (int)(textBoxDimension.width + 50), buttonY - 100), textBoxDimension);
		capacityChange = ComponentCreator.addTextBoxComponent(new Point(windowDimension.width - (int)(textBoxDimension.width + 50), buttonY - 50), textBoxDimension);
		ComponentCreator.addButtonComponent("Change Capacity", new Point(windowDimension.width - (buttonDimensions.width + 100), buttonY), buttonDimensions, (action) -> {
			try {
				int meetingRoomId = Integer.parseInt(roomIdChange.getText());
				boolean changed = bookingSession.changeRoomCapacity(meetingRoomId, Integer.parseInt(capacityChange.getText()));
				if(changed) {
					int oldCapacity = getRoomById(meetingRoomId).getProperty("capacity");
					updateAvaliableMeetingRooms();
					int newCapacity = getRoomById(meetingRoomId).getProperty("capacity");
					if(oldCapacity == newCapacity) {
						status = "Room " + meetingRoomId + " already has a capacity of " + oldCapacity + ".";
					} else {
						status = "Changed room capacity of room " + meetingRoomId + " from " + oldCapacity + " to " + newCapacity + ".";
					}
				} else {
					status = "This meeting room id doesn't exist.";
				}
			} catch(Exception e) {
				status = "Your meeting room id or new capacity is invalid.";
			}
		});
		
		ComponentCreator.addButtonComponent("Refresh", new Point(windowDimension.width - 120, windowDimension.height - 90), buttonDimensions, (action) -> {
			updateAvaliableMeetingRooms();
			status = "Updated rooms list.";
		});

		updateAvaliableMeetingRooms();
		int width = 355;
		roomsList.setBounds((windowDimension.width / 2) - (width / 2) , 310, width, 350);
		roomsList.setVisible(true);
		add(roomsList);
		add(new CustomPanel());
		
		setVisible(window);
	}

	
	public static RoomBookingWindow getInstance() {
		return instance;
	}
	
	public java.awt.List getRoomsList() {
		return roomsList;
	}
	
	//Render text components, and some lines
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
		gui.drawString(new Font("segoe ui", 1, 24), "Current Database", 0, 0, Color.BLACK);
		gui.pop();
		
		gui.drawCenteredString(new Font("arial", 1, 12), status, windowDimension.width / 2, 700, Color.BLACK);
	}
	
	//Update rooms list from server after performing an action.
	private void updateAvaliableMeetingRooms() {
		roomsList.removeAll();
		for(JsonObject object: bookingSession.listAvalibleMeetingRooms()) {
			roomsList.add(object.toString());
		}
	}
	
	/**
	 * Returns a room based off of your id inputted into parameter.
	 * @param id The room id that you want.
	 * @return A JsonObject based off of ID. Null if room doesn't exist
	*/
	public JsonObject getRoomById(int id) {
		for(String item: roomsList.getItems()) {
			JsonObject room = JsonObject.of(item);
			if(room.getProperty("id").equals(id)) {
				return room;
			}
		}
		return null;
	}
	
	private class CustomPanel extends JPanel {
		
		private final Timer timer = new Timer(16, (action) -> {
			repaint();
		});
		
		private CustomPanel() {
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
