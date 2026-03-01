package speech.test;

import java.io.File;

import org.junit.Test;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import speech.AIAgentDemo;
import speech.booking.RoomBookingRequests;
import speech.util.SpeechListenerUtil;
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
		String response = model.chat(speech + "? \n\n" + booking.listAvalibleMeetingRooms());
		print(response);
	}
}
