package fr.dinoattitude.anthopia.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.dinoattitude.anthopia.utils.Messages;

public class CacCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		/*TODO: Trouver une fa�on pas trop laggy pour faire un classement des joueurs en fontion de leur argent
		Oui c'est compliqu� � moins de traverser tous les fichiers joueurs � chaque appel de la commande je vois pas.
		Peut �tre une HashMap quand la cmd est appell� mais �a bouffe de la ram pour rien apr�s.
		ALED.*/
		//EconomyData.getCac40(player);
		sender.sendMessage(Messages.PLUGIN_NAME.toString() + Messages.DISABLE_CMD);
		
		return false;
	}
	
}