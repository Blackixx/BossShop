package org.black_ixx.bossshop.managers.config;



import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.PointsManager.PointsPlugin;
public class ConfigHandler{

	///////////////////////////////////////// <- Init class

	public ConfigHandler(BossShop plugin){
		this.plugin=plugin;

		addDefaults();
		
		if(plugin.getConfig().getBoolean("signs.enabled")||plugin.getConfig().getBoolean("EnableSigns")){
			ClassManager.manager.getSettings().setSignsEnabled(true);
		}
		
		String main = plugin.getConfig().getString("MainShop");
		ClassManager.manager.getSettings().setMainShop(main.toLowerCase());
		ClassManager.manager.getSettings().setInventoryCustomizingHideEnabled(plugin.getConfig().getBoolean("HideItemsPlayersDoNotHavePermissionsFor"));
		ClassManager.manager.getSettings().setTransactionLogEnabled(plugin.getConfig().getBoolean("EnableTransactionLog"));
		ClassManager.manager.getSettings().setServerPingingSpeed((plugin.getConfig().getInt("ServerPingingDelay")));
		ClassManager.manager.getSettings().setMetricsEnabled((!plugin.getConfig().getBoolean("DisableMetrics")));
		ClassManager.manager.getSettings().setUpdaterEnabled((!plugin.getConfig().getBoolean("DisableUpdateNotifications")));
		ClassManager.manager.getSettings().setUnsafeEnchantmentsEnabled((!plugin.getConfig().getBoolean("AllowUnsafeEnchantments")));
		String points_plugin = plugin.getConfig().getString("PointsPlugin");
		PointsPlugin p = null;
		if(points_plugin!=null){
		
			if (points_plugin.equalsIgnoreCase("PlayerPoints")||points_plugin.equalsIgnoreCase("PP")){
				p=PointsPlugin.PLAYERPOINTS;
			}
		
			if (points_plugin.equalsIgnoreCase("CommandPoints")||points_plugin.equalsIgnoreCase("CP")){
				p=PointsPlugin.COMMANDPOINTS;
			}
		
			if (points_plugin.equalsIgnoreCase("Enjin Minecraft Plugin")||points_plugin.equalsIgnoreCase("Enjin")||points_plugin.equalsIgnoreCase("EMP")){
				p=PointsPlugin.ENJIN_MINECRAFT_PLUGIN;
			}
			
		}
		
		if(p!=null){
			ClassManager.manager.getSettings().setPointsPlugin(p);
		}
				
		
	}

	///////////////////////////////////////// <- Variables

	private BossShop plugin;

	///////////////////////////////////////// <- Add Defaults

	public void addDefaults(){

		plugin.reloadConfig();
		plugin.getConfig().options().header(getHeader());
		plugin.getConfig().options().copyHeader();
		
		plugin.getConfig().addDefault("EnableSigns", true);
		plugin.getConfig().addDefault("MainShop", "Menu");
		plugin.getConfig().addDefault("HideItemsPlayersDoNotHavePermissionsFor", false);
		plugin.getConfig().addDefault("EnableTransactionLog", false);
		plugin.getConfig().addDefault("ServerPingingDelay", 20);
		plugin.getConfig().addDefault("AllowUnsafeEnchantments", false);
		plugin.getConfig().addDefault("DisableUpdateNotifications", false);
		plugin.getConfig().addDefault("MultiplierGroups.Enabled", false);
		plugin.getConfig().addDefault("MultiplierGroups.List", new String[]{
				"Permission.Node:<type>:<multiplier>",
				"BossShop.PriceMultiplier.Points1:points:0.75",
				"BossShop.PriceMultiplier.Points1:points:0.5",
				"BossShop.PriceMultiplier.Money1:money:0.75",
				"BossShop.PriceMultiplier.Money2:money:0.5",
				"BossShop.PriceMultiplier.MoneyNegative:money:2.0",
				"BossShop.PriceMultiplier.Exp:exp:0.8",
				
		});
		plugin.getConfig().addDefault("PointsPlugin", "auto-detect");
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();
		
	}
	
	
	private String getHeader(){
		
		String x = "BossShop#Help Links:			#			#BossShop Page:			#http://dev.bukkit.org/bukkit-plugins/bossshop/			#			#Downloads:			#http://dev.bukkit.org/bukkit-plugins/bossshop/files/			#			#			#Hey!			#You got BossShop? Awesome.			#Believe me: Setting it up is simple.			#			#			#You can either set it up the Classic (And boring/hard) way with editing the Config Files			#OR you can edit it the Boss way: Use \"BossShopConfigure\" (An awesome software which allows simple Setup with a few clicks).			#You can download BossShopConfigure here: http://dev.bukkit.org/bukkit-plugins/bossshop/			#			#			#EnableSigns:			#If set EnableSigns to 'false', BossShop signs will be disabled.			#			#MainShop:			#That's the name of your Main Shop. Whenever someone uses '/bossshop', this Shop is opened. Other Shops can be opened via '/bossshop <shop>'.			#Also, all Shops can be opened via Sign (If Signs are enabled) or with clicking a specific Item (If you have 'GuiShopManager installed).			#			#HideItemsPlayersDoNotHavePermissionsFor:			#If enabled, all Items your players do not have permissions for will be \"hidden\". That way you can create cool things like upgrade systems: 			#Put multiple Items on the same slot and hide the ones which are not available anymore. For example if you work with upgrades, hide the old upgrade and show the next one.			#			#EnableTransactionLog:			#If enabled, all Transactions will be logged in a TransactionLog file.			#			#ServerPingingDelay:			#ServerPinging Delay in seconds. ServerPinging is not ready yet! Just don't look at that feature :P			#			#DisableUpdateNotifications:			#If disabled, you won't get Update Notifications anymore.			#			#MultiplierGroups:			#Here you can set up Price Multipliers.			#If enabled, this MultiplierGroups will be loaded and players with the right permissions will get a price decrease/increase.			#The lines of the List look like this: <Permission>:<Type>:<Multiplier>.			#<Permission>: Here you can put in whatever you want. If you want a group to have this multiplier, just give that group your permission.			#<Type>: Multiplier Type. Valid: Money, Points, Exp			#<Multiplier>: Price = old Price * Multiplier			#Players can be in multiple \"MultiplierGroups\" at the same time.			#			#PointsPlugin:			#Only important if you work with Points.			#Here you can enter whatever you want. BossShop will always detect the Points Plugin you are using and it will hook into it 			#(If you are using one). Except you use multiple Points Plugins. In that case, fill one of this names in here: 			#''PlayerPoints''/''PP'', ''CommandPoints''/''CP'', ''Enjin Minecraft Plugin''/''Enjin''/''EMP''.			#			#			#";
		return x.replace("#", "\n");
		
	}
	

}
