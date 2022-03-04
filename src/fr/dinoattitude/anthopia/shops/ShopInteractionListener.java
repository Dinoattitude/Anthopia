package fr.dinoattitude.anthopia.shops;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.dinoattitude.anthopia.shops.shop_api.ShopInfo;
import fr.dinoattitude.anthopia.utils.Messages;

public class ShopInteractionListener implements Listener {

	private final ItemStack TRIGGER_ITEMSTACK = new ItemStack(Material.STICK, 1);
	private final Material REQUIRED_UNDERSHOP_BLOCK = Material.QUARTZ_BLOCK;
	private final Material REQUIRED_CARPET_MATERIAL = Material.WHITE_CARPET;
	
	private World currentWorld = null;
	private static Location carpet = null;
	private static ShopInfo shopInfo = null;

	public static Location getCarpet() {
		return carpet;
	}
	
	public static ShopInfo getClickedShopInfo() {
		return shopInfo;
	}
	
	@EventHandler
	public void onClic(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		
		if(event.getClickedBlock() == null) {
			return;
		}
		
		if(!(player.getInventory().getItemInMainHand().equals(TRIGGER_ITEMSTACK) && event.getClickedBlock().getType().equals(Material.CHEST))) {
			return;
		}
		
		if(!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
			return;
		}
		
		currentWorld = player.getWorld();
		
		int chestLocX = (int) event.getClickedBlock().getX();
		int chestLocY = (int) event.getClickedBlock().getY();
		int chestLocZ = (int) event.getClickedBlock().getZ();
		
		shopInfo = new ShopInfo(player, currentWorld, chestLocX, chestLocY, chestLocZ);
		
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			event.setCancelled(true);
			
			carpet = new Location(currentWorld, chestLocX, (chestLocY + 1), chestLocZ);
			
			if(carpet.getBlock().getType().equals(REQUIRED_CARPET_MATERIAL)) {
				if(!shopInfo.isShopOwner()) { 
					shopInfo.getCustomerShop(shopInfo.getShopOwner());
					event.setCancelled(true);
					return;
				}
				
				shopInfo.getOwnerShop();
				event.setCancelled(true);
				return;
			}
			
			Location requiredUndershopBlockLocation = new Location(currentWorld, chestLocX, (chestLocY - 1), chestLocZ);
			
			if(requiredUndershopBlockLocation.getBlock().getType().equals(REQUIRED_UNDERSHOP_BLOCK)) {
				shopInfo.inventoryCreateShop();
				event.setCancelled(true);
				return;
			}
			
			player.sendMessage(Messages.INVALID_BUILDING_SHOP_LOCATION.toString());
			return;
		}
		
		event.setCancelled(true);
		
		if(shopInfo.isShopOwner()) { 
			shopInfo.inventoryDeleteShop();
			event.setCancelled(true);
			return;
		}
		
		player.sendMessage(Messages.CANT_DESTROY_OTHERS_SHOPS.toString());
		
	}
}
