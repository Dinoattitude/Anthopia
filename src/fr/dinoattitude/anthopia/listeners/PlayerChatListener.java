package fr.dinoattitude.anthopia.listeners;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.dinoattitude.anthopia.Main;
import fr.dinoattitude.anthopia.utils.PlayerData;
import fr.dinoattitude.anthopia.utils.Rank;

public class PlayerChatListener implements Listener{

	//Stockage des messages pour les quotes
	public static Map<Player, String> lastMessage = new HashMap<>();
	
	private static FileConfiguration config;
	private File file;
	
	public final FileConfiguration getConfig() {
		initConfigSaveChat();
		return config;
		
	}
	
	public static Integer Test;
	public static String strMsg = "0";
	public static String Name = "";
	public static String msgName = "AnthopiaMsg";
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		PlayerData playerData = new PlayerData(player.getUniqueId());
		
		event.setCancelled(true);
		
		if(event.getMessage().startsWith("[]") && Main.getPermissions().has(player, "anthopia.moderation.chat")) {
			event.setCancelled(true);
			for(Player players : Bukkit.getOnlinePlayers()) {
				if(players.hasPermission("anthopia.moderation.chat")) {
					player.sendMessage("§8[§l§5STAFF CHAT§l§8] §9" + player.getName() + "§7: §e" + event.getMessage().substring(2));
				}
			}
		}
		else if(!event.getMessage().startsWith("[]") && event.getMessage() != null) {
			event.setFormat(Rank.powerToRank(playerData.getRank()).getDisplayName() + player.getName() + " §f: " + event.getMessage());
			getConfig(); 
			saveChat(event); 
			
			//Mentions
			if(event.getMessage().contains("@")) {
				String message = event.getMessage();
				String str[] = message.split("@");
				String str2[] = str[1].split(" ");
				String pseudo = str2[0];
				
				Player cible = Bukkit.getPlayer(pseudo);
				if(cible != null) {
					cible.playSound(cible.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1f);
					cible.sendMessage("§7Vous venez d'être mentionné par " + player.getName());
				}
				else
				System.out.println(pseudo);
			}
			
			//Enregistrement des quotes dans la HashMap
			lastMessage.put(player, event.getMessage());
    		
			for(Player players : Bukkit.getOnlinePlayers()) {
				players.sendMessage(event.getFormat());
			}
		}
		else {
			player.sendMessage("§8[§l§cAnthopia§r§8] §cVous n'avez pas les droits pour effectuer cette action");
		}
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
	
	public void saveChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		PlayerData playerData = new PlayerData(player.getUniqueId());
		Name = player.getName();
		try {
				
			for(int i = 1; i <= 11; i++) {
				if(((String) config.get(msgName + " [" + i + "] ")) == null) {
					Test = i;
					break;
				}
			}
			if(Test < 11) {
				strMsg = Test.toString();
				Name = player.getName();
				
				String rank = Rank.powerToRank(playerData.getRank()).getDisplayName();
							
				config.set(msgName + " [" + strMsg + "] ", rank + "§9" + Name + " §7: " + event.getMessage());
				config.save(file);
			}
			else {
				for(int i = 1; i <= 11; i++){
					if(i == 10){
					String rank = Rank.powerToRank(playerData.getRank()).getDisplayName();
					config.set(msgName + " [" + i + "] ",rank + "§9" + Name + " §7: " + event.getMessage());
					config.save(file);
					}
					else {
						Integer j = i+1;
						String h = j.toString();
						String Msg = (String) config.get(msgName + " [" + h + "] ");
						config.set(msgName + " [" + i + "] ", Msg);
						config.save(file);
						
					}
				}
			}
				
			
		}catch(IOException ioe) {ioe.printStackTrace();}
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
	
}
