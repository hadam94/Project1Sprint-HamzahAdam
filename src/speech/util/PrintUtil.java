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
	
	public static <T> void printArray(T[] array) {
		print("[", false);
		for(int i = 0; i < array.length; ++i) {
			T element = array[i];
			print(element + (i == array.length - 1 ? "" : ", "), false);
		}
		print("]");
	}
}
