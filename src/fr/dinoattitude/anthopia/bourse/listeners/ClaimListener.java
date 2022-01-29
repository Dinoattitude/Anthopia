package fr.dinoattitude.anthopia.bourse.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import biz.princeps.landlord.api.events.LandPostClaimEvent;
import biz.princeps.landlord.api.events.LandPreClaimEvent;
import biz.princeps.landlord.api.events.LandUnclaimEvent;
import fr.dinoattitude.anthopia.bourse.economy_api.EconomyData;

public class ClaimListener implements Listener{

	private final Double landValue = 120D; //The value for the land purchase
	private final Double landConsumedValue = 60D; //The value for the land sold
	
	@EventHandler
	public void onLandPreClaim(LandPreClaimEvent event) {
		Player player  = event.getPlayer();
		if(!(EconomyData.getBalance(player.getUniqueId()) > landValue)) {
			player.sendMessage("§8[§l§cAnthopia§r§8] §cVous n'avez pas assez d'argent pour claim !");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onLandPostClaim(LandPostClaimEvent event) {
		Player player  = event.getPlayer();
		if(EconomyData.getBalance(player.getUniqueId()) > landValue) {
			EconomyData.removeMoney(player.getUniqueId(), landValue);
		}
		else player.sendMessage("§8[§l§cAnthopia§r§8] §cVous n'avez pas assez d'argent pour claim !");
	}
	
	@EventHandler
	public void onLandUnclaimEvent(LandUnclaimEvent event) {
		Player player  = event.getPlayer();
		EconomyData.addMoney(player.getUniqueId(), landConsumedValue);
	}
	
}
