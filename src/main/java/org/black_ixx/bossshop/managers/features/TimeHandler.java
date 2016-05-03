package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.timedCommands.TimedCommands;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class TimeHandler {

	public TimeHandler(){

		final Plugin plugin= Bukkit.getServer().getPluginManager().getPlugin("TimedCommands");	     
		if (plugin!= null) {
			tc = (TimedCommands.class.cast(plugin));
		} else {
			ClassManager.manager.getBugFinder().warn("ScheduledCommands was not found... You need it if you want to work with timings! Get it there: http://dev.bukkit.org/bukkit-plugins/scheduledcommands/");
			return;
		}

	}

	private TimedCommands tc;


	public TimedCommands getTimedCommands(){
		return tc;
	}
}
