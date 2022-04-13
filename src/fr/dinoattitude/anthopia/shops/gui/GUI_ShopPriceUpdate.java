package fr.dinoattitude.anthopia.shops.gui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.dinoattitude.anthopia.shops.ShopName;
import fr.dinoattitude.anthopia.shops.api.ShopData;
import fr.dinoattitude.anthopia.utils.Utilities;

/** GUI constructor and action manager for the shop price update .
 * @author Dinoattitude
 * @since 2.4.3
 * @version 2.4.3
*/
public class GUI_ShopPriceUpdate implements IGUI_Inventory {
	
	private UUID shopOwner_UUID;
	private ShopData shopData;
	
	public GUI_ShopPriceUpdate(Player owner, ShopData gui_shopData) {
		shopOwner_UUID = owner.getUniqueId();
		shopData = gui_shopData;
	}

	@Override
	public Inventory getInventory() {
		Inventory GUI = Bukkit.createInventory(this, 9, ShopName.UPDATE_PRICE.toString());
		
		if(shopData.getShopSaleType().equals("purchase"))
			GUI.setItem(0, Utilities.getItem(Material.PAPER, 1, "Prix unitaire :", "" + shopData.getPurchasePrice(shopOwner_UUID), null));
		else
			GUI.setItem(0, Utilities.getItem(Material.PAPER, 1, "Prix unitaire :", "" + shopData.getSellingPrice(shopOwner_UUID), null));
		
		GUI.setItem(2, Utilities.getItem(Material.LIGHT_BLUE_STAINED_GLASS, 1, "§3-10 euros", null, null));
		GUI.setItem(3, Utilities.getItem(Material.BLUE_STAINED_GLASS, 1, "§3-1 euro", null, null));
		GUI.setItem(4, Utilities.getItem(Material.ORANGE_STAINED_GLASS, 1, "§6+1 euro", null, null));
		GUI.setItem(5, Utilities.getItem(Material.RED_STAINED_GLASS, 1, "§6+10 euros", null, null));
		GUI.setItem(7, Utilities.getItem(Material.BLACK_STAINED_GLASS, 1, "§8Réinitialiser", null, null));
		GUI.setItem(8, Utilities.getItem(Material.RED_STAINED_GLASS_PANE, 1, "§cRetour", null, null));
		
		return GUI;
	}

	@Override
	public void onGUIClick(Player playerWhoClicked, ItemStack clickedItem, int slot) {
		
		Material clickedMaterial = clickedItem.getType();

	    switch(clickedMaterial) {
	      case RED_STAINED_GLASS_PANE: {
	    	playerWhoClicked.closeInventory();
	    	
	    	GUI_ShopOwner gui = new GUI_ShopOwner(playerWhoClicked, shopData);
	    	playerWhoClicked.openInventory(gui.getInventory());

	        break;
	      }
	      case LIGHT_BLUE_STAINED_GLASS: { lowerPrice(10); break; }
	      case BLUE_STAINED_GLASS: { lowerPrice(1); break; }
	      case ORANGE_STAINED_GLASS: { increasePrice(1); break; }
	      case RED_STAINED_GLASS: { increasePrice(10); break; }
	      case BLACK_STAINED_GLASS: { resetPrice(); break; }
		  default: { break; }
	    }

		int price = shopData.getShopSaleType().equalsIgnoreCase("purchase")
		? shopData.getPurchasePrice(shopOwner_UUID)
		: shopData.getSellingPrice(shopOwner_UUID);

		ItemStack unitPriceItemStack = Utilities.getItem(Material.PAPER,
		1,
		"Prix unitaire :",
		"" + price,
		null);

		playerWhoClicked.getOpenInventory().setItem(0, unitPriceItemStack);
		playerWhoClicked.updateInventory();
		
	}
	
	public void increasePrice(int amount) {
		
		int PurchasePrice = shopData.getPurchasePrice(shopOwner_UUID);
		int SellingPrice = shopData.getSellingPrice(shopOwner_UUID);
		
		if(shopData.getShopSaleType().equals("purchase")) {
			shopData.setPurchasePrice(shopOwner_UUID, PurchasePrice + amount);
			return;
		}

		shopData.setSellingPrice(shopOwner_UUID, SellingPrice + amount);
	}

	public void lowerPrice(int amount) {
		
		int PurchasePrice = shopData.getPurchasePrice(shopOwner_UUID) - amount;
		int SellingPrice = shopData.getSellingPrice(shopOwner_UUID) - amount;
		
		if(shopData.getShopSaleType().equals("purchase")) {
			if(PurchasePrice < 0) {
				shopData.setPurchasePrice(shopOwner_UUID, 0);
				return;
			}

			shopData.setPurchasePrice(shopOwner_UUID, PurchasePrice);
			return;
		}
		
		if(SellingPrice < 0) {
			shopData.setSellingPrice(shopOwner_UUID, 0);
			return;
		}

		shopData.setSellingPrice(shopOwner_UUID, SellingPrice);
	}
	
	public void resetPrice() {
		
		if(shopData.getShopSaleType().equals("purchase")) {
			shopData.setPurchasePrice(shopOwner_UUID, 0);
			return;
		}	
		
		shopData.setSellingPrice(shopOwner_UUID, 0);
	}

}
