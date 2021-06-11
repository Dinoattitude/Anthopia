package fr.dinoattitude.anthopia.listeners;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerDeathListener implements Listener{

	@EventHandler
	public void onPlayerDeath (PlayerDeathEvent event) {
		Player player = event.getEntity();
		
		Random random = new Random();
		int rdm = random.nextInt(100);
		
		player.sendMessage("§4Vous êtes mort en §c: X §8[§4" + Math.floor(event.getEntity().getPlayer().getLocation().getX()) + "§8]§c, Y §8[§4" + Math.floor(event.getEntity().getPlayer().getLocation().getY()) + "§8]§c, Z §8[§4" + Math.floor(event.getEntity().getPlayer().getLocation().getZ()) + "§8]");
		
		if(player.getKiller() != null) {
			if(rdm <= 10) {
				ItemStack skull = getSkull(player, player.getName());
				event.getDrops().add(skull);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack getSkull(Player player, String pseudo) {
		
		boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD");
		
		Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");
		
		ItemStack skull = new ItemStack(type, 1);
		
		if(!isNewVersion)
			skull.setDurability((short) 3);
		
		SkullMeta skullm = (SkullMeta) skull.getItemMeta();
		skullm.setOwningPlayer(player);
		skullm.setDisplayName(pseudo);
		skull.setItemMeta(skullm);
		return skull;
	}
	
}
