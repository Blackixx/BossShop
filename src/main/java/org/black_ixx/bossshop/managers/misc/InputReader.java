package org.black_ixx.bossshop.managers.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.misc.Enchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class InputReader {


	public static String readString(Object o, boolean lowercase){
		if(o == null){
			return null;
		}
		String s = String.valueOf(o);
		if(s!=null && lowercase){
			s = s.toLowerCase();
		}
		return s;
	}

	@SuppressWarnings("unchecked")
	public static List<String> readStringList(Object o){
		if(o instanceof List<?>){
			return (List<String>) o;
		}
		if(o instanceof String){
			ArrayList<String> list = new ArrayList<>();
			list.add((String) o);
			return list;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static List<List<String>> readStringListList(Object o){
		if(!(o instanceof List<?>)){
			return null;
		}
		List<?> list = (List<?>) o;
		if(list.get(0) instanceof List<?>){ //Everything perfect: Having a list inside a list
			return (List<List<String>>) o;
		}else{ //Having one list only
			ArrayList<List<String>> main = new ArrayList<>();
			main.add((List<String>) o);
			return main;
		}
	}

	public static List<ItemStack> readItemList(Object o){
		List<List<String>> list = readStringListList(o);
		if(list!=null){
			List<ItemStack> items = new ArrayList<ItemStack>();
			for (List<String> s : list){
				items.add(ClassManager.manager.getItemStackCreator().createItemStack(s));
			}
			return items;
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static Enchant readEnchantment(Object o){
		String s = readString(o, false);
		if(s!=null){
			String parts[] = s.split("#",2);
			if(parts.length==2){
				String p_a = parts[0].trim();
				String p_b = parts[1].trim();
				int lvl = 0;
				Enchantment enchantment = null;
				int enchantment_id = -1;

				try{
					lvl = Integer.parseInt(p_b);
				}catch (NumberFormatException e){
					return null;
				}
				try{
					enchantment_id = Integer.parseInt(p_a);
				}catch (NumberFormatException e){
					//Nothing: Might fail because name instead of id is given
				}

				if(enchantment_id>=0){
					enchantment = Enchantment.getById(enchantment_id);				
				}
				if (enchantment == null){
					enchantment = Enchantment.getByName(p_a);
				}

				if(enchantment!=null){
					return new Enchant(enchantment, lvl);
				}

			}			
		}
		return null;
	}

	public static double getDouble(Object o, double exception){
		if(o instanceof Double){
			return (Double) o;
		}
		if(o instanceof Integer){
			return (Integer) o;	
		}
		return exception;
	}

	public static int getInt(Object o, int exception){
		if(o instanceof Integer){
			return (Integer) o;	
		}
		if(o instanceof Double){
			double d = (Double) o;
			return (int) d;
		}
		return exception;
	}

	public static HashMap<Integer, String> readTimedCommands(Object o){
		List<String> list = readStringList(o);
		if(list!=null){
			HashMap<Integer, String> cmds = new HashMap<Integer, String>();
			for (String s : list){
				try {
					String[] parts = s.split(":", 2);
					String a1 = parts[0].trim();
					int i = Integer.parseInt(a1);
					String cmd = parts[1].trim();
					cmds.put(i, cmd);
				} catch (Exception e){
					return null;
				}
			}
			return cmds;
		}
		return null;
	}



}
