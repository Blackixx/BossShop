package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.points.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public class PointsManager {

	private IPointsAPI pa;

	public enum PointsPlugin {
		PLAYERPOINTS("PlayerPoints", "PP"), 
		COMMANDPOINTS("CommandPoints", "CP"), 
		ENJIN_MINECRAFT_PLUGIN("EnjinMinecraftPlugin", "Enjin",  "EMP"), 
		POINTSAPI("PointsAPI", "PAPI"), 
		TOKENENCHANT("TokenEnchant", "TE", "TokenEnchants"),
		VOTINGPLUGIN("VotingPlugin", "VP"),
		Jobs("Jobs", "JobsReborn", "JobsPoints"),
		CUSTOM;

		private String[] nicknames;
		private String name;

		private PointsPlugin(String... nicknames){
			this.nicknames = nicknames;
		}

		public void setCustom(String name) {
			this.name = name;
		}

		public String getCustom() {
			return name;
		}
		public String[] getNicknames(){
			return nicknames;
		}
		public String getPluginName(){
			if(getNicknames()==null){
				return null;
			}
			if(getNicknames().length==0){
				return null;
			}
			return getNicknames()[0];
		}
	}

	public PointsManager() {
		PointsPlugin p = ClassManager.manager.getSettings().getPointsPlugin();
		if (p == null) {
			pa = new FailedPointsAPI();
			return;
		}


		switch (p) {

		case COMMANDPOINTS:
			if (Bukkit.getPluginManager().getPlugin("CommandPoints") == null) {
				ClassManager.manager.getBugFinder().severe("You defined CommandPoints as the Points Plugin... BUT IT WAS NOT FOUND?! Please download it at Bukkit.org!");
				break;
			}
			pa = new CommandPointsAPI();
			break;

		case ENJIN_MINECRAFT_PLUGIN:
			if (Bukkit.getPluginManager().getPlugin("EnjinMinecraftPlugin") == null) {
				ClassManager.manager.getBugFinder().severe("You defined Enjin Minecraft Plugin as the Points Plugin... BUT IT WAS NOT FOUND?! Please download it at Bukkit.org!");
				break;
			}

			Plugin enjinplugin = Bukkit.getPluginManager().getPlugin("EnjinMinecraftPlugin");
			if(enjinplugin.getDescription().getVersion().startsWith("2")){ //When using an older Enjin version
				pa = new EnjinPointsAPI_v2();
				break;

			}
			pa = new EnjinPointsAPI();
			break;

		case PLAYERPOINTS:
			if (Bukkit.getPluginManager().getPlugin("PlayerPoints") == null) {
				ClassManager.manager.getBugFinder().severe("You defined PlayerPoints as the Points Plugin... BUT IT WAS NOT FOUND?! Please download it at Bukkit.org!");
				break;
			}
			pa = new PlayerPointsAPI();
			break;

		case POINTSAPI:
			if (Bukkit.getPluginManager().getPlugin("PointsAPI") == null) {
				ClassManager.manager.getBugFinder().severe("You defined PointsAPI as the Points Plugin... BUT IT WAS NOT FOUND?! Please download it at Bukkit.org!");
				break;
			}
			pa = new PointsAPIPlugin();
			break;

		case TOKENENCHANT:
			if (Bukkit.getPluginManager().getPlugin("TokenEnchant") == null) {
				ClassManager.manager.getBugFinder().severe("You defined TokenEnchant as the Points(/Token) Plugin ... BUT IT WAS NOT FOUND?! Please download it at spigotmc.org!");
				break;
			}
			pa = new TokenEnchantAPIPlugin();
			break;

		case VOTINGPLUGIN:
			if (Bukkit.getPluginManager().getPlugin("VotingPlugin") == null) {
				ClassManager.manager.getBugFinder().severe("You defined VotingPlugin as the Points Plugin ... BUT IT WAS NOT FOUND?! Please download it at spigotmc.org!");
				break;
			}
			pa = new VotingPluginAPI();
			break;

		case Jobs:
			if (Bukkit.getPluginManager().getPlugin("Jobs") == null) {
				ClassManager.manager.getBugFinder().severe("You defined Jobs as the Point Plugin ... BUT IT WAS NOT FOUND?! Please download it at spigotmc.org!");
				break;
			}
			pa = new JobsPointsAPI();
			break;

		case CUSTOM:
			IPointsAPI customPoints = PointsAPI.get(p.getCustom());
			if (customPoints != null) {
				pa = customPoints;
				break;
			}	
			break;

		}

		if(pa == null){
			ClassManager.manager.getBugFinder().warn("No PointsPlugin was found... You need one if you want BossShop to work with Points! Get PlayerPoints here: http://dev.bukkit.org/server-mods/playerpoints/");		
			pa = new FailedPointsAPI();
		}


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
