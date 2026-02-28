package speech.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;
import static speech.util.PrintUtil.print;

public class SpeechListenerUtil {
	
	/**
	 * Listens to your speech through the microphone that is currently selected by the system. 
	 * @param The amount of seconds needed to speak.
	 * @return The words you have spoken, in a string format.
	 */
	public static String getSpeechFromMicrophone(float duration) {
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
	        String wordsSpoken = "";
	        print("Begin speech!");
	        Thread.sleep(250L);
	        long startTime = System.currentTimeMillis();
	        String partialResult = "";
	        while(bytes >= 0) {
	        	if((System.currentTimeMillis() - startTime) > (duration*1000L)) {
	        		break;
	        	}
	            if(recognizer.acceptWaveForm(b, bytes)) {
	            	String[] result = ParseSpeechUtil.parseSpeech(recognizer.getResult()).split(" : ");
	            	wordsSpoken += result[1];
	            	partialResult = null;
	            //	print(wordsSpoken);
	            } else {
	            	String[] result = ParseSpeechUtil.parseSpeech(recognizer.getPartialResult()).split(" : ");
	            	partialResult = result[1];
	            //	print(wordsSpoken);
	            }
	            //read the audio from microphone.
	        	bytes = audio.read(b, 0, b.length);
	        }
	        
	        if(partialResult != null) {
	        	wordsSpoken += partialResult;
	        }
	        
          //  print(wordsSpoken);
	        
	        audio.stop();
	        audio.close();
	        recognizer.close();
	        model.close();
	        return wordsSpoken;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Listens to speech through an audio file.
	 * The audio file must be in a WAV format, mono channel, and 44100khz sample rate for the speech recognition to work properly.
	 * @param The file location of the audio file, and whether or not to print speech inside the console.
	 * @return The words spoken in the audio file, in a string format.
	 * @throws IllegalArgumentException if the file is not in a WAV format.
	 */
	public static String getSpeechFromAudioFile(File audioFileLocation, boolean printSpeech) {
		try {
			if(!audioFileLocation.getPath().endsWith(".wav")) {
				throw new IllegalArgumentException("The audio file must be in a WAV format!");
			}
			InputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(audioFileLocation)));
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
	        return speech;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
