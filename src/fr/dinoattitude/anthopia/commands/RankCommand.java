package fr.dinoattitude.anthopia.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dinoattitude.anthopia.Main;
import fr.dinoattitude.anthopia.utils.PlayerData;
import fr.dinoattitude.anthopia.utils.Rank;
import fr.dinoattitude.anthopia.utils.TeamsTagsManager;

public class RankCommand implements CommandExecutor{

	@Override
	public boolean onCommand( CommandSender sender,  Command cmd,  String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		String valueRank = "";
		
		if(args.length >= 1) {
			if(Main.getInstance().setupPermissions()) {
				if(Main.getPermissions().has(player, "anthopia.moderation.rank")) {
					Player target = Bukkit.getPlayer(args[0]);
					int power = Integer.valueOf(args[1]);
					PlayerData playerData = new PlayerData(player.getUniqueId());
					PlayerData targetData = new PlayerData(target.getUniqueId());
					int playerRank = playerData.getRank();
					Boolean finder = false;
					
					//On check si le mec a la perm de rank au dessus de lui
					finder = playerRank > power ? true : false;

					if(finder) {
						targetData.setRank(power);
						player.sendMessage("§eLe joueur §9§l"+ target.getName()+ " §r§eà changé de grade");
						switch(power) {
						case 0:
							valueRank = "Visiteur";
							break;
						case 1:
							valueRank = "Citoyen";
							break;
						case 2:
							valueRank = "VIP";
							break;
						case 3:
							valueRank = "MODO";
							break;
						case 4:
							valueRank = "ADMIN";
							break;
						case 5:
							valueRank = "DEV";
							break;
						}
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + target.getUniqueId() + " group set " + valueRank);
						TeamsTagsManager.setNameTag(player, Rank.powerToRank(playerData.getRank()).getOrderRank(), Rank.powerToRank(playerData.getRank()).getDisplayName(), "");
					}
					else
						player.sendMessage("§cVous n'avez pas la permission pour effectuer cette action");
				}
			}
		}
		return true;
	}
	

}
