package org.black_ixx.bossshop.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSEnums.BSBuyType;
import org.black_ixx.bossshop.core.BSEnums.BSPriceType;
import org.black_ixx.bossshop.misc.Enchant;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class BuyItemHandler {

	@SuppressWarnings({ "unchecked", "deprecation" })
	public BSBuy createBuyItem(String name,ConfigurationSection c){
		
		
		
		String stage = "Basic Data";
		
		try{
			
			
			

		String priceType = c.getString("PriceType");
		String rewardType= c.getString("RewardType");
		String message= c.getString("Message");
		String permission = c.getString("ExtraPermission");
		if  (permission==null||permission==""){
			permission=null;			
		}
		int inventoryLocation = c.getInt("InventoryLocation")-1;
		
		if (inventoryLocation<0){
			inventoryLocation=0;
			ClassManager.manager.getBugFinder().warn("The InventoryLocation of the Shop Item "+name+" is "+inventoryLocation+". It has to be higher than 0! Setting it to 1...");
		}
		
		
		stage="Price- and RewardType Detection";
		

		BSBuyType buyT = null;
		BSPriceType priceT = null;

		for (BSBuyType type : BSBuyType.values()){
			if (rewardType.equalsIgnoreCase(type.name())){
				buyT=type;
			}
		}

		for (BSPriceType type : BSPriceType.values()){
			if (priceType.equalsIgnoreCase(type.name())){
				priceT=type;
			}
		}

		


		if (buyT==null){
			ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+rewardType+" is not a valid RewardType!");
			ClassManager.manager.getBugFinder().severe("Valid RewardTypes:");
			for (BSBuyType type : BSBuyType.values()){
				ClassManager.manager.getBugFinder().severe("-"+type.name());
			}
			return null;
		}

		if (priceT==null){
			ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+priceType+" is not a valid PriceType!");
			ClassManager.manager.getBugFinder().severe("Valid PriceTypes:");
			for (BSPriceType type : BSPriceType.values()){
				ClassManager.manager.getBugFinder().severe("-"+type.name());
			}
			return null;
		}


		stage="Price- and RewardType Adaption a)";
		
		
		switch (priceT){
		case Money:
			ClassManager.manager.getSettings().setMoneyEnabled(true);
			ClassManager.manager.getSettings().setVaultEnabled(true);
			break;
		case Points:
			ClassManager.manager.getSettings().setPointsEnabled(true);
			break;
		}

		switch (buyT){
		case Money:
			ClassManager.manager.getSettings().setMoneyEnabled(true);
			ClassManager.manager.getSettings().setVaultEnabled(true);
			break;
		case Permission:
			ClassManager.manager.getSettings().setPermissionsEnabled(true);
			ClassManager.manager.getSettings().setVaultEnabled(true);
			break;
		case Points:
			ClassManager.manager.getSettings().setPointsEnabled(true);
			break;
		case TimeCommand:
			ClassManager.manager.getSettings().setTimedCommandEnabled(true);
			break;
		case BungeeCordServer:
			ClassManager.manager.getSettings().setBungeeCordServerEnabled(true);
			break;
		}



		Object price = c.get("Price");
		Object reward = c.get("Reward");


		stage="Price- and RewardType Adaption b)";
		
		
		//Check ob der Reward passt
		switch (buyT){

		case Item:
			if (!(reward instanceof List<?>)){	
				ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+reward+" (Item) is no valid Reward Item List! There has to be a list of ItemData ( dev.bukkit.org/bukkit-plugins/bossshop/pages/setup-guide/ )");
				return null;
			}
			List<?> l = (List<?>) reward;
			boolean b = false;
			for (Object o : l){
				if (o instanceof List<?>){
					b=true;
				}
			}
			//Wenn Liste korrekt ist: List<List<String>>
			if (b){

				List<ItemStack> items = new ArrayList<ItemStack>();
				for (List<String> s : (List<List<String>>)reward){
					items.add(ClassManager.manager.getItemStackCreator().createItemStack(s));
				}
				reward=items;

			}else{

				//Wenn es eine Liste gibt, in der aber keine weitere Liste ist

				List<ItemStack> items = new ArrayList<ItemStack>();
				items.add(ClassManager.manager.getItemStackCreator().createItemStack((List<String>)reward));
				reward=items;
			}
			break;

		case TimeCommand:
			HashMap<Integer, String> cmds = new HashMap<Integer, String>();

			//Wenn reward = List<String>
			if (reward instanceof List<?>){
				for (String s : (List<String>)reward){

					try {
						String[] parts = s.split(":", 2);
						String a1 = parts[0].trim();
						int i = Integer.parseInt(a1);
						String cmd = parts[1].trim();

						cmds.put(i, cmd);

					} catch (Exception e){
						ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+s+" (TimeCommand) is no valid Reward line! It has to look like this: <time>:<command> For example 600:ban %name%");
						return null;
					}
				}

			}else{
				if (!(reward instanceof String)){
					ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+reward+" (TimeCommand) is no valid Reward list! It has to be a list of this: <time>:<command> For example - 600:ban %name%   - 400:kick %name%");
					return null;
				}
				String s = (String)reward;

				String[] parts = s.split(":", 2);
				String a1 = parts[0].trim();
				int i = Integer.parseInt(a1);
				String cmd = parts[1].trim();

				cmds.put(i, cmd);

			}
			reward=cmds;
			break;

		case Shop:
			if (reward instanceof String){
				String x = (String) reward;
				reward=x.toLowerCase();


			}else{
				ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+reward+" (Shop) is no valid Reward line! It needs to be a text-line!");			
				return null;
			}
			break;
			

		case BungeeCordServer:
			if (reward instanceof String){
				String x = (String) reward;
				reward=x.toLowerCase();


			}else{
				ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+reward+" (BungeeCordServer) is no valid Reward line! It needs to be a text-line!");			
				return null;
			}
			break;

		case Command:
			if (!(reward instanceof List<?>)){
				if (!(reward instanceof String)){
					ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+reward+" (Command) is no valid Reward list! It needs to be a list of commands!");			
					return null;
				}
				List<String> li = new ArrayList<String>();
				li.add((String)reward);
				reward=li;
			}
			break;

		case PlayerCommand:
			if (!(reward instanceof List<?>)){
				if (!(reward instanceof String)){
					ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+reward+" (PlayerCommand) is no valid Reward list! It needs to be a list of commands!");			
					return null;
				}
				List<String> li = new ArrayList<String>();
				li.add((String)reward);
				reward=li;
			}
			break;

		case Money:
			if (!(reward instanceof Double) &!(reward instanceof Integer)){

				try {

					reward = Double.parseDouble((String) (reward));	

				} catch (Exception exc){
					ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+reward+" (Money) is no valid Reward number!");			
					return null;
				}
			}
			break;

		case Points:
			if (!(reward instanceof Integer)){

				try {

					reward =Integer.parseInt((String) (reward));	

				} catch (Exception exc){
					ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+reward+" (Points) is no valid Reward number!");			
					return null;
				}
			}
			break;

		case Permission:
			if (!(reward instanceof List<?>)){
				if (!(reward instanceof String)){
					ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+reward+" (Permissions) is no valid Reward list! It needs to be a list of permissions!");			
					return null;
				}
				List<String> li = new ArrayList<String>();
				li.add((String)reward);
				reward=li;
			}
			break;
			
		case Enchantment:
			
			if(!(reward instanceof String)){
				ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+reward+" (Enchantment) is no valid Reward line! It needs to be a text line looking like this: <enchantmenttype/enchantmentid>#<level>!");			
				return null;
			}
			
			String line = (String) reward;
			String parts[] = line.split("#",2);
			
			if(parts.length!=2){
				ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+reward+" (Enchantment) is no valid Reward line! It needs to be a text line looking like this: <enchantmenttype/enchantmentid>#<level>!");			
				return null;
			}

			String p_a = parts[0].trim();
			String p_b = parts[1].trim();

			int lvl = 0;
			Enchantment enchantment = null;
			
			try{
				lvl = Integer.parseInt(p_b);
			}catch (Exception e){
				ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+reward+" (Enchantment) is no valid Reward line! Invalid level! It needs to be a number!");			
				return null;
			}

			int ench = -1;
			
			
			try{
				ench = Integer.parseInt(p_a);
			} catch (Exception e){	}
				
			if(ench>=0){
				enchantment = Enchantment.getById(ench);				
			}
				
			if (enchantment == null){
				enchantment = Enchantment.getByName(p_a);
			}
				
			
			if (enchantment ==null){
				ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+reward+" (Enchantment) is no valid Reward line! Enchantment not found! Here you can find the right enchantment names: http://jd.bukkit.org/doxygen/db/dbe/namespaceorg_1_1bukkit_1_1enchantments.html");			
				return null;
			}
				
			reward = new Enchant(enchantment, lvl);
			
			break;



		}

		switch (priceT){
		case Item:
			if (!(price instanceof List<?>)){			
				ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+price+" (Item) is no valid Price Item List! There has to be a list of ItemData ( dev.bukkit.org/bukkit-plugins/bossshop/pages/setup-guide/ )");
				return null;
			}
			List<?> l = (List<?>) price;
			boolean b = false;
			for (Object o : l){
				if (o instanceof List<?>){
					b=true;
				}
			}
			//Wenn Liste korrekt ist: List<List<String>>
			if (b){

				List<ItemStack> items = new ArrayList<ItemStack>();
				for (List<String> s : (List<List<String>>)price){
					items.add(ClassManager.manager.getItemStackCreator().createItemStack(s));
				}
				price=items;

			}else{

				//Wenn es eine Liste gibt, in der aber keine weitere Liste ist

				List<ItemStack> items = new ArrayList<ItemStack>();
				items.add(ClassManager.manager.getItemStackCreator().createItemStack((List<String>)price));
				price=items;
			}
			break;
			
		case Free:
			break;
		case Nothing:
			break;
			
		case Exp:
			if (!(price instanceof Integer)){

				try {

					price =Integer.parseInt((String) (price));	

				} catch (Exception exc){
					ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+price+" (Exp) is no valid Price number!");			
					return null;
				}
			}
			break;
			
		case Money:
			if (!(price instanceof Double) &!(price instanceof Integer)){

				try {

					price = Double.parseDouble((String) (price));	

				} catch (Exception exc){
					ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+price+" (Money) is no valid Price number!");			
					return null;
				}
			}
			break;
			
		case Points:
			if (!(price instanceof Integer)){

				try {

					price =Integer.parseInt((String) (price));	

				} catch (Exception exc){
					ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+price+" (Points) is no valid Price number!");			
					return null;
				}
			}
			break;
			
		}		
		
		return new BSBuy(buyT, priceT, reward, price,message, inventoryLocation, permission,name);

		
		
		
		

		} catch (Exception e){
			ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! Error at Stage '"+stage+"'.");
			e.printStackTrace();
			ClassManager.manager.getBugFinder().severe("Probably caused by Config Mistakes.");
			ClassManager.manager.getBugFinder().severe("For more help, create a ticket here: http://dev.bukkit.org/bukkit-plugins/bossshop/create-ticket/");
			return null;
		}
		
		
		
		
	}




}
