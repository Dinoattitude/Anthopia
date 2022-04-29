package fr.dinoattitude.anthopia.bourse2;

import java.util.UUID;

public interface IEconomyData {
	
	//Economic Player Account (EPA)
	
	public Double getBalance(UUID uuid);
	
	public void setBalance(UUID uuid, double amount);
	
	public void removePlayerBalanceFromMap(UUID uuid);
	
	public void addMoney(UUID uuid, double amount);
	
	public void removeMoney(UUID uuid, double amount);
	
	public boolean hasAccount(UUID uuid);
	
	//Economic Player Bank Account (EPBA)
	
	public Double getBankAccountBalance(UUID uuid);
	
	public void setBankAccountBalance(UUID uuid, double amount);
	
	public void removePlayerBankAccountFromMap(UUID uuid);
	
	public void addBankAccountMoney(UUID uuid, double amount);
	
	public void removeBankAccountMoney(UUID uuid, double amount);
	
	public boolean hasBankAccount(UUID uuid);
	
}
