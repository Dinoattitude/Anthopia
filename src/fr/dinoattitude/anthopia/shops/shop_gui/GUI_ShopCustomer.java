package fr.dinoattitude.anthopia.shops.shop_gui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.dinoattitude.anthopia.bourse.economy_api.EconomyData;
import fr.dinoattitude.anthopia.shops.ShopName;
import fr.dinoattitude.anthopia.shops.shop_api.ShopData;
import fr.dinoattitude.anthopia.utils.Messages;
import fr.dinoattitude.anthopia.utils.Utilities;

/** GUI constructor and action manager for the customer's shop .
 * @author Dinoattitude
 * @since 3.4.3
 * @version 3.4.3
*/
public class GUI_ShopCustomer implements IGUI_Inventory {

	private UUID shopOwner_UUID;
	private ShopData shopData;
	private ItemStack shopItemStack;
	private int shopStock;
	
	public GUI_ShopCustomer(ShopData gui_shopData) {
		shopData = gui_shopData;
		shopOwner_UUID = shopData.getShopOwner();
		shopItemStack = shopData.getItem(shopOwner_UUID);
		shopStock = shopData.getStock(shopOwner_UUID);
	}

	@Override
	public Inventory getInventory() {
		
		Inventory GUI = Bukkit.createInventory(this, 9, ShopName.CUSTOMER.toString());
		
		int priceAmount = 0;
		String typeTransaction = "§6Vendre: ";
		String saleType = shopData.getShopSaleType();
		Material ownerMerchMaterial = shopItemStack.getType();
		
		if(saleType.equalsIgnoreCase("purchase")) {
			GUI.setItem(0, Utilities.getItem(Material.PAPER, 1, "§6Acheter", "Prix unitaire :", "" + shopData.getPurchasePrice(shopOwner_UUID) + " euros"));
			priceAmount = shopData.getPurchasePrice(shopOwner_UUID);
			typeTransaction = "§6Acheter: ";
		}
		else {
			GUI.setItem(0, Utilities.getItem(Material.PAPER, 1, "§6Vendre", "Prix unitaire :", "" + shopData.getSellingPrice(shopOwner_UUID) + " euros"));
			priceAmount = shopData.getSellingPrice(shopOwner_UUID);
		}
		
		GUI.setItem(2, Utilities.getItem(ownerMerchMaterial, 64, typeTransaction, "x64 blocs ("+ (priceAmount * 64) + " euros)", null));
		GUI.setItem(3, Utilities.getItem(ownerMerchMaterial, 32, typeTransaction, "x32 blocs ("+ (priceAmount * 32) + " euros)", null));
		GUI.setItem(4, Utilities.getItem(ownerMerchMaterial, 16, typeTransaction, "x16 blocs ("+ (priceAmount * 16) + " euros)", null));
		GUI.setItem(5, Utilities.getItem(ownerMerchMaterial, 8, typeTransaction, "x8 blocs ("+ (priceAmount * 8) + " euros)", null));
		GUI.setItem(6, Utilities.getItem(ownerMerchMaterial, 1, typeTransaction, "x1 bloc ("+ (priceAmount) + " euros)", null));
		GUI.setItem(8, Utilities.getItem(Material.RED_STAINED_GLASS_PANE, 1, "§cQuitter le shop", null, null));
		
		return GUI;
	}

	@Override
	public void onGUIClick(Player playerWhoClicked, ItemStack clickedItem, int slot) {
		
		if(clickedItem.getType() == Material.RED_STAINED_GLASS_PANE) {
			playerWhoClicked.closeInventory();
			return;
		}
		
		if(clickedItem.getType() == Material.PAPER) {
			return;
		}

		int amount = clickedItem.getAmount();

		if(amount == 0) {
			return;
		}

		if(!isSpaceToTrade(playerWhoClicked, amount)) {
			playerWhoClicked.sendMessage(Messages.NOT_ENOUGH_PLACE.toString());
			return;
		}

		tradeItem(playerWhoClicked, amount);
		
	}
	
	public boolean isSpaceInPlayerInventory(Player player) {
		return player.getInventory().firstEmpty() == -1 ? false : true;
	}

	public boolean isSpaceForItems(Player player, int amount) {
		int freeSlots = 0;
		for(ItemStack stack : player.getInventory()) {
			if(stack == null) {
				freeSlots++;
			}
		}

		return freeSlots >= amount ? true : false;
	}
	
	public boolean isSpaceToTrade(Player player, int amount) {

		if(isSpaceInPlayerInventory(player) && isSpaceForItems(player, amount)) {
			return true;
		}

		return false;
	}
	
	public boolean isItemStackable(ItemStack item) {
		return item.getMaxStackSize() != 1 ? true : false;
	}
	
	public void setItemsInInventory(Player player, int amount, ItemStack item) {
		int compteur = 0;
		for(ItemStack stack : player.getInventory()) {
			if(stack == null) {
				stack = item;
			}
			
			if(compteur == amount) {
				break;
			}
			
			compteur++;
		}
	} 
	
	public void tradeItem(Player player, int amount) { 

		Material itemMaterial = shopItemStack.getType();
		
		String shopSaleType = shopData.getShopSaleType();
		
		if(shopStock < amount) {
			player.sendMessage("§cCe shop n'a plus assez d'item en stock !");
			return;
		}
		
		ItemStack itemToTrade = new ItemStack(itemMaterial, amount); 
		
		if(shopSaleType.equalsIgnoreCase("sale")) {
			
			saleProcessing(player, itemToTrade, amount);
			return;
			
		} 
		
		purchaseProcessing(player, itemToTrade, amount);

	}
	
	public void saleProcessing(Player player, ItemStack itemToTrade, int amountOfItem) {
		
		UUID playerUUID = player.getUniqueId();
		int shopSellingPrice = shopData.getSellingPrice(shopOwner_UUID);
		int shopMoneyAmount = shopData.getMoney(shopOwner_UUID);
		
		if(!player.getInventory().containsAtLeast(itemToTrade, amountOfItem)) { 
			player.sendMessage("§cVous n'avez pas assez d'item !");
			return;
		}

		double totalTransactionPrice =  shopSellingPrice * amountOfItem;
		
		if(shopMoneyAmount < totalTransactionPrice) {
			player.sendMessage("§cCe shop n'a plus assez d'argent en stock, il ne peut donc pas acheter vos items !");
			return;
		}
		
		shopData.setStock(shopOwner_UUID, shopStock + amountOfItem);
		shopData.setMoney(shopOwner_UUID, (int) Math.round(shopMoneyAmount - totalTransactionPrice)); 
		
		EconomyData.addMoney(playerUUID, totalTransactionPrice); 
		
		player.getInventory().removeItem(itemToTrade); 
		
		player.sendMessage("§eVous avez vendu §6" + totalTransactionPrice + " §eeuros de marchandise.");
		
	}
	
	public void purchaseProcessing(Player player, ItemStack itemToTrade, int amountOfItem) {
		
		UUID playerUUID = player.getUniqueId();
		int shopPurchasePrice = shopData.getPurchasePrice(shopOwner_UUID);
		int shopMoneyAmount = shopData.getMoney(shopOwner_UUID);
		
		if(!(EconomyData.getBalance(playerUUID) > shopPurchasePrice * amountOfItem && amountOfItem > 0)) {
			player.sendMessage("§8[§l§cAnthopia§r§8] §cVous n'avez pas assez d'argent pour acheter cette marchandise");
			return; 
		}
		
		if(!isSpaceForItems(player, amountOfItem)) {
			player.sendMessage("§8[§l§cAnthopia§r§8] §cIl n'y a pas assez de place dans votre inventaire.");
			return;
		} 
		
		double totalTransactionPrice =  shopPurchasePrice * amountOfItem;
		
		shopData.setStock(shopOwner_UUID, shopStock - amountOfItem);
		shopData.setMoney(shopOwner_UUID, (int) Math.round(shopMoneyAmount + (totalTransactionPrice)));
		
		EconomyData.removeMoney(playerUUID, totalTransactionPrice);
		
		player.sendMessage("§eVous avez acheté pour §6" + totalTransactionPrice + " §eeuros de marchandise.");
		
		if(isItemStackable(itemToTrade)) {
			setItemsInInventory(player, amountOfItem, itemToTrade); 
			return;
		}
		
		player.getInventory().addItem(itemToTrade); 
		
	}
	
}
