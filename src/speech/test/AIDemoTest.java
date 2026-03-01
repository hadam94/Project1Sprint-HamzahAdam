package speech.test;

import java.io.File;

import org.junit.Test;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import speech.AIAgentDemo;
import speech.booking.RoomBookingRequests;
import speech.util.JsonObject;
import speech.util.SpeechListenerUtil;
import speech.util.TimeUtil;

import static speech.util.PrintUtil.print;;

public class AIDemoTest {
	
	/**
	 * This test will check to see if there is a reservation based from an audio file.
	 */
	@Test
	public void reservationInfoFromVoiceFile() {
		String speech = SpeechListenerUtil.getSpeechFromAudioFile(new File("assets/sounds/reservation.wav"), false);
		ChatModel model = OpenAiChatModel.builder().apiKey(AIAgentDemo.API_KEY).modelName(OpenAiChatModelName.GPT_5).build();
		RoomBookingRequests booking = new RoomBookingRequests();
		booking.login("Comp490.002@bridgew.edu", "TuesThurs12:30");
		String time = TimeUtil.getCurrentTime();
		StringBuilder roomsList = new StringBuilder();
		for(JsonObject object: booking.listAvalibleMeetingRooms()) {
			roomsList.append(object + ",");
		}
		
		//the list of bookings, massive
		StringBuilder bookedRooms = new StringBuilder();
		int i = 0;
		for(JsonObject object: booking.listMyBookings()) {
			if(i % 2000 != 0) {
				bookedRooms.append(object.toString() + "\n");
			} else {
				String response = model.chat("Todays time is " + time + ". \n" + "The current rooms are: " + roomsList.toString() + "\n The booked rooms are: " + bookedRooms.toString() + "\n\n " + speech + "?");
				print(response);
				bookedRooms = new StringBuilder();
			}
			i++;
		}	
	}
}
