package site.code4fun.util;

import java.util.List;

public class CalculatorUtil {
	public static double sumPointFromList(List<Float> lst, int multi) {
		double total = 0;
		for(Float item : lst) total += item;
		return total * multi;
	}

}
