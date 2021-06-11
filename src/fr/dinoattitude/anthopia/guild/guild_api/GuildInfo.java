package fr.dinoattitude.anthopia.guild.guild_api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

public class GuildInfo {

	private GuildData guildData;
	
	public GuildInfo() {
		this.guildData = new GuildData();
	}
	
	public void deleteLogFile(String guildName) {
		guildData.deleteLogFile(guildName);
	}
	
	//+------------------------------------------------------+ 
	//|    	              Files - Getters                    | 
	//+------------------------------------------------------+
	
	public String getInfo(String guildName) {
		return guildData.getInfo(guildName);
	}
	
	public double getMoney(String guildName) {
		return guildData.getMoney(guildName);
	}
	
	public int getLvl(String guildName) {
		return guildData.getLvl(guildName);
	}

	public double getExp(String guildName) {
		return guildData.getExp(guildName);
	}
	
	public String getTax(String guildName) {
		return guildData.getTax(guildName);
	}
	
	public int getTaxRate(String guildName) {
		return guildData.getTaxRate(guildName);
	}
	
	public List<String> getGuildMembers(String guildName){
		return guildData.getGuildMembers(guildName);
	}
	
	public Map<String, String> getGuildsList(){
		return guildData.getGuildsList();
	}
	
	public Location getHomeLocation(String guildName) {
		return guildData.getHomeLocation(guildName);
	}
	
	public List<String> getLog(String guildName) {
		return guildData.getLog(guildName);
	}
	
	//+------------------------------------------------------+ 
	//|    	              Files - Setters                    | 
	//+------------------------------------------------------+
	
	public void setGuildData(String guildName, UUID ownerUuid) {
		guildData.setGuildData(guildName, ownerUuid);
	}
	
	public void removeGuildData(String guildName) {
		guildData.removeGuildData(guildName);
	}
	
	public void setInfo(String guildName, String info) {
		guildData.setInfo(guildName, info);
	}
	
	public void setMoney(String guildName, double amount) {
		guildData.setMoney(guildName, amount);
	}
	
	public void setLvl(String guildName, double Lvl) {
		guildData.setLvl(guildName, Lvl);
	}

	public void setExp(String guildName, double exp) {
		guildData.setExp(guildName, exp);
	}

	public void setTax(String guildName, String switchedTax) {
		guildData.setTax(guildName, switchedTax);
	}

	public void setTaxRate(String guildName, Integer rate) {
		guildData.setTaxRate(guildName, rate);
	}

	public void setMember(String guildName, UUID memberUuid, String rank) {
		guildData.setMember(guildName, memberUuid, rank);
	}

	public void removeMember(String guildName, UUID playerUuid) {
		guildData.removeMember(guildName, playerUuid);
	}

	public void setHomeLocation(String guildName, String world, int locX, int locY, int locZ) {
		guildData.setHomeLocation(guildName, world, locX, locY, locZ);
	}
	
	public void setLog(String guildName, UUID playerUuid, String log) {
		guildData.setLog(guildName, playerUuid, log);
	}
	
	//+------------------------------------------------------+ 
	//|    	               Inventories                       | 
	//+------------------------------------------------------+
	
	public Inventory getAvantageInventory(String guildName) {
		return guildData.getAvantageInventory(guildName);
	}
	
	public Inventory getGuildInventory(String guildName) {
		return guildData.getGuildInventory(guildName);
	}
	
	public Inventory getMembersInventory(String guildName) {
		return guildData.getMembersInventory(guildName);
	}
	
	//+------------------------------------------------------+ 
	//|    	                  Others                         | 
	//+------------------------------------------------------+
	
	public double NextGuildLevel(String guildName) {
		return guildData.NextGuildLevel(guildName);
	}
	
	public double GainExp(String guildName) {
		return guildData.GainExp(guildName);
	}
}
