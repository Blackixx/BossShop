package org.black_ixx.bossshop.points;

import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public class PlayerPointsAPI extends IPointsAPI {
	public PlayerPointsAPI() {
		super("PlayerPoints");

		final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");
		if (plugin != null) {
			pp = (PlayerPoints.class.cast(plugin));
		} else {
			// Sinnlos? Wird Klasse wird eh nur erstellt wenn es Plugin gibt
			ClassManager.manager.getBugFinder().warn("PlayerPoints was not found... You need it if you want to work with Points! Get it there: http://dev.bukkit.org/server-mods/playerpoints/");
			return;
		}
	}

	private PlayerPoints pp;

	@Override
	public int getPoints(OfflinePlayer player) {
		return pp.getAPI().look(player.getUniqueId());
	}

	@Override
	public int setPoints(OfflinePlayer player, int points) {
		pp.getAPI().set(player.getUniqueId(), points);
		return points;
	}

	@Override
	public int takePoints(OfflinePlayer player, int points) {
		pp.getAPI().take(player.getUniqueId(), points);
		return getPoints(player);
	}

	@Override
	public int givePoints(OfflinePlayer player, int points) {
		pp.getAPI().give(player.getUniqueId(), points);
		return getPoints(player);
	}

}
