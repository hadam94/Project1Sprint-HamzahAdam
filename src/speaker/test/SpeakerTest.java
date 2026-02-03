package speaker.test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioSystem;

import org.junit.Test;
import org.vosk.Model;
import org.vosk.Recognizer;

public class SpeakerTest {
	
	//JUnit test
	@Test
	public void speechToTextFromAudioFile() throws Exception {
        InputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream("assets/sounds/duck.wav")));
        Recognizer recognizer = new Recognizer(new Model("assets/model"), 44100);
        byte[] b = new byte[4096];
        int bytes = audioStream.read(b);
        while(bytes >= 0) {
        	if(recognizer.acceptWaveForm(b, bytes)) {
        		System.out.println(recognizer.getResult());
        	} else {
        		System.out.println(recognizer.getPartialResult());
        	}
        	bytes = audioStream.read(b);
        }
        recognizer.close();
        audioStream.close();
	}

}
