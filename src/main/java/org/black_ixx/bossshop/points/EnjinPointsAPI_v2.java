package org.black_ixx.bossshop.points;

import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import com.enjin.officialplugin.points.ErrorConnectingToEnjinException;
import com.enjin.officialplugin.points.PlayerDoesNotExistException;
import com.enjin.officialplugin.points.PointsAPI.Type;


public class EnjinPointsAPI_v2 extends IPointsAPI {

	public EnjinPointsAPI_v2() {
		super("EnjinMinecraftPlugin");

		final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("EnjinMinecraftPlugin");
		if (plugin == null) {
			// Sinnlos? Wird Klasse wird eh nur erstellt wenn es Plugin gibt
			ClassManager.manager.getBugFinder().warn("PlayerPoints was not found... You need it if you want to work with Points! Get it there: http://dev.bukkit.org/server-mods/playerpoints/");
			return;
		}

	}

	public int getPoints(OfflinePlayer player) {
		try {
			return com.enjin.officialplugin.points.PointsAPI.getPointsForPlayer(player.getName());
		} catch (PlayerDoesNotExistException e) {
			ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to get Player " + player.getName() + ". Not existing!");
		} catch (ErrorConnectingToEnjinException e) {
			ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to connect to Enjin!");
		}
		return 0;
	}

	public int setPoints(OfflinePlayer player, int points) {
		com.enjin.officialplugin.points.PointsAPI.modifyPointsToPlayerAsynchronously(player.getName(), points, Type.SetPoints);
		return points;
	}

	public int takePoints(OfflinePlayer player, int points) {
		String name = player.getName();
		try {
			return com.enjin.officialplugin.points.PointsAPI.modifyPointsToPlayer(name, points, Type.RemovePoints);
		} catch (NumberFormatException e) {
			ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to take Points... \"NumberFormatException\" Tried to take " + points + " Points from " + name + ".");
		} catch (PlayerDoesNotExistException e) {
			ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to get Player " + name + ". Not existing!");
		} catch (ErrorConnectingToEnjinException e) {
			ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to connect to Enjin!");
		}
		return getPoints(player);
	}

	public int givePoints(OfflinePlayer player, int points) {
		String name = player.getName();
		try {
			return com.enjin.officialplugin.points.PointsAPI.modifyPointsToPlayer(name, points, Type.AddPoints);
		} catch (NumberFormatException e) {
			ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to take Points... \"NumberFormatException\" Tried to take " + points + " Points from " + name + ".");
		} catch (PlayerDoesNotExistException e) {
			ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to get Player " + name + ". Not existing!");
		} catch (ErrorConnectingToEnjinException e) {
			ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to connect to Enjin!");
		}
		return getPoints(player);
	}
}
