package fr.dinoattitude.anthopia.shops;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import fr.dinoattitude.anthopia.shops.shop_api.ShopData;

public class ShopUtils {
	
	private ShopData shopData;
	private UUID playerUUID;
	private Location maxLocation;
	private Location minLocation;

	public ShopUtils(ShopData shopInfo, UUID playerUUID) {
		shopData = shopInfo;
		initLocations();
	}
	
	public Location getMaximumLocation() {
		return maxLocation;
	}
	
	public Location getMinimumLocation() {
		return minLocation;
	}
	
	private void initLocations() {
		maxLocation = new Location(shopData.getShopWorld(), 
				(shopData.getShopLocationX()), 
				(shopData.getShopLocationY() + 1), 
				(shopData.getShopLocationZ()));
		
		minLocation = new Location(shopData.getShopWorld(), 
				(shopData.getShopLocationX() + 1), 
				(shopData.getShopLocationY() - 1), 
				(shopData.getShopLocationZ() + 1));
	}
	
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
	public void deleteItem() {
		
		Location chest = new Location(shopData.getShopWorld(), 
				(shopData.getShopLocationX()), 
				(shopData.getShopLocationY()), 
				(shopData.getShopLocationZ()));
		
		
		List<Entity> entList = (List<Entity>) chest.getWorld().getNearbyEntities(chest, 1, 3, 1);
		
		for(Entity current : entList) {
			if(current instanceof Item) {
				if(isInArea(current, maxLocation, minLocation)) {
					current.remove();
				}
			}
		}
	}
	
	
	/** Gets the next item witch will be displayed and remove the old one.
	 * @param blockName The name of the item.
	 */
	public void replaceItem(Material itemMaterial) {
		
		deleteItem();
		
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
		
		Location displayedItem = new Location(shopData.getShopWorld(), 
				(shopData.getShopLocationX() + 0.5), 
				(shopData.getShopLocationY() + 2), 
				(shopData.getShopLocationZ() + 0.5));
		
		Item i = shopData.getShopWorld().dropItem(displayedItem, iDrop);
		i.setVelocity(new Vector(0, 0, 0));
	}
	
	/** Create the shop.
	 * @param carpet The location of the carpet.
	 * @param blockName The block name of the block we are selling.
	 */
	public void createShop(Player player, Location carpet, ItemStack itemStack) {
		
		carpet.getBlock().setType(Material.WHITE_CARPET);
		
		addDisplayedShopItem(itemStack.getType());
		
		shopData.setShopData(playerUUID, 0, 0, itemStack, 0, 0);
		shopData.setShopSaleType(playerUUID, "purchase");
	}
	
}
