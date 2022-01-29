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

public class SkipNightCommand implements CommandExecutor{

	public static HashMap<Integer,String>playerVote=new HashMap <Integer,String>();  //Stock les joueurs ayant fait la commande
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		//récupère joueur
		Player player = (Player) sender;
		
		//récupère le monde et l'heure
		World world = Bukkit.getWorld("world"); //Manuellement pour éviter les votes nether et end
		long time= world.getTime();
	
		int nbPlayers = Bukkit.getOnlinePlayers().size();  //nombre joueur sur le serveur en ligne
		
		//Pour les tests
			//int nbPlayers=5;
			//playerVote.put(1, "truc");
			//playerVote.put(2, "test");	

		if(time>12000 && time<24000)  //vérifie l'heure		
		{
			int halfPlayers=(Math.round(nbPlayers/2))+1;  //calcul la moitié des joueurs en ligne

			if(nbPlayers>1)  //vérifie le nombre de joueur
			{	
				//création de la barre
				BossBar boss=Bukkit.createBossBar("§b[ SkipNight ]   "+"§fCurrent:  §b"+playerVote.size()+"  §fTo Pass:  §b"+halfPlayers+"  §fRemaining:  §b"+(nbPlayers-playerVote.size()), BarColor.BLUE, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
				
				if(playerVote.size()==0)  //vérifie si premier skipNight
				{
					playerVote.put(playerVote.size()+1, player.getName());  //ajoute le joueur dans la liste
					
					Main INSTANCE = Main.getInstance();
					
					//affiche la barre pour tout les joueurs 
					for(Player players : Bukkit.getOnlinePlayers())
						boss.addPlayer(players);
					boss.setVisible(true);
					
					//création du timer
					new BukkitRunnable() {
						double value=1;  //init 
						@Override
						public void run()
						{
							boss.setTitle("§b[ SkipNight ]   "+"§fCurrent:  §b"+playerVote.size()+"  §fTo Pass:  §b"+halfPlayers+"  §fRemaining:  §b"+(nbPlayers-playerVote.size()));  //actualise les données affichée au dessus de la barre
								
							value=value-0.1;  //calcul pour la progression de la barre
							
							//convertir value pour le setProgress
							DecimalFormat df = new DecimalFormat("########.00");
							String str = df.format(value);
							value = Double.parseDouble(str.replace(',', '.'));
		
							boss.setProgress(value);   //init
		
							if(playerVote.size()==0)  //Vérifie si liste vide
							{
								//fermeture timer, barre et vidange de la liste
								this.cancel();
								boss.setVisible(false);
								playerVote.clear();  //Vidange la liste
							}
							if(value==0)  //si barre arrive à 0
							{
								//fermeture timer, barre, vidange de la liste et affichage du vote raté
								this.cancel();
								boss.setVisible(false);
								for(Player players : Bukkit.getOnlinePlayers())
									players.sendMessage("§bVote Denied");
								playerVote.clear();  //Vidange la liste
							}
						}
					}.runTaskTimer(INSTANCE, 0L, 60L); //60L ->tout les 3sec, action run()

					
					if(playerVote.size()==0)  //si liste vide, fermé prog
					{
						return true;
					}

				}
				else
				{
					//verifie les doublons de vote et affiche message erreur
					if(playerVote.containsValue(player.getName()))
						player.sendMessage("§bYou have already voted");
					else
					{
						playerVote.put(playerVote.size()+1, player.getName()); //add le joueur dans la liste

						if(playerVote.size()==halfPlayers) //verifie si liste egal moitié joueur
						{
							//envoie message a tout les joueurs
							for(Player players : Bukkit.getOnlinePlayers())
								players.sendMessage("§bVote Passed. Skipping the Night");

							world.setTime(24000);  //Met le jour dans le monde
							playerVote.clear();  //Vidange la liste
							return true;
						}
					}
				}
				
			}
			else
			{
				world.setTime(24000);  //Met le jour dans le monde
				//envoie message a tout les joueurs
				for(Player players : Bukkit.getOnlinePlayers())
					players.sendMessage("§bVote Passed. Skipping the Night");
			}
		}
		else
			player.sendMessage("§bYou can't skip the night yet"); //envoie message au joueur
	

		return true;
	}
		
}
