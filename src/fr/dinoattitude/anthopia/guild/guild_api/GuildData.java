package fr.dinoattitude.anthopia.guild.guild_api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.dinoattitude.anthopia.utils.PlayerData;
import fr.dinoattitude.anthopia.utils.Utilities;
import net.md_5.bungee.api.ChatColor;

/** Every data related to the guilds.
 * @author Dinoattitude
 * @version 3.0.0
*/
public class GuildData {
	
	private HashMap<UUID,String> members = new HashMap<UUID,String>();

	private static FileConfiguration config;
	private File file;
	private final String TAG = "Guilds.";
	
	//############################################################
	//# +------------------------------------------------------+ #
	//# |                   Configuration                      | #
	//# +------------------------------------------------------+ #
	//############################################################
	
	/** Return the configuration of the guild file
	 * @return The guild configuration
	 */
	public final FileConfiguration getConfig() {
		initConfigGuild();
		return config;
	}
	
	/** Return the configuration of the log file
	 * @param guildName The guild name
	 * @return The guild log configuration
	 */
	public final FileConfiguration getLogConfig(String guildName) {
		initConfigGuildLog(guildName);
		return config;
	}
	
	/** Initializes the configuration of the guild file */
	private void initConfigGuild() {
		File f = new File("plugins/Anthopia/Guilds");
		if(!f.exists()) f.mkdirs();
		file = new File(f, "Guilds.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			}catch(IOException ioe) { ioe.printStackTrace();}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	/** Initializes the configuration of the log file */
	private void initConfigGuildLog(String guildName) {
		File f = new File("plugins/Anthopia/Guilds/Logs");
		if(!f.exists()) f.mkdirs();
		file = new File(f, guildName + ".yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			}catch(IOException ioe) { ioe.printStackTrace();}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void deleteLogFile(String guildName) {
		File f = new File("plugins/Anthopia/Guilds/Logs");
		if(f.delete()) System.out.println("Logs de la guilde " + guildName + " supprimé");
		else System.out.println("Erreur dans la suppression des logs de la guilde " + guildName);
	}
	
	//############################################################
	//# +------------------------------------------------------+ #
	//# |    	          Getters and Setters                  | #
	//# +------------------------------------------------------+ #
	//############################################################
	
	//+------------------------------------------------------+ 
	//|    	              Files - Getters                    | 
	//+------------------------------------------------------+
	
	/** Gets the guild information
	 * @param guildName The guild name
	 * @return The guild information
	 */
	public String getInfo(String guildName) {
		getConfig();
		return config.getString(TAG + guildName + ".info");
	}
	
	/** Gets the guild money
	 * @param guildName The guild name
	 * @return The guild money
	 */
	public double getMoney(String guildName) {
		getConfig();
		return config.getDouble(TAG + guildName + ".money");
	}
	
	/** Gets the guild level
	 * @param guildName The guild name
	 * @return The guild level
	 */
	public int getLvl(String guildName) {
		getConfig();
		return config.getInt(TAG + guildName + ".lvl");
	}
	
	/** Gets the guild experience
	 * @param guildName The guild name
	 * @return The guild experience
	 */
	public double getExp(String guildName) {
		getConfig();
		return config.getDouble(TAG + guildName + ".exp");
	}
	
	/** Gets the guild tax
	 * @param guildName The guild name
	 * @return The guild tax
	 */
	public String getTax(String guildName) {
		getConfig();
		return config.getString(TAG + guildName + ".tax");
	}
	
	/** Gets the guild tax rate
	 * @param guildName The guild name
	 * @return The guild tax rate
	 */
	public int getTaxRate(String guildName) {
		getConfig();
		return config.getInt(TAG + guildName + ".taxRate");
	}
	
	/** Gets the list of guild members
	 * @param guildName The guild name
	 * @return The list of guild members
	 */
	public List<String> getGuildMembers(String guildName){
		getConfig();
		List<String> listOfMembers = new ArrayList<String>();
		for(String guildMembers : config.getConfigurationSection(TAG + guildName + ".members").getKeys(false)) {
			listOfMembers.add(guildMembers);
		}
		return listOfMembers;
	}
	
	/** Gets a list of guilds and the money they own
	 * @return A list of guilds and the money they own
	 */
	public Map<String, String> getGuildsList(){
		getConfig();
		Map<String,String> cac = new HashMap<String,String>();

		List<String> listOfGuildNames = new ArrayList<String>();
		for(String guildNames : config.getConfigurationSection("Guilds").getKeys(false)) {
			listOfGuildNames.add(guildNames);
		}
		
		for (int i = 0; i < listOfGuildNames.size(); i++) {
			cac.put(listOfGuildNames.get(i), String.valueOf(config.getInt(TAG + listOfGuildNames.get(i) + ".money")));
		}

		return cac;
	}
	
	/** Gets the home location using the guild's coordinates 
	 * @param guildName The guild name
	 * @return The home location
	 */
	public Location getHomeLocation(String guildName) {
		getConfig();
		String world = config.getString(TAG + guildName + ".location.world");
		int locX = config.getInt(TAG + guildName + ".location.locX");
		int locY = config.getInt(TAG + guildName + ".location.locY");
		int locZ = config.getInt(TAG + guildName + ".location.locZ");
		Location home = new Location(Bukkit.getWorld(world), locX, locY, locZ);
		return home;
	}
	
	/** Gets the guild logs
	 * @param guildName The guild name
	 * @return The guild logs
	 */
	public List<String> getLog(String guildName) {
		getLogConfig(guildName);
		List<String> messages = new ArrayList<String>();
		for(int i = 0; i <= 21; i++) {
			if(((String) config.get("AnthopiaLog" + " [" + i + "] ")) != null) {
				messages.add(config.getString("AnthopiaLog" + " [" + i + "] "));
			}
		}
		return messages;
	}
	
	//+------------------------------------------------------+ 
	//|    	              Files - Setters                    | 
	//+------------------------------------------------------+
	
	/** Sets the guild's data. Used to create a guild.
	 * @param guildName The guild name
	 * @param owerUuid The owner UUID
	 */
	public void setGuildData(String guildName, UUID owerUuid) {
		getConfig();
		try {
			config.set(TAG + guildName + ".info", "");
			config.set(TAG + guildName + ".money", 0);
			config.set(TAG + guildName + ".lvl", 1);
			config.set(TAG + guildName + ".exp", 0);
			config.set(TAG + guildName + ".tax", "disable");
			config.set(TAG + guildName + ".taxRate", 0);
			config.save(file);
			setMember(guildName, owerUuid, "3"); //TODO: A changer
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Remove the guild's data. Used to delete a guild.
	 * @param guildName The guild name
	 */
	public void removeGuildData(String guildName) {
		getConfig();
		try {
			config.set(TAG + guildName, null);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the guild information
	 * @param guildName The guild name
	 * @param info The guild info
	 */
	public void setInfo(String guildName, String info) {
		getConfig();
		try {
			config.set(TAG + guildName + ".info", info);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the guild money
	 * @param guildName The guild name
	 * @param money The amount of money
	 */
	public void setMoney(String guildName, double money) {
		getConfig();
		try {
			config.set(TAG + guildName + ".money", money);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the guild level
	 * @param guildName The guild name
	 * @param lvl The guild level
	 */
	public void setLvl(String guildName, double lvl) {
		getConfig();
		try {
			config.set(TAG + guildName + ".lvl", lvl);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the guild experience
	 * @param guildName The guild name
	 * @param exp The guild experience
	 */
	public void setExp(String guildName, double exp) {
		getConfig();
		try {
			config.set(TAG + guildName + ".exp", exp);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the guild tax
	 * @param guildName The guild name
	 * @param switchedTax The switched tax (enable or disable)
	 */
	public void setTax(String guildName, String switchedTax) {
		getConfig();
		try {
			config.set(TAG + guildName + ".tax", switchedTax);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the guild tax rate
	 * @param guildName The guild name
	 * @param rate The tax rate
	 */
	public void setTaxRate(String guildName, Integer rate) {
		getConfig();
		try {
			config.set(TAG + guildName + ".taxRate", rate);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the member in a guild 
	 * @param guildName The guild name
	 * @param memberUuid The member UUID
	 * @param rank The member rank
	 */
	public void setMember(String guildName, UUID memberUuid, String rank) {
		getConfig();
		try {
			members.put(memberUuid, rank);
			config.set(TAG + guildName + ".members." + memberUuid, rank);
			config.save(file);
			
			PlayerData playerData = new PlayerData(memberUuid);
			playerData.setGuild(guildName);
			playerData.setGuildRank(Integer.valueOf(rank));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Remove the member in a guild
	 * @param guildName The guild name
	 * @param playerUuid The player UUID
	 */
	public void removeMember(String guildName, UUID playerUuid) {
		try {
			getConfig();
			config.set(TAG + guildName + ".members." + playerUuid, null);
			config.save(file);
			
			PlayerData playerData = new PlayerData(playerUuid);
			playerData.setGuild("none");
			playerData.setGuildRank(0);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Sets the guild's home coordinates
	 * @param guildName The player UUID
	 * @param world The world
	 * @param locX The X coordinate
	 * @param locY The Y coordinate
	 * @param locZ The Z coordinate
	 */
	public void setHomeLocation(String guildName, String world, int locX, int locY, int locZ) {
		getConfig();
		try {
			config.set(TAG + guildName + ".location.world", world);
			config.set(TAG + guildName + ".location.locX", locX);
			config.set(TAG + guildName + ".location.locY", locY);
			config.set(TAG + guildName + ".location.locZ", locZ);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Set the guild's logs
	 * @param guildName The guild name
	 * @param playerUuid The player UUID
	 * @param log The log
	 */
	public void setLog(String guildName, UUID playerUuid, String log) {
		int valuesNb = 0;
		getLogConfig(guildName);
		for(int i = 1; i <= 21; i++) {
			if(((String) config.get("AnthopiaLog" + " [" + i + "] ")) == null) {
				valuesNb = i;
				break;
			}
		}
		
		try {
			if(valuesNb < 21) {
				config.set("AnthopiaLog" + " [" + String.valueOf(valuesNb) + "] ","§9" + Bukkit.getPlayer(playerUuid).getName() + " §7: " + log);
				config.save(file);
			}
			else {
				for(int i = 1; i <= 21; i++){ //Très chiant mais en gros on trie les logs. Si y'en a trop on efface le premier et on met un nouveau log en tant que dernier log.
					if(i == 20){
					config.set("AnthopiaLog" + " [" + i + "] ","§9" + Bukkit.getPlayer(playerUuid).getName() + " §7: " + log);
					config.save(file);
					}
					else {
						Integer j = i+1;
						String oldLog = config.getString("AnthopiaLog" + " [" + j + "] ");
						config.set("AnthopiaLog" + " [" + i + "] ", oldLog);
						config.save(file);
						
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//############################################################
	//# +------------------------------------------------------+ #
	//# |                    Inventories                       | #
	//# +------------------------------------------------------+ #
	//############################################################
	
	/** Gets an inventory created for the advantages
	 * @param guildName The guild name
	 * @return The inventory created
	 */
	public Inventory getAvantageInventory(String guildName) {
		Inventory avantage = Bukkit.createInventory(null, 27, "§8[Avantages de la guilde]");
		Material lock = Material.RED_STAINED_GLASS_PANE;
		Material unlock = Material.GREEN_STAINED_GLASS_PANE;
		
		//A HashMap is created that stores the levels to be reached by the guild for future guild benefits, 
		//and the numbers representing the location in the inventory.
		HashMap<String,String> compareLevels = Utilities.setHash(
				"100","22",
				"90","17",
				"80","16",
				"70","15",
				"60","14",
				"50","13",
				"40","12",
				"30","11",
				"20","10",
				"10","9"
				);
		
		//The advantages of guild, first parameter for the getItem method
		String[] avantageNb1 = new String[] {
				"- Salaire total : +10€","","- Salaire total : +15€","","- Allocation chômage : 25€","- Salaire total : +20€",
				"- Allocation chômage : 30€","","- Salaire total : +25€","- Salaire total : +30€"
		};
		
		//The advantages of guild, second parameter for the getItem method
		String[] avantageNb2 = new String[] {
				"","","","","","","","","- Allocation chômage : 35€","- Allocation chômage : 40€"
		};
		
		//Unassigned boxes are filled in with grey windows 
		for(int i=0; i<27; i++) {
			if(i < 9 || i > 17 || i!=22) {
				avantage.setItem(i, Utilities.getItem(Material.GRAY_STAINED_GLASS_PANE,1," ","",""));
			}
		}
		
		int i=0;
		//The affected boxes are filled in with the necessary information and tinted glass according to the guild level. 
		for(Map.Entry<String, String> map : compareLevels.entrySet()) {
			Material lockOrUnlock = Integer.parseInt(map.getKey()) <= getLvl(guildName) ? unlock : lock;
			String color = Integer.parseInt(map.getKey()) <= getLvl(guildName) ? "§a" : "§c";
			avantage.setItem(Integer.parseInt(map.getValue()), Utilities.getItem(lockOrUnlock,1,color + "Niveau " + map.getKey(),"§3" + avantageNb1[9-i],"§3" + avantageNb2[9-i]));
			i++;
		}
		
		return avantage;
	}
	
	/** Gets an inventory created for the guild
	 * @param guildName The guild name
	 * @return The inventory created
	 */
	public Inventory getGuildInventory(String guildName) {
		Inventory guild = Bukkit.createInventory(null, 27, "§8[" + guildName + "]");
		for(int i=0; i<27; i++) {
			if(i < 9 || i > 17 || i!=22) {
				guild.setItem(i, Utilities.getItem(Material.GRAY_STAINED_GLASS_PANE,1," ","",""));
			}
		}
		guild.setItem(11, Utilities.getItem(Material.ENCHANTING_TABLE,1,"§6Avantages","",""));
		guild.setItem(13, Utilities.getItem(Material.PAINTING,1, "§6" + guildName,"Members : " + String.valueOf(getGuildMembers(guildName).size()),""));
		guild.setItem(15, Utilities.getItem(Material.BREWING_STAND,1, "§9" + "Niveau " + getLvl(guildName) ,"§bExperience de la guilde " + ExpBar(guildName),"§8" + getExp(guildName) + " / " + NextGuildLevel(guildName)));
		guild.setItem(22, Utilities.getItem(Material.CHEST,1,"§6Coffre de guilde","§eArgent dans la guilde : ","§6 " + getMoney(guildName) + " euros"));
		return guild;
	}
	
	/** Gets an inventory created for members
	 * @param guildName The guild name
	 * @return The inventory created
	 */
	public Inventory getMembersInventory(String guildName) {
		Inventory membersInv = Bukkit.createInventory(null, 27, "§8[Membres de la guilde]");
		int size = getGuildMembers(guildName).size() > 26 ? 26 : getGuildMembers(guildName).size(); //Check pour éviter un crash si la liste des membres est plus grande que la capacité d'inventaire
		
		for(int i=0; i < size; i++) {
			String memberName = Bukkit.getOfflinePlayer(UUID.fromString(getGuildMembers(guildName).get(i))).getName();
			Player member = Bukkit.getPlayer(memberName);
			PlayerData playerData = new PlayerData(UUID.fromString(getGuildMembers(guildName).get(i)));
			int memberRank = playerData.getGuildRank();
			String afficheRank = "§eMembre";
			if(memberRank==1) afficheRank = "§9Comptable";
			else if(memberRank==2) afficheRank = "Admin";
			else if(memberRank==3) afficheRank = "§cFondateur";
			ItemStack skull = Utilities.getSkull(member, afficheRank, memberName);
			membersInv.setItem(i, skull);
		}
		return membersInv;
	}
	
	//############################################################
	//# +------------------------------------------------------+ #
	//# |                Experience and Levels                 | #
	//# +------------------------------------------------------+ #
	//############################################################
	
	/** Calculates the experience needed to pass the next guild level
	 * @param guildName The guild name
	 * @return The experience needed to pass the next guild level
	 */
	public double NextGuildLevel(String guildName) {
		double lvlbase = 500.0; //Niveau 1 : 500*1.20 = ? Niveau 2 : ?*1.20 = X
		Integer level = getLvl(guildName);
		for(Integer i = 0; i < 100; i++) {
			lvlbase = lvlbase * 1.20;
			if(level == i) 
				return (float) Math.floor(lvlbase);
		}
		return lvlbase;
	}
	
	/** Calculate the gain of experience for an action
	 * @param guildName The guild name
	 * @return The gain of experience
	 */
	public double GainExp(String guildName) {
		double exp = 50.0;
		Integer level = getLvl(guildName);
		for(Integer i = 0; i < 100; i++) {
			exp = exp * 1.05;
			if(level == i)
				return (double) Math.floor(exp);
		}
		return exp;
	}
	
	/** Show the experience bar based on the experience the guild has
	 * @param guildName The guild name
	 * @return The experience bar
	 */
	public String ExpBar(String guildName) {
		
		String niv = "§l";
		double maxExp = NextGuildLevel(guildName);
		double exp = getExp(guildName);
		double expComparatorValue = 0;
		String[] bar = new String[] {
				"§7||||||||||","|§7|||||||||","||§7||||||||","|||§7|||||||","||||§7||||||","|||||§7|||||",
				"||||||§7||||","|||||||§7|||","||||||||§7||","|||||||||§7|","||||||||||"
		};
		for (int i = -1; i < bar.length; i++) {
			if(exp < (maxExp * expComparatorValue)) {
				niv = "§l"+ ChatColor.DARK_AQUA + bar[i+1];
				break;
			}
			expComparatorValue += 0.10;
		}
		return niv;
	}
	
}
