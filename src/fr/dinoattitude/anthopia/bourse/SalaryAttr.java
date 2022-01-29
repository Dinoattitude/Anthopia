package fr.dinoattitude.anthopia.bourse;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.dinoattitude.anthopia.bourse.economy_api.BourseData;
import fr.dinoattitude.anthopia.bourse.economy_api.EconomyData;
import fr.dinoattitude.anthopia.guild.guild_api.GuildInfo;
import fr.dinoattitude.anthopia.utils.Messages;
import fr.dinoattitude.anthopia.utils.PlayerData;

public class SalaryAttr {
	
	Double compensationBonus = null, salaryBonus = null;
	Integer level = null;
	String guildName = null;
	
	public void setExpAndSalary() {
		
		for(Player player : Bukkit.getOnlinePlayers()){
			
			UUID playerUuid = player.getUniqueId();
			PlayerData playerData = new PlayerData(playerUuid);
			GuildInfo guildInfo = new GuildInfo();
			guildName = playerData.getGuild();
			
			level = guildInfo.getLvl(playerData.getGuild());
			getBonus(level);
			
			setSalaryOrCompensation(player);
			
			setGuildExpAndLevel(player, guildInfo);
		}

	}
	
	private void getBonus(int level){
		
		for(Salary salary : Salary.values()) {
			if(level < salary.getLevel()) {
				compensationBonus = Double.valueOf(salary.getCompensationAmount());
				salaryBonus = Double.valueOf(salary.getSalaryAmount());
			}
		}
	}
	
	private void setSalaryOrCompensation(Player player) {
		UUID uuid = player.getUniqueId();
		
		if(!BourseData.isPlayer(uuid)) {
			return;
		}
		
		final Double SALARY = Math.floor(BourseData.getSalary(uuid));
		final Double TOTAL_SALARY = SALARY + Math.floor(salaryBonus);
		
		player.playSound(player.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1f);
		
		if(SALARY < 20D) {
			EconomyData.addMoney(uuid, compensationBonus);
			player.sendMessage(Messages.PLUGIN_NAME.toString() + "§eVous obtenez une allocation chômage de §6" + compensationBonus.toString() + " §eeuros");
			return;
		}
		
		EconomyData.addMoney(uuid, TOTAL_SALARY);
		player.sendMessage(Messages.PLUGIN_NAME.toString() + "§eVous obtenez un salaire de §6" + TOTAL_SALARY.toString() + " §eeuros"); 
		
		if(!guildName.isEmpty()) {
			player.sendMessage(Messages.PLUGIN_NAME.toString() + "§eSalaire de §6" + SALARY.toString() + " §eeuros et §6" + salaryBonus.toString() + " §eeuros grâce au niveau de votre guilde");
		}
		
		BourseData.setSalary(uuid, 1D);
	}
	
	private void setGuildExpAndLevel(Player player, GuildInfo guildInfo) {
		
		if(guildName.isEmpty()) {
			return;
		}
		
		final Double EXP_GAIN = guildInfo.GainExp(guildName);

		guildInfo.setExp(guildName, guildInfo.getExp(guildName) + EXP_GAIN);
		player.sendMessage(Messages.PLUGIN_NAME.toString() + "§eVous avez gagné §9" + EXP_GAIN.toString() + " §eexp pour votre guilde");
		
		final Double EXP = guildInfo.getExp(guildName);
		
		if(EXP < guildInfo.NextGuildLevel(guildName)) {
			return;
		}
		
		final Double EXTRA_EXP = EXP - guildInfo.NextGuildLevel(guildName);
		
		guildInfo.setLvl(guildName, level + 1);
		guildInfo.setExp(guildName, EXTRA_EXP);
	}
	
}
