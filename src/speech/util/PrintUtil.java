package speech.util;

public class PrintUtil {
	
	public static void print(Object args) {
		print(args, true);
	}
	
	public static void print(Object args, boolean newLine) {
		if(newLine) {
			System.out.println(args);
		} else {
			System.out.print(args);
		}
	}
}
