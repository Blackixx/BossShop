package org.black_ixx.bossshop.points;

import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import com.enjin.core.EnjinServices;
import com.enjin.rpc.mappings.mappings.general.RPCData;
import com.enjin.rpc.mappings.services.PointService;



public class EnjinPointsAPI extends IPointsAPI {

	public EnjinPointsAPI() {
		super("EnjinMinecraftPlugin");

		final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("EnjinMinecraftPlugin");
		if (plugin == null) {
			// Sinnlos? Wird Klasse wird eh nur erstellt wenn es Plugin gibt
			ClassManager.manager.getBugFinder().warn("PlayerPoints was not found... You need it if you want to work with Points! Get it there: http://dev.bukkit.org/server-mods/playerpoints/");
			return;
		}
	}

	public int getPoints(OfflinePlayer player) {
		RPCData<Integer> data = EnjinServices.getService(PointService.class).get(player.getName());
		if(data == null || data.getResult() == null){
			ClassManager.manager.getBugFinder().warn("Hooked into EnjinMinecraftPlugin but unable to access the points of player "+player.getName()+". Maybe the points database is not set up correctly?");
			return 0;
		}
		return data.getResult();
	}

	public int setPoints(OfflinePlayer player, int points) {
		EnjinServices.getService(PointService.class).set(player.getName(), points);
		return points;
	}

	public int takePoints(OfflinePlayer player, int points) {
		EnjinServices.getService(PointService.class).remove(player.getName(), points);
		return getPoints(player);
	}

	public int givePoints(OfflinePlayer player, int points) {
		EnjinServices.getService(PointService.class).add(player.getName(), points);
		return getPoints(player);
	}
}
