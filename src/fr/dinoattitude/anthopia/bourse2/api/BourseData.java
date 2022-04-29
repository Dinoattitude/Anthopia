package fr.dinoattitude.anthopia.bourse2.api;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.dinoattitude.anthopia.Main;
import fr.dinoattitude.anthopia.bourse2.IBourseData;
import fr.dinoattitude.anthopia.utils.DatabaseManager;

/** Every data related to the bourse.
 * @author Dinoattitude
 * @since 2.4.4
 * @version 2.4.4
*/
public class BourseData implements IBourseData {
	
	private FileConfiguration config;
	private File file;
	
	private final String FILE_DIRECTORY = "plugins/Anthopia/Economy";
	private final String FILE_NAME = "Bourse.yml";
	
	private final String SQL_UPDATE = "UPDATE Bourse SET block_value = ? WHERE block_name = ?";
	
	private Map<String,Double> bourse = new HashMap<>();
	
	
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
	public Double getBlockValue(Block block) {
		
		if(block == null) {
			return null;
		}
		
		String blockName = getBlockName(block);
		
		if(!bourse.containsKey(blockName)) {
			return null;
		}
		
		return bourse.get(blockName);
	}

	@Override
	public void setBlockValue(Block block, Double value) {
		
		if(block == null) {
			return;
		}
		
		if(value == null) {
			return;
		}
		
		String blockName = getBlockName(block);
		
		bourse.put(blockName, value);
	}

	@Override
	public void saveBourseDataInDatabase() {
		
		Iterator<Entry<String,Double>> iterator = bourse.entrySet().iterator();
		
		try {
			
			PreparedStatement preparedStatement = DatabaseManager.getConnexion().prepareStatement(SQL_UPDATE);
			
			while(iterator.hasNext()) {
				Map.Entry<String, Double> entry = iterator.next();
				preparedStatement.setDouble(1, entry.getValue());
				preparedStatement.setString(2, entry.getKey());
				preparedStatement.addBatch();
				iterator.remove();
			}
			
			preparedStatement.executeBatch();
			preparedStatement.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			Main.setSevereLog("[SQL] Failed to update bourse in database");
			Main.setSevereLog("This is a fatal error.");
			Main.setSevereLog("Report this stack trace to Dinoattitude forthwith.");
		}
		
	}

	@Override
	public void saveBourseDataInFile() {
		
		getConfig();
		
		for(Map.Entry<String, Double> entry : bourse.entrySet()) {
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
	public void loadBourseDataFromFile() {
		
		getConfig();
		
        for (String key : config.getConfigurationSection("Values").getKeys(false)) {
        	bourse.put(key, getConfig().getDouble("Values." + key));
        }
        
        Main.setInfoLog(BourseData.class.getSimpleName() + " : " + bourse.size() + " values loaded.");
		
	}

	@Override
	public boolean isBlockInBourse(Block block) {
		
		if(block == null) {
			return false;
		}
		
		return bourse.containsKey(getBlockName(block));
	}
	
	private String getBlockName(Block block) {
		return block.getType().toString();
	}

}
