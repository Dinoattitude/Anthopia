package fr.dinoattitude.anthopia.utils;

import java.util.HashMap;
import java.util.Map;

public enum Rank {

	DEVELOPPEUR(5,"§1§cRank", "§cDEV","§8[§cDEV§8]§9"),
	
	ADMIN(4,"§2§cRank", "§5ADMIN","§8[§5ADMIN§8]§9"),
	
	MODERATEUR(3,"§3§cRank", "§eMODO","§8[§eMODO§8]§9"),
	
	VIP(2,"§4§cRank", "§bVIP","§8[§bVIP§8]§9"),
	
	CITOYEN(1,"§5§cRank", "§2Citoyen","§8[§2Citoyen§8]§9"),
	
	VISITEUR(0,"§6§cRank", "§7Visiteur","§8[§7Visiteur§8]§9");
	
	
	private int power;
	private String name, orderRank, displayName;
	public static Map<Integer, Rank> ranks = new HashMap<>();
	
	private Rank(int power, String orderRank, String name, String displayName) {
		this.power = power;
		this.name = name;
		this.orderRank = orderRank;
		this.displayName = displayName;
	}
	
	static {
		for(Rank rank : Rank.values()) {
			ranks.put(rank.getPower(), rank);
		}
	}

	public String getName() {
		return name;
	}
	
	public static Rank powerToRank(int power) {
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
