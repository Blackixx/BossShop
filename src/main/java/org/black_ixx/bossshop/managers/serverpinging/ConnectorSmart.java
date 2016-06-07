package org.black_ixx.bossshop.managers.serverpinging;

import org.black_ixx.bossshop.managers.ClassManager;

public class ConnectorSmart extends BasicConnector{


	private BasicConnector[] modules;
	private BasicConnector current;
	private int current_id = -1;
	private int fails_amount;
	
	public ConnectorSmart(BasicConnector... modules){
		this.modules = modules;
		next();
	}


	public void next(){
		if(modules!=null){
			current_id ++;
			if(modules.length <= current_id){ //Reached end of modules
				current_id = 0;
			}
			current = modules[current_id];
		}
	}

	@Override
	public boolean update() {
		if(current!=null){
			if(current.update()){
				fails_amount = 0;
				return true;
			}else{
				fails_amount ++;
				if(fails_amount > 4){
					ClassManager.manager.getBugFinder().warn("[ServerPinging] Connector '"+current.getClass()+"' does not seem to fit.. Trying an other Connector type.");
					fails_amount = 0;
					next();
				}
			}
		}
		return false;
	}

	@Override
	public String getMotd() {
		if(current!=null){
			return current.getMotd();
		}
		return null;
	}

	@Override
	public String getPlayerCount() {
		if(current!=null){
			return current.getPlayerCount();
		}
		return null;
	}

	@Override
	public boolean isOnline() {
		if(current!=null){
			return current.isOnline();
		}
		return false;
	}

	@Override
	public String getHost() {
		if(current!=null){
			return current.getHost();
		}
		return null;
	}

	@Override
	public int getPort() {
		if(current!=null){
			return current.getPort();
		}
		return 25565;
	}

}
