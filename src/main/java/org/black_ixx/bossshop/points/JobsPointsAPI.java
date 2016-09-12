package org.black_ixx.bossshop.points;

import org.bukkit.OfflinePlayer;

import com.gamingmesh.jobs.Jobs;

public class JobsPointsAPI extends IPointsAPI {

	public JobsPointsAPI() {
		super("Jobs");
	}

	@Override
	public int getPoints(OfflinePlayer player) {
		return (int) Jobs.getPlayerManager().getPointsData().getPlayerPointsInfo(player.getUniqueId()).getCurrentPoints();
	}

	@Override
	public int setPoints(OfflinePlayer player, int points) {
		Jobs.getPlayerManager().getPointsData().getPlayerPointsInfo(player.getUniqueId()).setPoints(points);
		return points;
	}

	@Override
	public int takePoints(OfflinePlayer player, int points) {
		Jobs.getPlayerManager().getPointsData().getPlayerPointsInfo(player.getUniqueId()).takePoints(points);
		return getPoints(player);
	}

	@Override
	public int givePoints(OfflinePlayer player, int points) {
		Jobs.getPlayerManager().getPointsData().getPlayerPointsInfo(player.getUniqueId()).addPoints(points);
		return getPoints(player);
	}

}
