package org.black_ixx.bossshop.points;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.Ben12345rocks.VotingPlugin.Objects.User;


public class VotingPluginAPI extends IPointsAPI {
	public VotingPluginAPI() {
		super("VotingPlugin");
	}


	@Override
	public int getPoints(OfflinePlayer player) {
		if(player instanceof Player){
			User user = new User((Player) player);
			return user.getPoints();
		}else{
			return 0;
		}
	}

	@Override
	public int setPoints(OfflinePlayer player, int points) {
		if(player instanceof Player){
			User user = new User((Player) player);
			user.setPoints(points);
			return points;
		}else{
			return 0;
		}
	}

	@Override
	public int takePoints(OfflinePlayer player, int points) {
		if(player instanceof Player){
			User user = new User((Player) player);
			user.removePoints(points);
			return getPoints(player);
		}else{
			return 0;
		}
	}

	@Override
	public int givePoints(OfflinePlayer player, int points) {
		if(player instanceof Player){
			User user = new User((Player) player);
			user.addPoints(points);
			return getPoints(player);
		}else{
			return 0;
		}
	}

}
