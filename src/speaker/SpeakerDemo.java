package speaker;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.junit.Test;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

public class SpeakerDemo {
	
	public static void main(String[] args) throws Exception {
		Speech speech = getFileFromInput();
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
        print("Begin speech!");
        Thread.sleep(250L);
        String partialResult = "";
        while(bytes >= 0) {
        	if((System.currentTimeMillis() - startTime) > (speech.speechDuration*1000L)) {
        		break;
        	}
            if(recognizer.acceptWaveForm(b, bytes)) {
            	String[] result = parseJson(recognizer.getResult()).split(" : ");
            	print(result[1]);
            	wordsSpoken += result[1];
            	partialResult = null;
            } else {
            	String[] result = parseJson(recognizer.getPartialResult()).split(" : ");
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
        
        saveSpeechToFile(wordsSpoken.toString(), speech.speechFile);
        print("All spoken words have been saved to " + speech.speechFile.getPath());
	}
	
	//parsing
	private static String parseJson(String speechResult) {
    	String speech = speechResult.substring(4);
    	speech = speech.substring(0, speech.length() - 1).replaceAll(String.valueOf('"'), "");
    	return speech;
    	
	}
	
	private static Speech getFileFromInput() {
		Scanner scanner = new Scanner(System.in);
		print("Where do you want to save your speech in?");
		String fileLocation = scanner.nextLine();
		int speechDuration;
		while(true) {
			try {
				print("how long do you want your speech to be? (seconds)");
				speechDuration = Integer.parseInt(scanner.nextLine());
				break;
			} catch(NumberFormatException e) {
				print(e.getMessage() + " is not a valid number!");
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		File file = new File(fileLocation);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		scanner.close();
		return new Speech(file, speechDuration);
	}
	
	/*The BufferedWriter class overwrites any existing text in the current file. 
	 * we need to scan all of the existing text in the file, and append that to a string.
	 * Then we can rewrite the existing text and any words you spoken to not loose any data.
	 */
	private static void saveSpeechToFile(String speech, File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		String existingText = "";
		while(line != null) {
			existingText += line + "\n";
			line = reader.readLine();
		}
		reader.close();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(existingText);
		writer.write(speech);
		writer.close();
	}
	
	private static void print(Object args) {
		System.out.println(args);
	}
	
	private record Speech(File speechFile, int speechDuration) {
		
	}
	
	//JUnit test
	@Test
	public void speechToTextFromAudioFile() throws Exception {
        InputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream("assets/sounds/duck.wav")));
        Recognizer recognizer = new Recognizer(new Model("assets/model"), 44100);
        byte[] b = new byte[4096];
        int bytes = audioStream.read(b);
        while(bytes >= 0) {
        	if(recognizer.acceptWaveForm(b, bytes)) {
        		print(recognizer.getResult());
        	} else {
        		print(recognizer.getPartialResult());
        	}
        	bytes = audioStream.read(b);
        }
        recognizer.close();
        audioStream.close();
	}
}
