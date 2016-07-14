package org.black_ixx.bossshop.managers.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.enums.BSBuyType;
import org.black_ixx.bossshop.core.enums.BSPriceType;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.serverpinging.BasicConnector;
import org.black_ixx.bossshop.managers.serverpinging.Connector4;
import org.black_ixx.bossshop.managers.serverpinging.Connector5;
import org.black_ixx.bossshop.managers.serverpinging.ConnectorSmart;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BSConfigShop extends BSShop{

	private String ymlName;
	private File f;
	private FileConfiguration config;

	//////////////////////////////////

	public BSConfigShop(int shop_id, String ymlName) {
		super(shop_id);


		f = new File(ClassManager.manager.getPlugin().getDataFolder().getAbsolutePath()+ "/shops/"+ymlName);

		try{
			config = ConfigLoader.loadConfiguration(f);

		} catch (InvalidConfigurationException e){
			ClassManager.manager.getBugFinder().severe("Invalid Configuration! File: /shops/"+ymlName+" Cause: "+e.getMessage());
			String name = ymlName.replace(".yml", "");
			setSignText( "["+name+"]" );
			setNeedPermToCreateSign(true);
			setShopName(name);

			ItemStack i = new ItemStack(Material.WOOL,1, (short)14 );
			ItemMeta m = i.getItemMeta();
			m.setDisplayName(ChatColor.RED+"Your Config File contains mistakes! ("+ymlName+")");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.YELLOW+"For more information check /plugins/BossShop/BugFinder.yml out!");
			m.setLore(lore);
			i.setItemMeta(m);
			addShopItem(new BSBuy(BSBuyType.Command, BSPriceType.Free, "tell %player% the config file ("+ymlName+") contains mistakes...", null, "", 0, "", name), i, ClassManager.manager);
			finishedAddingItems();
			return;
		}


		//Add defaults if not existing already
		addDefaults();

		setShopName(config.getString("ShopName"));
		setDisplayName(config.getString("DisplayName"));
		setSignText( config.getString("signs.text") );
		setNeedPermToCreateSign(config.getBoolean("signs.NeedPermissionToCreateSign"));
		setManualInventoryRows(config.getInt("InventoryRows", -1));
		
		
		//Load Items
		loadItems();
		finishedAddingItems();


	}	

	//////////////////////////////////

	public FileConfiguration getConfig(){
		return config;
	}

	//////////////////////////////////

	public void saveConfig(){
		try {
			config.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void reloadConfig() {
		f = new File(ClassManager.manager.getPlugin().getDataFolder().getAbsolutePath()+ "/shops/"+ymlName);
		config = YamlConfiguration.loadConfiguration(f);
		InputStream defConfigStream = ClassManager.manager.getPlugin().getResource(f.getName());
		if (defConfigStream != null) {
			@SuppressWarnings("deprecation")
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}

	//////////////////////////////////

	public void addDefault(String name, String rewardType, String priceType, Object reward, Object price, List<String> menuitem, String message, int loc, String permission){
		ConfigurationSection c = config.getConfigurationSection("shop").createSection(name);
		c.set("RewardType", rewardType);
		c.set("PriceType", priceType);
		c.set("Price", price);
		c.set("Reward", reward);
		c.set("MenuItem", menuitem);
		c.set("Message", message);
		c.set("InventoryLocation", loc);
		c.set("ExtraPermission", permission);
	}

	public void addDefaults(){
		config.addDefault("ShopName", "ExtraShop");
		config.addDefault("signs.text", "[ExtraShop]");
		config.addDefault("signs.NeedPermissionToCreateSign", false);

		if (config.getConfigurationSection("shop")==null){
			config.createSection("shop");

			List<String> menuItem = new ArrayList<String>();
			menuItem.add("type:STONE");
			menuItem.add("amount:1");
			menuItem.add("name:&8Example");
			List<String> cmd = new ArrayList<String>();
			cmd.add("tell %name% Example");
			addDefault("Example", "command", "money", cmd , 5000, menuItem , "", 1, "");
			config.options().copyDefaults(true);
			saveConfig();
		}
	}

	//////////////////////////////////

	@Override
	public int getInventorySize(int i){
		if (config.getInt("InventorySize")!=0){
			return config.getInt("InventorySize");
		}
		return super.getInventorySize(i);	
	}

	///////////////////////////////////////// <- Load Config-Items

	public BSBuy loadItem(String name){
		if (config.getConfigurationSection("shop").getConfigurationSection(name)==null){
			ClassManager.manager.getBugFinder().severe("Error when trying to create BuyItem "+name+"! (1) [Shop: "+getShopName()+"]");
			return null;
		}
		ConfigurationSection c = config.getConfigurationSection("shop").getConfigurationSection(name);

		if (c.getStringList("MenuItem")==null){
			ClassManager.manager.getBugFinder().severe("Error when trying to create BuyItem "+name+"! MenuItem is not existing?! (2) [Shop: "+getShopName()+"]");
			return null;
		}

		ItemStack i = ClassManager.manager.getItemStackCreator().createItemStack(c.getStringList("MenuItem"));
		BSBuy b = ClassManager.manager.getBuyItemHandler().createBuyItem(name, c, this);


		if (c.getString("ServerPinging")!=null){ //Server Pinging
			String a = c.getString("ServerPinging");
			String[] sp = a.split(":",2);
			String host = sp[0].trim();
			String port_string = sp[1].trim();
			int port = 25565;
			try{
				port = Integer.parseInt(port_string);
			}catch(Exception e){
				ClassManager.manager.getBugFinder().severe("Mistake in Config: "+b.getName()+" ServerPinging. Your line looks like this: '"+a+"' but it has to look like this: 'host:port'. 'port' needs to be a valid number! BossShop is setting the port to 25565 for you. [Shop: "+getShopName()+"]");
			}

			ClassManager.manager.getSettings().setServerPingingEnabled(true);
			

			//NOTE: Using ConnectorSmart as temporary fix until there is a stable system that knows which Connector to choose
			BasicConnector connector4 = new Connector4(host, port, 4000);
			BasicConnector connector5 = new Connector5(host, port, 4000);
			
			ClassManager.manager.getServerPingingManager().addItem(i, this, b, new ConnectorSmart(connector4, connector5));
		} //Server Pinging end

		if (b==null){
			return null;
		}		

		addShopItem(b, i, ClassManager.manager);
		return b;
	}

	public void loadItems(){
		ConfigurationSection c = config.getConfigurationSection("shop");

		for (String key : c.getKeys(false)){
			loadItem(key);
		}
	}

	@Override
	public void reloadShop() {
		reloadConfig();		
	}

}
