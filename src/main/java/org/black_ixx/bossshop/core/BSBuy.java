package org.black_ixx.bossshop.core;

import java.util.HashMap;
import java.util.List;

import net.milkbowl.vault.permission.Permission;

import org.black_ixx.bossshop.core.enums.BSBuyType;
import org.black_ixx.bossshop.core.enums.BSPriceType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.black_ixx.bossshop.misc.Enchant;
import org.black_ixx.bossshop.misc.MathTools;
import org.black_ixx.timedCommands.StoreHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

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
		this.msg = ClassManager.manager.getStringManager().transform(msg, this, null, null);
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
			if (!ClassManager.manager.getItemStackChecker().inventoryContainsItem(p, i, ClassManager.manager.getSettings().getCanPlayersSellItemsWithGreaterEnchants())) {
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
			ClassManager.manager.getItemStackChecker().takeItem(i, p, ClassManager.manager.getSettings().getCanPlayersSellItemsWithGreaterEnchants());
		}
		return null;
	}

	private String takeMoney(Player p, double money) {
		if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
			ClassManager.manager.getBugFinder().severe("Unable to take money! No economy account existing! (" + p.getName() + ", " + money + ")");
			return "";
		}
		ClassManager.manager.getVaultHandler().getEconomy().withdrawPlayer(p.getName(), money);
		return MathTools.displayDouble(ClassManager.manager.getVaultHandler().getEconomy().getBalance(p.getName()), 2);
	}

	private String takePoints(Player p, Integer d) {
		return MathTools.displayDouble(ClassManager.manager.getPointsManager().takePoints(p, d), 0);
	}

	// //////////////////////////////// <- Give reward

	@SuppressWarnings("unchecked")
	public boolean giveReward(Player p) { //Returns true if it might cause the shop to need an update

		switch (buyT) {

		case Command:
			giveRewardCommand(p, (List<String>) reward);
			return true;

		case PlayerCommand:
			giveRewardPlayerCommand(p, (List<String>) reward);
			return true;

		case TimeCommand:
			giveRewardTimeCommand(p, (HashMap<Integer, String>) reward);
			return false;

		case Item:
			giveRewardItem(p, (List<ItemStack>) reward);
			return false;

		case Permission:
			giveRewardPermission(p, (List<String>) reward);
			return true;

		case Money:
			double r = 0;
			if (reward instanceof Integer) {
				r = (int) reward;
				return true;
			}else{
				r = (double) reward;
			}
			giveRewardMoney(p, ClassManager.manager.getMultiplierHandler().calculateRewardWithMultiplier(p, BSPriceType.Money, (r)));
			return true;
		case Points:
			giveRewardPoints(p, ClassManager.manager.getMultiplierHandler().calculateRewardWithMultiplier(p, BSPriceType.Points, ((Integer)reward)));
			return true;
		case Custom:
			giveRewardCustom(p, (BSCustomLink) reward);
			return true;
		case Shop:
			giveRewardShop(p, (String) reward);
			return false;
		case BungeeCordServer:
			giveRewardBungeeCordServer(p, (String) reward);
			return false;
		case Enchantment:
			giveRewardEnchantment(p, (Enchant) reward);
			return false;
		case Nothing:
			return false;
		case Close:
			p.closeInventory();
			return false;
		}

		return false;
	}

	private void giveRewardCommand(Player p, List<String> commands) {
		for (String s : commands) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ClassManager.manager.getStringManager().transform(s, this, null, p));
		}
	}

	private void giveRewardPlayerCommand(Player p, List<String> commands) {
		for (String s : commands) {
			String command = ClassManager.manager.getStringManager().transform(s, this, null, p);
			PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(p, "/"+command);

			Bukkit.getPluginManager().callEvent(event);

			if (!event.isCancelled()) {
				p.performCommand(event.getMessage().substring(1));
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

			i = ClassManager.manager.getItemStackTranslator().translateItemStack(this, null, i.clone(), p);

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
		if (shopName == null || shopName == "" || shopName.length() < 1) {
			p.closeInventory();
			return;
		}
		ClassManager.manager.getShops().openShop(p, shopName);
	}

	private void giveRewardBungeeCordServer(Player p, String server) {
		ClassManager.manager.getBungeeCordManager().sendToServer(server, p, ClassManager.manager.getPlugin());
	}

	private void giveRewardEnchantment(Player p, Enchant e) {
		if (p.getInventory().getItemInMainHand() != null) {
			p.getInventory().getItemInMainHand().addUnsafeEnchantment(e.getType(), e.getLevel());
		}
	}

	// //////////////////////////////// <- Transform messages

	public String transformMessage(String msg, BSShop shop, Player p) {
		if (msg == null) {
			return null;
		}
		if(msg.length()==0){
			return msg;
		}

		//Handle reward and price variables
		if(msg.contains("%price%") || msg.contains("%reward%")){
			String rewardMessage = null;
			String priceMessage = null;


			if(msg.contains("%reward%")){
				rewardMessage = getObjectInfo(reward);
			}

			if(msg.contains("%price%")){
				priceMessage = getObjectInfo(price);
			}

			//Does shop need to be customizable?
			if(shop!=null){
				if(!shop.isCustomizable()){
					boolean has_pricevariable = (msg.contains("%price%") && (priceT==BSPriceType.Money || priceT==BSPriceType.Exp || priceT==BSPriceType.Points));
					boolean has_rewardvariable = (msg.contains("%reward%") && (buyT==BSBuyType.Money || buyT==BSBuyType.Points));
					if(has_pricevariable || has_rewardvariable){
						if(ClassManager.manager.getMultiplierHandler().hasMultipliers()){
							shop.setCustomizable(true);
							shop.setDisplaying(true);
						}
					}
				}
			}

			boolean possibly_customizable = shop == null;
			if(shop!=null){
				possibly_customizable = shop.isCustomizable();
			}
			if(possibly_customizable){
				if(p == null){ //When shop is customizable, the variables needs to be adapted to the player and can not be set here!
					priceMessage = null; 
					rewardMessage = null;
				}else{
					if(price instanceof Integer){
						priceMessage = MathTools.displayDouble((ClassManager.manager.getMultiplierHandler().calculateWithMultiplier(p, priceT, (int)price)), 0);
					}
					if(price instanceof Double){
						priceMessage = MathTools.displayDouble((ClassManager.manager.getMultiplierHandler().calculateWithMultiplier(p, priceT, (double)price)), 2);
					}
					if(reward instanceof Integer){
						rewardMessage = MathTools.displayDouble((ClassManager.manager.getMultiplierHandler().calculateRewardWithMultiplier(p, BSPriceType.detectType(buyT.name()), (int)reward)), 0);
					}
					if(reward instanceof Double){
						rewardMessage = MathTools.displayDouble((ClassManager.manager.getMultiplierHandler().calculateRewardWithMultiplier(p, BSPriceType.detectType(buyT.name()), (double)reward)), 2);
					}
				}
			}

			if (priceMessage != null && priceMessage != "" && priceMessage.length() > 0) {
				msg = msg.replace("%price%", priceMessage);
			}
			if (rewardMessage != null && rewardMessage != "" && rewardMessage.length() > 0) {
				msg = msg.replace("%reward%", rewardMessage);
			}
		}

		if (priceT != null && priceT.name() != "" && priceT.name().length() > 0) {
			msg = msg.replace("%pricetype%", priceT.name());
		}
		if (buyT != null && buyT.name() != "" && buyT.name().length() > 0) {
			msg = msg.replace("%rewardtype%", buyT.name());
		}

		//Handle rest
		msg = msg.replace("%shopitemname%", this.name);

		String name = this.name;
		if(shop != null){
			String item_title = ClassManager.manager.getItemStackTranslator().readName(shop.getMenuItem(this));
			if(item_title != null){
				name = item_title;
			msg = msg.replace("%itemname%", name);
			}
		}

		return msg;
	}
	
	
	@SuppressWarnings("unchecked")
	private String getObjectInfo(Object o){
		if(o == null){
			return "0";
		}
		
		if(ClassManager.manager.getItemStackTranslator().isItemList(o)){
			return ClassManager.manager.getItemStackTranslator().getFriendlyText((List<ItemStack>) o);
		}

		if(o instanceof Integer){
			int i = (int) o;
			return MathTools.displayDouble(i, 0);
		}
		if(o instanceof Double){
			double d = (double) o;
			return MathTools.displayDouble(d, 2);
		}
		
		return String.valueOf(o);
	}


	// //////////////////////////////// <- Rest

	@Deprecated
	public void setInventoryLocation(int i) {
		location = i;
	}

}
