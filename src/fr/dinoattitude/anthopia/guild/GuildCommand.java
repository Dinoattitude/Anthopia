package fr.dinoattitude.anthopia.guild;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.dinoattitude.anthopia.bourse.economy_api.EconomyData;
import fr.dinoattitude.anthopia.guild.guild_api.GuildInfo;
import fr.dinoattitude.anthopia.utils.PlayerData;

public class GuildCommand implements CommandExecutor{

	private HashMap<String, Player> accepted = new HashMap<String, Player>();
	private int guildRank = 0;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		GuildInfo guildInfo = new GuildInfo();
		UUID playerUuid = Bukkit.getPlayer(sender.getName()).getUniqueId();
		String guildName= "";
		PlayerData playerData = new PlayerData(playerUuid);
		guildName = playerData.getGuild();
		if(guildName == null) {
			guildName = "";
		}
		
		try {
			guildRank = playerData.getGuildRank();
		} catch (Exception e) {
			System.out.println("R�cup�ration du rank impossible");
		}
		

		
		if(args.length >= 1) {
			switch(args[0]) {
				case "deposit":{
					if(args.length == 2) {
						if(!guildName.isEmpty()) {
							double amount = Double.valueOf(args[1]);
							double guildCb = guildInfo.getMoney(guildName);
							if(guildCb < 100000 && guildCb + amount < 100000) {
								if(EconomyData.getBalance(playerUuid) > amount){
									guildInfo.setMoney(guildName, guildCb + amount);
									EconomyData.setBalance(playerUuid, EconomyData.getBalance(playerUuid) - amount);
									EconomyData.removeMoney(playerUuid, amount);
									guildInfo.setLog(guildName, playerUuid, "+ " + amount + " dans le coffre de guilde");
									sender.sendMessage("�eVous avez d�pos� �6" + amount + " � dans le coffre fort de votre guilde");
								}
								else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'avez pas assez d'argent pour effectuer cette action");
							}
							else sender.sendMessage("�cVous ne pouvez pas d�poser plus de 100 000� dans votre coffre fort de guilde");
						}
						else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'apartenez � aucune guilde");
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild deposit <amount>");
					break;
				}
				case "withdraw":{
					if(args.length == 2) {
						if(!guildName.isEmpty()) {
							double amount = Double.valueOf(args[1]);
							double guildCb = guildInfo.getMoney(guildName);
							if(guildCb >= 0) {
								if(isComptable() || isFondateur()) {
									guildInfo.setMoney(guildName, guildCb - amount);
									EconomyData.setBalance(playerUuid, EconomyData.getBalance(playerUuid) + amount);
									EconomyData.addMoney(playerUuid, amount);
									guildInfo.setLog(guildName, playerUuid, "- " + amount + " dans le coffre de guilde");
									sender.sendMessage("�eVous avez retir� �6" + amount + " � du coffre fort de votre guilde");
								}
								else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous ne pouvez pas retirer d'argent car vous n'�tes pas comptable");
							}
							else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous ne pouvez pas retirer une somme sup�rieure � celle contenue dans votre coffre de guilde");
						}
						else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'apartenez � aucune guilde");
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild withdraw <amount>");
					break;
				}
				case "list":{
					if(args.length == 1) {
						Map<String,String> cac = guildInfo.getGuildsList();
						int i = 1;
						sender.sendMessage("�e============[ �6LISTE DES GUILDES�e ]=============");
						for(Map.Entry<String,String> map : cac.entrySet()) {
							sender.sendMessage("�6" + (i) + " - �3" + map.getKey() + " �e : "+ map.getValue() + " �");
							i++;
						}
						sender.sendMessage("�e===========================================");
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild list");
					break;
				}
				case "member":{
					if(args.length == 3) {
						String argsGuildName = args[2];
						List<String> members = guildInfo.getGuildMembers(argsGuildName);
						sender.sendMessage("�e[ Liste des membres de la guilde �6" + argsGuildName + " �e]");
						for (int i = 0; i < members.size(); i++) {
							sender.sendMessage("�3 - " + Bukkit.getOfflinePlayer(UUID.fromString(members.get(i))).getName()); 
						}
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild member list <guild>");
					break;
				}
				case "info":{
					if(args.length == 2) {
						String targetGuildName = args[1];
						String info = guildInfo.getInfo(targetGuildName);
						if(!info.isEmpty()) {
							sender.sendMessage("�eInformation concernant la guilde �6" + targetGuildName + " �e:");
							sender.sendMessage(info);
						}
						else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCette guilde n'existe pas ou n'a pas d'information associ�!");	
					}
					else if(args.length == 4) {
						if(args[1].equalsIgnoreCase("modify")) {
							if(isFondateur()) {
								String info = "";
								
								for (int i = 2; i < args.length; i++) {
									String arg = args[i] + " ";
									info += arg;
								}
								
								guildInfo.setInfo(guildName, info);
								guildInfo.setLog(guildName, playerUuid, "Informations de guildes modifi�es");
								sender.sendMessage("�aInformations modifi�es avec succ�s");
							}
							else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'avez pas les droits n�cessaires pour effectuer cette action !");
						}
						else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild info modify <info>");
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild info <guild> ou /guild info modify <info>");
					break;
				}
				case "create":{
					if(args.length == 2) {
						String createdGuildName = String.valueOf(args[1]);
						if(guildName.isEmpty()) {
							boolean finder = false;
							try {
								for(String guildNames : guildInfo.getGuildsList().keySet()) { //On Check si une guilde � d�j� ce nom
									if(createdGuildName.equalsIgnoreCase(guildNames)) finder = true;
								}
							} catch (Exception e) {
								finder = false;
							}

							if(!finder) {
								if(!createdGuildName.isEmpty()) { 
									guildInfo.setGuildData(createdGuildName, playerUuid);
									guildInfo.setLog(createdGuildName, playerUuid, "Cr�ation de la guilde ! Yay !");
									sender.sendMessage("�aVous avez cr�� la guilde �b" + createdGuildName + " �a!");
								}
								else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild create <nom>");
							}
							else sender.sendMessage("�8[�l�cAnthopia�r�8] �cUne guilde porte d�j� ce nom");
						}
						else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous ne pouvez pas cr�er de guilde si vous faite partie d'une guilde");
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild create <nom>");
					break;
				}
				case "join":{
					if(args.length == 2) {
						if(sender instanceof Player) {
							Player player = (Player) sender;
							String targetGuild = String.valueOf(args[1]);
							if(guildName.isEmpty()) {
								getAcceptedPlayer().clear();
								getAcceptedPlayer().put(targetGuild, player);
								for(Player players : Bukkit.getOnlinePlayers()) {
									PlayerData playersData = new PlayerData(players.getUniqueId());
									if(!playersData.getGuild().equalsIgnoreCase("none")) {
										if(playersData.getGuild().equalsIgnoreCase(targetGuild)) {
											players.sendMessage("�eLe joueur " + player.getName() + " veux rejoindre votre guilde. /guild accept pour accepter");
										}
									}
								}
							}
							else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous ne pouvez pas rejoindre de guilde si vous faite partie d'une guilde");
						}
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild join <guild>");
					break;
				}
				case "leave":{ 
					if(args.length == 1) {
						if(!guildName.isEmpty()) {
							if(!isFondateur()) { //Si le mec est fondateur passer les droits � un autre. Sinon annul� si ya plusieurs personnes
								guildInfo.removeMember(guildName, playerUuid);
								sender.sendMessage("�aVous venez de quitter votre guilde");
								guildInfo.setLog(guildName, playerUuid, "A quitt� la guilde");
							}
							else {
								List<String> members = guildInfo.getGuildMembers(guildName);
								if(members.size()==1) {
									guildInfo.removeMember(guildName, playerUuid);
									sender.sendMessage("�aVous venez de quitter votre guilde");
									guildInfo.setLog(guildName, playerUuid, "A quitt� la guilde");
									guildInfo.removeGuildData(guildName);
									guildInfo.deleteLogFile(guildName);
									sender.sendMessage("�8[�l�cAnthopia�r�8] �cLa guilde � �t� dissoute");
								}
								else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous ne pouvez pas quitter une guilde o� vous n'�tes pas seul en �tant fondateur");
							}
						}
						else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'apartenez � aucune guilde");
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild leave");
					break;
				}
				case "pay":{
					if(args.length == 3) { 
						if(!guildName.isEmpty()) {
							double amount = Double.valueOf(args[1]);
							String targetGuild = String.valueOf(args[2]);
							double targetGuildCb = guildInfo.getMoney(targetGuild);
							double guildCb = guildInfo.getMoney(guildName);

							if(amount <= guildCb) { //On regarde si la guilde � l'argent ness�ssaire 
								if(targetGuildCb < 100000 && targetGuildCb + amount < 100000) { //On regarde si la guilde � la place de recevoir le montant
									if(isComptable() || isFondateur()) { //On v�rifie les permissions li�s au ranks
										guildInfo.setMoney(targetGuild, targetGuildCb + amount);
										guildInfo.setMoney(guildName, guildCb - amount);
										guildInfo.setLog(guildName, playerUuid, "Paiement de " + amount + " � la guilde " + targetGuild);
										guildInfo.setLog(targetGuild, playerUuid, "R�ception de " + amount + " de la part de la guilde " + guildName);
										
										for(Player players : Bukkit.getOnlinePlayers()) {
											PlayerData playersData = new PlayerData(players.getUniqueId());
											if(playersData.getGuild().equalsIgnoreCase(targetGuild))
												players.sendMessage("�eVotre guilde � re�u �6" + amount + " � �ede la part de la guilde �6" + guildName);
											else if(playersData.getGuild().equalsIgnoreCase(guildName))
												players.sendMessage("�eVotre guilde viens d'envoyer �6" + amount + " � �e� la guilde �6" + targetGuild);
										}
									}
									else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'avez pas les permissions pour utiliser le coffre de guilde");
								}
								else sender.sendMessage("�8[�l�cAnthopia�r�8] �cLe coffre de la guilde d�sign�e ne peut pas accueillir votre montant");
							}
							else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'avez pas l'argent n�cessaire � la transaction");
						}
						else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'apartenez � aucune guilde");
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild pay <amount> <guild>");
					break;
				}
				case "accept":{
					if(args.length == 1) {
						if(!guildName.isEmpty()) {
							Player player = (Player) sender;
							Player target = null;
							String targetGuildname = "";
							for(Map.Entry<String,Player> map : getAcceptedPlayer().entrySet()) {
								target = map.getValue();
								targetGuildname = map.getKey();
							}

							if(!getAcceptedPlayer().isEmpty()) {
								if(!player.getName().equalsIgnoreCase(target.getName())) {
									if(guildName.equalsIgnoreCase(targetGuildname)) {
										guildInfo.setMember(targetGuildname, target.getUniqueId(), "0");
										getAcceptedPlayer().clear();
										for(Player players : Bukkit.getOnlinePlayers()) {
											PlayerData playersData = new PlayerData(players.getUniqueId());
											if(playersData.getGuild().equalsIgnoreCase(targetGuildname)) { //TODO: Erreur ici qui ne fait pas grand chose juste montre l'internal error dans le chat
												players.sendMessage("�9" + target.getName() + " �eviens de rejoindre la guilde !");
											}
											target.sendMessage("�aVous venez de rejoindre la guilde " + targetGuildname);	
										}
										guildInfo.setLog(guildName, playerUuid, "A rejoint la guilde ! Bienvenue !");
									}
								}
							}
							else sender.sendMessage("�8[�l�cAnthopia�r�8] �cPersonne n'a demand� � faire parti de votre guilde");
						}
						else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'apartenez � aucune guilde");
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild accept");
					break;
				}
				case "rank":{
					if(args.length == 4) {
						if(!guildName.isEmpty()) {
							if(args[2].equalsIgnoreCase("set")) {
								Player target = Bukkit.getPlayer(args[1]);
								String writedRank = args[3];
								PlayerData targetData = new PlayerData(target.getUniqueId());

								if(targetData.getGuild().equalsIgnoreCase(guildName)) {
									if(writedRank.equalsIgnoreCase("membre") && isFondateur()) {
										guildInfo.setMember(guildName, target.getUniqueId(), "0");
										targetData.setGuildRank(0);
										guildInfo.setLog(guildName, target.getUniqueId(), "Nouveau rank -> Membre");
									}
									else if(writedRank.equalsIgnoreCase("comptable") && isFondateur()) {
										guildInfo.setMember(guildName, target.getUniqueId(), "1");
										targetData.setGuildRank(1);
										guildInfo.setLog(guildName, target.getUniqueId(), "Nouveau rank -> Comptable");
									}
									else if(writedRank.equalsIgnoreCase("admin") && isFondateur()) {
										guildInfo.setMember(guildName, target.getUniqueId(), "2");
										targetData.setGuildRank(2);
										guildInfo.setLog(guildName, target.getUniqueId(), "Nouveau rank -> Admin");
									}
									else if(writedRank.equalsIgnoreCase("fondateur") && isFondateur()) {
										guildInfo.setMember(guildName, target.getUniqueId(), "3");
										targetData.setGuildRank(3);
										guildInfo.setMember(guildName, playerUuid, "2"); //On promote le nouveau fondateur et met l'ancien en admin
										playerData.setGuildRank(2);
										guildInfo.setLog(guildName, target.getUniqueId(), "Nouveau rank -> Fondateur");
										guildInfo.setLog(guildName, playerUuid, "Nouveau rank -> Admin");
									}
									else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'avez pas les droits n�cessaires pour effectuer cette action !");
								}
								else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCette personne n'est pas dans votre guilde !");
							}
							else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild rank <player> set <rank>");
						}
						else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'apartenez � aucune guilde");
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild rank <player> set <rank>");
					break;
				}
				case "ranks":{
					sender.sendMessage("�6===========================================================");
					sender.sendMessage("�9- Fondateur : �3Fondateur de la guilde");
					sender.sendMessage("�9- Admin : �3S'occupe des probl�mes internes");
					sender.sendMessage("�9- Comptable : �3G�re l'argent de la guilde");
					sender.sendMessage("�9- Membres : �3Membres de la guilde");
					sender.sendMessage("�6===========================================================");
					break;
				}
				case "kick":{
					if(args.length == 2) {
						if(!guildName.isEmpty()) {
							Player target = Bukkit.getPlayer(args[1]);
							if(target != null) {
								PlayerData targetData = new PlayerData(target.getUniqueId());
								if(targetData.getGuild().equalsIgnoreCase(guildName)) {
									if(isAdmin() || isFondateur()) {
										int targetRank = targetData.getGuildRank();
										if(!(targetRank == 2|| targetRank == 3)) {
											guildInfo.removeMember(guildName, target.getUniqueId());
											if(target.isOnline()) 
												target.sendMessage("�8[�l�cAnthopia�r�8] �cVous venez de vous faire expulser de la guilde");
											sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous avez expuls� " + args[1] + "de la guilde");
											guildInfo.setLog(guildName, target.getUniqueId(), "A �t� expuls�");
										}
										else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous ne pouvez pas expulser de la guilde quelqu'un du m�me rank que vous ou supp�rieur !");
									}
									else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'avez pas les droits n�cessaires pour effectuer cette action !");
								}
								else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCette personne n'est pas dans votre guilde !");
							}
							else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCette personne n'existe pas !");
						}
						else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'apartenez � aucune guilde");
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild kick <player>");
					break;
				}
				case "logs":{
					if(isFondateur() || isAdmin()) {
						List<String> logs = guildInfo.getLog(guildName);
						sender.sendMessage("�e============[ �6Logs�e ]=============");
						for (int i = 0; i < logs.size(); i++) {
							sender.sendMessage(logs.get(i));
						}
						sender.sendMessage("�e===============================");
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'avez pas les droits n�cessaires pour effectuer cette action !"); 
					break;
				}
				case "home":{ 
					if(!guildName.isEmpty()) {
						if(args.length == 4) {
							if(args[1].equalsIgnoreCase("set") && args[2].equalsIgnoreCase("tax")) { //Si on veux regler le montant de la taxe
								if(isFondateur()) {
									Integer amount = Integer.valueOf(args[3]);
									if(amount != null) {
										guildInfo.setTaxRate(guildName, amount);
										guildInfo.setLog(guildName, playerUuid, "A activ� la taxe de t�l�portation");
										sender.sendMessage("�8[�l�cAnthopia�r�8] �aVous avez mis la taxe de t�l�portation � " + amount + " �");
									}
									else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild home set tax <amount>");
								}
								else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'avez pas les droits n�cessaires pour effectuer cette action !");
							}
						}
						else if(args.length == 3) {
							if(args[1].equalsIgnoreCase("tax")) { //Si on veux activer ou non la taxe
								if(isFondateur()) {
									String switchedTax = args[2];
									if(switchedTax.equalsIgnoreCase("enable") || switchedTax.equalsIgnoreCase("disable")) {
										guildInfo.setTax(guildName, switchedTax);
										guildInfo.setLog(guildName, playerUuid, "A chang� le taux de la taxe de t�l�portation");
										sender.sendMessage("�8[�l�cAnthopia�r�8] �aVous avez chang� chang� la taxe en " + switchedTax);
									}
									else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild home tax enabled/disabled");
								}
								else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'avez pas les droits n�cessaires pour effectuer cette action !");
							}
						}
						else {
							if(guildInfo.getTax(guildName).equalsIgnoreCase("enable")) {
								int amount = guildInfo.getTaxRate(guildName);
								if(amount < EconomyData.getBalance(playerUuid)) { //Check si le joueur � assez d'argent, sinon on tp pas
									if(amount + guildInfo.getMoney(guildName) < 100000) { //Check si la guilde peux prendre l'argent, sinon on tp pas
										EconomyData.setBalance(playerUuid, EconomyData.getBalance(playerUuid) - amount);
										EconomyData.removeMoney(playerUuid, amount);
										Location home = guildInfo.getHomeLocation(guildName);
										if(home != null) { //Check si le home � �t� d�fini
											Player player = (Player) sender;
											player.teleport(home);
											guildInfo.setMoney(guildName, guildInfo.getMoney(guildName) + amount);
											sender.sendMessage("�aVous vous �tes t�l�port� au home de la guilde");
											sender.sendMessage("�aVous avez donn� " + amount + " � de taxe � la guilde");
											break;
										}
										else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVotre home de guild n'a pas �t� d�fini");
									}
									else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVotre coffre de guilde est plein. T�l�portation annul�e.");
								}
								else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'avez pas assez d'argent pour effectuer cette action");
							}
							else {
								Player player = (Player) sender;
								Location home = guildInfo.getHomeLocation(guildName);
								player.teleport(home);
								sender.sendMessage("�aVous vous �tes t�l�port� au home de la guilde");
								break;
							}
						}
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'apartenez � aucune guilde");
					break;
				}
				case "sethome":{
					if(args.length == 1) {
						if(!guildName.isEmpty()) {
							if(isFondateur()) {
								if(guildInfo.getMoney(guildName) > 1000) {
									Player player = (Player) sender;
									guildInfo.setMoney(guildName, guildInfo.getMoney(guildName) - 1000);
									guildInfo.setHomeLocation(guildName, player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
									guildInfo.setLog(guildName, playerUuid, "A d�fini un nouveau home pour la guilde");
									sender.sendMessage("�aVous avez d�fini votre home de guilde");
								}
								else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'avez pas assez d'argent pour effectuer cette action : 1000� sont requis");
							}
							else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'avez pas les droits n�cessaires pour effectuer cette action !");
						}
						else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'apartenez � aucune guilde");
					}
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cCommande Incompl�te : /guild sethome");
					break;
				}
				default:{
					if(!guildName.isEmpty()) showGuildInv(guildName,guildInfo,sender);
					else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'apartenez � aucune guilde");
					break;
				}
			}
		}
		else {
			if(!guildName.isEmpty()) showGuildInv(guildName,guildInfo,sender);
			else sender.sendMessage("�8[�l�cAnthopia�r�8] �cVous n'apartenez � aucune guilde");
		}
	
	return true;
	}
	
	private void showGuildInv(String guildName, GuildInfo guildInfo, CommandSender sender) {
		//Ancien syst�me de reset d'exp pour l'exp suppl�mentaire au passage de niveau.
		//Remis au go�t du jour, sauce frustration, sur son plateau de futurs bugs. 
		//Je me fait pas d'illusions c'est de la merde mais laissez moi r�ver.
		double exp = guildInfo.getExp(guildName);
		if(exp > guildInfo.NextGuildLevel(guildName)) {
			double expSup = exp - guildInfo.NextGuildLevel(guildName);
			
			int lvl = guildInfo.getLvl(guildName);
			guildInfo.setLvl(guildName, lvl+1);
			guildInfo.setExp(guildName, expSup);
		}
		
		Player player = (Player) sender;
		Inventory guildinventory = guildInfo.getGuildInventory(guildName);
		player.openInventory(guildinventory);
	}
	
	private boolean isFondateur() {
		return guildRank == 3 ? true : false;
	}
	
	private boolean isAdmin() {
		return guildRank == 2 ? true : false;
	}
	
	private boolean isComptable() {
		return guildRank == 1 ? true : false;
	}

	public HashMap<String,Player> getAcceptedPlayer(){
		return accepted;
	}
}
