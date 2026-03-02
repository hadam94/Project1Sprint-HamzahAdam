package speech;

import static speech.util.PrintUtil.print;

import java.time.LocalDateTime;
import java.util.Scanner;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import speech.booking.RoomBookingRequests;
import speech.util.APIKeys;
import speech.util.JsonObject;
import speech.util.SpeechListenerUtil;
import speech.util.TimeUtil;

public class AIAgentDemo {
		
	@SuppressWarnings("resource")
	@Tool(name = "AI Agent Demo Tool", value = "An AI Agent that will assist you in information regarding reservations, rooms, and times.") 
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);

		print("Logging into room booking server...");
		RoomBookingRequests booking = new RoomBookingRequests();
		boolean loggedIn = booking.login("Comp490.002@bridgew.edu", "TuesThurs12:30");
		if(!loggedIn) {
			print("Failed to log in! exiting...");
			System.exit(-1);
			return;
		}
		
		print("Logged in. Loading GPT-4o Mini AI Model...");
		
        ChatMemory chatMemory = TokenWindowChatMemory.withMaxTokens(Integer.MAX_VALUE, new OpenAiTokenCountEstimator(OpenAiChatModelName.GPT_4_O_MINI));

        ChatModel model = OpenAiChatModel.builder().apiKey(APIKeys.AI_KEY).modelName(OpenAiChatModelName.GPT_4_O_MINI).build();
		print("Loaded AI Model. ", false);
		feedBookingInfoIntoAI(booking, chatMemory, model);
		
		while(true) {
			
			boolean loop = true;
			while(loop) {
				print("You have the following options:");
				print("press ENTER: Start speaking");
				print("RELOAD: Reload booking information");
				print("EXIT: Close Program");
	
				String action = scanner.nextLine();
				if(action.equalsIgnoreCase("exit")) {
					print("Closing program...");
					System.exit(-1);
				} else if(action.equalsIgnoreCase("reload")) {
					feedBookingInfoIntoAI(booking, chatMemory, model);
				} else {
					loop = false;
				}
			}
			
			print("Setting up microphone...");
			String speechPrompt = SpeechListenerUtil.getSpeechFromMicrophone(10);
			print("Prompt: " + speechPrompt);
			
									
			print("Generating AI Response...\n");
			StringBuilder prompt = new StringBuilder();
			prompt.append("Todays time is " + TimeUtil.getCurrentTime() + "\n");
			prompt.append(speechPrompt);

			chatMemory.add(UserMessage.userMessage(prompt.toString()));

	        //AI Response here
			String response = model.chat(chatMemory.messages()).aiMessage().text();
	        seperateLine();
			print(response);
			seperateLine();
			
			Thread.sleep(1000L);
		}
	}
	
	private static void feedBookingInfoIntoAI(RoomBookingRequests booking, ChatMemory chatMemory, ChatModel model) {
		print("Feeding booking information into the AI...");
		chatMemory.clear();
		StringBuilder roomsList = new StringBuilder();
		for(JsonObject object: booking.listAvalibleMeetingRooms()) {
			roomsList.append(object.getProperty("room_name") + ",");
			roomsList.append(object.getProperty("capacity") + "\n");
		}
		chatMemory.add(UserMessage.userMessage("Current List Of Places (room_name,capacity): \n" + roomsList.toString() + "\n"));
		
		StringBuilder bookedRooms = new StringBuilder();
		LocalDateTime currentDate = LocalDateTime.now();
		
		for(JsonObject object: booking.listMyBookings()) {
			String roomBookingTime = object.getProperty("start_time").toString();
			String[] roomTimeFormat = roomBookingTime.split("T")[0].split("-");
			LocalDateTime bookingDate = LocalDateTime.of(Integer.parseInt(roomTimeFormat[0]), Integer.parseInt(roomTimeFormat[1]), Integer.parseInt(roomTimeFormat[2]), 0, 0);
			
			//Too many bookings currently in the backend server (over 5k). 128k token limit in ai.
			if(bookingDate.minusDays(1).isAfter(currentDate)) {
				continue;
			}
			
//			bookedRooms.append(object.getProperty("id").toString() + ",");
//			bookedRooms.append(object.<JsonObject>getProperty("meeting_room").getProperty("room_name").toString() + ",");
//			bookedRooms.append(object.<JsonObject>getProperty("meeting_room").getProperty("capacity").toString() + ",");
//			bookedRooms.append(roomBookingTime + ",");
//			bookedRooms.append(object.getProperty("end_time").toString() + "\n");
			bookedRooms.append(object + "\n");
		}
		if(!bookedRooms.toString().isEmpty()) {
			chatMemory.add(UserMessage.userMessage("Current Booked Rooms (room_id,room_name,start_time,end_time): \n" + bookedRooms.toString()));
		}
		print("");
	}
	
	private static void seperateLine() {
		for(int i = 0; i < 1000; ++i) {
			print("-", false);
		}
		print("");
	}
}
