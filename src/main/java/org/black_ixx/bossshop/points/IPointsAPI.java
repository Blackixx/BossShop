package org.black_ixx.bossshop.points;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public abstract class IPointsAPI {
	private final String name;

	public IPointsAPI(String name) {
		if(!(this instanceof FailedPointsAPI)){
			Bukkit.getLogger().info("[BossShop] Successfully hooked into the Points plugin '" + name + "'!");
		}
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract int getPoints(OfflinePlayer player);

	public abstract int setPoints(OfflinePlayer player, int points);

	public abstract int takePoints(OfflinePlayer player, int points);

	public abstract int givePoints(OfflinePlayer player, int points);

	public void register() {
		PointsAPI.register(this);
	}
}
