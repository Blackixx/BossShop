package org.black_ixx.bossshop.misc;

import java.io.IOException;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.api.BossShopAddon;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.enums.BSBuyType;
import org.black_ixx.bossshop.core.enums.BSPriceType;
import org.black_ixx.bossshop.managers.Settings;
import org.black_ixx.bossshop.managers.features.PointsManager.PointsPlugin;
import org.bukkit.inventory.ItemStack;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

public class MetricsManager {

	public void sendData(final BossShop plugin){
		
		if(!plugin.getClassManager().getSettings().getMetricsEnabled()){
			return;
		}
		
		
		try {
			Metrics metrics = new Metrics(plugin);

			//Additional code goes here

			Graph version = metrics.createGraph("BossShop Version");
			addPlotter(version, plugin.getDescription().getVersion(), 1);

			Graph settings = metrics.createGraph("Used Features");
			final Settings s = plugin.getClassManager().getSettings();

			if(s.getBungeeCordServerEnabled()){
				addPlotter(settings, "BungeeCordTeleport", 1);
			}

			if(s.getBalanceVariableEnabled()){
				addPlotter(settings, "Balance Display", 1);
			}

			if(s.getBalancePointsVariableEnabled()){
				addPlotter(settings, "Points Display", 1);
			}

			if(s.getInventoryCustomizingHideEnabled()){
				addPlotter(settings, "Item Hider", 1);
			}

			if(s.getMoneyEnabled()){
				addPlotter(settings, "Money", 1);
			}

			if(s.getPermissionsEnabled()){
				addPlotter(settings, "Permissions", 1);
			}

			if(s.getPointsEnabled()){
				addPlotter(settings, "Points", 1);
			}

			if(s.getServerPingingEnabled()){
				addPlotter(settings, "ServerPinging", 1);
			}

			if(s.getSignsEnabled()){
				addPlotter(settings, "Signs", 1);
			}

			if(s.getTimedCommandEnabled()){
				addPlotter(settings, "TimedCommands", 1);
			}

			if(s.getTransactionLogEnabled()){
				addPlotter(settings, "TransactionLog", 1);
			}

			if(s.getVaultEnabled()){
				addPlotter(settings, "Vault", 1);
			}


			//TODO: ServerPinging: Speed, Servers to ping amount

			PointsPlugin ppl = s.getPointsPlugin();
			if(ppl!=null){
				Graph pp = metrics.createGraph("Points Plugin if used");
				addPlotter(pp, ppl.getPluginName(), 1);
			}

			Graph shops = metrics.createGraph("Shops");
			int sh_amount = plugin.getClassManager().getShops().getShops().size();
			int total_size = 0;
			for (int sh : plugin.getClassManager().getShops().getShops().keySet()){
				BSShop shop = plugin.getClassManager().getShops().getShops().get(sh);
				if(shop!=null){
					total_size+=shop.getInventorySize();
				}
			}

			addPlotter(shops, "Amount", sh_amount);
			addPlotter(shops, "Total Size", total_size);
			//addPlotter(shops, "Average Size", total_size/sh_amount);



			Graph items = metrics.createGraph("Items");
			Graph itemsP = metrics.createGraph("Items PriceType");
			Graph itemsR = metrics.createGraph("Items RewardType");

			int it_total_amount = 0;
			int it_item_stackssize = 0;
			int it_has_extra_perm = 0;

			int itp_money = 0;
			int itp_exp = 0;
			int itp_items = 0;
			int itp_points = 0;
			int itp_free = 0;

			int itr_bungeecordserver = 0;
			int itr_command = 0;
			int itr_custom = 0;
			int itr_item = 0;
			int itr_money = 0;
			int itr_permission = 0;
			int itr_playercommand = 0;
			int itr_points = 0;
			int itr_shop = 0;
			int itr_timecommand = 0;
			int itr_enchantment = 0;
			int itr_nothing = 0;
			int itr_close = 0;

			for (int sh : plugin.getClassManager().getShops().getShops().keySet()){
				BSShop shop = plugin.getClassManager().getShops().getShops().get(sh);
				if(shop!=null){
					for (ItemStack i : shop.getItems().keySet()){
						if(i!=null){
							BSBuy buy = shop.getItems().get(i);
							if(buy!=null){
								it_total_amount++;
								it_item_stackssize+=i.getAmount();

								if(buy.isExtraPermissionExisting()){
									it_has_extra_perm++;
								}

								BSBuyType p = buy.getBuyType();

								switch(p){
								case BungeeCordServer:
									itr_bungeecordserver++;
									break;
								case Command:
									itr_command++;
									break;
								case Custom:
									itr_custom++;
									break;
								case Item:
									itr_item++;
									break;
								case Money:
									itr_money++;
									break;
								case Permission:
									itr_permission++;
									break;
								case PlayerCommand:
									itr_playercommand++;
									break;
								case Points:
									itr_points++;
									break;
								case Shop:
									itr_shop++;
									break;
								case TimeCommand:
									itr_timecommand++;
									break;
								case Enchantment:
									itr_enchantment++;
									break;
								case Nothing:
									itr_nothing++;
									break;
								case Close:
									itr_close++;
									break;
								}

								BSPriceType price = buy.getPriceType();

								switch(price){
								case Exp:
									itp_exp++;
									break;
								case Free:
									itp_free++;
									break;
								case Nothing:
									itp_free++;
									break;
								case Item:
									itp_items++;
									break;
								case Money:
									itp_money++;
									break;
								case Points:
									itp_points++;
									break;
								}


							}
						}
					}
				}
			}

			addPlotter(items, "Total Amount", it_total_amount);
			//addPlotter(items, "Average Amount per Shop", it_total_amount/sh_amount);
			addPlotter(items, "Total ItemStacks Size", it_item_stackssize);
			//addPlotter(items, "Average ItemStack Size", it_item_stackssize/it_total_amount);
			addPlotter(items, "Has ExtraPermission", it_has_extra_perm);


			addPlotter(itemsP, "Exp", itp_exp);
			addPlotter(itemsP, "Free", itp_free);
			addPlotter(itemsP, "Items", itp_items);
			addPlotter(itemsP, "Money", itp_money);
			addPlotter(itemsP, "Points", itp_points);


			addPlotter(itemsR, "BungeecordServer", itr_bungeecordserver);
			addPlotter(itemsR, "Command", itr_command);
			addPlotter(itemsR, "Custom", itr_custom);
			addPlotter(itemsR, "Item", itr_item);
			addPlotter(itemsR, "Money", itr_money);
			addPlotter(itemsR, "Permission", itr_permission);
			addPlotter(itemsR, "PlayerCommand", itr_playercommand);
			addPlotter(itemsR, "Points", itr_points);
			addPlotter(itemsR, "Shop", itr_shop);
			addPlotter(itemsR, "TimeCommand", itr_timecommand);
			addPlotter(itemsR, "Enchantment", itr_enchantment);
			addPlotter(itemsR, "Nothing", itr_nothing);
			addPlotter(itemsR, "Close", itr_close);

			if(plugin.getAPI().getEnabledAddons()!=null){
				Graph addons = metrics.createGraph("Running Addons");
				for(BossShopAddon addon : plugin.getAPI().getEnabledAddons()){
					addPlotter(addons, addon.getAddonName(), 1);
				}
			}


			//Additional code end

			metrics.start();

		} catch (IOException e) {
			return;
		}
	}



	public void addPlotter(Graph g, String plotter, final int number){
		if(number==0){
			return;
		}
		g.addPlotter(new Metrics.Plotter(plotter) {
			@Override
			public int getValue() {
				return number;
			}
		});
	}



}
