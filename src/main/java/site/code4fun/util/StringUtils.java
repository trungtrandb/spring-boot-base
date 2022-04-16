package site.code4fun.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	private static final String ALPHABET_CHARACTER = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final Random random = new Random();
	
	public static boolean isNull(String inputString) {
		return ((inputString == null) || (inputString.equals("")) || (inputString.trim().length() == 0));
	}
	
	public static String randomString(int length) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < length; i++) {
			str.append(ALPHABET_CHARACTER.charAt(random.nextInt(ALPHABET_CHARACTER.length())));
		}
		return str.toString();
	}
	
	public static String randomString() {
		return randomString(6);
	}
	
	public static String stringFromList(List<?> lst) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lst.size();  i ++)
		{	
			if(i != 0){
				sb.append(",");
			}
		    sb.append(lst.get(i).toString().replace(".0", ""));
		}
		return sb.toString();
	}
	
	public static float round1(String s) {
		float d = Float.parseFloat(s);
		return BigDecimal.valueOf(d).setScale(1, RoundingMode.HALF_UP).floatValue();
	}
	
	public static String cleanToFloat(String str) {
		return str.replace(",", "\\.").replaceAll("[^0-9\\s.]", "");
	}
}
