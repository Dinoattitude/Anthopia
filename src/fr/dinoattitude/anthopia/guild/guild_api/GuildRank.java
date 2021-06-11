package fr.dinoattitude.anthopia.guild.guild_api;

import java.util.HashMap;
import java.util.Map;

public enum GuildRank {

	FONDATEUR(3,"§1§cGuildRank", "§cFondateur", "§8[§cFondateur§8]§9"),
	
	ADMIN(2,"§2§cGuildRank", "§5Admin","§8[§5Admin§8]§9"),
	
	COMPTABLE(1,"§3§cGuildRank", "§eComptable","§8[§eComptable§8]§9"),
	
	MEMBRE(0,"§4§cGuildRank", "§bMembre","§8[§bMembre§8]§9");
	
	
	private int power;
	private String name, orderRank, displayName;
	public static Map<Integer, GuildRank> ranks = new HashMap<>();
	
	private GuildRank(int power, String orderRank, String name, String displayName) {
		this.power = power;
		this.name = name;
		this.orderRank = orderRank;
		this.displayName = displayName;
	}
	
	static {
		for(GuildRank guildRank : GuildRank.values()) {
			ranks.put(guildRank.getPower(), guildRank);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public static GuildRank powerToRank(int power) {
		return ranks.get(power);
	}
	
	public int getPower() {
		return power;
	}
	
	public String getOrderRank() {
		return orderRank;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
}
