

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import speech.util.SpeechListenerUtil;

public class SoundFileTest {
	
	//JUnit test
	@Test
	public void speechToTextFromAudioFile() throws Exception {
        String speech = SpeechListenerUtil.getSpeechFromAudioFile(new File("assets/sounds/duck.wav"), true);
        assertEquals("incorrect string!", "the duck test the duck will fly quack and swim\n", speech);
	}

}
