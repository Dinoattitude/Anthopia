package fr.dinoattitude.anthopia.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class DiscordCommand implements CommandExecutor{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;
		
		TextComponent message = new TextComponent("§5Discord : http://discord.gg/CDPXnxq");
		message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://discord.gg/CDPXnxq"));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5Rejoint le discord de Anthopia !").create()));
		player.spigot().sendMessage(message);
		
		return false;
	}

}
