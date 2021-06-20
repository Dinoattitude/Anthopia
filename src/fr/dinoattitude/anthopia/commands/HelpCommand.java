package fr.dinoattitude.anthopia.commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class HelpCommand implements CommandExecutor{
	
	private static FileConfiguration config;
	private File file;
	
	public final FileConfiguration getConfig() {
		initConfig();
		return config;
	}
	
	private void initConfig() {
		File f = new File("plugins/Anthopia");
		if(!f.exists()) f.mkdirs();
		file = new File(f, "help.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
				
			}catch(IOException ioe) { ioe.printStackTrace();}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}

	@Override
	public boolean onCommand(CommandSender sender,Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;
		
		int index;
		
		try {
			index = Integer.parseInt(args[0]);
		} catch (Exception e) {
			index = 1;
		}
		sendHelp(player, index);
		return false;
	}
	
	public void sendHelp(Player player, int page) {
		try {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
		            getConfig().getString("help.page" + page).replace("$page", String.valueOf(page))));
		} catch (Exception e) {
			try {
		        player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
		              getConfig().getString("help.unknownPage").replace("$page", String.valueOf(page))));
		      } catch (Exception ee) {
		        player.sendMessage(
		            ChatColor.translateAlternateColorCodes('&', "&8------[ &6Help &7(" + page + "/3) &8]------"));
		        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCette page n'existe pas!"));
		      } 
		}
	}

}
