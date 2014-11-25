package org.black_ixx.bossshop.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.milkbowl.vault.permission.Permission;

import org.black_ixx.bossshop.core.BSEnums.BSBuyType;
import org.black_ixx.bossshop.core.BSEnums.BSPriceType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.black_ixx.bossshop.misc.Enchant;
import org.black_ixx.timedCommands.StoreHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BSBuy {

	// ////////////////////////////////

	public BSBuy(BSBuyType buyT, BSPriceType priceT, Object reward, Object price, String msg, int location, String permission, String name) {
		this.priceT = priceT;
		this.buyT = buyT;

		if (permission != null && permission != "") {
			this.permission = permission;
			if (permission.startsWith("[") && permission.endsWith("]")) {
				if (permission.length() > 2) {
					String group = permission.substring(1, permission.length() - 1);
					if (group != null) {
						ClassManager.manager.getSettings().setVaultEnabled(true);
						this.permission = group;
						perm_is_group = true;
					}
				}
			}
		}

		this.reward = reward;
		this.price = price;
		this.name = name;
		this.msg = transformMessage(msg);
		this.location = location;

	}

	// //////////////////////////////// <- Variablen

	private String name;

	private BSBuyType buyT;
	private BSPriceType priceT;

	private String permission;
	private boolean perm_is_group = false;

	private Object reward;
	private Object price;

	private String msg;

	private int location;

	// //////////////////////////////// <- Methoden um Daten zu bekommen

	public BSBuyType getBuyType() {
		return buyT;
	}

	public BSPriceType getPriceType() {
		return priceT;
	}

	public Object getReward() {
		return reward;
	}

	public Object getPrice() {
		return price;
	}

	public String getMessage() {
		return msg;
	}

	public int getInventoryLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public ConfigurationSection getConfigurationSection(BSConfigShop shop) {
		return shop.getConfig().getConfigurationSection("shop").getConfigurationSection(name);
	}

	public boolean hasPermission(Player p, boolean msg) {
		if (permission == null) {
			return true;
		}
		if (permission == "") {
			return true;
		}
		if (permission.equalsIgnoreCase("")) {
			return true;
		}

		if (perm_is_group) {
			boolean no_group = true;
			for (String group : ClassManager.manager.getVaultHandler().getPermission().getPlayerGroups(p)) {
				no_group = false;
				if (group.equalsIgnoreCase(permission)) {
					return true;
				}
			}
			if (no_group && permission.equalsIgnoreCase("default")) {
				return true;
			}
			if (msg) {
				ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", p);
			}
			return false;
		}

		if (p.hasPermission(permission)) {
			return true;
		}
		if (msg) {
			ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", p);
		}
		return false;
	}

	public boolean isExtraPermissionExisting() {
		if (permission == null) {
			return false;
		}
		if (permission == "") {
			return false;
		}
		if (permission.equalsIgnoreCase("")) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean alreadyBought(Player p) {

		if (buyT == BSBuyType.Permission) {

			for (String s : (List<String>) reward) { // Loop durch Perms

				if (!p.hasPermission(s)) { // Sobald ein Spieler eine Perm nicht
											// hat wird false returned
					return false;
				}
			}

			return true; // Hat ein Spieler alle Perms wird true returned

		}

		return false;
	}

	// //////////////////////////////// <- Check price

	@SuppressWarnings("unchecked")
	public boolean hasPrice(Player p) {
		switch (priceT) {

		// EXP
			case Exp:
				// return hasExp(p, (Integer)price); old
				return hasExp(p, ClassManager.manager.getMultiplierHandler().calculateWithMultiplier(p, priceT, (Integer) price));

				// Item
			case Item:
				return hasItems(p, (List<ItemStack>) price);

				// Money
			case Money:
				if (price instanceof Integer) {
					// return hasMoney(p, (Integer)price); old
					return hasMoney(p, ClassManager.manager.getMultiplierHandler().calculateWithMultiplier(p, priceT, (Integer) price));
				}
				// return hasMoney(p, (Integer)price); old
				return hasMoney(p, ClassManager.manager.getMultiplierHandler().calculateWithMultiplier(p, priceT, (Double) price));

				// Points
			case Points:
				// return hasPoints(p, (Integer)price); old
				return hasPoints(p, ClassManager.manager.getMultiplierHandler().calculateWithMultiplier(p, priceT, (Integer) price));

				// Free
			case Free:
				return true;

				// Nothing
			case Nothing:
				return true;

		}
		return false;
	}

	private boolean hasExp(Player p, int price) {

		if ((p.getLevel() < (Integer) price)) {
			ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Exp", p);
			return false;
		}

		return true;
	}

	private boolean hasItems(Player p, List<ItemStack> items) {

		for (ItemStack i : items) {
			if (!ClassManager.manager.getItemStackChecker().inventoryContainsItem(p, i)) {
				ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Item", p);
				return false;
			}
		}
		return true;
	}

	private boolean hasMoney(Player p, double money) {
		if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
			ClassManager.manager.getMessageHandler().sendMessage("Economy.NoAccount", p);
			return false;
		}
		if (ClassManager.manager.getVaultHandler().getEconomy().getBalance(p.getName()) < money) {
			ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Money", p);
			return false;
		}

		return true;
	}

	private boolean hasPoints(Player p, Integer points) {
		if (ClassManager.manager.getPointsManager().getPoints(p) < points) {
			ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Points", p);
			return false;
		}
		return true;
	}

	// //////////////////////////////// <- Take price

	@SuppressWarnings("unchecked")
	public String takePrice(Player p) {

		switch (priceT) {

		// EXP
			case Exp:
				return takeExp(p, ClassManager.manager.getMultiplierHandler().calculateWithMultiplier(p, priceT, (Integer) price));

				// Item
			case Item:
				return takeItems(p, (List<ItemStack>) price);

				// Money
			case Money:
				if (price instanceof Integer) {
					return takeMoney(p, ClassManager.manager.getMultiplierHandler().calculateWithMultiplier(p, priceT, (Integer) price));
				}
				return takeMoney(p, ClassManager.manager.getMultiplierHandler().calculateWithMultiplier(p, priceT, (Double) price));

				// Points
			case Points:
				return takePoints(p, ClassManager.manager.getMultiplierHandler().calculateWithMultiplier(p, priceT, (Integer) price));

				// Free
			case Free:
				return null;

				// Nothing
			case Nothing:
				return null;
		}

		return null;

	}

	private String takeExp(Player p, int exp) {
		p.setLevel(p.getLevel() - exp);
		return "" + p.getLevel();
	}

	private String takeItems(Player p, List<ItemStack> items) {
		for (ItemStack i : items) {
			ClassManager.manager.getItemStackChecker().takeItem(i, p);
		}
		return null;
	}

	private String takeMoney(Player p, double money) {
		if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
			ClassManager.manager.getBugFinder().severe("Unable to take money! No economy account existing! (" + p.getName() + ", " + money + ")");
			return "";
		}
		ClassManager.manager.getVaultHandler().getEconomy().withdrawPlayer(p.getName(), money);
		return "" + ClassManager.manager.getVaultHandler().getEconomy().getBalance(p.getName());
	}

	private String takePoints(Player p, Integer d) {
		return "" + ClassManager.manager.getPointsManager().takePoints(p, d);
	}

	// //////////////////////////////// <- Give reward

	@SuppressWarnings("unchecked")
	public void giveReward(Player p) {

		switch (buyT) {

			case Command:
				giveRewardCommand(p, (List<String>) reward);
				return;

			case PlayerCommand:
				giveRewardPlayerCommand(p, (List<String>) reward);
				return;

			case TimeCommand:
				giveRewardTimeCommand(p, (HashMap<Integer, String>) reward);
				return;

			case Item:
				giveRewardItem(p, (List<ItemStack>) reward);
				return;

			case Permission:
				giveRewardPermission(p, (List<String>) reward);
				return;

			case Money:
				if (reward instanceof Integer) {
					giveRewardMoney(p, (Integer) reward);
					return;
				}
				giveRewardMoney(p, (Double) reward);
				return;
			case Points:
				giveRewardPoints(p, (Integer) reward);
				return;
			case Custom:
				giveRewardCustom(p, (BSCustomLink) reward);
				return;
			case Shop:
				giveRewardShop(p, (String) reward);
				return;
			case BungeeCordServer:
				giveRewardBungeeCordServer(p, (String) reward);
				return;
			case Enchantment:
				giveRewardEnchantment(p, (Enchant) reward);
				return;
		}

	}

	private void giveRewardCommand(Player p, List<String> commands) {
		for (String s : commands) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClassManager.manager.getStringManager().transform(s, p));
		}
	}

	private void giveRewardPlayerCommand(Player p, List<String> commands) {
		for (String s : commands) {
			String command = ClassManager.manager.getStringManager().transform(s);
			PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(p, command);

			Bukkit.getPluginManager().callEvent(event);
			 
			if (!event.isCancelled()) {
				p.performCommand(event.getMessage());
			}			
		}
	}

	private void giveRewardTimeCommand(Player p, HashMap<Integer, String> commands) {
		StoreHandler s = ClassManager.manager.getTimeHandler().getTimedCommands().getStoreHandler();
		for (int i : commands.keySet()) {
			s.addCommandToHashMap(System.currentTimeMillis() + 1000 * i, commands.get(i));
		}
	}

	private void giveRewardItem(Player p, List<ItemStack> items) {
		for (ItemStack i : items) {

			i = transformRewardItem(i.clone(), p);

			if (p.getInventory().firstEmpty() == -1) { // Inventory full
				if (i.getMaxStackSize() == 1) { // Max Stack Size == 1
					p.getWorld().dropItem(p.getLocation(), i); // -> Drop Item
					continue;
				}
				int free = 0;
				for (ItemStack item : p.getInventory().getContents()) { // Loop
																		// durch
																		// Inventar
					if (item != null) {
						if (item.getType() == i.getType() && item.getDurability() == i.getDurability()) { // Selbes
																											// Item?
							free += item.getMaxStackSize() - item.getAmount(); // Freier
																				// Platz
																				// wird
																				// addiert
						}
					}
				}
				if (free < i.getAmount()) { // Nicht genug Platz?
					p.getWorld().dropItem(p.getLocation(), i);// Drop Item!
					continue;
				}
			}

			p.getInventory().addItem(i);
		}
	}

	private void giveRewardPermission(Player p, List<String> permissions) {
		Permission per = ClassManager.manager.getVaultHandler().getPermission();
		for (String s : permissions) {
			per.playerAdd(p, s);
		}
	}

	private void giveRewardMoney(Player p, double money) {
		if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
			ClassManager.manager.getMessageHandler().sendMessage("Economy.NoAccount", p);
			ClassManager.manager.getBugFinder().severe("Unable to give " + p.getName() + " his/her money: He/She does not have an Economy Account");
			return;
		}
		ClassManager.manager.getVaultHandler().getEconomy().depositPlayer(p.getName(), money);
	}

	private void giveRewardPoints(Player p, Integer points) {
		ClassManager.manager.getPointsManager().givePoints(p, points);
	}

	private void giveRewardCustom(Player p, BSCustomLink link) {
		link.doAction(p);
	}

	private void giveRewardShop(Player p, String shopName) {
		if (shopName == null || shopName == "" || shopName.length() < 1) { // Shop
																			// ==
																			// ""
																			// oder
																			// ''
																			// ->
																			// Close
																			// Inventory
			p.closeInventory();
			return;
		}
		ClassManager.manager.getShops().openShop(p, shopName);
	}

	private void giveRewardBungeeCordServer(Player p, String server) {
		ClassManager.manager.getBungeeCordManager().sendToServer(server, p, ClassManager.manager.getPlugin());
	}

	private void giveRewardEnchantment(Player p, Enchant e) {
		if (p.getItemInHand() != null) {
			p.getItemInHand().addUnsafeEnchantment(e.getType(), e.getLevel());
		}
	}

	// //////////////////////////////// <- Transform messages

	public String transformMessage(String msg) {

		if (reward == null) {
			reward = 1;
		}
		if (price == null) {
			price = 1;
		}

		if (msg == null || msg.length() == 0) {
			return "";
		}

		String priceM = "" + price;
		if (price instanceof List<?>) {
			List<?> list = (List<?>) price;
			if ((!list.isEmpty()) && list.get(0) instanceof ItemStack) {
				String m = "";
				int x = 0;
				for (Object i : list) {
					if (i instanceof ItemStack) {
						x++;
						String material = ((ItemStack) i).getType().name().toLowerCase();
						material = material.replaceFirst(material.substring(0, 1), material.substring(0, 1).toUpperCase());
						m += "" + ((ItemStack) i).getAmount() + " " + material + (x < list.size() ? ", " : "");
					}
				}
				priceM = m;
			}
		}

		String rewardM = "" + reward;
		if (reward instanceof List<?>) {
			List<?> list = (List<?>) reward;
			if ((!list.isEmpty()) && list.get(0) instanceof ItemStack) {
				String m = "";
				int x = 0;
				for (Object i : list) {
					if (i instanceof ItemStack) {
						x++;
						String material = ((ItemStack) i).getType().name().toLowerCase();
						material = material.replaceFirst(material.substring(0, 1), material.substring(0, 1).toUpperCase());
						m += "" + ((ItemStack) i).getAmount() + " " + material + (x < list.size() ? ", " : "");
					}
				}
				rewardM = m;
			}
		}

		if (name != null && name != "" && name.length() > 0) {
			msg = msg.replace("%itemname%", name);
			msg = msg.replace("%shopitemname%", name);
		}
		if (priceT != null && priceT.name() != "" && priceT.name().length() > 0) {
			msg = msg.replace("%pricetype%", priceT.name());
		}
		if (priceM != null && priceM != "" && priceM.length() > 0) {
			msg = msg.replace("%price%", priceM);
		}
		if (buyT != null && buyT.name() != "" && buyT.name().length() > 0) {
			msg = msg.replace("%rewardtype%", buyT.name());
		}
		if (rewardM != null && rewardM != "" && rewardM.length() > 0) {
			msg = msg.replace("%reward%", rewardM);
		}

		return msg;
	}

	private ItemStack transformRewardItem(ItemStack i, Player p) {

		if (i.hasItemMeta()) {

			ItemMeta meta = i.getItemMeta();

			if (meta.hasDisplayName()) {
				if (meta.getDisplayName().contains("%name%")) {
					meta.setDisplayName(meta.getDisplayName().replace("%name%", p.getName()));
				}
				if (meta.getDisplayName().contains("%playername%")) {
					meta.setDisplayName(meta.getDisplayName().replace("%playername%", p.getName()));
				}
				if (meta.getDisplayName().contains("%player%")) {
					meta.setDisplayName(meta.getDisplayName().replace("%player%", p.getName()));
				}
				if (meta.getDisplayName().contains("%itemname%")) {
					meta.setDisplayName(meta.getDisplayName().replace("%itemname%", name));
				}
			}

			if (meta.hasLore()) {

				List<String> new_lore = new ArrayList<String>();

				for (String s : meta.getLore()) {

					if (s.contains("%name%")) {
						s = s.replace("%name%", p.getName());
					}
					if (s.contains("%playername%")) {
						s = s.replace("%playername%", p.getName());
					}
					if (s.contains("%player%")) {
						s = s.replace("%player%", p.getName());
					}

					if (s.contains("%itemname%")) {
						s = s.replace("%itemname%", name);
					}

					new_lore.add(s);

				}

				meta.setLore(new_lore);

			}

			i.setItemMeta(meta);
		}

		return i;
	}

	// //////////////////////////////// <- Rest

	@Deprecated
	public void setInventoryLocation(int i) {
		location = i;
	}

}
