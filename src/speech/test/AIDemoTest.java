package speech.test;

import static speech.util.PrintUtil.print;

import java.io.File;

import org.junit.Test;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import speech.AIAgentDemo;
import speech.booking.RoomBookingRequests;
import speech.util.APIKeys;
import speech.util.JsonObject;
import speech.util.SpeechListenerUtil;
import speech.util.TimeUtil;;

public class AIDemoTest {
	
	/**
	 * This test will check to see if there is a reservation based from an audio file.
	 */
	@Test
	public void reservationInfoFromVoiceFile() {
		String speech = SpeechListenerUtil.getSpeechFromAudioFile(new File("assets/sounds/reservation.wav"), false);
		ChatModel model = OpenAiChatModel.builder().apiKey(APIKeys.AI_KEY).modelName(OpenAiChatModelName.GPT_5).build();
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
	
	/**
	 * This test will create a room, ask information about it through the AI, and delete the room afterwards.
	 */
	@Test
	public void createAndDeleteRoomTest() {
		RoomBookingRequests booking = new RoomBookingRequests();
		print(TimeUtil.getCurrentTime());
		booking.login("Comp490.002@bridgew.edu", "TuesThurs12:30");
		int id = 1231327833;
		booking.bookMeetingRoom(id, "2026-04-29 11:10 AM", "2026-04-29 11:20 PM", 1);
		String speech = SpeechListenerUtil.getSpeechFromAudioFile(new File("assets/sounds/reservation.wav"), false);
		ChatModel model = OpenAiChatModel.builder().apiKey(APIKeys.AI_KEY).modelName(OpenAiChatModelName.GPT_5).build();
		String response = model.chat(speech);
		print(response);
		booking.cancelMeetingRoom(id);
	}
}
