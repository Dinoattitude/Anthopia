package fr.dinoattitude.anthopia.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class QuoteData {
	
	private FileConfiguration config;
	private File file;
	private final String FILE_FOLDER_NAME = "Quotes"; 

	
	public final FileConfiguration getQuoteConfig() {
		initQuoteConfig();
		return config;
	}
	
	private void initQuoteConfig() {
		File f = new File("plugins/Anthopia/" + FILE_FOLDER_NAME);
		if(!f.exists()) f.mkdirs();
		file = new File(f, FILE_FOLDER_NAME + ".yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
				
			}catch(IOException ioe) { ioe.printStackTrace();}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	
	public String getQuote() {
		getQuoteConfig();
		String quote = config.getString("");
		return quote;
	}
	
}
