package fr.dinoattitude.anthopia.bourse2.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.dinoattitude.anthopia.Main;
import fr.dinoattitude.anthopia.bourse2.ISalaryData;

public class SalaryData implements ISalaryData {
	
	private FileConfiguration config;
	private File file;
	
	private final String FILE_DIRECTORY = "plugins/Anthopia/Economy";
	private final String FILE_NAME = "BourseSaveSalary.yml";
	
	private Map<UUID,Double> salary = new HashMap<>();
	
	public final FileConfiguration getConfig() {
		initConfig();
		return config;
	}
	
	private void initConfig() {
		File f = new File(FILE_DIRECTORY);
		if(!f.exists()) f.mkdirs();
		file = new File(f, FILE_NAME);
		if(!file.exists()) {
			try {
				file.createNewFile();
				
			} catch(IOException ioe) { ioe.printStackTrace();}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}

	@Override
	public Double getPlayerSalary(UUID uuid) {
		return salary.get(uuid);
	}

	@Override
	public void resetPlayerSalary(UUID uuid) {
		salary.replace(uuid, 0D);
	}

	@Override
	public void savePlayerSalaryInFile() {

    	getConfig();
		
		for(Map.Entry<UUID, Double> entry : salary.entrySet()) {
			config.set("Values." + entry.getKey(), entry.getValue());
		}
		
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			Main.setSevereLog("Failed to save " + FILE_NAME + ".");
			Main.setSevereLog("This is a fatal error.");
			Main.setSevereLog("Report this stack trace to Dinoattitude forthwith.");
		}
    	
	}

	@Override
	public void loadPlayerSalaryFromFile() {
		
		getConfig();
		
        for (String key : config.getConfigurationSection("Players").getKeys(false)) {
        	salary.put(UUID.fromString(key), getConfig().getDouble("Players." + key));
        }
        
        Main.setInfoLog(SalaryData.class.getSimpleName() + " : " + salary.size() + " salary loaded.");
	}

	@Override
	public boolean isPlayerInMap(UUID uuid) {
		if(salary.get(uuid)!= null) return true;
    	return false;
	}

}
