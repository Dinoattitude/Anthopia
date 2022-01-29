package fr.dinoattitude.anthopia.listeners;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.dinoattitude.anthopia.Main;
import fr.dinoattitude.anthopia.bourse.economy_api.BourseData;
import fr.dinoattitude.anthopia.bourse.economy_api.EconomyData;
import fr.dinoattitude.anthopia.utils.PlayerData;
import fr.dinoattitude.anthopia.utils.Rank;
import fr.dinoattitude.anthopia.utils.TeamsTagsManager;

public class PlayerJoinListener implements Listener {
	
	//Tout ce merdier c'est pour l'affichage du chat e-e
	private static FileConfiguration config;
	private File file;
	private boolean newPlayer = false;
	private final String TOKEN = "AnthopiaMsg";
	
	public final FileConfiguration getConfig() {
		initConfigSaveChat();
		return config;
	}
	
	private void initConfigSaveChat() {
		File f = new File("plugins/Anthopia");
		if(!f.exists()) f.mkdirs();
		file = new File(f, "saveChat.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
				
			}catch(IOException ioe) { ioe.printStackTrace();}
		}
		
		config = YamlConfiguration.loadConfiguration(file);
		
	}
	
	/**
	 * Gets chat history and display it to the player
	 * @param player the player whose history is displayed
	 */
	public void displayChatHistoryToPlayer(Player player) {
		
		player.sendMessage("§r§e=========[ Historique de la conversation ]=========");
		getConfig();
		
		for(int i = 0; i <= 11; i++) {
			
			if(((String) config.get(TOKEN + " [" + i + "] ")) != null) {
				String getMsg = (String) config.get(TOKEN + " [" + i + "] ");
				player.sendMessage(getMsg);
			}		
		
		}
		
		player.sendMessage("§e=============================================");
	}
	
	/**
	 * Initializes the player's accounts if they have never played before
	 * @param uuid the player uuid
	 */
	public void initAccountIfNotPlayedBefore(UUID uuid) {
		PlayerData playerData = new PlayerData(uuid);
		
		/* On évite !player.hasPlayedBefore() pour les tests sur la dom
		   Non en fait tout cours car faut init la map de base et pop erreur infine */
		/* EconomyData.hasAccount(uuid) ne marchera pas car 
		   EconomyData.loadPlayerEconomy plus bas et ne peux pas être mis plus haut */
		
		if(!playerData.isPlayerFileExist()) {
			playerData.addNewPlayerData();
			EconomyData.addMoney(uuid, 200D);
			EconomyData.setBankAccount(uuid, 0D);
			newPlayer = true;
		}

	}
	
	/**
	 * Set the player tablist 
	 * @param player the player to whom the tablist will apply
	 * @param header the tablist header (can be null)
	 * @param footer the tablist footer (can be null)
	 */
	public void setTablist(Player player, String header, String footer) {
		if(header == null) header = "";
		if(footer == null) footer = "";
		
		player.setPlayerListHeaderFooter(header, footer);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {

		Player player = event.getPlayer();	
		UUID playerUUID = event.getPlayer().getUniqueId();
		
		initAccountIfNotPlayedBefore(playerUUID);
		
		if(!newPlayer) {
			EconomyData.loadPlayerEconomy(playerUUID);
		}
		
		if(BourseData.getSalary(playerUUID) == null) {
			BourseData.setSalary(playerUUID, 1D);
		}
		
		event.setJoinMessage("§9§l" + event.getPlayer().getName() + " §r§eviens d'arriver sur le serveur");
			
		displayChatHistoryToPlayer(player);
		
		
		/* Tablist Runnable */
		new BukkitRunnable()
		{
		    @Override
		    public void run()
		    {
		    	
		    	if(!player.isOnline()) {
					cancel();
				}
		    	
		    	PlayerData playerData = new PlayerData(player.getUniqueId());
				Double balance = EconomyData.getBalance(player.getUniqueId());
				Integer tempAmount = balance.intValue();
				String amount = tempAmount.toString();
				
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				String texte_date = sdf.format(new Date());
				
				setTablist(player, "§b§lBienvenue sur Anthopia\n"
				+ " \n"
				+ "§aPing: §2§n" + String.valueOf(player.getPing()) + "§r§a ms\n"
				+ "§6§l" + amount + "§r§6 euros\n"
				+ "§3-------------------", 
				"§3-------------------\n" 
				+ "§7Heure §3[§b" + texte_date + "§3]\n"
				+ " \n"
				+ "§2Adresse: §amc.anthopia.net\n"
				+ "§ehttps://discord.gg/CDPXnxq");
				TeamsTagsManager.setNameTag(player, Rank.powerToRank(playerData.getRank()).getOrderRank(), Rank.powerToRank(playerData.getRank()).getDisplayName()+" ", "");
				
		    }
		}.runTaskTimer(Main.getInstance(), 0L, 5 * 20L);
		
		/* EDIT: Refactored, voir dead code dans l'ancien Anthopiaapi sur github.
		   Ouais j'ai eu du mal à rechercher ce que j'avais branlé. 
		   C'est une syncro avec l'économie de Vault pour ne pas avoir de différence entre les deux économies */
		new BukkitRunnable()
		{
			@Override
		    public void run()
		    {
		    	
		    	if(!player.isOnline()) {
					cancel();
				}

				final Double VAULT_BALANCE = Main.getEconomy().getBalance(player);
				final Double INTERN_BALANCE = EconomyData.getBalance(playerUUID);
				
				if(VAULT_BALANCE < INTERN_BALANCE) {
					return;
				}
				
				EconomyData.setBalance(playerUUID, VAULT_BALANCE);
		    }
		}.runTaskTimer(Main.getInstance(), 60 * 20L, 60 * 20L);

	}
	
}
