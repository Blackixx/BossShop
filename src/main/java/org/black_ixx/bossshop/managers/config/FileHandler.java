package org.black_ixx.bossshop.managers.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.black_ixx.bossshop.BossShop;

public class FileHandler {


	public void copyFromJar(BossShop plugin, boolean shop, String... files){
		for(String filename : files){
			if(filename!=null){
				copyFromJar(plugin, shop, filename);
			}
		}
	}

	public void copyFromJar(BossShop plugin, boolean shop, String filename){
		String additional = shop?"shops"+File.separator:"";
		File file = new File(plugin.getDataFolder()+File.separator+additional+filename);
		if(!file.exists()){
			file.getParentFile().mkdirs();
		}
		InputStream in = plugin.getResource(filename);
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while((len=in.read(buf))>0){
				out.write(buf,0,len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
