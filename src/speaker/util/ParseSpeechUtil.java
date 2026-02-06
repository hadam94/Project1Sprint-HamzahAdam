package speaker.util;

public class ParseSpeechUtil {
	
	//parsing
	public static String parseSpeech(String speechResult) {
    	String speech = speechResult.substring(4);
    	speech = speech.substring(0, speech.length() - 1).replaceAll(String.valueOf('"'), "");
    	return speech;
	}
}
