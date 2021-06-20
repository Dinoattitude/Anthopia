package fr.dinoattitude.anthopia.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RestartCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(label.equalsIgnoreCase("restartath")) {
			if(sender.hasPermission("anthopia.moderation.redemarrage")) {
				Bukkit.getServer().reload();
				tellConsole("§8[§l§cAnthopia§r§8] §eRedémarrage");
			}
			else
				sender.sendMessage("§8[§l§cAnthopia§r§8] §cVous n'avez pas la permission pour executer cette commande");
		}
		else if(label.equalsIgnoreCase("restartauto")) {
			if(sender.hasPermission("anthopia.moderation.redemarrage")) {
				tellConsole("§8[§l§cAnthopia§r§8] §eRedémarrage dans une minute !");
			}
			else
				sender.sendMessage("§8[§l§cAnthopia§r§8] §cVous n'avez pas la permission pour executer cette commande");
		}
		return true;
	}
	

	public void tellConsole(String message){
		Bukkit.getServer().broadcastMessage(message);
	}
	
}