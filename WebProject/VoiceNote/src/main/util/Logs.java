package main.util;

public class Logs {
	private static boolean isDebug = true;
	
	public static void d(String msg) {
		if (isDebug) {
			System.out.println(msg);
		}
	}
	
	public static void e(String msg) {
		if (isDebug) {
			System.err.println(msg);
		}
	}
}
