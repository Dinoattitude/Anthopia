package fr.dinoattitude.anthopia.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import fr.dinoattitude.anthopia.Main;
import fr.dinoattitude.anthopia.utils.Messages;
import fr.dinoattitude.anthopia.utils.PlayerData;
import fr.dinoattitude.anthopia.utils.Rank;
import fr.dinoattitude.anthopia.utils.TeamsTagsManager;

public class RankCommand implements CommandExecutor{

	@Override
	public boolean onCommand( CommandSender sender,  Command cmd,  String label, String[] args) {
		
		if(!((sender instanceof Player) || (sender instanceof ConsoleCommandSender))) {
			return true;
		}
		
		boolean bypass = isOp(sender);
		
		if(args.length <= 1) {
			sender.sendMessage(Messages.INCORRECT_COMMAND.toString() + " <playerName> <rank>");
			return true;
		}
		
		Player target = Bukkit.getPlayer(args[0]);
		PlayerData targetData = new PlayerData(target.getUniqueId());
		
		if(!StringUtils.isNumeric(args[1])) {
			sender.sendMessage(Messages.INCORRECT_COMMAND.toString() + " Vous devez spécifier un nombre.");
			return true;
		}
		
		int power = Integer.valueOf(args[1]);
		
		if(bypass) {
			setRank(targetData, target, power);
			return true;
		}
			
		Player player = (Player) sender;
		
		boolean hasPerms = checkPerms(player);
		
		if(hasPerms) {
			PlayerData playerData = new PlayerData(player.getUniqueId());
			
			int playerRank = playerData.getRank();
			Boolean finder = false;
			
			//On check si le mec a la perm de rank au dessus de lui
			finder = playerRank > power ? true : false;

			if(finder) {
				
				player.sendMessage("§eLe joueur §9§l"+ target.getName()+ " §r§eà changé de grade");
				setRank(targetData, target, power);
				
			}
			else {
				player.sendMessage("§cVous n'avez pas la permission pour effectuer cette action");
			}
				
		}

		return true;
	}
	
	public void setRank(PlayerData targetData, Player target, int power) {
		String rankName = "";
		
		targetData.setRank(power);
		
		switch(power) {
		case 0:
			rankName = "Visiteur";
			break;
		case 1:
			rankName = "Citoyen";
			break;
		case 2:
			rankName = "VIP";
			break;
		case 3:
			rankName = "MODO";
			break;
		case 4:
			rankName = "ADMIN";
			break;
		case 5:
			rankName = "DEV";
			break;
		}
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + target.getUniqueId() + " group set " + rankName);
		TeamsTagsManager.setNameTag(target, Rank.powerToRank(targetData.getRank()).getOrderRank(), Rank.powerToRank(targetData.getRank()).getDisplayName(), "");
	}
	
	public boolean isOp(CommandSender sender) {
		
		boolean op = false;
		
		if(sender instanceof ConsoleCommandSender) {
			op = true;
		}
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.isOp()) {
				op = true;
			}
		}
		
		return op;
	}
	
	public boolean checkPerms(Player player) {
		
		if(Main.getPermissions().has(player, "anthopia.moderation.rank")) {
			return true;
		}
		
		return false;
	}
	
}
