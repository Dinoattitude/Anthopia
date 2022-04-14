package fr.dinoattitude.anthopia.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dinoattitude.anthopia.bourse.economy_api.EconomyData;
import fr.dinoattitude.anthopia.utils.Messages;

public class PayCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {
			return true;
		}
		
		if(!sender.hasPermission("anthopia.player")) {
			return true;
		}
		
		if(args.length != 2) {
			sender.sendMessage(Messages.INCOMPLETE_COMMAND.toString() + "/pay <player> <amount>");
			return true;
		}
		
		Player player = (Player) sender;
		Player target = Bukkit.getPlayer(args[0]);
		
		if(target == null) {
			sender.sendMessage(Messages.UNKNOWN_PLAYER.toString());
			return true;
		}
		
		Double amount = Double.parseDouble(args[1]);
		
		if(!(EconomyData.getBalance(player.getUniqueId()) > amount && amount > 0)) {
			sender.sendMessage(Messages.NOT_ENOUGH_MONEY.toString());
			return true;
		}
		
		EconomyData.addMoney(target.getUniqueId(), amount);
		EconomyData.removeMoney(player.getUniqueId(), amount);
		sender.sendMessage("§eVous avez envoyé §6" + amount + " €  à " + target.getName());
		target.sendMessage("§eVous avez reçu §6" + amount + " €  de la part de " + sender.getName());

		return true;
	}

}
