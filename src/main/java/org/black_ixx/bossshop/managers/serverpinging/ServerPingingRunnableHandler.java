package org.black_ixx.bossshop.managers.serverpinging;

import org.black_ixx.bossshop.BossShop;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ServerPingingRunnableHandler{

	private int id = -1;

	public void start(int speed, BossShop plugin){
		BukkitTask t = new ServerPingingRunnable(plugin).runTaskTimerAsynchronously(plugin, speed, speed);
		id = t.getTaskId();
	}
	
	public void stop(){
		if(id==-1){
			return;
		}
		
		Bukkit.getScheduler().cancelTask(id);
		
	}



	public class ServerPingingRunnable extends BukkitRunnable{

		public ServerPingingRunnable(BossShop plugin){
			this.plugin=plugin;
		}

		private BossShop plugin;

		@Override
		public void run() {
			plugin.getClassManager().getServerPingingManager().updateItems();		
		}
	}

}