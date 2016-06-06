package org.black_ixx.bossshop.misc;

public class MathTools{


	public static double round(double d, int decimal_place){
		int a = (int) Math.pow(10, decimal_place);
		int i = (int) (Math.round(d)*a);
		return  ((double)i) / a;		
	}

}
