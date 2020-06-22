package site.code4fun.util;

import java.util.Random;

public class StringUtils {
	final static String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static boolean isNull(String inputString) {
		return ((inputString == null) || (inputString.equals("")) || (inputString.trim().length() == 0));
	}
	
	public static String randomString(int length) {
		Random r = new Random();
		String str = "";
		for (int i = 0; i < length; i++) {
			str += alphabet.charAt(r.nextInt(alphabet.length()));
		}
		return str;
	}
	
	public static String randomString() {
		return randomString(6);
	}
}
