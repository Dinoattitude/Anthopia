package fr.dinoattitude.anthopia.bourse.economy_api;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.dinoattitude.anthopia.Main;
import fr.dinoattitude.anthopia.utils.PlayerData;

public class EconomyData {
	
	public static HashMap<UUID,Double> balance = new HashMap<>();
	public static HashMap<UUID,Double> bankAccount = new HashMap<>();
	
	
	//Balance's methods
	
	public static void setBalance(UUID uuid, double amount) {
		balance.put(uuid, amount);
	}
	
	public static Double getBalance(UUID uuid) {
		return balance.get(uuid);
	}
	
	public static boolean hasAccount(UUID uuid) {
		return balance.containsKey(uuid);
	}
	
	public static HashMap<UUID,Double> getBalanceMap(){
		return balance;
	}
	
	public static void addMoney(UUID uuid, double amount) {
		Player p = Bukkit.getPlayer(uuid);
		if(Main.getInstance().setupEconomy()) {
			Main.getEconomy().depositPlayer(p, amount);
		}
	}
	
	public static void removeMoney(UUID uuid, double amount) {
		Player p = Bukkit.getPlayer(uuid);
		if(Main.getInstance().setupEconomy()) {
			Main.getEconomy().withdrawPlayer(p, amount);
		}
	}
	
	//BankAccount's methods
	
	public static void setBankAccount(UUID uuid, double amount) {
		bankAccount.put(uuid, amount);
	}
	
	public static Double getBankAccount(UUID uuid) {
		return bankAccount.get(uuid);
	}
	
	public static boolean hasBankAccount(UUID uuid) {
		return bankAccount.containsKey(uuid);
	}
	
	public static HashMap<UUID,Double> getBankAccountMap(){
		return bankAccount;
	}
	
	//Others
	
	public static void loadPlayerEconomy(UUID uuid) {
		PlayerData playerData = new PlayerData(uuid);
		
		setBalance(uuid, playerData.getMoney());
		setBankAccount(uuid, playerData.getCb());
	}
	
	public static void savePlayerEconomy(UUID uuid) {
		PlayerData playerData = new PlayerData(uuid);

		playerData.setMoney(getBalance(uuid));
		playerData.setCb(getBankAccount(uuid));
	}
	
}
