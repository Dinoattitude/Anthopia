package fr.dinoattitude.anthopia.shops.shop_gui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.dinoattitude.anthopia.shops.ShopName;
import fr.dinoattitude.anthopia.shops.ShopUtils;
import fr.dinoattitude.anthopia.shops.shop_api.ShopData;
import fr.dinoattitude.anthopia.utils.Messages;
import fr.dinoattitude.anthopia.utils.Utilities;

/** GUI constructor and action manager for the shop deletion.
 * @author Dinoattitude
 * @since 3.4.3
 * @version 3.4.3
*/
public class GUI_ShopDeletion implements IGUI_Inventory {
	
	private ShopData shopData;
	
	public GUI_ShopDeletion(ShopData gui_shopData) {
		shopData = gui_shopData;
	}

	@Override
	public Inventory getInventory() {
		
		Inventory GUI = Bukkit.createInventory(this, 9, ShopName.DELETE.toString());
		GUI.setItem(1, Utilities.getItem(Material.RED_STAINED_GLASS_PANE, 1, "§cNon", null, null));
		GUI.setItem(7, Utilities.getItem(Material.GREEN_STAINED_GLASS_PANE, 1, "§2Oui", null, null));
		
		return GUI;
	}

	@Override
	public void onGUIClick(Player playerWhoClicked, ItemStack clickedItem, int slot) {
		
		UUID shopOwner_UUID = playerWhoClicked.getUniqueId();
		
		if(clickedItem.getType() == Material.RED_STAINED_GLASS_PANE) {
			playerWhoClicked.closeInventory();
			return;
		}

		if(clickedItem.getType() != Material.GREEN_STAINED_GLASS_PANE) {
			return;
		}

		if(!(shopData.getStock(shopOwner_UUID) == 0 && shopData.getMoney(shopOwner_UUID) == 0)) {
			playerWhoClicked.sendMessage(Messages.PLUGIN_NAME.toString() + "§cRetirez vos stocks et votre monnaie du shop avant de le détruire.");
			return;
		}

		shopData.deletePlayerShopData(shopOwner_UUID);

		ShopUtils shopUtils = new ShopUtils(shopData, shopOwner_UUID);
		
		shopUtils.deleteItem();

		//The carpet is at the same coordinates as max.
		shopUtils.getMaximumLocation().getBlock().setType(Material.AIR);
		playerWhoClicked.closeInventory();
		
	}

}
