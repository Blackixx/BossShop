package org.black_ixx.bossshop.managers;

import org.black_ixx.bossshop.points.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PointsManager {

	private IPointsAPI pa;

	public enum PointsPlugin {
		PLAYERPOINTS, COMMANDPOINTS, ENJIN_MINECRAFT_PLUGIN, CUSTOM;
		
		private String name;
		
		public void setCustom(String name) {
			this.name = name;
		}
		
		public String getCustom() {
			return name;
		}
	}

	public PointsManager() {

		PointsPlugin p = ClassManager.manager.getSettings().getPointsPlugin();
		if (p == null) {
			pa = new FailedPointsAPI();
			return;
		}

		Bukkit.getLogger().info("[BossShop] Seems like you defined a Points Plugin in the config... " + p.name().toLowerCase() + ". I hope it's installed on this server!");

		switch (p) {

			case COMMANDPOINTS:
				if (Bukkit.getPluginManager().getPlugin("CommandPoints") == null) {
					ClassManager.manager.getBugFinder().severe("You defined CommandPoints as Points Plugin... BUT IT WAS NOT FOUND?! Please download it at Bukkit.org!");
					return;
				}
				pa = new CommandPointsAPI();
				return;

			case ENJIN_MINECRAFT_PLUGIN:
				if (Bukkit.getPluginManager().getPlugin("Enjin Minecraft Plugin") == null) {
					ClassManager.manager.getBugFinder().severe("You defined Enjin Minecraft Plugin as Points Plugin... BUT IT WAS NOT FOUND?! Please download it at Bukkit.org!");
					return;
				}
				pa = new EnjinPointsAPI();
				return;

			case PLAYERPOINTS:
				if (Bukkit.getPluginManager().getPlugin("PlayerPoints") == null) {
					ClassManager.manager.getBugFinder().severe("You defined PlayerPoints as Points Plugin... BUT IT WAS NOT FOUND?! Please download it at Bukkit.org!");
					return;
				}
				pa = new PlayerPointsAPI();
				return;

		}

		IPointsAPI customPoints = PointsAPI.get(p.getCustom());

		if (customPoints != null) {
			pa = customPoints;
			return;
		}

		ClassManager.manager.getBugFinder().warn("PlayerPoints/CommandPoints was not found... You need one of that plugins if you want to work with Points! Get PlayerPoints there: http://dev.bukkit.org/server-mods/playerpoints/");
	}

	public int getPoints(OfflinePlayer player) {
		return pa.getPoints(player);
	}

	public int setPoints(OfflinePlayer player, int points) {
		return pa.setPoints(player, points);
	}

	public int givePoints(OfflinePlayer player, int points) {
		return pa.givePoints(player, points);
	}

	public int takePoints(OfflinePlayer player, int points) {
		return pa.takePoints(player, points);
	}

}
