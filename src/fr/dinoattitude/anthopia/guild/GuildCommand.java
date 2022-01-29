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
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.dinoattitude.anthopia.Main;
import fr.dinoattitude.anthopia.bourse.economy_api.EconomyData;
import fr.dinoattitude.anthopia.guild.guild_api.GuildInfo;
import fr.dinoattitude.anthopia.utils.Messages;
import fr.dinoattitude.anthopia.utils.PlayerData;

/** Guild CommandExecutor 
 * @author Dinoattitude
 * @version 2.4.3
 */
public class GuildCommand implements CommandExecutor{
	
	private HashMap<String, UUID> accepted = new HashMap<String, UUID>();
	private int guildRank = 0;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof ConsoleCommandSender) {
			return true;
		}
		
		UUID playerUUID = Bukkit.getPlayer(sender.getName()).getUniqueId();
		PlayerData playerData = new PlayerData(playerUUID);
		GuildInfo guildInfo = new GuildInfo();
		
		String guildName = playerData.getGuild();
		guildRank = playerData.getGuildRank();
		int guildBankAccountCap = 100000;
		
		// +------------------------------------------------------+
		// |                     DEFAULT                          |
		// +------------------------------------------------------+
		
		if(args.length == 0) {
			
			if(!hasGuild(guildName)) {
				sender.sendMessage(Messages.NO_GUILD.toString());
				return true;
			}
			
			showGuildInv(guildName, guildInfo, sender);
			return true;
		}
		
		switch(args[0]) {
		
			// +------------------------------------------------------+
			// |                      DEPOSIT                         |
			// +------------------------------------------------------+
		
			case "deposit": {
				if(!hasGuild(guildName)) {
					sender.sendMessage(Messages.NO_GUILD.toString());
					return true;
				}
				
				if(args.length != 2) {
					sender.sendMessage(Messages.INCOMPLETE_COMMAND.toString() + "/guild deposit <amount>");
					return true;
				}
				
				double depositAmount = Double.valueOf(args[1]);
				double guildBankAccount = guildInfo.getMoney(guildName);
				
				if(!((guildBankAccount < guildBankAccountCap) && 
						((guildBankAccount + depositAmount) < guildBankAccountCap))) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous ne pouvez pas déposer plus de " + String.valueOf(guildBankAccountCap) + "€ dans votre coffre fort de guilde");
					return true; 
				}
				
				if(EconomyData.getBalance(playerUUID) < depositAmount) {
					sender.sendMessage(Messages.NOT_ENOUGH_MONEY.toString());
					return true;
				}
				
				guildInfo.setMoney(guildName, guildBankAccount + depositAmount);
				EconomyData.removeMoney(playerUUID, depositAmount);
				
				guildInfo.setLog(guildName, playerUUID, "+ " + depositAmount + " dans le coffre de guilde");
				sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§eVous avez déposé §6" + depositAmount + " € dans le coffre fort de votre guilde");
				break;
			}
			
			// +------------------------------------------------------+
			// |                     WITHDRAW                         |
			// +------------------------------------------------------+
			
			case "withdraw": {
				if(!hasGuild(guildName)) {
					sender.sendMessage(Messages.NO_GUILD.toString());
					return true;
				}
				
				if(!(isFondateur() || isComptable())) {
					sender.sendMessage(Messages.NO_PERMS.toString());
					return true;
				}
				
				if(args.length != 2) {
					sender.sendMessage(Messages.INCOMPLETE_COMMAND.toString() + "/guild withdraw <amount>");
					return true;
				}
				
				double depositAmount = Double.valueOf(args[1]);
				double guildBankAccount = guildInfo.getMoney(guildName);
				
				if((guildBankAccount - depositAmount) < 0 || guildBankAccount < depositAmount) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous ne pouvez pas retirer une somme supérieure à celle contenue dans votre coffre de guilde");
					return true;
				}
				
				guildInfo.setMoney(guildName, guildBankAccount - depositAmount);
				EconomyData.addMoney(playerUUID, depositAmount);
				
				guildInfo.setLog(guildName, playerUUID, "- " + depositAmount + " dans le coffre de guilde");
				sender.sendMessage("§eVous avez retiré §6" + depositAmount + " € du coffre fort de votre guilde");
				break;
			}
			
			// +------------------------------------------------------+
			// |                        PAY                           |
			// +------------------------------------------------------+
			
			case "pay": {
				
				if(!hasGuild(guildName)) {
					sender.sendMessage(Messages.NO_GUILD.toString());
					return true;
				}
				
				if(!(isFondateur() || isComptable())) {
					sender.sendMessage(Messages.NO_PERMS.toString());
					return true;
				}
				
				if(args.length != 3) {
					sender.sendMessage(Messages.INCOMPLETE_COMMAND.toString() + "/guild pay <amount> <guild>");
					return true;
				}
				
				double amount = Double.valueOf(args[1]);
				String targetGuild = String.valueOf(args[2]);
				double targetGuildBankAccount = guildInfo.getMoney(targetGuild);
				double guildBankAccount = guildInfo.getMoney(guildName);
				
				if(amount >= guildBankAccount) {
					sender.sendMessage(Messages.NOT_ENOUGH_MONEY.toString());
					return true;
				}
				
				if(!((targetGuildBankAccount < guildBankAccountCap) && 
						((targetGuildBankAccount + amount) < guildBankAccountCap))) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cLe coffre de la guilde désignée ne peut pas accueillir votre montant");
					return true;
				}
				
				guildInfo.setMoney(targetGuild, targetGuildBankAccount + amount);
				guildInfo.setMoney(guildName, guildBankAccount - amount);
				guildInfo.setLog(guildName, playerUUID, "Paiement de " + amount + " à la guilde " + targetGuild);
				guildInfo.setLog(targetGuild, playerUUID, "Réception de " + amount + " de la part de la guilde " + guildName);
				
				for(Player players : Bukkit.getOnlinePlayers()) {
					PlayerData playersData = new PlayerData(players.getUniqueId());
					if(playersData.getGuild().equalsIgnoreCase(targetGuild)) {
						players.sendMessage("§eVotre guilde à reçu §6" + amount + " € §ede la part de la guilde §6" + guildName);
					}	
					
					if(playersData.getGuild().equalsIgnoreCase(guildName)) {
						players.sendMessage("§eVotre guilde viens d'envoyer §6" + amount + " € §eà la guilde §6" + targetGuild);
					}
						
				}
				
				break;
			}
			
			// +------------------------------------------------------+
			// |                        LIST                          |
			// +------------------------------------------------------+
			
			case "list": {
				
				if(args.length != 1) {
					sender.sendMessage(Messages.INCOMPLETE_COMMAND.toString() + "/guild list");
					return true;
				}

				Map<String,String> guildMap = guildInfo.getGuildsList();
				
				if(guildMap.isEmpty()) {
					sender.sendMessage(Messages.ERR_NOT_FOUND.toString());
					Main.setWarningLog(this.getClass().getSimpleName() + " : guildInfo.getGuildsList() in usecase 'list' returned null");
					return true;
				}
				
				int i = 1;
				sender.sendMessage("§e============[ §6LISTE DES GUILDES§e ]=============");
				for(Map.Entry<String,String> map : guildMap.entrySet()) {
					sender.sendMessage("§6" + (i) + " - §3" + map.getKey() + " §e : "+ map.getValue() + " €");
					i++;
				}
				sender.sendMessage("§e===========================================");
				
				break;
			}
			
			// +------------------------------------------------------+
			// |                       MEMBER                         |
			// +------------------------------------------------------+
			
			case "member": {
				
				if(args.length != 3) {
					sender.sendMessage(Messages.INCOMPLETE_COMMAND.toString() + "/guild member list <guild>");
					return true;
				}
				
				String specifiedGuildName = args[2];
				
				if(!guildInfo.isGuildExist(specifiedGuildName)) {
					sender.sendMessage(Messages.GUILD_NOT_FOUND.toString());
					return true;
				}
				
				List<String> memberList = guildInfo.getGuildMembers(specifiedGuildName);
				
				if(memberList.isEmpty()) {
					sender.sendMessage(Messages.ERR_NOT_FOUND.toString());
					Main.setWarningLog(this.getClass().getSimpleName() + " : guildInfo.getGuildMembers in usecase 'member' returned null");
					return true;
				}
				
				sender.sendMessage("§e[ Liste des membres de la guilde §6" + specifiedGuildName + " §e]");
				for (int i = 0; i < memberList.size(); i++) {
					sender.sendMessage("§3 - " + Bukkit.getOfflinePlayer(UUID.fromString(memberList.get(i))).getName()); 
				}
				
				break;
			}
			
			// +------------------------------------------------------+
			// |                        INFO                          |
			// +------------------------------------------------------+
			
			case "info": {
				
				if(args.length != 2 || args.length != 3) {
					sender.sendMessage(Messages.INCOMPLETE_COMMAND.toString() + "/guild info <guild> ou /guild info modify <info>");
					return true;
				}
				
				if(args[1].equalsIgnoreCase("modify")) {
					
					if(!isFondateur()) {
						sender.sendMessage(Messages.NO_PERMS.toString());
						return true;
					}
					
					String info = "";
					
					for (int i = 2; i < args.length; i++) {
						String arg = args[i] + " ";
						info += arg;
					}
					
					guildInfo.setInfo(guildName, info);
					guildInfo.setLog(guildName, playerUUID, "Informations de guildes modifiées");
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§aInformations modifiées avec succès.");
					
					return true;
				}
				
				String targetGuildName = args[1];
				String info = guildInfo.getInfo(targetGuildName);
				
				if(info.isEmpty()) {
					sender.sendMessage(Messages.GUILD_NOT_FOUND.toString());
				}
				
				sender.sendMessage("§eInformation concernant la guilde §6" + targetGuildName + " §e:");
				sender.sendMessage(info);
				break;
			}
			
			// +------------------------------------------------------+
			// |                       CREATE                         |
			// +------------------------------------------------------+
			
			case "create": {
				
				if(hasGuild(guildName)) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous ne pouvez pas créer de guilde si vous êtes membre d'une autre guilde");
					return true;
				}
				
				if(args.length != 2) {
					sender.sendMessage(Messages.INCOMPLETE_COMMAND.toString() + "/guild create <nom>");
					return true;
				}
				
				String createdGuildName = String.valueOf(args[1]);
				
				/* Code legacy */
				boolean finder = false;
				try {
					for(String guildNames : guildInfo.getGuildsList().keySet()) { //On Check si une guilde à déjà ce nom
						if(createdGuildName.equalsIgnoreCase(guildNames)) finder = true;
					}
				} catch (Exception e) {
					finder = false;
				}
				/* ========== */
				
				if(finder) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cUne guilde porte déjà le nom passé en paramètre.");
				}
				
				guildInfo.setGuildData(createdGuildName, playerUUID);
				guildInfo.setLog(createdGuildName, playerUUID, "Création de la guilde ! Yay !");
				sender.sendMessage("§aVous avez créé la guilde §b" + createdGuildName + " §a!");
				break;
			}
			
			// +------------------------------------------------------+
			// |                        JOIN                          |
			// +------------------------------------------------------+
			
			case "join": {
				
				if(hasGuild(guildName)) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous ne pouvez pas joindre une guilde si vous êtes membre d'une autre guilde");
					return true;
				}
				
				if(args.length != 2) {
					sender.sendMessage(Messages.INCOMPLETE_COMMAND.toString() + "/guild join <guild>");
					return true;
				}
				
				String targetGuild = String.valueOf(args[1]);
				
				getAcceptedPlayer().clear();
				getAcceptedPlayer().put(targetGuild, playerUUID);
				
				for(Player players : Bukkit.getOnlinePlayers()) {
					PlayerData playersData = new PlayerData(players.getUniqueId());
					if(playersData.getGuild().equalsIgnoreCase(targetGuild)) {
						players.sendMessage("§eLe joueur " + Bukkit.getPlayer(playerUUID).getName() + " veux rejoindre votre guilde ! /guild accept pour accepter.");
					}
				}
				
				break;
			}
			
			// +------------------------------------------------------+
			// |                       LEAVE                          |
			// +------------------------------------------------------+
			
			case "leave": {
				
				if(!hasGuild(guildName)) {
					sender.sendMessage(Messages.NO_GUILD.toString());
					return true;
				}
				
				if(args.length != 1) {
					sender.sendMessage(Messages.INCOMPLETE_COMMAND.toString() + "/guild leave");
					return true;
				}
				
				if(isFondateur()) {
					List<String> members = guildInfo.getGuildMembers(guildName);
					
					if(members.size() != 1) {
						sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous ne pouvez pas quitter une guilde où vous n'êtes pas seul en étant fondateur.");
					}
					
					guildInfo.removeMember(guildName, playerUUID);
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§aVous venez de quitter votre guilde");
					guildInfo.setLog(guildName, playerUUID, "A quitté la guilde");
					guildInfo.removeGuildData(guildName);
					guildInfo.deleteLogFile(guildName);
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cLa guilde à été dissoute");
					return true;
				}
				
				guildInfo.removeMember(guildName, playerUUID);
				sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§aVous venez de quitter votre guilde");
				guildInfo.setLog(guildName, playerUUID, "A quitté la guilde");

				break;
			}
			
			// +------------------------------------------------------+
			// |                      ACCEPT                          |
			// +------------------------------------------------------+
			
			case "accept": {
				
				if(!hasGuild(guildName)) {
					sender.sendMessage(Messages.NO_GUILD.toString());
					return true;
				}
				
				if(args.length != 1) {
					sender.sendMessage(Messages.INCOMPLETE_COMMAND.toString() + "/guild leave");
					return true;
				}
				
				UUID targetUUID = null;
				String targetGuildname = "";
				
				for(Map.Entry<String,UUID> map : getAcceptedPlayer().entrySet()) {
					targetUUID = map.getValue();
					targetGuildname = map.getKey();
				}
				
				if(getAcceptedPlayer().isEmpty()) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cPersonne n'a demandé à rejoindre votre guilde.");
					return true;
				}
				
				if(!guildName.equalsIgnoreCase(targetGuildname)) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous ne pouvez pas accepter ce joueur.");
					return true;
				}
				
				guildInfo.setMember(targetGuildname, targetUUID, "0");
				getAcceptedPlayer().clear();
				
				for(Player players : Bukkit.getOnlinePlayers()) {
					PlayerData playersData = new PlayerData(players.getUniqueId());
					if(playersData.getGuild().equalsIgnoreCase(targetGuildname)) { 
						players.sendMessage("§9" + Bukkit.getPlayer(targetUUID).getName() + " §eviens de rejoindre la guilde !");
					}
					Bukkit.getPlayer(targetUUID).sendMessage("§aVous venez de rejoindre la guilde " + targetGuildname);	
				}
				guildInfo.setLog(guildName, targetUUID, "A rejoint la guilde ! Bienvenue !");
				
				break;
			}
			
			// +------------------------------------------------------+
			// |                       RANK                           |
			// +------------------------------------------------------+
			
			case "rank": {
				
				if(!hasGuild(guildName)) {
					sender.sendMessage(Messages.NO_GUILD.toString());
					return true;
				}
				
				if(!isFondateur()) {
					sender.sendMessage(Messages.NO_PERMS.toString());
					return true;
				}
				
				if(!(args.length == 4 || args[2].equalsIgnoreCase("set"))) {
					sender.sendMessage(Messages.INCOMPLETE_COMMAND.toString() + "/guild rank <player> set <rank>");
					return true;
				}
				
				if(!isPlayerExist(args[1])) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cLe joueur passé en paramètre n'existe pas.");
					return true;
				}
				
				UUID targetUUID = Bukkit.getPlayer(args[1]).getUniqueId();
				String writedRank = args[3];
				PlayerData targetData = new PlayerData(targetUUID);
				
				if(!(targetData.getGuild().equalsIgnoreCase(guildName))) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cCette personne n'est pas dans votre guilde !");
					return true;
				}
				
				if(playerUUID.equals(targetUUID)) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous ne pouvez pas changer votre rang en étant fondateur !");
					return true;
				}
				
				switch(writedRank) {
					case "membre": {
						guildInfo.setMember(guildName, targetUUID, "0");
						targetData.setGuildRank(0);
						guildInfo.setLog(guildName, targetUUID, "Nouveau rank -> Membre");
						break;
					}
					case "comptable": {
						guildInfo.setMember(guildName, targetUUID, "1");
						targetData.setGuildRank(1);
						guildInfo.setLog(guildName, targetUUID, "Nouveau rank -> Comptable");
						break;
					}
					case "admin": {
						guildInfo.setMember(guildName, targetUUID, "2");
						targetData.setGuildRank(2);
						guildInfo.setLog(guildName, targetUUID, "Nouveau rank -> Admin");
						break;
					}
					case "fondateur": {
						guildInfo.setMember(guildName, targetUUID, "3");
						targetData.setGuildRank(3);
						guildInfo.setMember(guildName, playerUUID, "2"); //On promote le nouveau fondateur et met l'ancien en admin
						playerData.setGuildRank(2);
						guildInfo.setLog(guildName, targetUUID, "Nouveau rank -> Fondateur");
						guildInfo.setLog(guildName, playerUUID, "Nouveau rank -> Admin");
						break;
					}
				}
				break;
			}
			
			// +------------------------------------------------------+
			// |                      RANKS                           |
			// +------------------------------------------------------+
			
			case "ranks": {
				sender.sendMessage("§6===========================================================");
				sender.sendMessage("§9- Fondateur : §3Fondateur de la guilde");
				sender.sendMessage("§9- Admin : §3S'occupe des problèmes internes");
				sender.sendMessage("§9- Comptable : §3Gère l'argent de la guilde");
				sender.sendMessage("§9- Membres : §3Membres de la guilde");
				sender.sendMessage("§6===========================================================");
				break;
			}
			
			// +------------------------------------------------------+
			// |                       KICK                           |
			// +------------------------------------------------------+
			
			case "kick": {
				
				if(!hasGuild(guildName)) {
					sender.sendMessage(Messages.NO_GUILD.toString());
					return true;
				}
				
				if(!(isFondateur() || isAdmin())) {
					sender.sendMessage(Messages.NO_PERMS.toString());
					return true;
				}
				
				if(args.length != 2) {
					sender.sendMessage(Messages.INCOMPLETE_COMMAND.toString() + "/guild kick <player>");
					return true;
				}
				
				if(!isPlayerExist(args[1])) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cLe joueur passé en paramètre n'existe pas.");
					return true;
				}
				
				Player target = Bukkit.getPlayer(args[1]);
				PlayerData targetData = new PlayerData(target.getUniqueId());
				
				if(!(targetData.getGuild().equalsIgnoreCase(guildName))) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cCette personne n'est pas dans votre guilde !");
					return true;
				}
				
				int targetRank = targetData.getGuildRank();
				
				if(targetRank == 2 || targetRank == 3) {
					sender.sendMessage(Messages.NO_PERMS.toString());
					return true;
				}
				
				guildInfo.removeMember(guildName, target.getUniqueId());
				if(target.isOnline()) {
					target.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous venez de vous faire expulser de la guilde");
				}
				sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous avez expulsé " + args[1] + "de la guilde");
				guildInfo.setLog(guildName, target.getUniqueId(), "A été expulsé");
				
				targetData.resetGuild();
				
				break;
			}
			
			// +------------------------------------------------------+
			// |                       LOGS                           |
			// +------------------------------------------------------+
			
			case "logs": {
				
				if(!hasGuild(guildName)) {
					sender.sendMessage(Messages.NO_GUILD.toString());
					return true;
				}
				
				if(!(isFondateur() || isAdmin())) {
					sender.sendMessage(Messages.NO_PERMS.toString());
					return true;
				}
				
				List<String> logs = guildInfo.getLog(guildName);
				sender.sendMessage("§e============[ §6Logs§e ]=============");
				for (int i = 0; i < logs.size(); i++) {
					sender.sendMessage(logs.get(i));
				}
				sender.sendMessage("§e===============================");
				
				break;
			}
			
			// +------------------------------------------------------+
			// |                       HOME                           |
			// +------------------------------------------------------+
			
			case "home": {
				
				if(!hasGuild(guildName)) {
					sender.sendMessage(Messages.NO_GUILD.toString());
					return true;
				}
				
				/* Taxe rate */
				if(args.length == 4) {
					
					if(!isFondateur()) {
						sender.sendMessage(Messages.NO_PERMS.toString());
						return true;
					}
					
					Integer amount = Integer.valueOf(args[3]);
					
					if(!(args[1].equalsIgnoreCase("set") && args[2].equalsIgnoreCase("tax") && amount != null)) {
						sender.sendMessage(Messages.INCORRECT_COMMAND.toString() + "/guild home set tax <amount>");
						return true;
					}
					
					guildInfo.setTaxRate(guildName, amount);
					guildInfo.setLog(guildName, playerUUID, "A activé la taxe de téléportation");
					sender.sendMessage("§8[§l§cAnthopia§r§8] §aVous avez mis la taxe de téléportation à " + amount + " €");
					
					return true;
				}
				
				/* Enable / Disable tax */
				if(args.length == 3) {
					
					if(!isFondateur()) {
						sender.sendMessage(Messages.NO_PERMS.toString());
						return true;
					}
					
					String switchedTax = args[2];
					
					if(!(args[1].equalsIgnoreCase("tax") || switchedTax.equalsIgnoreCase("enable") || switchedTax.equalsIgnoreCase("disable") )) {
						sender.sendMessage(Messages.INCORRECT_COMMAND.toString() + "/guild home tax enabled/disabled");
						return true;
					}
					
					guildInfo.setTax(guildName, switchedTax);
					guildInfo.setLog(guildName, playerUUID, "A changé le taux de la taxe de téléportation");
					
					return true;
				}
				
				/* Home TP */
				Location home = guildInfo.getHomeLocation(guildName);
				if(home == null) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVotre home de guilde n'a pas été défini");
					return true;
				}
				
				if(guildInfo.getTax(guildName).equalsIgnoreCase("enable")) {
					int amount = guildInfo.getTaxRate(guildName);
					
					if(amount > EconomyData.getBalance(playerUUID)) {
						sender.sendMessage(Messages.NOT_ENOUGH_MONEY.toString());
						return true;
					}
					
					if(amount + guildInfo.getMoney(guildName) > guildBankAccountCap) {
						sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVotre coffre de guilde est plein. Téléportation annulée.");
						return true;
					}
					
					EconomyData.removeMoney(playerUUID, amount);
					
					Player player = (Player) sender;
					player.teleport(home);
					guildInfo.setMoney(guildName, guildInfo.getMoney(guildName) + amount);
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§aVous vous êtes téléporté au home de la guilde");
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§aVous avez donné " + amount + " € de taxe à la guilde");
					return true;
				}
				
				Player player = (Player) sender;
				player.teleport(home);
				sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§aVous vous êtes téléporté au home de la guilde");
				
				break;
			}
			
			// +------------------------------------------------------+
			// |                      SETHOME                         |
			// +------------------------------------------------------+
			
			case "sethome": {
				
				if(!hasGuild(guildName)) {
					sender.sendMessage(Messages.NO_GUILD.toString());
					return true;
				}
				
				if(!isFondateur()) {
					sender.sendMessage(Messages.NO_PERMS.toString());
					return true;
				}
				
				if(guildInfo.getMoney(guildName) < 1000) {
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous n'avez pas assez d'argent pour effectuer cette action : 1000€ sont requis");
					return true;
				}
				
				Player player = (Player) sender;
				guildInfo.setMoney(guildName, guildInfo.getMoney(guildName) - 1000);
				guildInfo.setHomeLocation(guildName, player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
				guildInfo.setLog(guildName, playerUUID, "A défini un nouveau home pour la guilde");
				sender.sendMessage(Messages.PLUGIN_NAME.toString() + "§aVous avez défini votre home de guilde");

				break;
			}

		}
		
		return true;
	}
	

	private void showGuildInv(String guildName, GuildInfo guildInfo, CommandSender sender) {
		//Ancien système de reset d'exp pour l'exp supplémentaire au passage de niveau.
		//Remis au goût du jour, sauce frustration, sur son plateau de futurs bugs. 
		//Je me fait pas d'illusions c'est de la merde mais laissez moi rêver.
		/* Code Legacy */
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
		/* ========== */
	}
	
	@SuppressWarnings("deprecation")
	public boolean isPlayerExist(String player) {
		if(Bukkit.getPlayer(player) == null) {
			if(Bukkit.getOfflinePlayer(player) == null) {
				return false;
			}
		}
		return true;
	}
	
	
	private boolean hasGuild(String guildName) {
		return guildName.isEmpty() ? false : true;
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
	
	public HashMap<String,UUID> getAcceptedPlayer(){
		return accepted;
	}
}