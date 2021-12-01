package fr.dinoattitude.anthopia.commands;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dinoattitude.anthopia.listeners.PlayerChatListener;


public class QuoteCommand implements CommandExecutor{

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		return false;
	}
	
	public static String getDate() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String date = "[" + format.format(now) + "] ";
		return date;
	}
	
	public static String getQuote(Player player) {
		return PlayerChatListener.lastMessage.containsKey(player) ? PlayerChatListener.lastMessage.get(player) : "";
	}
	
}
