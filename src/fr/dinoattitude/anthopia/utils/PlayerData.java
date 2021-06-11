package fr.dinoattitude.anthopia.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/** Contains and accesses all player data
 * @author Dinoattitude
 * @version 3.0.0
 */
public class PlayerData {
	
	private FileConfiguration config;
	private File file;
	private final String FILE_NAME = "PlayersData"; 

	private UUID uuid;
	
	/** The Player's constructor
	 * @param p_uuid The player UUID
	 */
	public PlayerData(UUID p_uuid) {
		this.uuid = p_uuid;
	}
	
	
	
	//############################################################
	//# +------------------------------------------------------+ #
	//# |                   Configuration                      | #
	//# +------------------------------------------------------+ #
	//############################################################
	
	
	/** Take the player's config
	 * @return The player's config
	 */
	public final FileConfiguration getPlayerConfig() {
		initPlayerConfig();
		return config;
	}
	
	/** Initializing player files and verification of its existence. */
	private void initPlayerConfig() {
		File f = new File("plugins/Anthopia/" + FILE_NAME);
		if(!f.exists()) f.mkdirs();
		file = new File(f, uuid.toString() + ".yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
				
			}catch(IOException ioe) { ioe.printStackTrace();}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	
	//############################################################
	//# +------------------------------------------------------+ #
	//# |                     Getters                          | #
	//# +------------------------------------------------------+ #
	//############################################################
	
	
	/** Gets the player's rank
	 * @return The rank
	 */
	public Integer getRank() {
		getPlayerConfig();
		Integer rank = 0;
		rank = config.getInt("rank");
		return rank;
	}
	
	/** Gets the player's money
	 * @return The money
	 */
	public double getMoney() {
		getPlayerConfig();
		double amount = 0;
		amount = config.getDouble("money");
		return amount;
	}
	
	/** Gets the player's bank account
	 * @return The bank account money
	 */
	public double getCb() {
		getPlayerConfig();
		double amount = 0;
		amount = config.getDouble("cb");
		return amount;
	}
	
	/** Gets the name of the player's guild
	 * @return The guild name
	 */
	public String getGuild() {
		getPlayerConfig();
		String guild = "none";
		guild = config.getString("guild");
		return guild;
	}
	
	/** Gets the player's guild rank
	 * @return The guild rank
	 */
	public Integer getGuildRank() {
		getPlayerConfig();
		Integer guildRank = 0;
		guildRank = config.getInt("guildRank");
		return guildRank;
	}
	
	/** Gets the player's playtime
	 * @return The playtime
	 */
	public long getPlaytime() {
		getPlayerConfig();
		long time = 0;
		time = config.getLong("playtime");
		return time;
	}
	
	
	//############################################################
	//# +------------------------------------------------------+ #
	//# |                     Setters                          | #
	//# +------------------------------------------------------+ #
	//############################################################
	
	/** Allocating and creating memory space for a new player data */
	public void addNewPlayerData() {
		try {
			getPlayerConfig();
			config.set("rank", 1);
			config.set("money", 200);
			config.set("cb", 0);
			config.set("guild", "none");
			config.set("guildRank", "0");
			config.set("playtime", "0");
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the player's rank
	 * @param rank The new rank
	 */
	public void setRank(Integer rank) {
		try {
			getPlayerConfig();
			config.set("rank", rank);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the player's money
	 * @param money The new amount
	 */
	public void setMoney(double money) {
		double roundedMoney = Utilities.getRoundedValue(money);
		try {
			getPlayerConfig();
			config.set("money", roundedMoney);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the player's bank account
	 * @param cb The new amount
	 */
	public void setCb(double cb) {
		double roundedCb = Utilities.getRoundedValue(cb);
		try {
			getPlayerConfig();
			config.set("cb", roundedCb);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the player's guild name
	 * @param guild The new guild name
	 */
	public void setGuild(String guild) {
		try {
			getPlayerConfig();
			config.set("guild", guild);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the player's guild rank
	 * @param guildRank The new guild rank
	 */
	public void setGuildRank(Integer guildRank) {
		try {
			getPlayerConfig();
			config.set("guildRank", guildRank);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the player's playtime
	 * @param time The new time
	 */
	public void setPlaytime(long time) {
		try {
			getPlayerConfig();
			config.set("guildRank", time);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
