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


public class ChestShop implements Listener{
	
	private World world;
	private static Location carpet;
	
	private static int chestLocX;
	private static int chestLocY;
	private static int chestLocZ;
	
	//Nedeed to transmit data to ShopInventoryListener
	public static int getChestLocX() {
		return chestLocX;
	}

	public static int getChestLocY() {
		return chestLocY;
	}

	public static int getChestLocZ() {
		return chestLocZ;
	}
	
	public static Location getCarpet() {
		return carpet;
	}
	
	
	@EventHandler
	public void onClic(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack stick = new ItemStack(Material.STICK, 1);
		
		//Checking the action of the player
		//If want to open the shop :
		if(player.getInventory().getItemInMainHand().equals(stick) && (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST)) {
			event.setCancelled(true); //Cancelling the event

			world = player.getWorld();
			
			//Gets the location of the shop
			chestLocX = (int)event.getClickedBlock().getX();
			chestLocY = (int)event.getClickedBlock().getY();
			chestLocZ = (int)event.getClickedBlock().getZ();
			
			//Creating a shopInfo instance
			ShopInfo shopInfo = new ShopInfo(player, world, chestLocX, chestLocY, chestLocZ);
			
			//Gets the carpet location
			carpet = new Location(world, chestLocX, (chestLocY + 1), chestLocZ);
			
			Location verifBlock = new Location(world, chestLocX, (chestLocY - 1), chestLocZ);
			
			if(carpet.getBlock().getType().equals(Material.WHITE_CARPET)) {
				if(!shopInfo.isShopOwner()) { 
					shopInfo.getCustomerShop(shopInfo.getShopOwner());
					event.setCancelled(true);
				}
				else {
					shopInfo.getOwnerShop();
					event.setCancelled(true);
				}
			}
			else {
				if(verifBlock.getBlock().getType().equals(Material.QUARTZ_BLOCK)) {
					shopInfo.inventoryCreateShop();
					event.setCancelled(true);
				}
				else
					player.sendMessage("§cVous ne pouvez pas construire un shop ici !");
			}
		}
		//If want to destroy the shop :
		else if(player.getInventory().getItemInMainHand().equals(stick) && (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST)) {
			event.setCancelled(true);
			
			world = player.getWorld();
			
			chestLocX = (int)event.getClickedBlock().getX();
			chestLocY = (int)event.getClickedBlock().getY();
			chestLocZ = (int)event.getClickedBlock().getZ();
			
			ShopInfo shopInfo = new ShopInfo(player, world, chestLocX, chestLocY, chestLocZ);
			if(shopInfo.isShopOwner()) { 
				shopInfo.inventoryDeleteShop();
				event.setCancelled(true);
			}
			else
				player.sendMessage("§cVous ne pouvez pas détruire un shop qui ne vous appratient pas !");
			
		}
	}
}
