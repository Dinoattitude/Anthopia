package fr.dinoattitude.anthopia.listeners;

import java.security.SecureRandom;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import fr.dinoattitude.anthopia.utils.Utilities;

public class PlayerDeathListener implements Listener{

	@EventHandler
	public void onPlayerDeath (PlayerDeathEvent event) {
		Player player = event.getEntity();
		
		SecureRandom secureRandom = new SecureRandom();
		int randomNumber = secureRandom.nextInt(100);
		
		player.sendMessage("§4Vous êtes mort en §c: X §8[§4" + Math.floor(event.getEntity().getPlayer().getLocation().getX()) + "§8]§c, Y §8[§4" + Math.floor(event.getEntity().getPlayer().getLocation().getY()) + "§8]§c, Z §8[§4" + Math.floor(event.getEntity().getPlayer().getLocation().getZ()) + "§8]");
		
		if(player.getKiller() != null) {
			if(randomNumber <= 10) {
				ItemStack skull = Utilities.getSkull(player, null, player.getName());
				event.getDrops().add(skull);
			}
		}
	}
	
}
