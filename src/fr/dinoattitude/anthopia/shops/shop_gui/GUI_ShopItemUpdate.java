package fr.dinoattitude.anthopia.shops.shop_gui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.dinoattitude.anthopia.shops.ShopName;
import fr.dinoattitude.anthopia.shops.ShopUtils;
import fr.dinoattitude.anthopia.shops.shop_api.ShopData;

/** GUI constructor and action manager for the shop item upgrade .
 * @author Dinoattitude
 * @since 3.4.3
 * @version 3.4.3
*/
public class GUI_ShopItemUpdate implements IGUI_Inventory {
	
	private UUID shopOwner_UUID;
	private ShopData shopData;
	
	public GUI_ShopItemUpdate(Player owner, ShopData gui_shopData) {
		shopOwner_UUID = owner.getUniqueId();
		shopData = gui_shopData;
	}

	@Override
	public Inventory getInventory() {
		
		Inventory GUI = Bukkit.createInventory(this, 9, ShopName.UPDATE_ITEM.toString());
		
		ItemStack item = shopData.getItem(shopOwner_UUID);
		GUI.setItem(4, item);
		
		return GUI;
	}

	@Override
	public void onGUIClick(Player playerWhoClicked, ItemStack clickedItem, int slot) {
		
		ShopUtils shopUtils = new ShopUtils(shopData, shopOwner_UUID);

		shopData.setItem(shopOwner_UUID, clickedItem);
		
		shopUtils.replaceItem(clickedItem.getType());
		playerWhoClicked.closeInventory();
		
		GUI_ShopOwner gui = new GUI_ShopOwner(playerWhoClicked, shopData);
    	playerWhoClicked.openInventory(gui.getInventory());
		
	}
	
	

}
