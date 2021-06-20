package fr.dinoattitude.anthopia.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dinoattitude.anthopia.bourse.economy_api.BourseData;


public class TestCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender.hasPermission("anthopia.moderation.eco")) {
			if(args.length >= 1) {
				if(args[0].equalsIgnoreCase("economy")) {
					for (UUID playerUuid : BourseData.getSalaryMap().keySet()) {
						Player player = Bukkit.getPlayer(playerUuid);
						if(player != null) {
							sender.sendMessage(player.getName() + " |§6 " + BourseData.getSalary(playerUuid) + " €");
						}
						else sender.sendMessage("§cErreur : Le joueur est null pour l'id §4"+ playerUuid);
					}

				}
			}
		}
		return false;
	}

}
