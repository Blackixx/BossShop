package org.black_ixx.bossshop.misc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathTools{

	
	private static DecimalFormat df = new DecimalFormat("#");


	public static String displayDouble(double d, int decimal_place){
		df.setMaximumFractionDigits(decimal_place);
		return df.format(d);
	}

	public static double round(double d, int decimal_place){
		int a = (int) Math.pow(10, decimal_place);
		int i = (int) (Math.round(d*a));
		double result = ((double)i) / a;

		if(Math.abs(result - d) > 1){ //Difference between result and original value is too big
			return roundBigValue(d, decimal_place);
		}


		return  result;		
	}

	public static double roundBigValue(double value, int decimal_place) {
		if (decimal_place < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(decimal_place, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
