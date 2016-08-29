package org.black_ixx.bossshop.points;

import org.bukkit.OfflinePlayer;
import org.kingdoms.constants.kingdom.Kingdom;
import org.kingdoms.constants.player.OfflineKingdomPlayer;
import org.kingdoms.manager.game.GameManagement;

public class KingdomsAPI extends IPointsAPI {

	
	
	
	
	public KingdomsAPI() {
		super("Kingdoms");
	}
	
	
	
	@Override
    public int getPoints(OfflinePlayer player) {
        OfflineKingdomPlayer p = GameManagement.getPlayerManager().getOfflineKingdomPlayer(player);
        if(p.getKingdomName() != null){
            Kingdom kingdom = GameManagement.getKingdomManager().getOrLoadKingdom(p.getKingdomName());
            if(kingdom != null){
                return kingdom.getResourcepoints();
            }
        }
        return 0;
    }

	@Override
	public int setPoints(OfflinePlayer player, int points) {
        OfflineKingdomPlayer p = GameManagement.getPlayerManager().getOfflineKingdomPlayer(player);
        if(p.getKingdomName() != null){
            Kingdom kingdom = GameManagement.getKingdomManager().getOrLoadKingdom(p.getKingdomName());
			if(kingdom != null){
				kingdom.setResourcepoints(points);
				return points;
			}
		}
		return 0;
	}

	@Override
	public int takePoints(OfflinePlayer player, int points) {
        OfflineKingdomPlayer p = GameManagement.getPlayerManager().getOfflineKingdomPlayer(player);
        if(p.getKingdomName() != null){
            Kingdom kingdom = GameManagement.getKingdomManager().getOrLoadKingdom(p.getKingdomName());
			if(kingdom != null){
				int current = kingdom.getResourcepoints();
				int result = current - points;
				kingdom.setResourcepoints(result);
				return result;
			}
		}
		return 0;
	}

	@Override
	public int givePoints(OfflinePlayer player, int points) {
        OfflineKingdomPlayer p = GameManagement.getPlayerManager().getOfflineKingdomPlayer(player);
        if(p.getKingdomName() != null){
            Kingdom kingdom = GameManagement.getKingdomManager().getOrLoadKingdom(p.getKingdomName());
			if(kingdom != null){
				int current = kingdom.getResourcepoints();
				int result = current + points;
				kingdom.setResourcepoints(result);
				return result;
			}
		}
		return 0;
	}

}
