package speech.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioSystem;

import org.junit.Test;
import org.vosk.Model;
import org.vosk.Recognizer;

import speech.util.ParseSpeechUtil;

public class SoundFileTest {
	
	//JUnit test
	@Test
	public void speechToTextFromAudioFile() throws Exception {
        InputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream("assets/sounds/duck.wav")));
        Recognizer recognizer = new Recognizer(new Model("assets/model"), 44100);
        byte[] b = new byte[4096];
        int bytes = audioStream.read(b);
        String speech = "";
        while(bytes >= 0) {
        	if(recognizer.acceptWaveForm(b, bytes)) {
        		speech = ParseSpeechUtil.parseSpeech(recognizer.getResult()).split(" : ")[1];
        		System.out.println(speech);
        	} else {
        		speech = ParseSpeechUtil.parseSpeech(recognizer.getPartialResult()).split(" : ")[1];
        		System.out.println(speech);
        	}
        	bytes = audioStream.read(b);
        }
        recognizer.close();
        audioStream.close();
        assertEquals("incorrect string!", "the duck test the duck will fly quack and swim\n", speech);
	}

}
