package org.black_ixx.bossshop.managers;

import org.black_ixx.bossapi.BossAPI;
import org.black_ixx.bossshop.BossShop;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.enjin.officialplugin.points.ErrorConnectingToEnjinException;
import com.enjin.officialplugin.points.PlayerDoesNotExistException;
import com.enjin.officialplugin.points.PointsAPI.Type;

import pgDev.bukkit.CommandPoints.CommandPoints;

public class PointsManager {


	private PointsAPI pa;

	public enum PointsPlugin{
		PLAYERPOINTS,
		COMMANDPOINTS,
		ENJIN_MINECRAFT_PLUGIN,
	}

	public PointsManager(){
		
		


		PointsPlugin p = ClassManager.manager.getSettings().getPointsPlugin();
		if(p!=null){

			Bukkit.getLogger().info("[BossShop] Seems like you defined a Points Plugin in the config... "+p.name().toLowerCase()+". I hope it's installed on this server!");

			switch(p){

			case COMMANDPOINTS:
				if (Bukkit.getPluginManager().getPlugin("CommandPoints")==null){
					ClassManager.manager.getBugFinder().severe("You defined CommandPoints as Points Plugin... BUT IT WAS NOT FOUND?! Please download it at Bukkit.org!");
					return;
				}
				pa = new CommandPointsAPI();
				return;

			case ENJIN_MINECRAFT_PLUGIN:
				if (Bukkit.getPluginManager().getPlugin("Enjin Minecraft Plugin")==null){
					ClassManager.manager.getBugFinder().severe("You defined Enjin Minecraft Plugin as Points Plugin... BUT IT WAS NOT FOUND?! Please download it at Bukkit.org!");
					return;
				}
				pa = new EnjinPointsAPI();
				return;

			case PLAYERPOINTS:
				if (Bukkit.getPluginManager().getPlugin("PlayerPoints")==null){
					ClassManager.manager.getBugFinder().severe("You defined PlayerPoints as Points Plugin... BUT IT WAS NOT FOUND?! Please download it at Bukkit.org!");
					return;
				}
				pa = new PlayerPointsAPI();
				return;

			}


			return;
		}

		if (Bukkit.getPluginManager().getPlugin("BossAPI")!=null){
			pa = new BossAPIAPI();
			return;
		}

		if (Bukkit.getPluginManager().getPlugin("PlayerPoints")!=null){
			pa = new PlayerPointsAPI();
			return;
		}

		if (Bukkit.getPluginManager().getPlugin("CommandPoints")!=null){
			pa = new CommandPointsAPI();
			return;
		}

		if (Bukkit.getPluginManager().getPlugin("Enjin Minecraft Plugin")!=null){
			pa = new EnjinPointsAPI();
			return;
		}

		pa = new FailedPointsPlugin();
		ClassManager.manager.getBugFinder().warn("PlayerPoints/CommandPoints was not found... You need one of that plugins if you want to work with Points! Get PlayerPoints there: http://dev.bukkit.org/server-mods/playerpoints/");




	}





	public int getPoints(String name){
		return pa.getPoints(name);
	}

	public int setPoints(String name, int i){
		return pa.setPoints(name, i);
	}

	public int givePoints(String name, int i){
		return pa.givePoints(name, i);
	}

	public int takePoints(String name, int i){
		return pa.takePoints(name, i);
	}







	//Template
	public abstract class PointsAPI{

		public abstract int getPoints(String name);
		public abstract int setPoints(String name, int i);
		public abstract int takePoints(String name, int i);
		public abstract int givePoints(String name, int i);
	}


	//No Points Plugin found?!
	public class FailedPointsPlugin extends PointsAPI{

		@Override
		public int getPoints(String name) {
			Bukkit.broadcastMessage("PlayerPoints/CommandPoints was not found... " +
					"You need one of that plugins if you want to work with Points! " +
					"Get PlayerPoints there: " +
					"http://dev.bukkit.org/server-mods/playerpoints/");
			return 0;
		}

		@Override
		public int setPoints(String name, int i) {
			Bukkit.broadcastMessage("PlayerPoints/CommandPoints was not found... " +
					"You need one of that plugins if you want to work with Points! " +
					"Get PlayerPoints there: " +
					"http://dev.bukkit.org/server-mods/playerpoints/");
			return 0;
		}

		@Override
		public int takePoints(String name, int i) {
			Bukkit.broadcastMessage("PlayerPoints/CommandPoints was not found... " +
					"You need one of that plugins if you want to work with Points! " +
					"Get PlayerPoints there: " +
					"http://dev.bukkit.org/server-mods/playerpoints/");
			return 0;
		}

		@Override
		public int givePoints(String name, int i) {
			Bukkit.broadcastMessage("PlayerPoints/CommandPoints was not found... " +
					"You need one of that plugins if you want to work with Points! " +
					"Get PlayerPoints there: " +
					"http://dev.bukkit.org/server-mods/playerpoints/");
			return 0;
		}



	}

	//CommandPoints
	public class CommandPointsAPI extends PointsAPI{

		private pgDev.bukkit.CommandPoints.CommandPointsAPI pp;
		private BossShop plugin;

		public CommandPointsAPI(){
			plugin=ClassManager.manager.getPlugin();
			Plugin commandPoints =plugin.getServer().getPluginManager().getPlugin("CommandPoints");
			if (commandPoints != null) {
				pp = ((CommandPoints)commandPoints).getAPI();
			} 

		}

		@Override
		public int getPoints(String name) {
			return pp.getPoints(name, plugin);
		}

		@Override
		public int setPoints(String name, int i) {
			pp.setPoints(name, i, plugin);
			return getPoints(name);
		}

		@Override
		public int takePoints(String name, int i) {
			pp.removePoints(name, i, "Purchase", plugin);
			return getPoints(name);
		}

		@Override
		public int givePoints(String name, int i) {
			pp.addPoints(name, i, "Reward", plugin);
			return getPoints(name);
		}


	}


	//PlayerPoints
	public class PlayerPointsAPI extends PointsAPI{

		private PlayerPoints pp;

		public PlayerPointsAPI(){

			final Plugin plugin= Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");	     
			if (plugin!= null) {
				pp = (PlayerPoints.class.cast(plugin));
			} else {
				//Sinnlos? Wird Klasse wird eh nur erstellt wenn es Plugin gibt
				ClassManager.manager.getBugFinder().warn("PlayerPoints was not found... You need it if you want to work with Points! Get it there: http://dev.bukkit.org/server-mods/playerpoints/");
				return;
			}

		}

		@Override
		public int getPoints(String name) {
			return pp.getAPI().look(name);
		}

		@Override
		public int setPoints(String name, int i) {
			pp.getAPI().set(name, i);
			return i;
		}

		@Override
		public int takePoints(String name, int i) {
			pp.getAPI().take(name, i);
			return getPoints(name);
		}

		@Override
		public int givePoints(String name, int i) {
			pp.getAPI().give(name, i);
			return getPoints(name);
		}

	}


	//Enjin
	public class EnjinPointsAPI extends PointsAPI{


		public EnjinPointsAPI(){
			final Plugin plugin= Bukkit.getServer().getPluginManager().getPlugin("Enjin Minecraft Plugin");	
			if (plugin!= null) {
				//Nothing weil API statisch ist. Nur Check ob es ueberhaupt Plugin gibt
			} else {
				//Sinnlos? Wird Klasse wird eh nur erstellt wenn es Plugin gibt
				ClassManager.manager.getBugFinder().warn("PlayerPoints was not found... You need it if you want to work with Points! Get it there: http://dev.bukkit.org/server-mods/playerpoints/");
				return;
			}

		}

		@Override
		public int getPoints(String name) {
			try {
				return com.enjin.officialplugin.points.PointsAPI.getPointsForPlayer(name);
			} catch (PlayerDoesNotExistException e) {
				ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to get Player "+name+". Not Existing!");
			} catch (ErrorConnectingToEnjinException e) {
				ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to connect to Enjin!");
			}
			return 0;
		}

		@Override
		public int setPoints(String name, int i) {
			com.enjin.officialplugin.points.PointsAPI.modifyPointsToPlayerAsynchronously(name, i, Type.SetPoints);
			return i;
		}

		@Override
		public int takePoints(String name, int i) {
			try {
				return com.enjin.officialplugin.points.PointsAPI.modifyPointsToPlayer(name, i, Type.RemovePoints);
			} catch (NumberFormatException e) {
				ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to take Points... \"NumberFormatException\" Tried to take "+i+" Points from "+name+".");
			} catch (PlayerDoesNotExistException e) {
				ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to get Player "+name+". Not Existing!");
			} catch (ErrorConnectingToEnjinException e) {
				ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to connect to Enjin!");
			}
			return getPoints(name);
		}

		@Override
		public int givePoints(String name, int i) {
			try {
				return com.enjin.officialplugin.points.PointsAPI.modifyPointsToPlayer(name, i, Type.AddPoints);
			} catch (NumberFormatException e) {
				ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to take Points... \"NumberFormatException\" Tried to take "+i+" Points from "+name+".");
			} catch (PlayerDoesNotExistException e) {
				ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to get Player "+name+". Not Existing!");
			} catch (ErrorConnectingToEnjinException e) {
				ClassManager.manager.getBugFinder().warn("[Enjin Minecraft Plugin] Not able to connect to Enjin!");
			}
			return getPoints(name);
		}

	}


	public class BossAPIAPI extends PointsAPI{

		private BossAPI plugin;

		public BossAPIAPI(){
			final Plugin plugin= Bukkit.getServer().getPluginManager().getPlugin("BossAPI");	
			if (plugin!= null) {
				this.plugin=(BossAPI) plugin;
			} else {
				//Sinnlos? Wird Klasse wird eh nur erstellt wenn es Plugin gibt
				ClassManager.manager.getBugFinder().warn("PlayerPoints was not found... You need it if you want to work with Points! Get it there: http://dev.bukkit.org/server-mods/playerpoints/");
				return;
			}

		}

		@Override
		public int getPoints(String name) {
			return plugin.getPointsPluginManager().getInterface().getPoints(name);
		}

		@Override
		public int setPoints(String name, int i) {
			plugin.getPointsPluginManager().getInterface().setPoints(name, i);
			return plugin.getPointsPluginManager().getInterface().getPoints(name);
		}

		@Override
		public int takePoints(String name, int i) {
			plugin.getPointsPluginManager().getInterface().takePoints(name, i);
			return plugin.getPointsPluginManager().getInterface().getPoints(name);
		}

		@Override
		public int givePoints(String name, int i) {
			plugin.getPointsPluginManager().getInterface().givePoints(name, i);
			return plugin.getPointsPluginManager().getInterface().getPoints(name);
		}



	}

}


