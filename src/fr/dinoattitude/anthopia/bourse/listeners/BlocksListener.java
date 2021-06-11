package fr.dinoattitude.anthopia.bourse.listeners;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.dinoattitude.anthopia.bourse.economy_api.BourseData;

public class BlocksListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBreakEvent(BlockBreakEvent event) {
		String blockBreak = event.getBlock().getType().toString();
		UUID uuidPlayer = event.getPlayer().getUniqueId();
		if(BourseData.isBlock(blockBreak)) {
			if(BourseData.isPlayer(uuidPlayer)) BourseData.setSalary(uuidPlayer, BourseData.getSalary(uuidPlayer) + BourseData.blockInitPrice.get(blockBreak));
			else BourseData.setSalary(uuidPlayer, BourseData.blockInitPrice.get(blockBreak));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlaceEvent(BlockPlaceEvent event) {
		String blockPlace = event.getBlock().getType().toString();
		UUID uuidPlayer = event.getPlayer().getUniqueId();
		if(BourseData.isBlock(blockPlace)) {
			if(BourseData.isPlayer(uuidPlayer)) BourseData.setSalary(uuidPlayer, BourseData.getSalary(uuidPlayer) - BourseData.blockInitPrice.get(blockPlace));
			else BourseData.setSalary(uuidPlayer, 0.0);
		}
	}
	
}
