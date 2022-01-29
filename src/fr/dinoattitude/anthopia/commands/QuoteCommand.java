package fr.dinoattitude.anthopia.commands;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dinoattitude.anthopia.listeners.PlayerChatListener;
import fr.dinoattitude.anthopia.utils.QuoteData;


public class QuoteCommand implements CommandExecutor{
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		QuoteData quoteData = new QuoteData();
		
		Player player = (Player) sender;
		
		if(args.length == 1) {
			String playerName = args[0];
			String date = getDate();
			
			Player playerCible = Bukkit.getPlayer(playerName);
			String quote = getQuote(playerCible);
			
			/* Quote checks */
			boolean verifQuote = quoteData.isNewQuote(quote);
			if(verifQuote == true) {
				player.sendMessage("§8[§l§cAnthopia§r§8] §cVous ne pouvez pas /quote un message déjà /quote.");
				return true;
			}
			
			if(player == playerCible) {
				player.sendMessage("§8[§l§cAnthopia§r§8] §cVous ne pouvez pas vous /quote vous même !");
				return true;
			}
			
			if(playerCible == null) {
				player.sendMessage("§8[§l§cAnthopia§r§8] §cLe joueur " + playerName + " est introuvable");
				return true;
			}

			quoteData.setQuote(playerName, quote, date);
			Bukkit.broadcastMessage("§b" + playerName + " a été quote par §9" + player.getName());
		}
		else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("de")) {
				String playerName = args[1];
				Bukkit.broadcastMessage("§9" + player.getName() + " demande une quote de " + playerName + " :");
				Bukkit.broadcastMessage(quoteData.getRandomQuoteByPlayer(Bukkit.getOfflinePlayer(playerName).getUniqueId()));
			}
			else {
				player.sendMessage("§8[§l§cAnthopia§r§8] §cLe joueur ne s'est pas fait /quote ou la commande est mauvaise");
			}
		}
		else {
			Bukkit.broadcastMessage("§9" + player.getName() + " demande une quote :");
			Bukkit.broadcastMessage(quoteData.getRandomQuote());
			
		}
		
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
