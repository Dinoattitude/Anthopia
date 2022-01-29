package fr.dinoattitude.anthopia.commands;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dinoattitude.anthopia.Main;


public class CacCommand implements CommandExecutor{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;
		HashMap<String,Double> cac = new HashMap<>();
		
		for (OfflinePlayer OffPlayer : Bukkit.getOfflinePlayers()) {
			double amount = 0D;
			amount = Main.getEconomy().getBalance(OffPlayer.getName());
			cac.put(OffPlayer.getName(), amount);
		}
		
		//Trieur de Map
		Map<String, Double> sortedCac = cac.entrySet()
				.stream()
				.sorted(Map.Entry.<String,Double>comparingByValue().reversed())
				.collect(Collectors.toMap(
						Map.Entry::getKey, 
						Map.Entry::getValue, 
						(oldValue, newValue) -> oldValue, LinkedHashMap<String, Double>::new));
		
		int compteur = 1;
		for (Map.Entry<String, Double> entry : sortedCac.entrySet()) {
			String firstColor = " §7: §6";
			if(compteur == 1) firstColor = " §7: §a";
		    player.sendMessage("§e" + compteur + ". §9" + entry.getKey() + firstColor + entry.getValue());
		    compteur++;
		    if(compteur == 16) break;
		}

		return false;
	}
	
}