package org.black_ixx.bossshop.points;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
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
		if(player instanceof Player){
			return pp.getPoints((Player) player);
		}
		return pp.getPoints(player);
	}

	@Override
	public int setPoints(OfflinePlayer player, int points) {
		if(player instanceof Player){
			pp.setPoints((Player) player, points);
		}else{
			pp.setPoints(player, points);
		}
		return points;
	}

	@Override
	public int takePoints(OfflinePlayer player, int points) {
		if(player instanceof Player){
			pp.removePoints((Player) player, points);
		}else{
			pp.removePoints(player, points);
		}
		return getPoints(player);
	}

	@Override
	public int givePoints(OfflinePlayer player, int points) {
		if(player instanceof Player){
			pp.addPoints((Player) player, points);
		}else{
			pp.addPoints(player, points);
		}
		return getPoints(player);
	}

}
