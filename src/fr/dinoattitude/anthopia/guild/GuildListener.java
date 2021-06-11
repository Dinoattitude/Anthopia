package fr.dinoattitude.anthopia.guild;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import fr.dinoattitude.anthopia.guild.guild_api.GuildInfo;
import fr.dinoattitude.anthopia.utils.PlayerData;


public class GuildListener implements Listener{

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		
		String inventoryName = event.getView().getTitle();
		UUID playerUuid = event.getWhoClicked().getUniqueId();
		
		GuildInfo guildInfo = new GuildInfo();
		PlayerData playerData = new PlayerData(playerUuid);
		String guildName = playerData.getGuild();
		Material current = null;

		if(inventoryName.equalsIgnoreCase("§8[Avantages de la guilde]") || inventoryName.equalsIgnoreCase("§8[Membres de la guilde]") || inventoryName.equalsIgnoreCase("§8[" + guildName + "]")) {
			Player player = Bukkit.getPlayer(playerUuid);

			Inventory avantages = guildInfo.getAvantageInventory(guildName); 
			Inventory members = guildInfo.getMembersInventory(guildName);
			
			if(event.getCurrentItem() == null) event.setCancelled(true);
			else current = event.getCurrentItem().getType();

			if(inventoryName.equalsIgnoreCase("§8[" + guildName + "]")) {
				event.setCancelled(true);
				if(current == Material.ENCHANTING_TABLE) {
					player.closeInventory();
					player.openInventory(avantages);
				}
				else if(current == Material.PAINTING) {
					player.closeInventory();
					player.openInventory(members);
				}
			}
			else if(inventoryName.equalsIgnoreCase("§8[Avantages de la guilde]") || inventoryName.equalsIgnoreCase("§8[Membres de la guilde]")){
				event.setCancelled(true);
			}
		}
	}
}
