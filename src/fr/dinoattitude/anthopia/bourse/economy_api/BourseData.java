package fr.dinoattitude.anthopia.bourse.economy_api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.dinoattitude.anthopia.Main;

public class BourseData {
	
	public static HashMap<UUID, Double> salary = new HashMap<UUID, Double>(); //Player's salary [Player, salary]
	public static HashMap<UUID, Double> savedSalary = new HashMap<UUID, Double>(); //Player's saved salary [Player, saved_salary]
	public static HashMap<String, Double> blockInitPrice = new HashMap<String, Double>(); //Initial block prices [Block_name, init_price]
	
	private static FileConfiguration config;
	private static File file;
	private final static String BOURSEINI = "Bourse.yml"; //Contain the initial price of blocks
	private final static String BOURSESAVESALARY = "BourseSaveSalary.yml"; 
	private final static String DIR_FILES = "plugins/Anthopia/Economy";
	
	public final static FileConfiguration getBourseConfig(String accessFile) {
		initBourseConfig(accessFile);
		return config;
	}
	
	private static void initBourseConfig(String accessFile) {
		File f = new File(DIR_FILES);
		if(!f.exists()) f.mkdirs();
		file = new File(f, accessFile);
		if(!file.exists()) {
			try {
				file.createNewFile();
				
			}catch(IOException ioe) { ioe.printStackTrace();}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public static void loadBlockPrice() throws IOException{
    	getBourseConfig(BOURSEINI);
        for (String key : config.getConfigurationSection("Values").getKeys(false)) {
        	blockInitPrice.put(key, getBourseConfig(BOURSEINI).getDouble("Values." + key));
        }
        Main.setInfoLog(BourseData.class.getSimpleName() + " : " + blockInitPrice.size() + " values loaded.");
    }
	
	// +------------------------------------------------------+ 
	// |                       Blocks                         | 
	// +------------------------------------------------------+
	
	public static boolean isBlock(String block) {
    	if(blockInitPrice.get(block)!=null) return true;
    	return false;
    }
	
	// +------------------------------------------------------+ 
	// |                       Salary                         | 
	// +------------------------------------------------------+
	
	public static HashMap<UUID,Double> getSalaryMap(){
		return salary;
	}
	
	public static void setSalary(UUID uuid, double amount) {
		salary.put(uuid, amount);
	}
	
	public static Double getSalary(UUID uuid) {
		return salary.get(uuid);
	}
	
	public static boolean isPlayer(UUID uuid) {
    	if(salary.get(uuid)!= null) return true;
    	return false;
    }
	
	// +------------------------------------------------------+ 
	// |                    Saved Salary                      | 
	// +------------------------------------------------------+
	
	//Save the salary left from offline players
	public static void savePlayersTemporarySalary() {
		getBourseConfig(BOURSESAVESALARY);
		for (UUID player : savedSalary.keySet()) {
    	    config.set("Player."+player, savedSalary.get(player));
    	}
    	try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Load the salary left from offline player into an HashMap
	public void loadPlayersSavedSalary() {
		getBourseConfig(BOURSESAVESALARY);
        for (String uuid : config.getConfigurationSection("Player").getKeys(false)) {
        	savedSalary.put(UUID.fromString(uuid), getBourseConfig(BOURSESAVESALARY).getDouble("Player." + uuid));
        }
        Main.setInfoLog(this.getClass().getSimpleName() + " : " + blockInitPrice.size() + " Temporary salary loaded.");
	}
	
	
}
