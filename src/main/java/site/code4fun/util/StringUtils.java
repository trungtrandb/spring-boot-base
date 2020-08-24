package site.code4fun.util;

import java.math.BigDecimal;
import java.util.List;
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
	
	public static String stringFromList(List<?> lst) {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < lst.size();  i ++)
		{	
			if(i != 0) sb.append(",");
		    sb.append(lst.get(i).toString().replace(".0", ""));
		}
		return sb.toString();
	}
	
	public static float round1(String s) {
		Float d = Float.parseFloat(s);
		return BigDecimal.valueOf(d).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
	public static String cleanToFloat(String str) {
		return str.replaceAll(",", "\\.").replaceAll("[^0-9\\s\\.]", "");
	}
}
