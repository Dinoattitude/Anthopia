package fr.dinoattitude.anthopia.shops.api;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

/** Every data related to the physical shop.
 * @author Dinoattitude
 * @since 2.3.6
 * @version 2.4.3
*/
public class ShopData {
	
	private World world;
	
	private int locationX;
	private int locationY;
	private int locationZ;
	
	private FileConfiguration config;
	private File file;

	/** Creates an ShopData with Location, world and coordinates.
	 * @param world The world where the player is located.
	 * @param chestLocX The x-coordinate of the shop.
	 * @param chestLocX The y-coordinate of the shop.
	 * @param chestLocX The z-coordinate of the shop.
	*/
	public ShopData(World world, int chestLocX, int chestLocY, int chestLocZ) {
		this.world = world;
		this.locationX = chestLocX;
		this.locationY = chestLocY;
		this.locationZ = chestLocZ;
	}
	
	public World getShopWorld() {
		return world;
	}

	public int getShopLocationX() {
		return locationX;
	}

	public int getShopLocationY() {
		return locationY;
	}

	public int getShopLocationZ() {
		return locationZ;
	}

	
	
	//############################################################
	//# +------------------------------------------------------+ #
	//# |                   Configuration                      | #
	//# +------------------------------------------------------+ #
	//############################################################
	



	/** Gets the config of the player shop.
	 * @param playerUUID The player UUID.
	 * @return The config of the playerShop.
	 */
	public final FileConfiguration getPlayerShopConfig(UUID playerUUID) {
		initPlayerConfigShop(playerUUID);
		return config;
	}
	
	/** Gets the general shop configuration of all the shops.
	 * @return The general shop configuration.
	 */
	public final FileConfiguration getMainShopConfig() {
		initMainShopConfigShop();
		return config;
	}
	
	/** Initializing player files and verification of its existence.
	 * @param playerUUID The player UUID.
	 */
	private void initPlayerConfigShop(UUID playerUUID) {
		File f = new File("plugins/Anthopia/Shop");
		if(!f.exists()) f.mkdirs();
		file = new File(f, playerUUID + ".yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
				
			}catch(IOException ioe) { ioe.printStackTrace();}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	/** Initialisation of general shop files and verification of its existence.
	 */
	private void initMainShopConfigShop() {
		File f = new File("plugins/Anthopia/Shop/Util");
		if(!f.exists()) f.mkdirs();
		file = new File(f, "saveShop.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
				
			}catch(IOException ioe) { ioe.printStackTrace();}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	
	
	//############################################################
	//# +------------------------------------------------------+ #
	//# |    	  Getters and Setters for main file            | #
	//# +------------------------------------------------------+ #
	//############################################################
	

	
	/** Gets the shop sale type.
	 * @return The type of the transation.
	 */
	public String getShopSaleType() {
		getMainShopConfig();
		String type = "";
		type = config.getString(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".typeVente");
		return type;
	}
	
	/** Gets the name of the shop owner.
	 * @return The name of the shop owner.
	 */
	public UUID getShopOwner() {
		getMainShopConfig();
		UUID uuid;
		uuid = UUID.fromString(config.getString(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".owner"));
		return uuid;
	}
	
	/** Set the shop sale type.
	 * @param playerUUID The player UUID.
	 * @param typeVente Either "sale" or "purchase".
	 */
	public void setShopSaleType(UUID playerUUID, String typeVente) {
		try {
			getMainShopConfig();
			config.set(world.getName() + "." + locationX + " " + locationY + " " + locationZ +  ".typeVente", typeVente);
			config.set(world.getName() + "." + locationX + " " + locationY + " " + locationZ +  ".owner", playerUUID);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Delete the player shop data.
	 */
	public void deletePlayerShopData(UUID playerUUID) {
		try {
			getPlayerShopConfig(playerUUID);
			config.set(world.getName() + "." + locationX + " " + locationY + " " + locationZ, null);
			config.save(file);
			getMainShopConfig();
			config.set(world.getName() + "." + locationX + " " + locationY + " " + locationZ, null);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	//############################################################
	//# +------------------------------------------------------+ #
	//# |                     Getters                          | #
	//# +------------------------------------------------------+ #
	//############################################################

	
	
	/** Gets the shop stock.
	 * @param playerUUID The player UUID.
	 * @return The shop stock of the owner.
	 */
	public int getStock(UUID playerUUID) {
		getPlayerShopConfig(playerUUID);
		int stock = 0;
		stock = config.getInt(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".stock");
		return stock;
	}
	
	/** Gets the money of the shop.
	 * @param playerUUID The player UUID.
	 * @return The money in the shop.
	 */
	public int getMoney(UUID playerUUID) {
		getPlayerShopConfig(playerUUID);
		int amount = 0;
		amount = config.getInt(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".money");
		return amount;
	}
	
	/** Gets the item sold in the shop.
	 * @param playerUUID The player UUID.
	 * @return The item sold in the shop.
	 */
	public ItemStack getItem(UUID playerUUID) {
		getPlayerShopConfig(playerUUID);
		ItemStack item = null;
		item = (ItemStack) config.get(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".item");
		return item;
	}
	
	/** Gets the unit price for purchase.
	 * @param playerUUID The player UUID.
	 * @return The unit price for purchase.
	 */
	public int getPurchasePrice(UUID playerUUID) {
		getPlayerShopConfig(playerUUID);
		int amount = 0;
		amount = config.getInt(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".purchasePrice");
		return amount;
	}
	
	/** Gets the unit price for sale.
	 * @param playerUUID The player UUID.
	 * @return The unit price for sale.
	 */
	public int getSellingPrice(UUID playerUUID) {
		getPlayerShopConfig(playerUUID);
		int amount = 0;
		amount = config.getInt(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".sellingPrice");
		return amount;
	}
	
	
	
	//############################################################
	//# +------------------------------------------------------+ #
	//# |                     Setters                          | #
	//# +------------------------------------------------------+ #
	//############################################################
	
	
	
	/** Sets the shop data of a shop.
	 * @param stock The stock of blocks.
	 * @param amount The amount of money.
	 * @param type The type of the block in sale.
	 * @param purchasePrice The unit price for purchase.
	 * @param sellingPrice The unit price for sale.
	 */
	public void setShopData(UUID playerUUID, int stock, int amount, ItemStack item, int purchasePrice, int sellingPrice) {
		try {
			getPlayerShopConfig(playerUUID);
			config.set(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".stock", stock);
			config.set(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".money", amount);
			config.set(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".item", item);
			config.set(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".purchasePrice", purchasePrice);
			config.set(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".sellingPrice", sellingPrice);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the shop stock.
	 * @param playerUUID The player UUID.
	 * @param amount The stock amount.
	 */
	public void setStock(UUID playerUUID, int amount) {
		try {
			getPlayerShopConfig(playerUUID);
			config.set(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".stock", amount);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the money of the shop.
	 * @param playerUUID The player UUID.
	 * @param amount The amount who would be set.
	 */
	public void setMoney(UUID playerUUID, int amount) {
		try {
			getPlayerShopConfig(playerUUID);
			config.set(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".money", amount);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the itemStack sold in the shop.
	 * @param type The item sold in the shop.
	 */
	public void setItem(UUID playerUUID, ItemStack item) {
		try {
			getPlayerShopConfig(playerUUID);
			config.set(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".item", item);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the unit price for purchase.
	 * @param purchasePrice the unit price.
	 */
	public void setPurchasePrice(UUID playerUUID, int purchasePrice) {
		try {
			getPlayerShopConfig(playerUUID);
			config.set(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".purchasePrice", purchasePrice);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the unit price for sale.
	 * @param sellingPrice the unit price.
	 */
	public void setSellingPrice(UUID playerUUID, int sellingPrice) {
		try {
			getPlayerShopConfig(playerUUID);
			config.set(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".sellingPrice", sellingPrice);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//############################################################
	//# +------------------------------------------------------+ #
	//# |                       Others                         | #
	//# +------------------------------------------------------+ #
	//############################################################
	
	/** Check if the player is the shop owner.
	 * @return true if the player is the shop owner.
	*/
	public boolean isShopOwner(UUID playerUUID) {
		getPlayerShopConfig(playerUUID);
		if(config.get(world.getName() + "." + locationX + " " + locationY + " " + locationZ + ".stock") == null) return false;
		return true;
	}

}

