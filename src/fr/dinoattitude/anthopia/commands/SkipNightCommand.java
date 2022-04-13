package fr.dinoattitude.anthopia.commands;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.dinoattitude.anthopia.Main;
import fr.dinoattitude.anthopia.utils.Messages;

/** Vote for skiping the night.
 * @author Yolowix
 * @author Dinoattitude
 * @since 2.3.6
 * @version 2.4.3
*/
public class SkipNightCommand implements CommandExecutor{
	
	private static HashMap<Integer,String> playerVote = new HashMap<Integer,String>();
	private final long SUNSET_TIME = 12000;
	private final long SUNRISE_TIME = 24000;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player player = (Player) sender;
		World world = Bukkit.getWorld("world"); //Manuellement pour éviter les votes nether et end
		
		long worldTime = world.getTime();
		int numberOfOnlinePlayers = Bukkit.getOnlinePlayers().size();
		
		if(worldTime < SUNSET_TIME && worldTime > SUNRISE_TIME) {
			player.sendMessage(Messages.CANT_SKIPNIGHT.toString());
			return true;
		}
		
		int halfOfOnlinePlayers = (Math.round(numberOfOnlinePlayers / 2)) + 1;
		
		if(numberOfOnlinePlayers < 1) {
			endVote(world);
			
			return true;
		}
		
		BossBar skipnightHeader = Bukkit.createBossBar("§b[ SkipNight ]   " 
				+ "§fCurrent:  §b" 
				+ playerVote.size() 
				+ "  §fTo Pass:  §b" 
				+ halfOfOnlinePlayers 
				+ "  §fRemaining:  §b" 
				+ (numberOfOnlinePlayers - playerVote.size()), BarColor.BLUE, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
		
		if(playerVote.size() != 0) {
			
			if(playerVote.containsValue(player.getName())) {
				player.sendMessage("§bYou have already voted");
				return true;
			}
				
			playerVote.put(playerVote.size() + 1, player.getName());
			
			if(playerVote.size() == halfOfOnlinePlayers) {
				endVote(world);
				playerVote.clear();
				return true;
			}
			
			return true;
		}
		
		playerVote.put(playerVote.size() + 1, player.getName());
		
		Main INSTANCE = Main.getInstance();
		
		for(Player players : Bukkit.getOnlinePlayers()) {
			skipnightHeader.addPlayer(players);
		}
			
		skipnightHeader.setVisible(true);
		
		new BukkitRunnable() {
			
			double timer = 1;

			@Override
			public void run() {
				
				setSkipnightHeaderTitle(skipnightHeader, halfOfOnlinePlayers, numberOfOnlinePlayers);
				
				timer = timer - 0.1; 
				
				//convertit le timer pour le setProgress
				DecimalFormat df = new DecimalFormat("########.00");
				String str = df.format(timer);
				timer = Double.parseDouble(str.replace(',', '.'));
				
				skipnightHeader.setProgress(timer);
				
				if(playerVote.size() == 0) 
				{
					this.cancel();
					skipnightHeader.setVisible(false);
					playerVote.clear();
				}
				
				if(timer == 0)
				{
					this.cancel();
					
					skipnightHeader.setVisible(false);
					
					for(Player players : Bukkit.getOnlinePlayers()) {
						players.sendMessage(Messages.VOTE_DENIED.toString());
					}
						
					playerVote.clear();
				}
				
			}
			
		}.runTaskTimer(INSTANCE, 0L, 60L); //60L -> toutes les 3sec
		
		return true;
	}
	
	public void endVote(World world) {
		
		for(Player players : Bukkit.getOnlinePlayers()) {
			players.sendMessage(Messages.SKIPING_NIGHT.toString());
		}
		
		world.setTime(SUNRISE_TIME); 
	}
	
	public void setSkipnightHeaderTitle(BossBar skipnightHeader, int halfOfOnlinePlayers, int numberOfOnlinePlayers) {
		skipnightHeader.setTitle("§b[ SkipNight ]   " 
				+ "§fCurrent:  §b" 
				+ playerVote.size() 
				+ "  §fTo Pass:  §b" 
				+ halfOfOnlinePlayers 
				+ "  §fRemaining:  §b" 
				+ (numberOfOnlinePlayers - playerVote.size()));
	}

}
