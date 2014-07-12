package org.black_ixx.bossshop.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSEnums.BSBuyType;
import org.black_ixx.bossshop.core.BSEnums.BSPriceType;
import org.bukkit.configuration.ConfigurationSection;

public class ExampleManager {

	private BossShop plugin;
	private Random r;

	public ExampleManager(BossShop plugin){
		this.plugin=plugin;
		r = new Random();
	}


	public void addDefault(String name, String rewardType, String priceType, Object reward, Object price, List<String> menuitem, String message, int loc, String permission){
		ConfigurationSection c = plugin.getConfig().getConfigurationSection("shop").createSection(name);
		c.set("RewardType", rewardType);
		c.set("PriceType", priceType);
		c.set("Price", price);
		c.set("Reward", reward);
		c.set("MenuItem", menuitem);
		c.set("Message", message);
		c.set("InventoryLocation", loc);
		c.set("ExtraPermission", permission);
		plugin.saveConfig();
		plugin.reloadConfig();
	}

	private List<String> createExampleItemData(){
		List<String> itemdata = new ArrayList<String>();
		int id = r.nextInt(5)+1;
		int amount = r.nextInt(64)+1;
		itemdata.add("id:"+id);
		itemdata.add("amount:"+amount);
		return itemdata;
	}

	private List<List<String>> createExampleItemList(){
		List<List<String>> items = new ArrayList<List<String>>();
		items.add(createExampleItemData());
		items.add(createExampleItemData());
		return items;
	}


	private List<String> createExampleCommandList(){
		List<String> x = new ArrayList<String>();
		x.add("say Hey %name%");
		x.add("say Hello %name%");
		x.add("say %name% is using the BossShop");
		x.add("say kick %name%");
		x.add("say whitelist add %name%");
		x.add("say ban %name%");
		List<String> y = new ArrayList<String>();
		y.add(x.get(r.nextInt(x.size())));
		return y;		
	}

	private List<String> createExamplePermissionsList(){
		List<String> x = new ArrayList<String>();
		x.add("bukkit.command.give ");
		x.add("bukkit.command.teleport");
		x.add("bukkit.command.tell ");
		x.add("bukkit.command.kill ");
		x.add("bukkit.command.me ");
		x.add("bukkit.command.kick");
		List<String> y = new ArrayList<String>();
		y.add(x.get(r.nextInt(x.size())));
		return y;		
	}

	private List<String> createExampleTimeCommandList(){
		List<String> x = new ArrayList<String>();
		x.add("600:say Hey %name%");
		x.add("600:say Hello %name%");
		x.add("3600:say %name% is using the BossShop");
		x.add("3600:say kick %name%");
		x.add("1200:say whitelist add %name%");
		x.add("1200:say ban %name%");
		List<String> y = new ArrayList<String>();
		y.add(x.get(r.nextInt(x.size())));
		return y;		
	}

	private List<String> createMenuItem(String name){


		List<String> moneyMenuItem = new ArrayList<String>();
		moneyMenuItem.add("type:WRITTEN_BOOK");
		moneyMenuItem.add("amount:1");
		moneyMenuItem.add("name:&6"+name);
		moneyMenuItem.add("lore:&eI'm a lore!");

		return moneyMenuItem;

	}

	public void createExample(BSBuyType buyT, BSPriceType priceT, String name){


		Object oR = null;
		Object oP = null;

		switch (buyT){
		case Command:
			oR = createExampleCommandList();
			break;
		case Item:
			oR = createExampleItemList();
			break;
		case Money:
			oR = r.nextInt(5000)+1;
			break;
		case Permission:
			oR = createExamplePermissionsList();
			break;
		case Points:
			oR = r.nextInt(5000)+1;
			break;
		case TimeCommand:
			oR = createExampleTimeCommandList();
			break;
		}

		switch (priceT){

		case Exp:
			oP = r.nextInt(40)+1;
			break;
		case Item:
			oP = createExampleItemList();
			break;
		case Money:
			oP = r.nextInt(5000)+1;
			break;
		case Points:
			oP= r.nextInt(5000)+1;
			break;
		case Free:
			break;
		case Nothing:
			break;		
		}



		addDefault(name, buyT.name().toLowerCase(), priceT.name().toLowerCase(), oR, oP, createMenuItem(name), "This message is sent when a player purchases me!", getInvLocation(), "");

	}

	private int getInvLocation(){
		int i = 1;

		HashMap<Integer, String> existing = new HashMap<Integer, String>();

		for (String key : plugin.getConfig().getConfigurationSection("shop").getKeys(false)){

			if (plugin.getConfig().getConfigurationSection("shop").getConfigurationSection(key)!=null){
				int z = plugin.getConfig().getConfigurationSection("shop").getConfigurationSection(key).getInt("InventoryLocation");
					existing.put(z, key);
			}

		}
		while (i<63){
			if (!existing.containsKey(i)){
				return i;
			}

			i++;

		}




		return i;

	}



}
