package org.black_ixx.bossshop.listeners;


import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.enums.BSBuyType;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.events.BSPlayerPurchaseEvent;
import org.black_ixx.bossshop.events.BSPlayerPurchasedEvent;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.misc.Enchant;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;

public class InventoryListener implements Listener{


	private BossShop plugin;

	public InventoryListener(BossShop plugin){
		this.plugin=plugin;
	}


	@EventHandler
	public void closeShop(InventoryCloseEvent e){
		if (!(e.getInventory().getHolder() instanceof BSShopHolder)){
			return;
		}

		BSShopHolder holder = (BSShopHolder) e.getInventory().getHolder();

		if (e.getPlayer() instanceof Player){
			plugin.getClassManager().getMessageHandler().sendMessage("Main.CloseShop", (Player)e.getPlayer(), null, (Player) e.getPlayer(), holder.getShop(), null);
		}
	}


	@EventHandler
	public void purchase(InventoryClickEvent event){
		if (!(event.getInventory().getHolder() instanceof BSShopHolder)){
			return;
		}

		boolean cancel = true;

		if(!ClassManager.manager.getSettings().getAdvancedSecurityEnabled()){
			try{
				if(! (ClassManager.manager.getPlugin().getAPI().isValidShop(event.getClickedInventory()))){
					switch(event.getAction()){
					case CLONE_STACK:
					case DROP_ALL_SLOT:
					case DROP_ONE_SLOT:
					case HOTBAR_MOVE_AND_READD:
					case HOTBAR_SWAP:
					case PICKUP_ALL:
					case PICKUP_HALF:
					case PICKUP_ONE:
					case PICKUP_SOME:
					case PLACE_ALL:
					case PLACE_ONE:
					case PLACE_SOME:
					case SWAP_WITH_CURSOR:
						cancel = false;
						break;
					default:
						break;
					}
				}
			}catch (NoSuchMethodError e){
				//error when not using spigot api
			}
		}
		if(cancel){
			event.setCancelled(true);
			event.setResult(Result.DENY);
		}

		BSShopHolder holder = (BSShopHolder)event.getInventory().getHolder();


		if (event.getWhoClicked() instanceof Player){
			if (event.getCurrentItem()==null){
				return;							
			}

			if (event.getCursor()==null){
				return;
			}

			if (event.getSlotType() == SlotType.QUICKBAR){
				return;
			}

			BSBuy buy = holder.getShopItem(event.getRawSlot());
			if(buy==null){
				return;
			}

			event.setCancelled(true);
			event.setResult(Result.DENY);
			//event.setCurrentItem(null);

			if (buy.getInventoryLocation()==event.getRawSlot()){

				Player p = (Player) event.getWhoClicked();
				if (!buy.hasPermission(p,true)){
					return;
				}

				BSShop shop = ((BSShopHolder)event.getInventory().getHolder()).getShop();

				BSPlayerPurchaseEvent e1 = new BSPlayerPurchaseEvent(p, shop, buy);//Custom Event
				Bukkit.getPluginManager().callEvent(e1);
				if(e1.isCancelled()){
					return;
				}//Custom Event end


				if (!buy.hasPrice(p)){
					return;
				}

				if(buy.alreadyBought(p)){
					plugin.getClassManager().getMessageHandler().sendMessage("Main.AlreadyBought", p, null, p, shop, buy);
					return;
				}

				if(buy.getBuyType()==BSBuyType.Enchantment){
					Enchant e = (Enchant) buy.getReward();
					if(p.getInventory().getItemInMainHand()==null || (!e.getType().canEnchantItem(p.getInventory().getItemInMainHand()) &! plugin.getClassManager().getSettings().getUnsafeEnchantmentsEnabled())){
						plugin.getClassManager().getMessageHandler().sendMessage("Enchantment.Invalid", p, null, p, shop, buy);
						return;
					}
				}

				String o = buy.takePrice(p);
				String message = buy.getMessage();
				if (message!=null){
					message = plugin.getClassManager().getStringManager().transform(message, buy, shop, p);
					if (o!=null&&o!=""&&message.contains("%left%")){
						message=message.replace("%left%", o);
					}
				}

				boolean need_update = buy.giveReward(p);

				if(plugin.getClassManager().getSettings().getTransactionLogEnabled()){
					plugin.getClassManager().getTransactionLog().addTransaction(p, buy);
				}

				BSPlayerPurchasedEvent e2 = new BSPlayerPurchasedEvent(p, shop, buy);//Custom Event
				Bukkit.getPluginManager().callEvent(e2);//Custom Event end


				if (message!=null && message!=""&&message.length()!=0){
					p.sendMessage(message);
				}

				if(shop.isCustomizable() && need_update){
					if(p.getOpenInventory() == event.getView()){ //only when inventory is still open
						shop.updateInventory(event.getInventory(), p, plugin.getClassManager());
					}
				}

			}

		}
	}


	@EventHandler
	public void drag(InventoryDragEvent event){
		if (!(event.getInventory().getHolder() instanceof BSShopHolder)){
			return;
		}
		//When there is a good way to detect whether the upper inventory has been affected by the drag event or not then please inbuilt it and only cancel the event in that case
		event.setCancelled(true);
		event.setResult(Result.DENY);
	}


}
