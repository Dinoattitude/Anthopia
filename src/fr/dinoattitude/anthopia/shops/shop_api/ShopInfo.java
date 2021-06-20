package fr.dinoattitude.anthopia.shops.shop_api;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;


/** Accessor for the ShopData class.
 * @author Dinoattitude
 * @version 2.3.6
*/
public class ShopInfo {

	private Player player;
	private World world;
	private int chestLocX;
	private int chestLocY;
	private int chestLocZ;
	private ShopData shopData;
	
	/** Creates an ShopInfo with Location, world and coordinates to access the ShopData methods.
	 * @param player The player data.
	 * @param world The world where the player is located.
	 * @param chestLocX The x-coordinate of the shop.
	 * @param chestLocX The y-coordinate of the shop.
	 * @param chestLocX The z-coordinate of the shop.
	*/
	public ShopInfo(Player player, World world, int chestLocX, int chestLocY, int chestLocZ) {
		this.player = player;
		this.world = world;
		this.chestLocX = chestLocX;
		this.chestLocY = chestLocY;
		this.chestLocZ = chestLocZ;
		this.shopData = new ShopData(player, world, chestLocX, chestLocY, chestLocZ);
	}

	public Player getPlayer() {
		return player;
	}

	public World getWorld() {
		return world;
	}

	public int getChestLocX() {
		return chestLocX;
	}

	public int getChestLocY() {
		return chestLocY;
	}

	public int getChestLocZ() {
		return chestLocZ;
	}

	public ShopData getShopData() {
		return shopData;
	}
	
	//Main Configuration
	
	public void deletePlayerShopData() {
		shopData.deletePlayerShopData();
	}
	
	public String getShopOwner() {
		return shopData.getShopOwner();
	}
	
	public String getShopSaleType() {
		return shopData.getShopSaleType();
	}
	
	public void setShopSaleType(String playerUUID, String typeVente) {
		shopData.setShopSaleType(playerUUID, typeVente);
	}
	
	//Getters shop
	
	public int getStock(String PlayerUUID) {
		return shopData.getStock(PlayerUUID);
	}
	
	public int getMoney(String PlayerUUID) {
		return shopData.getMoney(PlayerUUID);
	}
	
	public String getType(String playerUUID) {
		return shopData.getType(playerUUID);
	}
	
	public int getPurchasePrice(String playerUUID) {
		return shopData.getPurchasePrice(playerUUID);
	}
	
	public int getSellingPrice(String playerUUID) {
		return shopData.getSellingPrice(playerUUID);
	}
	
	public int getRate() {
		return shopData.getRate();
	}
	
	//Setters shop

	public void setStock(String PlayerUUID, int amount) {
		shopData.setStock(PlayerUUID, amount);
	}
	
	public void setMoney(String playerUUID, int amount) {
		shopData.setMoney(playerUUID, amount);
	}
	
	public void setType(String type) {
		shopData.setType(type);
	}
	
	public void setPurchasePrice(int purchasePrice) {
		shopData.setPurchasePrice(purchasePrice);
	}
	
	public void setSellingPrice(int sellingPrice) {
		shopData.setSellingPrice(sellingPrice);
	}
	
	public void setRate(int rate) {
		shopData.setRate(rate);
	}
	
	//Inventories
	
	public void getOwnerShop() {
		shopData.getOwnerShop();
	}
	
	public void getCustomerShop(String ownerUUID) {
		shopData.getCustomerShop(ownerUUID);
	}
	
	public void inventoryCreateShop() {
		shopData.inventoryCreateShop();
	}
	
	public void inventoryChangeItemShop() {
		shopData.inventoryChangeItemShop();
	}
	
	public void inventoryChangePriceShop() {
		shopData.inventoryChangePriceShop();
	}
	
	public void inventoryDeleteShop() {
		shopData.inventoryDeleteShop();
	}

	//Item / Shop : Utilities
	
	public void deleteItem(Location max, Location min) {
		shopData.deleteItem(max, min);
	}
	
	public void replaceItem(String blockName) {
		shopData.replaceItem(blockName);
	}
	
	public void createShop(Location carpet, String blockName) {
		shopData.createShop(carpet, blockName);
	}
	
	public void tradeItem(int amount) {
		shopData.tradeItem(amount);
	}
	
	//Others
	
	public boolean isShopOwner() {
		return shopData.isShopOwner();
	}
	
	public int getRateValue() {
		return shopData.getRateValue();
	}
}
