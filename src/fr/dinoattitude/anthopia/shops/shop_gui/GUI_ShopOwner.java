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

/** GUI constructor and action manager for the owner's shop .
 * @author Dinoattitude
 * @since 3.4.3
 * @version 3.4.3
*/
public class GUI_ShopOwner implements IGUI_Inventory {
	
	private final Integer INTERNAL_WITHDRAW_DEPOSIT_SHOPBANK_AMOUNT = 100;
	
	private UUID shopOwner_UUID;
	private ShopData shopData;
	private ItemStack shopItemStack;
	
	private int shopStock;
	private int selectedRateValue;
	private int rateCounter;
	
	public GUI_ShopOwner(Player owner, ShopData gui_shopData) {
		shopOwner_UUID = owner.getUniqueId();
		shopData = gui_shopData;
		shopItemStack = shopData.getItem(shopOwner_UUID);
		shopStock = shopData.getStock(shopOwner_UUID);
		selectedRateValue = getRateValue();
		rateCounter = 1;
	}
	
	@Override
	public Inventory getInventory() {
		
		Inventory GUI = Bukkit.createInventory(this, 9, ShopName.OWNER.toString());
		
		String itemName = shopItemStack.getType().toString();
		String saleType = shopData.getShopSaleType();
		
		ItemStack saleOrPurchaseItemStack = getSaleOrPurchaseItemStack();
		
		GUI.setItem(0, Utilities.getItem(Material.CHEST_MINECART, 1, "§6Déposer : " + itemName, "En stock: " + shopStock, null));
		GUI.setItem(1, Utilities.getItem(Material.HOPPER_MINECART, 1, "§6Retirer : " + itemName, "En stock: " + shopStock, null));
		GUI.setItem(2, Utilities.getItem(Material.MINECART, selectedRateValue, "§6Taux de change", "Clic droit pour augmenter", "Clic gauche pour diminuer"));
		GUI.setItem(4, shopItemStack);
		GUI.setItem(6, saleOrPurchaseItemStack);
		GUI.setItem(7, Utilities.getItem(Material.COMPARATOR, 1, "§6Modifier le shop", null, null));
		GUI.setItem(8, Utilities.getItem(Material.OAK_SIGN, 1, "§6Etat du shop :", "§3" + saleType, "Cliquez pour changer"));
		
		return GUI;
	}

	@Override
	public void onGUIClick(Player playerWhoClicked, ItemStack clickedItem, int slot) {

		//Selector for stocking items
	    if(clickedItem.getType() == Material.MINECART) {
	    	
	    	scaleSelector();
	    	
	    	ItemStack itemStack = Utilities.getItem(Material.MINECART,
	    			getRateValue(),
	    			"§6Taux de change",
	    			"Clic droit pour augmenter",
	    			"Clic gauche pour diminuer");
	    	
	    	playerWhoClicked.getOpenInventory().setItem(2, itemStack);
	    	
	    	playerWhoClicked.updateInventory();
	    }
	    
	    //Deposit player's items and put them into shop stock
	    if(clickedItem.getType() == Material.CHEST_MINECART) {
	    	
	    	if(shopItemStack == null) {
	    		return;
	    	}
	    	
	    	if(!playerWhoClicked.getInventory().containsAtLeast(shopItemStack, selectedRateValue)) {
	    		playerWhoClicked.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous n'avez pas le nombre d'items nécessaire.");
	    		return;
	    	}
	    	
	    	if(selectedRateValue >= (1728 - shopStock)) {
	    		playerWhoClicked.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVotre stock est plein !");
	    		return;
	    	}
	    	
	    	withdrawOrDepositItemstack(playerWhoClicked, false);
	    	
	    }
	    
	    //Withdraw stock items and put it into player's inventory
	    if(clickedItem.getType() == Material.HOPPER_MINECART) {
	    	
	    	if(selectedRateValue > shopStock) {
	    		playerWhoClicked.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous n'avez pas ce nombre d'item disponible dans votre stock !");
	    		return;
	    	}
	    	
	    	if(!isSpaceInPlayerInventory(playerWhoClicked)) {
	    		playerWhoClicked.sendMessage(Messages.NOT_ENOUGH_PLACE.toString());
	    		return;
	    	}
	    	
	    	withdrawOrDepositItemstack(playerWhoClicked, true);
	    	
	    }
	    
	    //Modify the shop's prices
	    if(clickedItem.getType() == Material.COMPARATOR) {
	    	
	    	playerWhoClicked.closeInventory();
	    	rateCounter = 1;
	    	
	    	GUI_ShopPriceUpdate gui = new GUI_ShopPriceUpdate(playerWhoClicked, shopData);
	    	playerWhoClicked.openInventory(gui.getInventory());
	    	
	    }
	    
	    //Withdraw or deposit money into the shop account
	    if(clickedItem.getType() == Material.PAPER) {
	    	
	    	updateMoneyInShop(playerWhoClicked);
	    	playerWhoClicked.getOpenInventory().setItem(6, getSaleOrPurchaseItemStack());
	    	playerWhoClicked.updateInventory();
	    	
	    }
	    
	    //Change shop's status to "purchase" or "sale"
	    if(clickedItem.getType() == Material.OAK_SIGN) {
	    	
	    	if(shopData.getShopSaleType().equalsIgnoreCase("purchase")) {
	    		shopData.setShopSaleType(shopOwner_UUID, "sale");
	    	}
	    	else {
	    		shopData.setShopSaleType(shopOwner_UUID, "purchase");
	    	}
	    	
	    	playerWhoClicked.getOpenInventory().setItem(6, getSaleOrPurchaseItemStack());
	    	
	    	ItemStack shopState = Utilities.getItem(Material.OAK_SIGN,
	    			  1,
	    			  "§6Etat du shop :",
	    			  "§3" + shopData.getShopSaleType(),
	    			  "Cliquez pour changer");
	    	
	    	playerWhoClicked.getOpenInventory().setItem(8, shopState);
	    	playerWhoClicked.updateInventory();
	    	
	    }
	    
	    //Change the sold item in the shop
	    if(slot != 4) {
	    	return;
	    }
	    
	    if(shopStock != 0) {
	    	playerWhoClicked.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVeuillez vider votre stock avant de changer d'item.");
	    	playerWhoClicked.closeInventory();
	    	return;
	    }
	    
	    rateCounter = 1;
	    playerWhoClicked.closeInventory();
	    
	    GUI_ShopItemUpdate gui = new GUI_ShopItemUpdate(playerWhoClicked, shopData);
	    playerWhoClicked.openInventory(gui.getInventory());
		
	}
	
	private boolean isSpaceInPlayerInventory(Player player) {
		return player.getInventory().firstEmpty() == -1 ? false : true;
	}
	
	public void scaleSelector() {
	    if(rateCounter == 6) {
	    	rateCounter = 2;
	    	return;
	    }

	    rateCounter++;
	    
	    selectedRateValue = getRateValue();
	}
	
	/** Gets the rate value for the selector.
	 * @return The rate value.
	 */
	public int getRateValue() {
		switch(rateCounter) {
			case 1: return 1;
			case 2: return 8;
			case 3: return 16;
			case 4: return 32;
			case 5: return 64;
			case 6: return 1;
		}
		return 1;
	}
	
	private void withdrawOrDepositItemstack(Player player, boolean status) {
		final int totalItemNumber = status == true 
				? shopStock - selectedRateValue 
						: shopStock + selectedRateValue;
		
		ItemStack tempSaveItemStack = shopItemStack;
		
		shopItemStack.setAmount(selectedRateValue);
		
		if(status) {
			player.getInventory().addItem(shopItemStack);
		}
		else {
			player.getInventory().removeItem(shopItemStack);
		}
		
		shopData.setStock(shopOwner_UUID, totalItemNumber);
		
		shopStock = shopData.getStock(shopOwner_UUID);
		
		ItemStack depositItemStack = Utilities.getItem(Material.CHEST_MINECART,
			      1,
			      "§6Déposer : " + tempSaveItemStack.getType().toString(),
			      "En stock: " + shopStock,
			      null);
		
		ItemStack withdrawItemStack = Utilities.getItem(Material.HOPPER_MINECART,
			      1,
			      "§6Retirer : " + tempSaveItemStack.getType().toString(),
			      "En stock: " + shopStock,
			      null);
		
		player.getOpenInventory().setItem(0, depositItemStack);
		player.getOpenInventory().setItem(1, withdrawItemStack);
		
	    player.updateInventory();
	    
	}
	
	private void updateMoneyInShop(Player player) {
		
		Double playerMoneyAmount = EconomyData.getBalance(shopOwner_UUID);
		int shopInfoMoneyAmount = shopData.getMoney(shopOwner_UUID);
		
		
		if(shopOwner_UUID == null || playerMoneyAmount == null) {
			return;
		}
		
		if(shopData.getShopSaleType().equalsIgnoreCase("purchase") && shopInfoMoneyAmount > 0) {			
			EconomyData.addMoney(shopOwner_UUID, shopInfoMoneyAmount);
			shopData.setMoney(shopOwner_UUID, 0);
			return;
		}
		
		if(playerMoneyAmount < INTERNAL_WITHDRAW_DEPOSIT_SHOPBANK_AMOUNT) {
			player.sendMessage(Messages.NOT_ENOUGH_MONEY.toString());			
			return;
		}
		
		EconomyData.removeMoney(shopOwner_UUID, INTERNAL_WITHDRAW_DEPOSIT_SHOPBANK_AMOUNT);
		shopData.setMoney(shopOwner_UUID, shopData.getMoney(shopOwner_UUID) + INTERNAL_WITHDRAW_DEPOSIT_SHOPBANK_AMOUNT);

	}
	
	private ItemStack getSaleOrPurchaseItemStack() {
		
		final String[] sale = {"§6Stock d'argent", "Montant: ", "Cliquez pour ajouter 100 euros"},
			      purchase = {"§6Montant récolté", "Total: ", "Cliquez pour récolter"};
		
		String[] choosenTab = new String[3];
		
		choosenTab = shopData.getShopSaleType().equalsIgnoreCase("sale") ? sale : purchase;
		
		ItemStack saleOrPurchaseItemStack = Utilities.getItem(Material.PAPER,
			      1,
			      choosenTab[0],
			      choosenTab[1] + shopData.getMoney(shopOwner_UUID),
			      choosenTab[2]);
		
		return saleOrPurchaseItemStack;
		
	}

}
