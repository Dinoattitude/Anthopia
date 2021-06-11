package fr.dinoattitude.anthopia.bourse;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.dinoattitude.anthopia.bourse.economy_api.BourseData;
import fr.dinoattitude.anthopia.bourse.economy_api.EconomyData;
import fr.dinoattitude.anthopia.guild.guild_api.GuildInfo;
import fr.dinoattitude.anthopia.utils.PlayerData;

public class SalaryAttr {
 
	public static ArrayList<Integer> getBonus(int level){
		
		ArrayList<Integer> bonus = new ArrayList<Integer>();
		for(Salary s : Salary.values()) {
			if(level < s.getLevel()) {
				bonus.add(s.getGainAlloc());
				bonus.add(s.getGainSalary());
				return bonus;
			}
		}
		return bonus;
	}
	
	public static void setExpAndSalary() {
		
		for(Player player : Bukkit.getOnlinePlayers()){
			
			UUID playerUuid = player.getUniqueId();
			PlayerData playerData = new PlayerData(playerUuid);
			GuildInfo guildInfo = new GuildInfo();
			
			Integer level = guildInfo.getLvl(playerData.getGuild());
			ArrayList<Integer> bonus = getBonus(level);
			String guildName = playerData.getGuild();
			
			double salaireGain = bonus.get(0);
			double allocGain = bonus.get(1);
			
			//On attribue le salaire ou l'allocation chomage au joueur.
			if(BourseData.isPlayer(playerUuid)) {
				double playerBalance = EconomyData.getBalance(playerUuid);
				final double SALARY = BourseData.getSalary(playerUuid) + salaireGain;
				
				//Play a sound for players to notice
				player.playSound(player.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1f);
				
				if(BourseData.getSalary(playerUuid) > 20D) {
					EconomyData.setBalance(playerUuid, playerBalance + SALARY);
					EconomyData.addMoney(playerUuid, playerBalance + SALARY);
					player.sendMessage("§8[§cAnthopia§8] §7: §eVous obtenez un salaire de §6" + Math.floor(SALARY) + " §eeuros"); 
					if(guildName != null) {
						player.sendMessage("§8[§cAnthopia§8] §7: §eSalaire de §6" + Math.floor(BourseData.getSalary(playerUuid)) + " §eeuros et §6" + salaireGain + " §eeuros grâce au niveau de votre guilde");
					}
				}
				else {
					EconomyData.setBalance(playerUuid, playerBalance + allocGain);
					EconomyData.addMoney(playerUuid, playerBalance + allocGain);
					player.sendMessage("§8[§cAnthopia§8] §7: §eVous obtenez une allocation chômage de §6" + allocGain + " §eeuros");
				}
				BourseData.setSalary(playerUuid, 1D);
			}
			
			//On donne l'exp à la guilde du joueur et on vérifie le passage à niveau
			if(guildName != null) {
				double gainExp = guildInfo.GainExp(guildName);
				guildInfo.setExp(guildName, guildInfo.getExp(guildName) + gainExp);
				player.sendMessage("§8[§cAnthopia§8] §7: §eVous avez gagné §9" + String.valueOf(gainExp) + " §eexp pour votre guilde");
				
				double exp = guildInfo.getExp(guildName);
				if(exp > guildInfo.NextGuildLevel(guildName)) {
					double expSup = exp - guildInfo.NextGuildLevel(guildName);
					
					guildInfo.setLvl(guildName, level+1);
					guildInfo.setExp(guildName, expSup);
				}
			}
			
		}

	}
	
}
