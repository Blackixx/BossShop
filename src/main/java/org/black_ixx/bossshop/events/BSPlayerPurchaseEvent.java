package org.black_ixx.bossshop.events;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BSPlayerPurchaseEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;

	private final BSBuy buy;

	private final BSShop shop;

	private boolean cancelled = false;

	public BSPlayerPurchaseEvent(Player player, BSShop shop, BSBuy buy) {
		this.player=player;
		this.buy=buy;
		this.shop=shop;
	}



	public Player getPlayer() {
		return player;
	}
	public BSBuy getShopItem(){
		return buy;
	};

	public BSShop getShop(){
		return shop;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}


	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}