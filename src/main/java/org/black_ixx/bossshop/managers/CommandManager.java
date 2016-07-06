package org.black_ixx.bossshop.managers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSMultiplier;
import org.black_ixx.bossshop.core.BSShop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandManager implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {

		if (cmd.getName().equalsIgnoreCase("bossshop") || cmd.getName().equalsIgnoreCase("shop") || cmd.getName().equalsIgnoreCase("bs")) {

			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("reload")) {

					if (sender.hasPermission("BossShop.reload")) {

						sender.sendMessage(ChatColor.YELLOW + "Starting BossShop reload...");
						ClassManager.manager.getPlugin().reloadPlugin(sender);
						sender.sendMessage(ChatColor.YELLOW + "Done!");

					} else {
						ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
						return false;
					}
					return true;
				}

				if (args[0].equalsIgnoreCase("close")) {
					if (sender.hasPermission("BossShop.close")) {
						Player p = null;
						String name = sender instanceof Player ? sender.getName() : "CONSOLE";

						if (sender instanceof Player) {
							p = (Player) sender;
						}
						if (args.length >= 2) {
							name = args[1];
							p = Bukkit.getPlayer(name);
						}

						if (p == null) {
							ClassManager.manager.getMessageHandler().sendMessage("Main.PlayerNotFound", sender, name);
							return false;
						}

						p.closeInventory();
						if(p!=sender){
							ClassManager.manager.getMessageHandler().sendMessage("Main.CloseShopOtherPlayer", sender, p);
						}

					} else {
						ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
						return false;
					}
					return true;
				}

				if (args.length >= 3 && args[0].equalsIgnoreCase("open")) {
					if (sender.hasPermission("BossShop.open.other")) {

						if (args.length < 2) {
							sendCommandList(sender);
							return false;
						}

						String shopname = args[1];
						String name = args[2];						
						Player p = Bukkit.getPlayerExact(name);

						if (p == null) {
							p = Bukkit.getPlayer(name);
						}

						if (p == null) {
							ClassManager.manager.getMessageHandler().sendMessage("Main.PlayerNotFound", sender, name);
							return false;
						}

						BSShop shop = ClassManager.manager.getShops().getShop(shopname.toLowerCase());

						if (shop == null) {
							ClassManager.manager.getMessageHandler().sendMessage("Main.ShopNotExisting", sender);
							return false;
						}

						shop.openInventory(p);
						if(p!=sender){
							ClassManager.manager.getMessageHandler().sendMessage("Main.OpenShopOtherPlayer", sender, null, p, shop, null);
						}

					} else {
						ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
						return false;
					}
					return true;
				}

				if (args[0].equalsIgnoreCase("check")) {

					if (sender.hasPermission("BossShop.check")) {

						Settings s = ClassManager.manager.getSettings();

						sender.sendMessage(ChatColor.YELLOW + "Checking BossShop data...");

						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM-hh-mm-ss");

						String date = /*
						 * ClassManager.manager.getBugFinder().getDate
						 * ();
						 */formatter.format(new Date());
						File f = new File(ClassManager.manager.getPlugin().getDataFolder().getAbsolutePath() + "/checks/" + date + ".yml");
						FileConfiguration conf = YamlConfiguration.loadConfiguration(f);
						List<String> l = new ArrayList<String>();

						check(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "" + "Settings", l, sender);
						check(ChatColor.YELLOW + "Vault enabled: " + s.getVaultEnabled(), l, sender);
						check(ChatColor.YELLOW + "Money enabled: " + s.getMoneyEnabled(), l, sender);
						check(ChatColor.YELLOW + "Permissions enabled: " + s.getPermissionsEnabled(), l, sender);
						check(ChatColor.YELLOW + "Points enabled: " + s.getPointsEnabled(), l, sender);
						check(ChatColor.YELLOW + "ScheduledCommands enabled: " + s.getTimedCommandEnabled(), l, sender);
						check(ChatColor.YELLOW + "Signs enabled: " + s.getSignsEnabled(), l, sender);
						check(ChatColor.YELLOW + "Name of main shop: " + s.getMainShop(), l, sender);
						check(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "" + "Loaded Shops", l, sender);

						// Loop through Shop
						for (String sh : ClassManager.manager.getShops().getShopIds().keySet()) {
							BSShop c = ClassManager.manager.getShops().getShops().get(ClassManager.manager.getShops().getShopIds().get(sh));

							check(ChatColor.YELLOW + "* " + sh + " (Id: " + c.getShopId() + ")", l, sender);
							check(ChatColor.YELLOW + "  1. ShopItems", l);

							// Loop through items in shop
							for (ItemStack i : c.getItems().keySet()) {

								BSBuy b = c.getItems().get(i);
								String nam = "";
								if (i.getItemMeta().getDisplayName() != null) {
									nam = i.getItemMeta().getDisplayName();
								} else {
									nam = "x";
								}
								if (b == null || b.getBuyType() == null || b.getReward() == null || b.getPriceType() == null || b.getPrice() == null) {
									continue;
								}
								check(ChatColor.YELLOW + "   - " + nam + ChatColor.RESET + " " + ChatColor.YELLOW + "| R " + ChatColor.GREEN + b.getBuyType().name() + " -> " + b.getReward() + ChatColor.YELLOW + " | P  " + ChatColor.RED + b.getPriceType().name() + " -> " + b.getPrice(), l);

							}

							check(ChatColor.YELLOW + "  2. Sign Text: " + c.getSignText(), l);

						}

						check(ChatColor.YELLOW + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "" + "Loaded Multiplier Groups", l, sender);

						// Loop through Multipliers
						for (BSMultiplier m : ClassManager.manager.getMultiplierHandler().getMultipliers()) {

							check(ChatColor.YELLOW + "* " + m.getPermission() + ":" + m.getType().name() + ":" + m.getMultiplier(), l, sender);

						}

						// TODO VillagerShop support
						// check(ChatColor.YELLOW+""+ChatColor.BOLD+""+ChatColor.UNDERLINE+""+"Loaded Villagers",l,sender);
						//
						// if(ClassManager.manager.getVillagerHandler().getVillagers()==null){
						// check(ChatColor.RED+"None",l,sender);
						// }else{
						//
						// for (int b :
						// ClassManager.manager.getVillagerHandler().getVillagers().keySet()){
						// Villager v =
						// ClassManager.manager.getVillagerHandler().getVillagers().get(b);
						// String n = v.getCustomName();
						// if (n==null){
						// n="NoName";
						// }
						// String sho = "Unknown Shop";
						// if (v.hasMetadata("BossShop")){
						// if (!v.getMetadata("BossShop").isEmpty()){
						// sho =
						// ((ShopConfigHandler)v.getMetadata("BossShop").get(0).value()).getShopName();
						// String txt=
						// ChatColor.YELLOW+" - "+b+" | Name "+n+" | Shop "+sho+" | Profession id "+v.getProfession().getId();
						// check(txt,l,sender);
						// }
						// }
						//
						//
						// }
						//
						// }

						check(ChatColor.YELLOW + "Check done! Saving more detailed data in " + date + ".yml in the /BossShop/checks/ folder...", l, sender);
						conf.set("info", l);
						try {
							conf.save(f);
						} catch (Exception exc) {
							check(ChatColor.RED + "Error... was not able to save the file!", l, sender);
							return false;
						}

						check(ChatColor.YELLOW + "Finished with success :)", l, sender);

					} else {
						ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
						return false;
					}

					return true;
				}

			}

			if (sender instanceof Player) {
				Player p = (Player) sender;

				String shop = ClassManager.manager.getSettings().getMainShop();
				if(args.length != 0){
					shop = args[0].toLowerCase();
				}

				if (p.hasPermission("BossShop.open") || p.hasPermission("BossShop.open.command") || p.hasPermission("BossShop.open.command." + shop)) {
					ClassManager.manager.getShops().openShop(p, shop);
					return true;
				}

				ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", p);
				return false;
			}

			sendCommandList(sender);
			return false;
		}

		return false;
	}

	private void sendCommandList(CommandSender s) {
		s.sendMessage(ChatColor.RED + "/BossShop - Opens the Main Shop");
		s.sendMessage(ChatColor.RED + "/BossShop <shop> - Opens the named Shop");
		s.sendMessage(ChatColor.RED + "/BossShop open <Shop> <Player> - Opens the named Shop for the named Player");
		s.sendMessage(ChatColor.RED + "/BossShop close [Player] - Closes the Inventory of the named Player");
		s.sendMessage(ChatColor.RED + "/BossShop reload - Reloads the Plugin");
		s.sendMessage(ChatColor.RED + "/BossShop check - Displays Stats and Information");
		// s.sendMessage(ChatColor.RED+"/BossShop villager create <name> <shop> - Spawns named Villager at your Location");
		// s.sendMessage(ChatColor.RED+"/BossShop villager remove <id> - Deletes named Villager");
		// s.sendMessage(ChatColor.RED+"/BossShop villager setshop <id> <shop> - Sets/Modifies Shop of named Villager");
		// s.sendMessage(ChatColor.RED+"/BossShop villager settype <id> <type id> - Sets Profession of named Villager");
		// s.sendMessage(ChatColor.RED+"/BossShop villager tp <id> - Moves named Villager");
	}

	private void check(String s, List<String> c) {
		c.add(ChatColor.stripColor(s));

	}

	private void check(String s, List<String> c, CommandSender sender) {
		sender.sendMessage(s);
		c.add(ChatColor.stripColor(s));
	}

}
