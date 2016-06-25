package org.black_ixx.bossshop.managers;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.enums.BSBuyType;
import org.black_ixx.bossshop.core.enums.BSPriceType;
import org.bukkit.configuration.ConfigurationSection;

public class BuyItemHandler {

	public BSBuy createBuyItem(String name, ConfigurationSection c, BSShop shop){		
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
				ClassManager.manager.getBugFinder().warn("The InventoryLocation of the Shop Item "+name+" is "+inventoryLocation+". It has to be higher than 0! Setting it to 1... [Shop: "+shop.getShopName()+"]");
			}

			stage="Price- and RewardType Detection";

			BSBuyType rewardT = BSBuyType.detectType(rewardType);
			BSPriceType priceT = BSPriceType.detectType(priceType);

			if (rewardT==null){
				ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+rewardType+" is not a valid RewardType! [Shop: "+shop.getShopName()+"]");
				ClassManager.manager.getBugFinder().severe("Valid RewardTypes:");
				for (BSBuyType type : BSBuyType.values()){
					ClassManager.manager.getBugFinder().severe("-"+type.name());
				}
				return null;
			}

			if (priceT==null){
				ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+"! "+priceType+" is not a valid PriceType! [Shop: "+shop.getShopName()+"]");
				ClassManager.manager.getBugFinder().severe("Valid PriceTypes:");
				for (BSPriceType type : BSPriceType.values()){
					ClassManager.manager.getBugFinder().severe("-"+type.name());
				}
				return null;
			}


			stage="Price- and RewardType Enabling";
			rewardT.enable();
			priceT.enable();
			

			Object price = c.get("Price");
			Object reward = c.get("Reward");

			stage="Price- and RewardType Adaption";
			price = priceT.createObject(price, true);
			reward = rewardT.createObject(reward, true);
			
			if(!priceT.validityCheck(name, price)){
				return null;
			}			
			if(!rewardT.validityCheck(name, reward)){
				return null;
			}

			return new BSBuy(rewardT, priceT, reward, price,message, inventoryLocation, permission,name);



		} catch (Exception e){
			ClassManager.manager.getBugFinder().severe("Was not able to create BuyItem "+name+" of Shop "+shop.getShopName()+"! Error at Stage '"+stage+"'.");
			e.printStackTrace();
			ClassManager.manager.getBugFinder().severe("Probably caused by Config Mistakes.");
			ClassManager.manager.getBugFinder().severe("For more help, create a ticket here: http://dev.bukkit.org/bukkit-plugins/bossshop/create-ticket/");
			return null;
		}




	}




}
