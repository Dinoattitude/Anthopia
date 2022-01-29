package fr.dinoattitude.anthopia.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dinoattitude.anthopia.bourse.economy_api.EconomyData;

public class PayCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		if(sender.hasPermission("anthopia.player")) {
			if(args.length != 2) sender.sendMessage("§8[§l§cAnthopia§r§8] §cCommande Incomplète : /pay <player> <amount>");
			else {
				Player target = Bukkit.getPlayer(args[0]);
				if(target != null) {
					Double amount = Double.parseDouble(args[1]);
					if(EconomyData.getBalance(player.getUniqueId()) > amount && amount > 0) {
						EconomyData.addMoney(target.getUniqueId(), amount);
						EconomyData.removeMoney(player.getUniqueId(), amount);
						sender.sendMessage("§eVous avez envoyé §6" + amount + " €  à " + target.getName());
						target.sendMessage("§eVous avez reçu §6" + amount + " €  de la part de " + sender.getName());
					}
					else {
						sender.sendMessage("§8[§l§cAnthopia§r§8] §cVous n'avez pas assez d'argent !");
					}
				}
				else
					sender.sendMessage("§8[§l§cAnthopia§r§8] §cCette personne n'existe pas ou n'est pas connectée");
			}
		}
		return true;
	}

}
