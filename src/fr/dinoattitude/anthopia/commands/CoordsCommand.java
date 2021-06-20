package fr.dinoattitude.anthopia.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoordsCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;
		
		if(args.length == 0) {
			for(Player players : Bukkit.getOnlinePlayers()) {
				players.sendMessage("§eCoordonnées de §9" + player.getName() + " §7: §2X §8[§3" + Math.floor(player.getLocation().getX()) + "§8]§7, §2Y §8[§3" + Math.floor(player.getLocation().getY()) + "§8]§7, §2Z §8[§3" + Math.floor(player.getLocation().getZ()) + "§8]");
			}
		}
		else {
			player.sendMessage("§8[§l§cAnthopia§r§8] §cCommande Incomplète : /coords");
		}
		return true;
	}

}