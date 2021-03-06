package fr.dinoattitude.anthopia.shops.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/** GUI Interface for easily launching multiples instances of GUI inventories.
 * @author Dinoattitude
 * @since 2.4.3
 * @version 2.4.3
*/
public interface IGUI_Inventory extends InventoryHolder {
	
	public void onGUIClick(Player playerWhoClicked, ItemStack clickedItem, int slot);
	
}
