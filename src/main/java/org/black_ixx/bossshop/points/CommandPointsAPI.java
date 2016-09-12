package org.black_ixx.bossshop.points;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import pgDev.bukkit.CommandPoints.CommandPoints;

public class CommandPointsAPI extends IPointsAPI {
	private pgDev.bukkit.CommandPoints.CommandPointsAPI pp;
	private BossShop plugin;

	public CommandPointsAPI() {
		super("CommandPoints");

		plugin = ClassManager.manager.getPlugin();
		Plugin commandPoints = plugin.getServer().getPluginManager().getPlugin("CommandPoints");
		if (commandPoints != null) {
			pp = ((CommandPoints) commandPoints).getAPI();
		}
	}

	@Override
	public int getPoints(OfflinePlayer player) {
		return pp.getPoints(player.getName(), plugin);
	}

	@Override
	public int setPoints(OfflinePlayer player, int points) {
		pp.setPoints(player.getName(), points, plugin);
		return points;
	}

	@Override
	public int takePoints(OfflinePlayer player, int points) {
		pp.removePoints(player.getName(), points, "Purchase", plugin);
		return getPoints(player);
	}

	@Override
	public int givePoints(OfflinePlayer player, int points) {
		pp.addPoints(player.getName(), points, "Reward", plugin);
		return getPoints(player);
	}

}
