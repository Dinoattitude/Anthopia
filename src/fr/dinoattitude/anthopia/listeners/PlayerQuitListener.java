package fr.dinoattitude.anthopia.listeners;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.dinoattitude.anthopia.bourse.economy_api.EconomyData;

public class PlayerQuitListener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		EconomyData.savePlayerEconomy(uuid);
	}
}
