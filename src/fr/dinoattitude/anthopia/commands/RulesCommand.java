package fr.dinoattitude.anthopia.commands;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class RulesCommand implements CommandExecutor{

	@Override
	public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		
		String l = "\n";
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);

		BookMeta bookmeta = (BookMeta) book.getItemMeta();
		bookmeta.setAuthor("Anthopia");
		bookmeta.setTitle(ChatColor.DARK_PURPLE + "Bouquin Anthopien Sacr�");
		
		ArrayList<String> pages = new ArrayList<String>();
		
		pages.add(0, ChatColor.BLACK + "Bien le bonjour jeune Joueur/Joueuse ! Tu te trouves sur le serveur Anthopia." + l + "On te souhaite une belle aventure parmi nous !" + l + " Ce livre t'expliquera en d�tail les informations � connaitre sur le serveur.");
		pages.add(1,"�n�c---SOMMAIRE---�r" + l + "Page 4 : Le staff" + l + "Page 5 : R�gles" + l + "Page 9 : Information et Commandes" + l + "Page 15 : Les guildes");
		pages.add(2, "�n�c---STAFF---�r" + l + "Le Staff c'est quatres personnes :" + l + "�lectapower�r:" + l + "Dev / Admin" + l + "�lFanfanfan94�r:" + l + "Dev / Admin" + l + "�lHayto_�r:" + l + "Administrateur" + l + "�lRaptorJesus74x�r:" + l + "Mod�rateur");
		pages.add(3, "�n�c---REGLES---�r" + l + "Pour ce qui est des r�gles, c'est assez convenu, pas de grief, pas de cheat, pas de usebug, sous quelques formes que ce soit. On est tr�s strict en ce qui concerne les banissements.");
		pages.add(4, "�n�c---REGLES---�r" + l + "�nEn g�n�ral si vous cheatez une fois on opte pour un ban def <3");
		pages.add(5, "Il y a 2 commandes que les modos et admin utilisent pour sanctionner, le �2/jail�r, qui vous mettra en prison pour une certaine dur�e, et qui vous permettra de r�fl�chir sur votre pitoyable vie pendant que les autres joueurs se moqueront de vous.");
		pages.add(6, "Ne vous inqui�tez pas, normalement le �2/jail�r n'est pas cens� �tre abusif <3" + l + l + "Il y a �galement le �2/mute�r, si vous d�rangez le chat � spammer, entre autres." + l + "Sans oublier le fameux /ban bien s�r :D");
		pages.add(7, "Afin de maintenir la bonne entente sur le serveur, un minimum de respect envers chaque joueur est demand� (et encore plus envers les personnes du Staff, qui sont des �tres sup�rieurs.)");
		pages.add(8, "�n�c-INFOS ET COMMANDES-�r" + l + "Lorsque vous spawnerez sur la map, vous aurez le grade visiteur." + l + "Une fois un minimum de temps pass� sur le serv et au moins une base de fortune de faite, un modo/admin vous passera citoyen.");
		pages.add(9, "Cela vous permettra d'utiliser des commandes comme le �2/claim�r ou le �2/home�r." + l + l + "Le �2/claim�r est une commande qui vous permet de s�curiser un chunk o� vous vous trouvez pour 120�");
		pages.add(10, "Dans ce claim, vous seul pourrez casser des blocks/ouvrir des coffres etc." + l + l + "Si vous le voulez vous pouvez autoriser un joueur � agir sur un de vos claim avec la commande �2/coloc <pseudo>�r");
		pages.add(11, "Si vous souhaitez l'ajouter sur tout vos claims, un �2/colocall <pseudo>�r s'impose." + l + l + "Vous pourrez �galement utiliser le �2/manage�r et �2/manageall�r, afin de r�gler les param�tres de votre claim.");
		pages.add(12, "Enfin, le �2/sethome�r vous permet comme son nom l'indique de d�finir un home, auquel vous pourrez vous t�l�porter � chaque fois que vous entrez la commande �2/home�r.");
		pages.add(13, "Un shop est disponible avec la commande �2/shop�r, o� vous pourrez acheter et vendre certains items." + l + l + "Pour les joueurs les plus fid�les, un grade �3VIP�r peut leur �tre accord�.");
		pages.add(14, "�n�c---GUILDES---�r" + l + "Le serveur poss�de un syst�me de guilde qui vous permet de vous rassembler entre joueurs, et qui comporte un syst�me de niveau, qui, au fur et � mesure que vous progresserez, vous permettra de gagner de l'argent.");
		pages.add(15, "Des Events se font parfois, les joueurs peuvent �videmment en proposer (Un portail est disponible au spawn pour acc�der � la zone Event).");
		pages.add(16, "Si vous avez des questions, vous pouvez les poser au Staff ou m�me aux joueurs." + l + l + "Bon jeu sur �cAnthopia�r !");
		bookmeta.setPages(pages);
		book.setItemMeta(bookmeta);
		
		player.playSound(player.getLocation(),Sound.ENTITY_ITEM_PICKUP,1f,1f);
		
		final Map<Integer, ItemStack> inv = player.getInventory().addItem(book);
		
		if(!inv.isEmpty()) {
			if(!player.getInventory().contains(book))
				player.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'avez plus de place pour recevoir le livre des r�gles");
			else
				player.sendMessage("�8[�l�cAnthopia�r�8] �cVous avez d�j� le livre des r�gles dans votre inventaire");
		}
		
		return false;
	}

}
