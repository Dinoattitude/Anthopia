package fr.dinoattitude.anthopia.shops.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.dinoattitude.anthopia.shops.gui.IGUI_Inventory;

/** Click listener for interactions within shops GUI.
 * @author Dinoattitude
 * @since 2.3.6
 * @version 2.4.3
*/
public class ShopInventoryListener implements Listener {
	
	@EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
		
        if(event.getInventory().getHolder() instanceof IGUI_Inventory) {
        	
        	if(event.isShiftClick() || event.getClick() == ClickType.DOUBLE_CLICK) {
    			event.setCancelled(true);
    			return;
    		}
        	
        	event.setCancelled(true);
        	
        	IGUI_Inventory gui = (IGUI_Inventory) event.getInventory().getHolder();
            gui.onGUIClick((Player) event.getWhoClicked(), event.getCurrentItem(), event.getRawSlot());
            
        }  
    }

}
