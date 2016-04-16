package org.black_ixx.bossshop.points;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import me.BukkitPVP.PointsAPI.PointsAPI;

public class PointsAPIPlugin extends IPointsAPI {
	private PointsAPI pp;
	private BossShop plugin;

	public PointsAPIPlugin() {
		super("PointsAPI");

		plugin = ClassManager.manager.getPlugin();
		Plugin pointsApi = plugin.getServer().getPluginManager().getPlugin("PointsAPI");
		if (pointsApi != null) {
			pp = ((PointsAPI) pointsApi);
		}
	}

	@Override
	public int getPoints(OfflinePlayer player) {
		return pp.getPoints(player);
	}

	@Override
	public int setPoints(OfflinePlayer player, int points) {
		pp.setPoints(player.getPlayer(), points);
		return getPoints(player);
	}

	@Override
	public int takePoints(OfflinePlayer player, int points) {
		return setPoints(player, getPoints(player)-points);
	}

	@Override
	public int givePoints(OfflinePlayer player, int points) {
		pp.addPoints(player, points);
		return getPoints(player);
	}

}
