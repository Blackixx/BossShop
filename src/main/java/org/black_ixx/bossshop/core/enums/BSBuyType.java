package org.black_ixx.bossshop.core.enums;

import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;

public enum BSBuyType{
	Item{
		public Object createObject(Object o, boolean force_final_state){
			if(force_final_state){
				return InputReader.readItemList(o);
			}else{
				return InputReader.readStringListList(o);
			}
		}
		public boolean validityCheck(String item_name, Object o){
			if(o!=null){
				return true;
			}
			ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem "+item_name+"! The reward object needs to be a valid list of ItemData ( dev.bukkit.org/bukkit-plugins/bossshop/pages/setup-guide/ ).");	
			return false;
		}
	},
	Enchantment{
		public Object createObject(Object o, boolean force_final_state){
			if(force_final_state){
				return InputReader.readEnchantment(o);
			}else{
				return InputReader.readString(o, false);
			}
		}
		public boolean validityCheck(String item_name, Object o){
			if(o!=null){
				return true;
			}
			ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem "+item_name+"! The reward object needs to be a text line looking like this: '<enchantmenttype/enchantmentid>#<level>'.");
			return false;	
		}
	},
	Command{
		public Object createObject(Object o, boolean force_final_state){
			return InputReader.readStringList(o);
		}
		public boolean validityCheck(String item_name, Object o){
			if(o!=null){
				return true;
			}
			ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem "+item_name+"! The reward object needs to be a list of commands (text lines).");
			return false;	
		}
	},
	TimeCommand{
		public void enable(){
			ClassManager.manager.getSettings().setTimedCommandEnabled(true);
		}
		public Object createObject(Object o, boolean force_final_state){
			if(force_final_state){
				return InputReader.readTimedCommands(o);
			}else{
				return InputReader.readStringList(o);
			}
		}
		public boolean validityCheck(String item_name, Object o){
			if(o!=null){
				return true;
			}
			ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem "+item_name+"! The reward object needs to be a list of timed-commands with the following formatting: '<time>:<command>'. For example '600:ungod %name%'");
			return false;	
		}
	},
	Permission{
		public void enable(){
			ClassManager.manager.getSettings().setPermissionsEnabled(true);
			ClassManager.manager.getSettings().setVaultEnabled(true);
		}
		public Object createObject(Object o, boolean force_final_state){
			return InputReader.readStringList(o);
		}
		public boolean validityCheck(String item_name, Object o){
			if(o!=null){
				return true;
			}
			ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem "+item_name+"! The reward object needs to be a list of permissions (text lines).");	
			return false;
		}
	},
	Money{
		public void enable(){
			ClassManager.manager.getSettings().setMoneyEnabled(true);
			ClassManager.manager.getSettings().setVaultEnabled(true);
		}
		public Object createObject(Object o, boolean force_final_state){
			return InputReader.getDouble(o, -1);
		}
		public boolean validityCheck(String item_name, Object o){
			if((Double)o!=-1){
				return true;
			}
			ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem "+item_name+"! The reward object needs to be a valid number. Example: '4.0' or '10'.");	
			return false;
		}
	},
	Points{
		public void enable(){
			ClassManager.manager.getSettings().setPointsEnabled(true);
		}
		public Object createObject(Object o, boolean force_final_state){
			return InputReader.getInt(o, -1);
		}
		public boolean validityCheck(String item_name, Object o){
			if((Integer)o!=-1){
				return true;
			}
			ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem "+item_name+"! The reward object needs to be a valid Integer number. Example: '7' or '12'.");	
			return false;
		}
	},
	Shop{
		public Object createObject(Object o, boolean force_final_state){
			return InputReader.readString(o, true);
		}
		public boolean validityCheck(String item_name, Object o){
			if(o!=null){
				return true;
			}
			ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem "+item_name+"! The reward object needs to be the name of a shop (a single text line).");	
			return false;
		}
	},
	PlayerCommand{
		public Object createObject(Object o, boolean force_final_state){
			return InputReader.readStringList(o);
		}
		public boolean validityCheck(String item_name, Object o){
			if(o!=null){
				return true;
			}
			ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem "+item_name+"! The reward object needs to be a list of commands (text lines).");	
			return false;
		}
	},
	BungeeCordServer{
		public void enable(){
			ClassManager.manager.getSettings().setBungeeCordServerEnabled(true);
		}
		public Object createObject(Object o, boolean force_final_state){
			return InputReader.readString(o, true);
		}
		public boolean validityCheck(String item_name, Object o){
			if(o!=null){
				return true;
			}
			ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem "+item_name+"! The reward object needs to be the name of connected BungeeCordServer (a single text line).");	
		return false;
		}
	},
	Custom{
		public Object createObject(Object o, boolean force_final_state){
			return o; //Because nothing is known about the custom reward type
		}
		public boolean validityCheck(String item_name, Object o){ //Because nothing is known about the custom reward type
			return true;
		}
	},
	Nothing{
		public Object createObject(Object o, boolean force_final_state){
			return null;
		}
		public boolean validityCheck(String item_name, Object o){ //Here can't be any mistakes
			return true;
		}
	},
	Close{
		public Object createObject(Object o, boolean force_final_state){
			return null;
		}
		public boolean validityCheck(String item_name, Object o){ //Here can't be any mistakes
			return true;
		}
	};

	public void enable(){
		//Nothing needs to be done.
	}


	public static BSBuyType detectType(String s){
		if(s==null){
			return BSBuyType.Nothing;
		}
		for (BSBuyType type : values()){
			if (s.equalsIgnoreCase(type.name())){
				return type;
			}
		}
		return null;
	}
	public abstract Object createObject(Object o, boolean force_final_state);
	public abstract boolean validityCheck(String item_name, Object o);
}
