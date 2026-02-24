package speech.microphone;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import speech.util.ParseSpeechUtil;

public class SpeechListener {
	
	/**
	 * Listens to your speech through the microphone that is currently selected by the system. 
	 * @param The amount of seconds needed to speak.
	 * @return The words you have spoken.
	 */
	public static String getPromptFromSpeech(float duration) {
		try {
			LibVosk.setLogLevel(LogLevel.DEBUG);
			Model model = new Model("assets/model");
	        //44.1khz for cd quality, mono channel
	        AudioFormat format = new AudioFormat(44100f, 16, 1, true, false);
			TargetDataLine audio = (TargetDataLine) AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, format));
			audio.open(format);
			audio.start();
	        
	        Recognizer recognizer = new Recognizer(model, 44100f);
	        byte[] b = new byte[4096];
	        int bytes = audio.read(b, 0, b.length);
	        long startTime = System.currentTimeMillis();
	        String wordsSpoken = "";
	       // print("Begin speech!");
	        Thread.sleep(250L);
	        String partialResult = "";
	        while(bytes >= 0) {
	        	if((System.currentTimeMillis() - startTime) > (duration*1000L)) {
	        		break;
	        	}
	            if(recognizer.acceptWaveForm(b, bytes)) {
	            	String[] result = ParseSpeechUtil.parseSpeech(recognizer.getResult()).split(" : ");
	            	wordsSpoken += result[1];
	            	partialResult = null;
	            } else {
	            	String[] result = ParseSpeechUtil.parseSpeech(recognizer.getPartialResult()).split(" : ");
	            	partialResult = result[1];
	            }
	            //read the audio from microphone.
	        	bytes = audio.read(b, 0, b.length);
	        }
	        
	        if(partialResult != null) {
	        	wordsSpoken += partialResult;
	        }
	        
	        audio.stop();
	        recognizer.close();
	        model.close();
	        return wordsSpoken;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
