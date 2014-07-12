package org.black_ixx.bossshop.points;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class FailedPointsAPI extends IPointsAPI {

	public FailedPointsAPI() {
		super("Failed");
	}

	public int getPoints(OfflinePlayer player) {
		Bukkit.broadcastMessage("PlayerPoints/CommandPoints was not found... " + "You need one of that plugins if you want to work with Points! " + "Get PlayerPoints there: " + "http://dev.bukkit.org/server-mods/playerpoints/");
		return 0;
	}

	public int setPoints(OfflinePlayer player, int points) {
		Bukkit.broadcastMessage("PlayerPoints/CommandPoints was not found... " + "You need one of that plugins if you want to work with Points! " + "Get PlayerPoints there: " + "http://dev.bukkit.org/server-mods/playerpoints/");
		return 0;
	}

	public int takePoints(OfflinePlayer player, int points) {
		Bukkit.broadcastMessage("PlayerPoints/CommandPoints was not found... " + "You need one of that plugins if you want to work with Points! " + "Get PlayerPoints there: " + "http://dev.bukkit.org/server-mods/playerpoints/");
		return 0;
	}

	public int givePoints(OfflinePlayer player, int points) {
		Bukkit.broadcastMessage("PlayerPoints/CommandPoints was not found... " + "You need one of that plugins if you want to work with Points! " + "Get PlayerPoints there: " + "http://dev.bukkit.org/server-mods/playerpoints/");
		return 0;
	}
}
