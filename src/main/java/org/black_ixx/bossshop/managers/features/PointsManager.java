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
		Kingdoms("Kingdoms", "Kingdom"),
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
		this(ClassManager.manager.getSettings().getPointsPlugin());
	}
	
	public PointsManager(PointsPlugin p) {
		if (p == null) {
			pa = new FailedPointsAPI();
			return;
		}

		if(Bukkit.getPluginManager().getPlugin(p.getPluginName()) == null){
			ClassManager.manager.getBugFinder().severe("You defined "+p.getPluginName()+" as Points Plugin... BUT IT WAS NOT FOUND?! Please install it or use an alternative like PlayerPoints (http://dev.bukkit.org/server-mods/playerpoints/). If you want BossShop to auto-detect your Points plugin simply set 'PointsPlugin: auto-detect'.");
			pa = new FailedPointsAPI();
			return;
		}

		switch (p) {

		case COMMANDPOINTS:
			pa = new CommandPointsAPI();
			break;

		case ENJIN_MINECRAFT_PLUGIN:
			Plugin enjinplugin = Bukkit.getPluginManager().getPlugin("EnjinMinecraftPlugin");
			if(enjinplugin.getDescription().getVersion().startsWith("2")){ //When using an older Enjin version
				pa = new EnjinPointsAPI_v2();
				break;

			}
			pa = new EnjinPointsAPI();
			break;

		case PLAYERPOINTS:
			pa = new PlayerPointsAPI();
			break;

		case POINTSAPI:
			pa = new PointsAPIPlugin();
			break;

		case TOKENENCHANT:
			pa = new TokenEnchantAPIPlugin();
			break;
			
		case Kingdoms:
			pa = new KingdomsAPI();
			break;

		case Jobs:
			pa = new JobsPointsAPI();
			break;
			
		case VOTINGPLUGIN:
			pa = new VotingPluginAPI();
			break;

		case CUSTOM:
			IPointsAPI customPoints = PointsAPI.get(p.getCustom());
			if (customPoints == null) {
				break;
			}	
			pa = customPoints;
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
