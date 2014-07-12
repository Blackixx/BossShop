package org.black_ixx.bossshop.points;

import java.util.HashMap;

public class PointsAPI {
	private static HashMap<String, IPointsAPI> interfaces = new HashMap<String, IPointsAPI>();

	public static void register(IPointsAPI points) {
		interfaces.put(points.getName(), points);		
	}
	
	public static IPointsAPI get(String name) {
		return interfaces.get(name);
	}
}
