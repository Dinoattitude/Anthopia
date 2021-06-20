package fr.dinoattitude.anthopia.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.dinoattitude.anthopia.utils.Messages;

public class CacCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		/*TODO: Trouver une façon pas trop laggy pour faire un classement des joueurs en fontion de leur argent
		Oui c'est compliqué à moins de traverser tous les fichiers joueurs à chaque appel de la commande je vois pas.
		Peut être une HashMap quand la cmd est appellé mais ça bouffe de la ram pour rien après.
		ALED.*/
		//EconomyData.getCac40(player);
		sender.sendMessage(Messages.PLUGIN_NAME.toString() + Messages.DISABLE_CMD);
		
		return false;
	}
	
}