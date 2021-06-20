package fr.dinoattitude.anthopia.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dinoattitude.anthopia.bourse.economy_api.EconomyData;

public class DonCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;
		
		if(sender.hasPermission("anthopia.player")) {
			if(args.length != 2) sender.sendMessage("§8[§l§cAnthopia§r§8] §cCommande Incomplète : /don <amount>");
			else {
				Double amount  = Double.parseDouble(args[0]);
				Double pAmount = EconomyData.getBalance(player.getUniqueId());
				
				String drinkName = "";
				String idx = "";
				boolean msg = false;
				Double divPlayerAmount = amount / (Bukkit.getOnlinePlayers().size() - 1);
				
				//On construit le message du joueur en prenant les arguments spécifiés dans la commande
				String message = "";
				for (int i = 1; i < args.length; i++) {
					String arg = args[i] + " ";
					message += arg;
				}
				
				//On attribut une boisson/message au don
				if(pAmount > amount && amount > 0) {
					if(amount >= 0 && amount < 20) {
						drinkName = "Champomy";
						idx = "du";
					}
					else if(amount >= 20 && amount < 100) {
						drinkName = "Cidre";
						idx = "du";
					}
					else if(amount >= 100 && amount < 500) {
						drinkName = "Vin";
						idx = "du";
					}
					else if(amount >= 500 && amount < 2000) {
						drinkName = "Vodka";
						idx = "de la";
					}
					else if(amount >= 2000 && amount < 10000) {
						drinkName = "Whisky";
						idx = "du";
						msg = true;
					}
					else if(amount >= 10000) {
						drinkName = "Champagne"; 
						idx = "du";
						msg = true;
					}
				
					Bukkit.broadcastMessage("§l§9" + sender.getName() + " §evous offre " + idx + " " + drinkName + ", remerciez le comme il se doit !");
					if(msg == true) {
						Bukkit.broadcastMessage("§eMessage de§9 " + sender.getName() + " §e" + message);
					}
					
					//Opérations économiques pour le sender et les joueurs
					EconomyData.removeMoney(player.getUniqueId(), amount);
					
					if(divPlayerAmount > 0) {
						for(Player players : Bukkit.getOnlinePlayers()) {
							if(!players.equals(sender)) {
								EconomyData.addMoney(player.getUniqueId(), divPlayerAmount);
								players.sendMessage("Vous venez de recevoir " + divPlayerAmount + " de la part de " + sender.getName());
							}	
						}
					}
					else 
						sender.sendMessage("§8[§l§cAnthopia§r§8] §cL'argent ne peux pas être partagé entre les joueurs : Trop peu d'argent comparé au nombre de joueurs présents");
				}
				else
					sender.sendMessage("§8[§l§cAnthopia§r§8] §cVous n'avez pas assez d'argent pour faire ce don !");
			}
		}
		return false;
	}

}
