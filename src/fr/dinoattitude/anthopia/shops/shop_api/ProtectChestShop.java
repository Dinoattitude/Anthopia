package fr.dinoattitude.anthopia.shops.shop_api;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;

/** Every event who are protecting the shop from being damaged.
 * @author Dinoattitude
 * @version 2.3.6
*/
public class ProtectChestShop implements Listener{
	
	//Toutes les méthodes accèdent au fichier général du shop
	//Sauf: Méthodes avec events
	private World world = Bukkit.getWorld("world"); //Attention au nom du monde qui peux faire des threads infinis dans la console
	
	private FileConfiguration config;
	private File file;
	
	public final FileConfiguration getMainShopConfig() {
		initMainShopConfigShop();
		return config;
	}
	
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
	
	/** Check if there is a shop in the specified location.
	 * @param locX The x-coordinate.
	 * @param locY The y-coordinate.
	 * @param locZ The z-coordinate.
	 * @return true if a shop is found.
	 */
	public boolean isShop(int locX, int locY, int locZ) {
		try {
			getMainShopConfig();
			String result = config.getString(world.getName() + "." + locX + " " + locY + " " + locZ +  ".typeVente");
			if(result == null) return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	//Event that prevents the lava/water from destroying the shop.
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockFromTo(BlockFromToEvent event) {
		Block block = event.getBlock();
		Block targetBlock = event.getToBlock();
		if(targetBlock == null) return;
		if(targetBlock.getType() == Material.WHITE_CARPET && isShop(targetBlock.getX(),targetBlock.getY() - 1,targetBlock.getZ())) {
			if(block.getType() == Material.WATER || block.getType() == Material.LAVA) {
				event.setCancelled(true);
			}
		}
	}
	
	//Event that prevents the pistons from destroying the shop.
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPistonExtend(BlockPistonExtendEvent event) {
		if(event.getBlocks() == null) return;
		for(Block block : event.getBlocks()) {
			if(block.getType() == Material.WHITE_CARPET && isShop(block.getX(),block.getY() - 1,block.getZ())) {
				event.setCancelled(true);
			}
		}
	}
	
	//Event that prevents the shop from being deleted by an explosion.
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent event) {
		if(event.blockList() == null) return;
		for (Block block : event.blockList()) {
			if(block.getType() == Material.WHITE_CARPET && isShop(block.getX(),block.getY() - 1,block.getZ())) {
				event.setCancelled(true);
			}
			else if(block.getType() == Material.CHEST && isShop(block.getX(),block.getY(),block.getZ())){
				event.setCancelled(true);
			}
		}
	}
	
	//Event that prevents the shop from burning.
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBurn(BlockBurnEvent event) {
		if(event.getBlock() == null) return;
		Block block = event.getBlock();
		if(block.getType() == Material.WHITE_CARPET && isShop(block.getX(),block.getY() - 1,block.getZ()))
			event.setCancelled(true);
	}
	
	//Event that prevents the shop from taking damage.
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.getEntity() == null) return;
		Location entityLoc = event.getEntity().getLocation();
		if(isShop((int)entityLoc.getX()-1,(int)entityLoc.getY() - 1,(int)entityLoc.getZ()-1)){
			event.setCancelled(true);
		}
	}
	
	//Event that prevents the shop from being broken by hand by the players.
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBreakEvent(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if(event.getBlock() == null) return;
		if(block.getType() == Material.CHEST) {
			if(isShop(block.getX(), block.getY(), block.getZ())) {
				player.sendMessage("§cVous ne pouvez pas casser un shop !");
				event.setCancelled(true);
			}
		}
		else if(block.getType() == Material.WHITE_CARPET) {
			if(isShop(block.getX(), block.getY() - 1, block.getZ())) {
				player.sendMessage("§cVous ne pouvez pas casser un shop !");
				event.setCancelled(true);
			}
		}
	}
	
	//Event taht prevent the item on the shop to despawn
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void itemVanish(ItemDespawnEvent event) {
		ItemStack iDespawn = event.getEntity().getItemStack();
		if(iDespawn == null) return;
		if(iDespawn.getItemMeta().getLore() == null) return;
		if(iDespawn.getItemMeta().getLore().contains("FLOATING")) {
			event.setCancelled(true);
		}
	}
	
	//Event that prevent a player from pick up the item on the shop
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void itemPickup(EntityPickupItemEvent event) {
		if(event.getItem().getItemStack() == null) return;
		if(event.getItem().getItemStack().getItemMeta().getLore() == null) return;
		if(event.getItem().getItemStack().getItemMeta().getLore().contains("FLOATING")) {
			event.setCancelled(true);
		}
	}
	
}

