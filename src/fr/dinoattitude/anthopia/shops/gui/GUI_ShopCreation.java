package fr.dinoattitude.anthopia.shops.gui;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.dinoattitude.anthopia.shops.ShopName;
import fr.dinoattitude.anthopia.shops.ShopUtils;
import fr.dinoattitude.anthopia.shops.api.ShopData;
import fr.dinoattitude.anthopia.utils.Messages;
import fr.dinoattitude.anthopia.utils.Utilities;

/** GUI constructor and action manager for the shop creation .
 * @author Dinoattitude
 * @since 3.4.3
 * @version 3.4.3
*/
public class GUI_ShopCreation implements IGUI_Inventory {
	
	private ShopData shopData;
	private Location carpetLocation;
	
	public GUI_ShopCreation(Location carpet, ShopData gui_shopData) {
		carpetLocation = carpet;
		shopData = gui_shopData;
	}

	@Override
	public Inventory getInventory() {
		
		Inventory GUI = Bukkit.createInventory(this, 9, ShopName.CREATE.toString());
		GUI.setItem(1, Utilities.getItem(Material.RED_STAINED_GLASS_PANE, 1, "§cNon", null, null));
		GUI.setItem(7, Utilities.getItem(Material.GREEN_STAINED_GLASS_PANE, 1, "§2Oui", null, null));
		
		return GUI;
	}

	@Override
	public void onGUIClick(Player playerWhoClicked, ItemStack clickedItem, int slot) {
		
		if(clickedItem.getType() == Material.RED_STAINED_GLASS_PANE) {
			playerWhoClicked.closeInventory();
			return;
		}

		if(clickedItem.getType() != Material.GREEN_STAINED_GLASS_PANE) {
			playerWhoClicked.getOpenInventory().setItem(4, clickedItem);
			playerWhoClicked.updateInventory();
			return;
		}

		ItemStack itemForSale = playerWhoClicked.getOpenInventory().getItem(4);

		if(itemForSale == null) {

			playerWhoClicked.closeInventory();
			playerWhoClicked.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous devez choisir un item à vendre !");
			return;

		}
		
		ShopUtils shopUtils = new ShopUtils(shopData, playerWhoClicked.getUniqueId());

		shopUtils.createShop(playerWhoClicked, carpetLocation, itemForSale);
		playerWhoClicked.closeInventory();
		
	}

}
