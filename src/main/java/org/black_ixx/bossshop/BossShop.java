package org.black_ixx.bossshop;

import java.util.ArrayList;
import java.util.List;

import org.black_ixx.bossshop.api.BossShopAPI;
import org.black_ixx.bossshop.api.BossShopAddon;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.events.BSReloadedEvent;
import org.black_ixx.bossshop.listeners.InventoryListener;
import org.black_ixx.bossshop.listeners.PlayerJoinListener;
import org.black_ixx.bossshop.listeners.SignListener;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.CommandManager;
import org.black_ixx.bossshop.misc.MetricsManager;
import org.black_ixx.bossshop.misc.UpdaterManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BossShop extends JavaPlugin{

	@Override
	public void onEnable(){
		Bukkit.getLogger().info("[BossShop] Loading data...");
		manager = new ClassManager(this);
		api = new BossShopAPI(this);

		CommandManager commander = new CommandManager();

		if (getCommand("bs")!=null){
			getCommand("bs").setExecutor(commander);
		}
		if (getCommand("bossshop")!=null){
			getCommand("bossshop").setExecutor(commander);
		}
		if (getCommand("shop")!=null){
			getCommand("shop").setExecutor(commander);
		}


		////////////////<- Listeners

		il = new InventoryListener(this);
		getServer().getPluginManager().registerEvents(il, this);

		sl = new SignListener(manager.getSettings().getSignsEnabled(),this);
		getServer().getPluginManager().registerEvents(sl, this);

		jl = new PlayerJoinListener();
		getServer().getPluginManager().registerEvents(jl, this);

		new BukkitRunnable() {
			@Override
			public void run() 
			{
				new MetricsManager().sendData(ClassManager.manager.getPlugin());
				new UpdaterManager().run(getFile());

			}
		}.runTaskLaterAsynchronously(this, 5);
	}

	@Override
	public void onDisable(){	
		reloadPlayerAction();
		unloadClasses();
		Bukkit.getLogger().info("[BossShop] Disabling... bye!");
	}

	/////////////////////////////////////////////////

	private ClassManager manager;
	private InventoryListener il;
	private SignListener sl;
	private PlayerJoinListener jl;

	private BossShopAPI api;

	/////////////////////////////////////////////////

	public ClassManager getClassManager(){
		return manager;
	}

	public SignListener getSignListener(){
		return sl;
	}

	public PlayerJoinListener getPlayerJoinListener(){
		return jl;
	}

	public BossShopAPI getAPI(){
		return api;
	}

	/////////////////////////////////////////////////

	public void reloadPlugin(CommandSender sender){		
		reloadPlayerAction();

		reloadConfig();
		manager.getMessageHandler().reloadConfig();

		for (String s : manager.getShops().getShopIds().keySet()){
			BSShop shop = manager.getShops().getShops().get(s);
			if (shop!=null){
				shop.reloadShop();
			}
		}
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.getOpenInventory() != null){
				if(p.getOpenInventory().getTopInventory()!=null){
					if(p.getOpenInventory().getTopInventory().getHolder() instanceof BSShopHolder){
						p.closeInventory();
					}
				}
			}
		}

		sl.setSignsEnabled(false); // Wird durch ConfigHandler umgesetzt (ClassManager laedt ConfigHandler)

		unloadClasses();

		manager = new ClassManager(this);

		if(api.getEnabledAddons()!=null){
			for (BossShopAddon addon : api.getEnabledAddons()){
				addon.bossShopReloaded(sender);
			}
		}

		BSReloadedEvent event = new BSReloadedEvent(this);
		Bukkit.getPluginManager().callEvent(event);		
	}

	private void unloadClasses(){		
		if(manager==null){
			return;
		}

		if(manager.getSettings()==null){
			return;
		}

		if (manager.getSettings().getTransactionLogEnabled()){
			manager.getTransactionLog().saveConfig();
		}

		if(manager.getSettings().getServerPingingEnabled()){
			manager.getServerPingingManager().clearItems();
			manager.getServerPingingManager().getServerPingingRunnableHandler().stop();			
		}		
	}

	private void reloadPlayerAction(){
		if (manager==null||manager.getShops()==null||manager.getShops().getShops()==null){
			return;
		}
		for (int i: manager.getShops().getShops().keySet()){
			BSShop shop = manager.getShops().getShops().get(i);
			if (shop!=null){
				Inventory in = shop.getInventory();
				if(in==null){
					continue;
				}
				List<HumanEntity> toRemove = new ArrayList<HumanEntity>();
				for (HumanEntity hu : in.getViewers()){
					toRemove.add(hu);
				}
				for (HumanEntity hu : toRemove){
					hu.closeInventory();
				}
			}
		}		
	}



}
