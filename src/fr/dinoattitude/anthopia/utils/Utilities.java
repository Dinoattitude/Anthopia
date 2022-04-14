package fr.dinoattitude.anthopia.utils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.bukkit.Material;
//import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import fr.dinoattitude.anthopia.Main;

//import net.minecraft.server.v1_16_R2.EntityPlayer;

/** Contains various useful functions all over the plugin
 * @author Dinoattitude
 * @version 2.4.3
 */
public class Utilities {
	
	/** Simplifies the raw construction of HashMaps
	 * @param pairs Data pairs
	 * @return The build HashMap
	 */
	public static HashMap<String,String> setHash(String...pairs) {
	    HashMap<String,String> map = new HashMap<String,String>(pairs.length/2);
	    for (int i=0; i < pairs.length; i+=2) 
	    	map.put(pairs[i], pairs[i + 1]);
	    return map; 
	}
	
	/** Round a double variable to two digit after the decimal point (Help manage money)
	 * @param value The value to round
	 * @return The rounded value
	 */
	public static double getRoundedValue(double value) {
		double roundedValue = value;
		if(value % 1 != 0) {
			DecimalFormat df = new DecimalFormat("#.##");
			roundedValue = Double.valueOf(df.format(value));
		}
		return roundedValue;
	}
	
	/** Calculates the time and put it into a string
	 * @param time The time to transform
	 * @return A string who's containing the time
	 */
	public static String calculateTime(long time) {
		long sec = time % 60;
		long min = (time / 60) % 60;
		long hours = (time / 60) / 60;
		return hours + ":" + min + ":" + sec;
	}
	
	@SuppressWarnings("deprecation")
	/** Create a head representing a player
	 * @param player The player instance
	 * @param rank The player rank | null if pseudo only
	 * @param pseudo The player name
	 * @return A head representing a player
	 */
	public static ItemStack getSkull(Player player, String rank, String pseudo) {
		
		boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD");
		
		Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");
		
		ItemStack skull = new ItemStack(type, 1);
		
		if(!isNewVersion)
			skull.setDurability((short) 3);
		
		SkullMeta skullm = (SkullMeta) skull.getItemMeta();
		skullm.setOwningPlayer(player);
		
		if(rank == null) {
			skullm.setDisplayName(pseudo);
		}
		else {
			skullm.setDisplayName("§3" + pseudo + " : §5" + rank);
		}
		
		skull.setItemMeta(skullm);
		return skull;
	}
	
	/** Create an ItemStack according to the entered parameters
	 * @param material The Material of the ItemStack
	 * @param nbItem The number of items the ItemStack has
	 * @param customName The customName (can be null)
	 * @param info1 The first information (can be null)
	 * @param info2 The second information (can be null)
	 * @return
	 */
	public static ItemStack getItem(Material material,int nbItem, String customName, String info1, String info2) {
		ItemStack it = new ItemStack(material, nbItem);
		ItemMeta itM = it.getItemMeta();
		itM.setDisplayName(customName);
		itM.setLore(Arrays.asList(info1,info2));
		it.setItemMeta(itM);
		return it;
	}
	
	/** Log and print exception's infos to the console
	 * @param exception The throwed exception 
	 * @param player The player involved
	 * @param className The class name where the exception comes from
	 */
	public static void customPlayerExceptionLogger(Exception exception, Player player, String className, long exceptionAmount) {
		
		if (exceptionAmount > 4)  {
			return;
		}
		
		if (exceptionAmount > 3)  {
			Main.setInfoLog(" [Antilag] Too many exceptions : disabling log messages for player " + player.getName());
			return;
		}
		
		Main.setSevereLog(
				className 
				+ ": " + exception.getCause() + ": "
				+ "Name [" + player.getName() +"] , "
				+ "UUID [" + player.getUniqueId() + "] , "
				+ "Online [" + player.isOnline() + "]"
		);
		exception.printStackTrace();

	}
}
