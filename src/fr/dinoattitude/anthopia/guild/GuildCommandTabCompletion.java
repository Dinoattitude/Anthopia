package fr.dinoattitude.anthopia.guild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.dinoattitude.anthopia.guild.guild_api.GuildInfo;
import fr.dinoattitude.anthopia.utils.PlayerData;


public class GuildCommandTabCompletion implements TabCompleter{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		String[] initArguments = new String[] {
				"deposit","withdraw","list","member","info","create","join","leave","pay",
				"accept","rank","ranks","kick","logs","home","sethome" 
		};
		String[] amount = new String[] { "10","100","1000" };
		
		String[] guildRanks = new String[] { "membre","comptable","admin","fondateur" };
		
		List<String> arguments = new ArrayList<String>(); //Les arguments retournés
		
		GuildInfo guildInfo = new GuildInfo();
		List<String> guildNameList = new ArrayList<String>(); //List des nom des guildes existantes
		try {
			for(String guildNames : guildInfo.getGuildsList().keySet()) { //Loop pour récup les nom des guildes
				guildNameList.add(guildNames);
			}
		} catch (Exception e) {
			guildNameList = new ArrayList<String>();
		}

		List<String> playerNameList = new ArrayList<String>(); //List des nom des joueurs connectés
		for(Player players : Bukkit.getOnlinePlayers()) { //Loop pour récup les nom des joueurs
			playerNameList.add(players.getName());
		}
		
		if(sender instanceof Player) {				
			
			if(args.length == 1) {
				if(!args[0].equals("")) {
					for(String argument : initArguments) {
						if(argument.startsWith(args[0].toLowerCase())) {
							arguments.add(argument);
						}
					}
				}
				else Collections.addAll(arguments, initArguments);
				return arguments;
			}
			else if(args.length == 2) {
				if(args[0].equals("deposit") || args[0].equals("withdraw") || args[0].equals("pay")) {
					Collections.addAll(arguments, amount);
				}
				else if(args[0].equals("member")) {
					arguments.add("list");
				}
				else if(args[0].equals("info") || args[0].equals("join")) {
					if(args[0].equals("info")) {
						arguments.add("modify");
					}
					arguments = guildNameList;
				}
				else if(args[0].equals("rank")) {
					arguments = playerNameList;
				}
				else if(args[0].equals("kick")) {
					PlayerData playerData = new PlayerData(((Player) sender).getUniqueId());
					String guildName = playerData.getGuild();
					if(guildName != null) {
						for(int i=0; i < guildInfo.getGuildMembers(guildName).size(); i++) {
							String memberName = Bukkit.getOfflinePlayer(UUID.fromString(guildInfo.getGuildMembers(guildName).get(i))).getName();
							arguments.add(memberName);
						}
					}
				}
				else if(args[0].equals("home")) {
					arguments.add("tax");
					arguments.add("set");
				}
				else return new ArrayList<>();
				return arguments;
			}
			else if(args.length == 3) {
				if(args[1].equals("list") || args[0].equals("pay")) {
					arguments = guildNameList;
				}
				else if(args[0].equals("rank")){
					arguments.add("set");
				}
				else if(args[1].equals("tax") || args[1].equals("set")){
					if(args[1].equals("set")) {
						arguments.add("tax");
					}
					else {
						arguments.add("enable");
						arguments.add("disable");
					}
				}
				else return new ArrayList<>();
				return arguments;
			}
			else if(args.length == 4) {
				if(args[0].equals("rank")) {
					Collections.addAll(arguments, guildRanks);
				}
				else if(args[2].equals("tax")) {
					Collections.addAll(arguments, amount);
				}
				else return new ArrayList<>();
				return arguments;
			}
			else return new ArrayList<>();

		}

		return new ArrayList<>(); //Return null renvoie un erreur car pas d'arguments retournés
	}
}
