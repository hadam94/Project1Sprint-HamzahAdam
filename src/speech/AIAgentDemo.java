package speech;

import static speech.util.PrintUtil.print;

import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import speech.booking.RoomBookingRequests;
import speech.util.JsonObject;
import speech.util.SpeechListenerUtil;
import speech.util.TimeUtil;

public class AIAgentDemo {
	
    private static final String API_KEY = "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";
	
	@SuppressWarnings("resource")
	@Test
	public static void main(String[] args) throws Exception {
		String time = TimeUtil.getCurrentTime();
		Scanner scanner = new Scanner(System.in);

		print("Logging into room booking server...");
		RoomBookingRequests booking = new RoomBookingRequests();
		boolean loggedIn = booking.login("Comp490.002@bridgew.edu", "TuesThurs12:30");
		if(!loggedIn) {
			print("Failed to log in! exiting...");
			System.exit(-1);
			return;
		}
		
		print("Logged in. Loading GPT-5 AI Model...");
		
		ChatModel model = OpenAiChatModel.builder().apiKey(API_KEY).modelName(OpenAiChatModelName.GPT_5).build();
		print("Loaded AI Model. ", false);
		
		while(true) {
			print("Please press enter to start speaking, or type EXIT to finish.");
			
			String action = scanner.nextLine();
			if(action.equalsIgnoreCase("exit")) {
				print("Closing program...");
				System.exit(-1);
				break;
			}
			
			print("Setting up microphone...");
			String speechPrompt = SpeechListenerUtil.getSpeechFromMicrophone(10);
			print("Prompt: " + speechPrompt);
			
			StringBuilder builder = new StringBuilder();
			if(speechPrompt.contains("room")) {
				for(JsonObject object: booking.listAvalibleMeetingRooms()) {
					builder.append(object);
				}
			} 
			if(speechPrompt.contains("reservation")) {
				builder.append(booking.listMyBookings());
			}
			String response = model.chat("Todays time is " + time + "." + speechPrompt + "\n\n " + builder.toString());
			
			print(response);
			print("-------------------------------------------------------------------------------------------------------------------");
			Thread.sleep(1000L);
		}
	}
}
