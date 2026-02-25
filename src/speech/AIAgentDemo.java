package speech;

import static speech.util.PrintUtil.print;

import java.util.List;
import java.util.Scanner;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import speech.booking.RoomBookingRequests;
import speech.util.JsonObject;
import speech.util.SpeechListenerUtil;

public class AIAgentDemo {
	
    private static final String API_KEY = "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";
	
	public static void main(String[] args) throws Exception {
		print("Logging into room booking server...");
//		ChatModel model = OpenAiChatModel.builder().apiKey(API_KEY).modelName(OpenAiChatModelName.GPT_4_O_MINI).build();
//
		RoomBookingRequests booking = new RoomBookingRequests();
		booking.login("Comp490.002@bridgew.edu", "TuesThurs12:30");
		List<JsonObject> bookings = booking.listAvalibleMeetingRooms();
		print(bookings);
	}
}
