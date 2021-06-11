package fr.dinoattitude.anthopia.bourse;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dinoattitude.anthopia.Main;
import fr.dinoattitude.anthopia.bourse.economy_api.EconomyData;
import fr.dinoattitude.anthopia.utils.Messages;

public class EconomyCommand implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;
		UUID pUuid = player.getUniqueId();
		
		if(args.length == 0){
			sender.sendMessage("§eVous avez actuellement §6" + EconomyData.getBalance(pUuid) + " € §esur vous,");
			sender.sendMessage("§eainsi que §6" + EconomyData.getBankAccount(pUuid) + " € §edans votre compte bancaire.");
		}
		else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("set")) {
			if(sender.hasPermission("anthopia.moderation.eco")) {
				if(args.length != 3) sender.sendMessage(Messages.PLUGIN_NAME.toString() + Messages.INCOMPLETE_COMMAND + "/money add/remove/set <player> <amount>");
				else {
					if(!EconomyData.hasAccount(Bukkit.getPlayer(args[1]).getUniqueId())) {
						sender.sendMessage("§8[§l§cAnthopia§r§8] §cLe joueur n'a pas de compte banquaire");
					}
					else {
						double amount = 0;
						try {
							amount = Double.parseDouble(args[2]);
						} catch (Exception e) {
							sender.sendMessage("§8[§l§cAnthopia§r§8] §cVous devez entrer un nombre");
						}
						Player target = Bukkit.getPlayer(args[1]);
						if(target != null) {
							UUID playerUuid = Bukkit.getPlayer(args[1]).getUniqueId();
							if(args[0].equalsIgnoreCase("add")) {
								EconomyData.setBalance(playerUuid, EconomyData.getBalance(playerUuid) + amount);
								EconomyData.addMoney(playerUuid, amount);
								sender.sendMessage("§eVous venez d'ajouter §6" + amount + "€ §eau joueur §9" + args[1]);
							}
							else if(args[0].equalsIgnoreCase("remove")) {
								EconomyData.setBalance(playerUuid, EconomyData.getBalance(playerUuid) - amount);
								EconomyData.removeMoney(playerUuid, amount);
								sender.sendMessage("§eVous venez de retirer §6" + amount + "€ §eau joueur §9" + args[1]);
							}
							else if(args[0].equalsIgnoreCase("set")) {
								EconomyData.setBalance(playerUuid, amount);
								EconomyData.removeMoney(playerUuid, Main.getEconomy().getBalance(target));
								EconomyData.addMoney(playerUuid, amount);
								sender.sendMessage("§eVous venez de définir le montant de §6" + amount + "€ §epour la bourse du joueur §9" + args[1]);
							}
							else sender.sendMessage(Messages.PLUGIN_NAME.toString() + Messages.INCORRECT_COMMAND + "/money add/remove/set <player> <amount>");
						}
						else sender.sendMessage(Messages.PLUGIN_NAME.toString() + Messages.UNKNOWN_PLAYER);
					}
				}
			}
		}
		else if(args[0].equalsIgnoreCase("deposit") || args[0].equalsIgnoreCase("retire")) {
			if(sender.hasPermission("anthopia.player") || sender.hasPermission("anthopia.moderation.eco")) {
				if(args.length != 2) sender.sendMessage(Messages.PLUGIN_NAME.toString() + Messages.INCOMPLETE_COMMAND + "/money deposit/retire <amount>");
				else {
					double amount = 0;
					try {
						amount = Double.parseDouble(args[1]);
					} catch (Exception e) {
						sender.sendMessage("§8[§l§cAnthopia§r§8] §cVous devez entrer un nombre");
					}
					if(EconomyData.getBalance(pUuid) > amount && EconomyData.getBalance(pUuid) > 0) {
						if(args[0].equalsIgnoreCase("deposit")) {
							EconomyData.setBalance(pUuid, EconomyData.getBalance(pUuid) - amount);
							EconomyData.setBankAccount(pUuid, EconomyData.getBankAccount(pUuid) + amount);
							EconomyData.removeMoney(pUuid, amount);
							sender.sendMessage("§eVous avez mis dans votre compte bancaire §6" + amount + " €");
						}
						else if(args[0].equalsIgnoreCase("retire")) {
							if(EconomyData.getBankAccount(pUuid) > amount) {
								EconomyData.setBalance(pUuid, EconomyData.getBalance(pUuid) + amount);
								EconomyData.setBankAccount(pUuid, EconomyData.getBankAccount(pUuid) - amount);
								EconomyData.addMoney(pUuid, amount);
								sender.sendMessage("§eVous avez retiré de votre compte bancaire §6" + amount + " €");
							}
							else sender.sendMessage(Messages.PLUGIN_NAME.toString() + Messages.NOT_ENOUGH_CB_MONEY);
							
						}
						else sender.sendMessage(Messages.PLUGIN_NAME.toString() + Messages.INCOMPLETE_COMMAND + "/money deposit/retire <amount>");
					}
					else
						sender.sendMessage(Messages.PLUGIN_NAME.toString() + Messages.NOT_ENOUGH_MONEY);
				}
			}
		}
		else
			sender.sendMessage("§8[§l§cAnthopia§r§8] §cVous n'avez pas la permission ou cette commande n'existe pas -> /help");
		
		return true;
	}
}
