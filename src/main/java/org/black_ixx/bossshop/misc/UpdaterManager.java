package org.black_ixx.bossshop.misc;

import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.ReleaseType;
import net.gravitydevelopment.updater.Updater.UpdateType;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;


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

	
	public void run() {
		
		if(!ClassManager.manager.getSettings().isUpdaterEnabled()){
			return;
		}
		
		BossShop plugin = ClassManager.manager.getPlugin();

		Updater updater = new Updater(plugin, 65031, plugin.getDataFolder(), UpdateType.NO_DOWNLOAD, true);
		update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
		name = updater.getLatestName(); // Get the latest name
		version = updater.getLatestGameVersion(); // Get the latest game version
		type = updater.getLatestType(); // Get the latest file's type
		
		if(update==true){
		
			ClassManager.manager.getBugFinder().addMessage("Update was found: "+name+". You can download it here: "+link+".");
			System.out.print("[BossShop] Update was found: "+name+". You can download it here: "+link+".");
			ClassManager.manager.setUpdaterManager(this);
			if(plugin.getPlayerJoinListener()!=null){
				plugin.getPlayerJoinListener().setEnabled(true);
			}
		}
	}


}
