package org.black_ixx.bossshop.managers;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSShops;
import org.black_ixx.bossshop.managers.config.ConfigHandler;
import org.black_ixx.bossshop.managers.serverpinging.ServerPingingManager;
import org.black_ixx.bossshop.misc.UpdaterManager;
import org.bukkit.Bukkit;
public class ClassManager {

	public static ClassManager manager;

	public ClassManager(BossShop plugin){

		this.plugin=plugin;

		manager=this;


		settings = new Settings();

		//////////////// <- Independent Classes
		

		bugfinder= new BugFinder(plugin);

		stringmanager= new StringManager();

		itemstackCreator = new ItemStackCreator();

		buyItemHandler = new BuyItemHandler();		

		configHandler = new ConfigHandler(plugin);

		itemstackChecker= new ItemStackChecker();

		messagehandler= new MessageHandler(plugin);
		
		multiplierHandler = new MultiplierHandler(plugin);
		

		////////////////<- Dependent Classes

		shops = new BSShops(plugin);
		

		if (settings.getPointsEnabled()){
			pointsmanager = new PointsManager();
		}

		if (settings.getVaultEnabled()){
			vaulthandler= new VaultHandler(settings.getMoneyEnabled(), settings.getPermissionsEnabled());
		}

		if (settings.getTimedCommandEnabled()){
			timehandler= new TimeHandler();
		}
		
		if (settings.getBungeeCordServerEnabled()){
			bungeeCordManager = new BungeeCordManager();
			Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
		}

		if (settings.getInventoryCustomizingBalanceEnabled()||settings.getInventoryCustomizingBalancePointsEnabled()||settings.getInventoryCustomizingHideEnabled()){
			customizer = new ShopCustomizer(settings.getInventoryCustomizingBalanceEnabled(), settings.getInventoryCustomizingBalancePointsEnabled(), settings.getInventoryCustomizingHideEnabled(), this);
		}
		
		if (settings.getTransactionLogEnabled()){
			transactionLog = new TransactionLog(plugin);
		}
		
		if (settings.getServerPingingEnabled()){
			getServerPingingManager().getServerPingingRunnableHandler().start(settings.getServerPingingSpeed(), plugin);
		}
		
		


	}	

	///////////////////////////////

	private ItemStackChecker itemstackChecker;
	private StringManager stringmanager;
	private PointsManager pointsmanager;
	private VaultHandler vaulthandler;
	private MessageHandler messagehandler;
	private TimeHandler timehandler;
	private ItemStackCreator itemstackCreator;
	private BuyItemHandler buyItemHandler;
	private ConfigHandler configHandler;
	private BugFinder bugfinder;
	private BossShop plugin;
	private Settings settings;
	private BSShops shops;
	private BungeeCordManager bungeeCordManager;
	private ShopCustomizer customizer;
	private TransactionLog transactionLog;
	private ServerPingingManager serverPingingManager;
	private MultiplierHandler multiplierHandler;
	private UpdaterManager updaterManager;

	///////////////////////////////

	public Settings getSettings(){
		return settings;
	}

	public ItemStackChecker getItemStackChecker(){
		return itemstackChecker;
	}

	public StringManager getStringManager(){
		return stringmanager;
	}

	public PointsManager getPointsManager(){
		return pointsmanager;
	}

	public VaultHandler getVaultHandler(){
		return vaulthandler;
	}

	public MessageHandler getMessageHandler(){
		return messagehandler;
	}

	public TimeHandler getTimeHandler(){
		return timehandler;
	}

	public ItemStackCreator getItemStackCreator(){
		return itemstackCreator;
	}

	public BuyItemHandler getBuyItemHandler(){
		return buyItemHandler;
	}

	public ConfigHandler getConfigHandler(){
		return configHandler;
	}

	public BugFinder getBugFinder(){
		return bugfinder;
	}

	public BossShop getPlugin(){
		return plugin;
	}

	public BSShops getShops(){
		return shops;
	}
	
	public BungeeCordManager getBungeeCordManager(){
		return bungeeCordManager;
	}
	
	public ShopCustomizer getShopCustomizer(){
		if(customizer==null){
			customizer = new ShopCustomizer(settings.getInventoryCustomizingBalanceEnabled(), settings.getInventoryCustomizingBalancePointsEnabled(), settings.getInventoryCustomizingHideEnabled(), this);
		}
		return customizer;
	}
	
	public TransactionLog getTransactionLog(){
		return transactionLog;
	}
	
	public ServerPingingManager getServerPingingManager(){
		if(serverPingingManager==null){
			serverPingingManager= new ServerPingingManager();
		}
		return serverPingingManager;
	}
	
	public MultiplierHandler getMultiplierHandler(){
		return multiplierHandler;
	}
	
	public UpdaterManager getUpdaterManager(){
		return updaterManager;
	}
	
	
	
	
	
	
	
	
	
	
	

	public void setUpdaterManager(UpdaterManager m){
		this.updaterManager=m;
	}

	




}
