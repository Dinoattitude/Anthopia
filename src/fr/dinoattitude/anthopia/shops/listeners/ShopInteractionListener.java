package fr.dinoattitude.anthopia.shops.listeners;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.dinoattitude.anthopia.shops.api.ShopData;
import fr.dinoattitude.anthopia.shops.gui.GUI_ShopCreation;
import fr.dinoattitude.anthopia.shops.gui.GUI_ShopCustomer;
import fr.dinoattitude.anthopia.shops.gui.GUI_ShopDeletion;
import fr.dinoattitude.anthopia.shops.gui.GUI_ShopOwner;
import fr.dinoattitude.anthopia.utils.Messages;

/** Interaction listener to start the shops use.
 * @author Dinoattitude
 * @since 2.4.3
 * @version 2.4.3
*/
public class ShopInteractionListener implements Listener {

	private final ItemStack TRIGGER_ITEMSTACK = new ItemStack(Material.STICK, 1);
	private final Material REQUIRED_UNDERSHOP_BLOCK = Material.QUARTZ_BLOCK;
	private final Material REQUIRED_CARPET_MATERIAL = Material.WHITE_CARPET;

	@EventHandler
	public void onClic(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();
		
		if(event.getClickedBlock() == null) {
			return;
		}
		
		if(!(player.getInventory().getItemInMainHand().equals(TRIGGER_ITEMSTACK) && event.getClickedBlock().getType().equals(Material.CHEST))) {
			return;
		}
		
		if(!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
			return;
		}

		World currentWorld = player.getWorld();
		
		int chestLocX = (int) event.getClickedBlock().getX();
		int chestLocY = (int) event.getClickedBlock().getY();
		int chestLocZ = (int) event.getClickedBlock().getZ();
		
		ShopData shopData = new ShopData(currentWorld, chestLocX, chestLocY, chestLocZ);
		
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			event.setCancelled(true);
			
			Location carpet = new Location(currentWorld, chestLocX, (chestLocY + 1), chestLocZ);
			
			if(carpet.getBlock().getType().equals(REQUIRED_CARPET_MATERIAL)) {
				if(!shopData.isShopOwner(playerUUID)) { 
					
					GUI_ShopCustomer gui = new GUI_ShopCustomer(shopData);
				    player.openInventory(gui.getInventory());
					
					event.setCancelled(true);
					return;
				}
				
				GUI_ShopOwner gui = new GUI_ShopOwner(player, shopData);
			    player.openInventory(gui.getInventory());
			    
				event.setCancelled(true);
				return;
			}
			
			Location requiredUndershopBlockLocation = new Location(currentWorld, chestLocX, (chestLocY - 1), chestLocZ);
			
			if(requiredUndershopBlockLocation.getBlock().getType().equals(REQUIRED_UNDERSHOP_BLOCK)) {
				
				GUI_ShopCreation gui = new GUI_ShopCreation(carpet, shopData);
			    player.openInventory(gui.getInventory());
				
				event.setCancelled(true);
				return;
			}
			
			player.sendMessage(Messages.INVALID_BUILDING_SHOP_LOCATION.toString());
			return;
		}
		
		event.setCancelled(true);
		
		if(shopData.isShopOwner(playerUUID)) { 
			
			GUI_ShopDeletion gui = new GUI_ShopDeletion(shopData);
		    player.openInventory(gui.getInventory());
			
			event.setCancelled(true);
			return;
		}
		
		player.sendMessage(Messages.CANT_DESTROY_OTHERS_SHOPS.toString());
	}
}
