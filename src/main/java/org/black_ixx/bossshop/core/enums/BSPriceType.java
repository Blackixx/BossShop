package org.black_ixx.bossshop.core.enums;

import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;

public enum BSPriceType{		
	Money{
		public void enable(){
			ClassManager.manager.getSettings().setMoneyEnabled(true);
			ClassManager.manager.getSettings().setVaultEnabled(true);
		}
		public Object createObject(Object o, boolean force_final_state){
			return InputReader.getDouble(o, -1);
		}
		public boolean validityCheck(String item_name, Object o){
			if((Double)o != -1){
				return true;
			}
			ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem "+item_name+"! The price object needs to be a valid number. Example: '4.0' or '10'.");	
			return false;
		}
	},
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
			ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem "+item_name+"! The price object needs to be a valid list of ItemData ( dev.bukkit.org/bukkit-plugins/bossshop/pages/setup-guide/ ).");	
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
			ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem "+item_name+"! The price object needs to be a valid Integer number. Example: '7' or '12'.");	
			return false;
		}
	},
	Exp{
		public Object createObject(Object o, boolean force_final_state){
			return InputReader.getInt(o, -1);
		}
		public boolean validityCheck(String item_name, Object o){
			if((Integer)o!=-1){
				return true;
			}
			ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem "+item_name+"! The price object needs to be a valid Integer number. Example: '7' or '12'.");	
			return false;
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
	Free{
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


	public static BSPriceType detectType(String s){
		if(s==null){
			return BSPriceType.Nothing;
		}
		for (BSPriceType type : values()){
			if (s.equalsIgnoreCase(type.name())){
				return type;
			}
		}
		return BSPriceType.Nothing;
	}
	public abstract Object createObject(Object o, boolean force_final_state);
	public abstract boolean validityCheck(String item_name, Object o);
}

