package fr.dinoattitude.anthopia.shops.shop_api;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import fr.dinoattitude.anthopia.bourse.economy_api.EconomyData;
import fr.dinoattitude.anthopia.utils.Utilities;

/** Every data related to the physical shop.
 * @author Dinoattitude
 * @version 2.3.6
*/
public class ShopData {
	
	private Player player;
	private World world;
	
	private int locX;
	private int locY;
	private int locZ;

	private String playerUuid;
	private UUID uuid;
	
	private FileConfiguration config;
	private File file;

	/** Creates an ShopData with Location, world and coordinates.
	 * @param player The player data.
	 * @param world The world where the player is located.
	 * @param chestLocX The x-coordinate of the shop.
	 * @param chestLocX The y-coordinate of the shop.
	 * @param chestLocX The z-coordinate of the shop.
	*/
	public ShopData(Player player, World world, int chestLocX, int chestLocY, int chestLocZ) {
		this.player = player;
		this.world = world;
		this.locX = chestLocX;
		this.locY = chestLocY;
		this.locZ = chestLocZ;
		this.uuid = player.getUniqueId();
		this.playerUuid = uuid.toString();
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
	public final FileConfiguration getPlayerShopConfig(String playerUUID) {
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
	private void initPlayerConfigShop(String playerUUID) {
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
		type = config.getString(world.getName() + "." + locX + " " + locY + " " + locZ + ".typeVente");
		return type;
	}
	
	/** Gets the name of the shop owner.
	 * @return The name of the shop owner.
	 */
	public String getShopOwner() {
		getMainShopConfig();
		String name = "";
		name = config.getString(world.getName() + "." + locX + " " + locY + " " + locZ + ".owner");
		return name;
	}
	
	/** Set the shop sale type.
	 * @param playerUUID The player UUID.
	 * @param typeVente Either "sale" or "purchase".
	 */
	public void setShopSaleType(String playerUUID, String typeVente) {
		try {
			getMainShopConfig();
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ +  ".typeVente", typeVente);
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ +  ".owner", playerUUID);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Delete the player shop data.
	 */
	public void deletePlayerShopData() {
		try {
			getPlayerShopConfig(playerUuid);
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ, null);
			config.save(file);
			getMainShopConfig();
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ, null);
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
	public int getStock(String playerUUID) {
		getPlayerShopConfig(playerUUID);
		int stock = 0;
		stock = config.getInt(world.getName() + "." + locX + " " + locY + " " + locZ + ".stock");
		return stock;
	}
	
	/** Gets the money of the shop.
	 * @param playerUUID The player UUID.
	 * @return The money in the shop.
	 */
	public int getMoney(String playerUUID) {
		getPlayerShopConfig(playerUUID);
		int amount = 0;
		amount = config.getInt(world.getName() + "." + locX + " " + locY + " " + locZ + ".money");
		return amount;
	}
	
	/** Gets the item sold in the shop.
	 * @param playerUUID The player UUID.
	 * @return The item sold in the shop.
	 */
	public ItemStack getItem(String playerUUID) {
		getPlayerShopConfig(playerUUID);
		ItemStack item = null;
		item = (ItemStack) config.get(world.getName() + "." + locX + " " + locY + " " + locZ + ".item");
		return item;
	}
	
	/** Gets the unit price for purchase.
	 * @param playerUUID The player UUID.
	 * @return The unit price for purchase.
	 */
	public int getPurchasePrice(String playerUUID) {
		getPlayerShopConfig(playerUUID);
		int amount = 0;
		amount = config.getInt(world.getName() + "." + locX + " " + locY + " " + locZ + ".purchasePrice");
		return amount;
	}
	
	/** Gets the unit price for sale.
	 * @param playerUUID The player UUID.
	 * @return The unit price for sale.
	 */
	public int getSellingPrice(String playerUUID) {
		getPlayerShopConfig(playerUUID);
		int amount = 0;
		amount = config.getInt(world.getName() + "." + locX + " " + locY + " " + locZ + ".sellingPrice");
		return amount;
	}
	
	/** Gets the item rate for the selector.
	 * @return The item rate.
	 */
	public int getRate() {
		getPlayerShopConfig(playerUuid);
		int rate = config.getInt(world.getName() + "." + locX + " " + locY + " " + locZ + ".rate");
		return rate;
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
	private void setShopData(int stock, int amount, ItemStack item, int purchasePrice, int sellingPrice) {
		try {
			getPlayerShopConfig(playerUuid);
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ + ".stock", stock);
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ + ".money", amount);
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ + ".item", item);
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ + ".purchasePrice", purchasePrice);
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ + ".sellingPrice", sellingPrice);
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ + ".rate", 1);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the shop stock.
	 * @param playerUUID The player UUID.
	 * @param amount The stock amount.
	 */
	public void setStock(String playerUUID, int amount) {
		try {
			getPlayerShopConfig(playerUUID);
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ + ".stock", amount);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the money of the shop.
	 * @param playerUUID The player UUID.
	 * @param amount The amount who would be set.
	 */
	public void setMoney(String playerUUID, int amount) {
		try {
			getPlayerShopConfig(playerUUID);
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ + ".money", amount);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the itemStack sold in the shop.
	 * @param type The item sold in the shop.
	 */
	public void setItem(ItemStack item) {
		try {
			getPlayerShopConfig(playerUuid);
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ + ".item", item);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the unit price for purchase.
	 * @param purchasePrice the unit price.
	 */
	public void setPurchasePrice(int purchasePrice) {
		try {
			getPlayerShopConfig(playerUuid);
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ + ".purchasePrice", purchasePrice);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the unit price for sale.
	 * @param sellingPrice the unit price.
	 */
	public void setSellingPrice(int sellingPrice) {
		try {
			getPlayerShopConfig(playerUuid);
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ + ".sellingPrice", sellingPrice);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Sets the item rate for the selector.
	 * @param rate The item rate.
	 */
	public void setRate(int rate) {
		try {
			getPlayerShopConfig(playerUuid);
			config.set(world.getName() + "." + locX + " " + locY + " " + locZ + ".rate", rate);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	//############################################################
	//# +------------------------------------------------------+ #
	//# |                    Inventories                       | #
	//# +------------------------------------------------------+ #
	//############################################################
	
	
	
	/** Gets the owner shop inventory.
	*/
	public void getOwnerShop() {
		ItemStack item = getItem(playerUuid);
		Inventory i9 = Bukkit.createInventory(null, 9, "§8[§cAnthopia Shop§8] Vendeur");
		i9.setItem(0, Utilities.getItem(Material.CHEST_MINECART, 1, "§6Déposer : " + item.getType().toString(), "En stock: " + getStock(playerUuid), null));
		i9.setItem(1, Utilities.getItem(Material.HOPPER_MINECART, 1, "§6Retirer : " + item.getType().toString(), "En stock: " + getStock(playerUuid), null));
		i9.setItem(2, Utilities.getItem(Material.MINECART, getRateValue(), "§6Taux de change", "Clic droit pour augmenter", "Clic gauche pour diminuer"));
		i9.setItem(4, item);
		if(getShopSaleType().equalsIgnoreCase("purchase"))
			i9.setItem(6, Utilities.getItem(Material.PAPER, 1, "§6Montant récolté", "Total: " + getMoney(playerUuid), "Cliquez pour récolter"));
		else
			i9.setItem(6, Utilities.getItem(Material.PAPER, 1, "§6Stock d'argent", "Montant: " + getMoney(playerUuid), "Cliquez pour ajouter 100 euros"));
		i9.setItem(7, Utilities.getItem(Material.COMPARATOR, 1, "§6Modifier le shop", null, null));
		i9.setItem(8, Utilities.getItem(Material.OAK_SIGN, 1, "§6Etat du shop :", "§3" + getShopSaleType(), "Cliquez pour changer"));
		player.openInventory(i9);
	}
	
	/** Gets the customer shop inventory.
	 * @param ownerUUID The UUID of the owner.
	*/
	public void getCustomerShop(String ownerUUID) {
		Inventory i9 = Bukkit.createInventory(null, 9, "§8[§cAnthopia Shop§8] Acheteur");
		String shopOwner = getShopOwner();
		if(shopOwner != null) {
			int priceAmount = 0;
			String typeTransaction = "";
			Material ownerMerchMaterial = getItem(ownerUUID).getType();
			
			if(getShopSaleType().equalsIgnoreCase("purchase")) {
				i9.setItem(0, Utilities.getItem(Material.PAPER, 1, "§6Acheter", "Prix unitaire :", "" + getPurchasePrice(shopOwner) + " euros"));
				priceAmount = getPurchasePrice(shopOwner);
				typeTransaction = "§6Acheter: ";
			}
			else if(getShopSaleType().equalsIgnoreCase("sale")) {
				i9.setItem(0, Utilities.getItem(Material.PAPER, 1, "§6Vendre", "Prix unitaire :", "" + getSellingPrice(shopOwner) + " euros"));
				priceAmount = getSellingPrice(shopOwner);
				typeTransaction = "§6Vendre: ";
			}
			i9.setItem(2, Utilities.getItem(ownerMerchMaterial, 64, typeTransaction, "x64 blocs ("+ (priceAmount * 64) + " euros)", null));
			i9.setItem(3, Utilities.getItem(ownerMerchMaterial, 32, typeTransaction, "x32 blocs ("+ (priceAmount * 32) + " euros)", null));
			i9.setItem(4, Utilities.getItem(ownerMerchMaterial, 16, typeTransaction, "x16 blocs ("+ (priceAmount * 16) + " euros)", null));
			i9.setItem(5, Utilities.getItem(ownerMerchMaterial, 8, typeTransaction, "x8 blocs ("+ (priceAmount * 8) + " euros)", null));
			i9.setItem(6, Utilities.getItem(ownerMerchMaterial, 1, typeTransaction, "x1 bloc ("+ (priceAmount) + " euros)", null));
			i9.setItem(8, Utilities.getItem(Material.RED_STAINED_GLASS_PANE, 1, "§cQuitter le shop", null, null));
			player.openInventory(i9);
		}
	}
	
	/** Gets the shop's implementation inventory.
	*/
	public void inventoryCreateShop() {
		Inventory i9 = Bukkit.createInventory(null, 9, "§8[§cAnthopia Shop§8] Créer un shop ?");
		i9.setItem(1, Utilities.getItem(Material.RED_STAINED_GLASS_PANE, 1, "§cNon", null, null));
		i9.setItem(7, Utilities.getItem(Material.GREEN_STAINED_GLASS_PANE, 1, "§2Oui", null, null));
		player.openInventory(i9);
	}
	
	/** Create the inventory for switching the selling product.
	*/
	public void inventoryChangeItemShop() {
		ItemStack item = getItem(playerUuid);
		Inventory i9 = Bukkit.createInventory(null, 9, "§8[§cAnthopia Shop§8] Change item ?");
		i9.setItem(4, item);
		player.openInventory(i9);
	}
	
	/** Gets the inventory for changing the unit price of both "purchase" and "sale" types.
	 */
	public void inventoryChangePriceShop() {
		Inventory i9 = Bukkit.createInventory(null, 9, "§8[§cAnthopia Shop§8] Modification");
		if(getShopSaleType().equals("purchase"))
			i9.setItem(0, Utilities.getItem(Material.PAPER, 1, "Prix unitaire :", "" + getPurchasePrice(playerUuid), null));
		else
			i9.setItem(0, Utilities.getItem(Material.PAPER, 1, "Prix unitaire :", "" + getSellingPrice(playerUuid), null));
		i9.setItem(2, Utilities.getItem(Material.LIGHT_BLUE_STAINED_GLASS, 1, "§3-10 euros", null, null));
		i9.setItem(3, Utilities.getItem(Material.BLUE_STAINED_GLASS, 1, "§3-1 euro", null, null));
		i9.setItem(4, Utilities.getItem(Material.ORANGE_STAINED_GLASS, 1, "§6+1 euro", null, null));
		i9.setItem(5, Utilities.getItem(Material.RED_STAINED_GLASS, 1, "§6+10 euros", null, null));
		i9.setItem(7, Utilities.getItem(Material.BLACK_STAINED_GLASS, 1, "§8réinitialiser", null, null));
		i9.setItem(8, Utilities.getItem(Material.RED_STAINED_GLASS_PANE, 1, "§cRetour", null, null));
		player.openInventory(i9);
	}
	
	/** Gets the inventory for deleting the current shop.
	 */
	public void inventoryDeleteShop() {
		Inventory i9 = Bukkit.createInventory(null, 9, "§8[§cAnthopia Shop§8] Supprimer ?");
		i9.setItem(1, Utilities.getItem(Material.RED_STAINED_GLASS_PANE, 1, "§cNon", null, null));
		i9.setItem(7, Utilities.getItem(Material.GREEN_STAINED_GLASS_PANE, 1, "§2Oui", null, null));
		player.openInventory(i9);
	}
	
	
	
	//############################################################
	//# +------------------------------------------------------+ #
	//# |               Item / Shop : Utilities                | #
	//# +------------------------------------------------------+ #
	//############################################################
	
	
	
	/** Check if an entity is in the defined area.
	 * @param entity The shearched entity.
	 * @param max The maximum location of the area.
	 * @param min The minimum location of the area.
	 * @return true if the entity is found in the area.
	*/
	private boolean isInArea(Entity entity, Location max, Location min) {
		double[] dim = new double[2];
		
		dim[0] = max.getX();
		dim[1] = min.getX();
		Arrays.sort(dim);
		if(entity.getLocation().getX() > dim[1] || entity.getLocation().getX() < dim[0]) return true;
		
		dim[0] = max.getY();
		dim[1] = min.getY();
		Arrays.sort(dim);
		if(entity.getLocation().getY() > dim[1] || entity.getLocation().getY() < dim[0]) return true;
		
		dim[0] = max.getZ();
		dim[1] = min.getZ();
		Arrays.sort(dim);
		if(entity.getLocation().getZ() > dim[1] || entity.getLocation().getZ() < dim[0]) return true;
		
		return false;
	}
	
	/** Delete the displayed item on the shop.
	 * @param max The maximum location of the area to check.
	 * @param min The minimum location of the area to check.
	*/
	public void deleteItem(Location max, Location min) {
		Location chest = new Location(world, locX, locY, locZ);
		List<Entity> entList = (List<Entity>) chest.getWorld().getNearbyEntities(chest, 1, 3, 1);
		for(Entity current : entList) {
			if(current instanceof Item) {
				if(isInArea(current, max, min)) {
					current.remove();
				}
			}
		}
	}
	
	/** Gets the next item witch will be displayed and remove the old one.
	 * @param blockName The name of the item.
	 */
	public void replaceItem(Material itemMaterial) {
		Location max = new Location(world, locX, locY+1, locZ);
		Location min = new Location(world, locX+1, locY-1, locZ+1);
		deleteItem(max, min);
		addDisplayedShopItem(itemMaterial);
}
	/** Display the new item onto the shop.
	 * @param blockName the block who will be displayed.
	 */
	public void addDisplayedShopItem(Material itemMaterial) {
		ItemStack iDrop = new ItemStack(itemMaterial, 1);
		ItemMeta iDropm = (ItemMeta) iDrop.getItemMeta();
		iDropm.setLore(Arrays.asList("FLOATING", "SHOP_ITEM"));
		iDropm.setUnbreakable(true);
		iDrop.setItemMeta(iDropm);
		Location displayedItem = new Location(world, (locX + 0.5), (locY + 2), (locZ + 0.5));
		Item i = world.dropItem(displayedItem, iDrop);
		i.setVelocity(new Vector(0,0,0));
	}
	
	/** Create the shop.
	 * @param carpet The location of the carpet.
	 * @param blockName The block name of the block we are selling.
	 */
	public void createShop(Location carpet, ItemStack itemStack) {
		carpet.getBlock().setType(Material.WHITE_CARPET);
		addDisplayedShopItem(itemStack.getType());
		setShopData(0, 0, itemStack, 0, 0);
		setShopSaleType(playerUuid, "purchase");
	}
	
	/** Make the transaction for selling or buying an item.
	 * @param amount The amount of blocks.
	 */
	public void tradeItem(int amount) { 
		// EDIT: Mais ça n'a rien à foutre la ?! 
		// EDIT: Moi du passé je te hais. C'est dla merde ptn. 
		// EDIT: Est-ce qu'au moins ça marche ???
		//PlayerInfo playerInfo = new PlayerInfo(player);
		String owner = getShopOwner();
		Material itemRA = getItem(owner).getType();
		ItemStack item = getItem(owner);
		
		if(getStock(owner) < amount) {
			player.sendMessage("§cCe shop n'a plus assez d'item en stock !");
		}
		else if(getShopSaleType().equals("purchase")) {
			if(EconomyData.getBalance(uuid) > getPurchasePrice(owner) * amount && amount > 0) {
				
				if(isSpaceForItems(player, amount)) { //Voir si l'item est stackable
					if(isItemStackable(item)) {
						setItemsInInventory(player, amount, item);
					}
					else {
						item.setAmount(amount);
						player.getInventory().addItem(item);
					}
					setStock(owner, getStock(owner) - amount);
					setMoney(owner, getMoney(owner) + (getPurchasePrice(owner) * amount));
					EconomyData.removeMoney(uuid, (double) getPurchasePrice(owner) * amount);
					player.sendMessage("§eVous avez acheté pour §6" + (getPurchasePrice(owner) * amount) + " §eeuros de marchandise.");
				}
				else {
					player.sendMessage("§8[§l§cAnthopia§r§8] §cIl n'y a pas assez de place dans votre inventaire.");
				}
				
			}
			else player.sendMessage("§8[§l§cAnthopia§r§8] §cVous n'avez pas assez d'argent pour acheter cette marchandise");
			
		}
		else {
			if(player.getInventory().containsAtLeast(new ItemStack(itemRA, amount), amount)) {
				if(getMoney(owner) < (getSellingPrice(owner) * amount)){
					player.sendMessage("§cCe shop n'a plus assez d'argent en stock, il ne peut donc pas acheter vos items !");
				}
				else {
					setStock(owner, getStock(owner) + amount);
					setMoney(owner, getMoney(owner) - (getSellingPrice(owner) * amount));
					EconomyData.addMoney(uuid, (double) getPurchasePrice(owner) * amount);
					player.getInventory().removeItem(new ItemStack(itemRA, amount));
					player.sendMessage("§eVous avez vendu §6" + (getPurchasePrice(owner) * amount) + " §eeuros de marchandise.");
				}
	    	}
			else
				player.sendMessage("§cVous n'avez pas assez d'item !");
			
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
	public boolean isShopOwner() {
		getPlayerShopConfig(playerUuid);
		if(config.get(world.getName() + "." + locX + " " + locY + " " + locZ + ".stock") == null) return false;
		return true;
	}
	
	/** Gets the rate value for the selector.
	 * @return The rate value.
	 */
	public int getRateValue() {
		switch(getRate()) {
			case 1: return 1;
			case 2: return 8;
			case 3: return 16;
			case 4: return 32;
			case 5: return 64;
			case 6: return 1;
		}
		return 1;
	}
	
	public boolean isSpaceForItems(Player player, int amount) {
		int freeSlots = 0;
		for(ItemStack stack : player.getInventory()) {
			if(stack == null) {
				freeSlots++;
			}
		}
		
		return freeSlots >= amount ? true : false;
	}
	
	public void setItemsInInventory(Player player, int amount, ItemStack item) {
		int compteur = 0;
		for(ItemStack stack : player.getInventory()) {
			if(stack == null) {
				stack = item;
			}
			
			if(compteur == amount) {
				break;
			}
			
			compteur++;
		}
	}
	
	public boolean isItemStackable(ItemStack item) {
		return item.getMaxStackSize() != 1 ? true : false;
	}

}

