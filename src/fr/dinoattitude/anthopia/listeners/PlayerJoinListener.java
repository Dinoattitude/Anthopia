package fr.dinoattitude.anthopia.listeners;

import java.io.File;
import java.io.IOException;
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
import fr.dinoattitude.anthopia.utils.Tablist;
import fr.dinoattitude.anthopia.utils.TeamsTagsManager;
import fr.dinoattitude.anthopia.utils.Utilities;

public class PlayerJoinListener implements Listener {
	
	//Tout ce merdier c'est pour l'affichage du chat e-e
	private static FileConfiguration config;
	private File file;
	
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
	
	public static void getChat(Player player, String token) {
		
		for(int i = 0; i <= 11; i++) {
			
			if(((String) config.get(token + " [" + i + "] ")) == null) {
				
			}
			else {
				String getMsg = (String) config.get(token + " [" + i + "] ");
				player.sendMessage(getMsg);
			}
			
			
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {

		Player player = event.getPlayer();	
		UUID uuidPlayer = event.getPlayer().getUniqueId();
		
		//creation compte config
		if(!EconomyData.hasAccount(uuidPlayer)) {
			EconomyData.setBalance(uuidPlayer, 200D);
			EconomyData.addMoney(uuidPlayer, 200D);
		}
		if(!EconomyData.hasBankAccount(uuidPlayer))
			EconomyData.setBankAccount(uuidPlayer, 0D);
		
		if(BourseData.getSalary(uuidPlayer) == null)
			BourseData.setSalary(uuidPlayer, 1D);
		
		
		event.setJoinMessage("§9§l" + event.getPlayer().getName() + " §r§eviens d'arriver sur le serveur");
		
		player.sendMessage("§r§e=========[ Historique de la conversation ]=========");
		getConfig();
		getChat(player, "AnthopiaMsg");
		player.sendMessage("§e=============================================");


		new BukkitRunnable()
		{
		    @Override
		    public void run()
		    {
		    	PlayerData playerData = new PlayerData(player.getUniqueId());
				Double balance = EconomyData.getBalance(player.getUniqueId());
				Integer tempAmount = balance.intValue();
				String amount = tempAmount.toString();
				
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
				String texte_date = sdf.format(new Date());
				
				Tablist.Tab(player, "§b§lBienvenue sur Anthopia\n"
				+ " \n"
				+ "§aPing: §2§n" + Utilities.getPing(player).toString() + "§r§a ms\n"
				+ "§6§l" + amount + "§r§6 euros\n"
				+ "§3-------------------", 
				"§3-------------------\n" 
				+ "§7Heure §3[§b" + texte_date + "§3]\n"
				+ " \n"
				+ "§2Adresse: §amc.anthopia.net\n"
				+ "§ehttps://discord.gg/CDPXnxq");
				TeamsTagsManager.setNameTag(player, Rank.powerToRank(playerData.getRank()).getOrderRank(), Rank.powerToRank(playerData.getRank()).getDisplayName()+" ", "");
		    }
		}.runTaskTimer(Main.getInstance(), 0L, 20L);
		
		new BukkitRunnable()
		{
		    @SuppressWarnings("deprecation")
			@Override
		    public void run()
		    {
		    	PlayerData playerData = new PlayerData(player.getUniqueId());
				final String SEPARATEUR = ",";
				
				if(Main.getInstance().setupEconomy()) {
					String econ = Main.getEconomy().format(Main.getEconomy().getBalance(player.getName()));
					String eco = econ.substring(1, econ.length());
					String Amount[] = eco.split(SEPARATEUR);
					String ecoFinal = "";
					for(int i = 0;i < Amount.length;i++) {
						ecoFinal = ecoFinal + "" + Amount[i];
					}
					playerData.setMoney(Float.parseFloat(ecoFinal));
	    		}
		    }
		}.runTaskTimer(Main.getInstance(), 60*20L, 60*20L);
		
		
	}
	
}
