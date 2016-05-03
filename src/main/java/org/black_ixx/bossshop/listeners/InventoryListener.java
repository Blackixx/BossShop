package org.black_ixx.bossshop.listeners;


import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.enums.BSBuyType;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.events.BSPlayerPurchaseEvent;
import org.black_ixx.bossshop.events.BSPlayerPurchasedEvent;
import org.black_ixx.bossshop.misc.Enchant;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;

public class InventoryListener implements Listener{

	public InventoryListener(BossShop plugin){
		this.plugin=plugin;
	}
	private BossShop plugin;


	@EventHandler
	public void closeShop(InventoryCloseEvent e){
		if (!(e.getInventory().getHolder() instanceof BSShopHolder)){
			return;
		}

		if (e.getPlayer() instanceof Player){
			plugin.getClassManager().getMessageHandler().sendMessage("Main.CloseShop", (Player)e.getPlayer());
		}
	}




	@EventHandler
	public void purchase(InventoryClickEvent event){
		if (!(event.getInventory().getHolder() instanceof BSShopHolder)){
			return;
		}

		BSShopHolder holder = (BSShopHolder)event.getInventory().getHolder();

		event.setCancelled(true);
		event.setResult(Result.DENY);
		//event.setCurrentItem(null);

		if (event.getWhoClicked() instanceof Player){
			if (event.getCurrentItem()==null){
				event.setCancelled(true);
				return;							
			}

			if (event.getCursor()==null){
				event.setCancelled(true);
				return;
			}

			if (event.getSlotType() == SlotType.QUICKBAR){
				return;
			}

			BSBuy buy = holder.getShopItem(event.getRawSlot());
			if(buy==null){
				return;
			}
			if (buy.getInventoryLocation()==event.getRawSlot()){
				event.setCancelled(true);

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
					plugin.getClassManager().getMessageHandler().sendMessage("Main.AlreadyBought", p);
					return;
				}

				if(buy.getBuyType()==BSBuyType.Enchantment){
					Enchant e = (Enchant) buy.getReward();
					if(p.getInventory().getItemInMainHand()==null |! plugin.getClassManager().getItemStackChecker().isValidEnchantment(p.getInventory().getItemInMainHand(), e.getType(), e.getLevel()) &! plugin.getClassManager().getSettings().getUnsafeEnchantmentsEnabled()){
						plugin.getClassManager().getMessageHandler().sendMessage("Enchantment.Invalid", p);
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

				buy.giveReward(p);

				if(plugin.getClassManager().getSettings().getTransactionLogEnabled()){
					plugin.getClassManager().getTransactionLog().addTransaction(p, buy);
				}

				BSPlayerPurchasedEvent e2 = new BSPlayerPurchasedEvent(p, shop, buy);//Custom Event
				Bukkit.getPluginManager().callEvent(e2);//Custom Event end


				if (message!=null && message!=""&&message.length()!=0){
					p.sendMessage(message);
				}

				if(shop.isCustomizable()){
					if(p.getOpenInventory() == event.getView()){ //only when inventory is still open
						shop.updateInventory(event.getInventory(), p, plugin.getClassManager());
					}
				}

			}

		}
	}


}
