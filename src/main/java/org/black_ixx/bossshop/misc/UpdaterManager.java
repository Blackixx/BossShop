package org.black_ixx.bossshop.misc;

import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.ReleaseType;
import net.gravitydevelopment.updater.Updater.UpdateType;

import java.io.File;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Bukkit;


public class UpdaterManager{



	private boolean update = false;
	private String name = "";
	private ReleaseType type = null;
	private String version = "";
	private String link = "http://dev.bukkit.org/bukkit-plugins/bossshop/files/";


	public String getName(){
		return name;
	}

	public ReleaseType getType(){
		return type;
	}

	public String getVersion(){
		return version;
	}

	public String getLink(){
		return link;
	}


	public void run(File pluginfile) {
		if(!ClassManager.manager.getSettings().getUpdaterEnabled()){
			return;
		}

		BossShop plugin = ClassManager.manager.getPlugin();

		boolean auto_download_update = false; //ClassManager.manager.getSettings().getAutoDownloadUpdateEnabled();
		Updater updater = new Updater(plugin, 65031, pluginfile, auto_download_update?UpdateType.DEFAULT:UpdateType.NO_DOWNLOAD, true){
			@Override
			public boolean shouldUpdate(String localVersion, String remoteVersion){
				if(getWorth(remoteVersion) > getWorth(localVersion)){
					Bukkit.getLogger().info("Found BossShop '"+remoteVersion+"'. You currently are using '"+localVersion+"'.");
					return true;
				}
				return false;
			}

		};
		update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
		name = updater.getLatestName(); // Get the latest name
		version = updater.getLatestGameVersion(); // Get the latest game version
		type = updater.getLatestType(); // Get the latest file's ty
		if(update==true){
			ClassManager.manager.getBugFinder().warn("Update was found: "+name+". You can download it here: "+link+".");
			ClassManager.manager.setUpdaterManager(this);
			if(plugin.getPlayerJoinListener()!=null){
				plugin.getPlayerJoinListener().setEnabled(true);
			}
		}
	}

	private double getWorth(String versionname){
		versionname = versionname.replace("v", "");
		double goal = 0;
		int n = 0;
		String[] parts = versionname.split("\\.");
		for(String s : parts){
			try{
				double increase = Integer.parseInt(s.trim());
				goal += increase*Math.pow(10, -n);
				n++;

			} catch (NumberFormatException e){ //When the version name contains any other letters it must be special (like Beta) and nothing to download
				return -1;
			}
		}
		return goal;
	}


}
