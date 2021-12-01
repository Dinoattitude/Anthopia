package fr.dinoattitude.anthopia.shops;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.dinoattitude.anthopia.bourse.economy_api.EconomyData;
import fr.dinoattitude.anthopia.shops.shop_api.ShopInfo;
import fr.dinoattitude.anthopia.utils.Messages;
import fr.dinoattitude.anthopia.utils.Utilities;


public class ShopInventoryListener implements Listener{
	
	//HashMap for shortly stocking the item name while waiting for the change to take place
	//Can't really do otherwise
	public static HashMap<Integer, ItemStack> pi = new HashMap<Integer, ItemStack>();

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		
		Player player = (Player) event.getWhoClicked();
		ItemStack current = event.getCurrentItem();
		ShopInfo shopInfo = new ShopInfo(player, player.getWorld(), ChestShop.getChestLocX(), ChestShop.getChestLocY(), ChestShop.getChestLocZ());
		
		ItemStack sellItem;
		String playerUuid = shopInfo.getPlayer().getUniqueId().toString();
		int stock = shopInfo.getStock(playerUuid);
		int rate = shopInfo.getRateValue();
		
		
		if(current != null) { //We check that there is an item
			//Seller Inventory
			if(event.getView().getTitle().equalsIgnoreCase("§8[§cAnthopia Shop§8] Vendeur")) {
				event.setCancelled(true); //We cancelled the event so that the player cannot take an item 
				ItemStack stack = shopInfo.getItem(playerUuid);
				ItemStack stackChanged = stack;
				//We check for the Material to do an action
				if(current.getType() == Material.MINECART) { //Selector for stocking items
					if(shopInfo.getRate() == 6)
						shopInfo.setRate(2);
					else 
						shopInfo.setRate(shopInfo.getRate() + 1);
					event.getInventory().setItem(2, Utilities.getItem(Material.MINECART, shopInfo.getRateValue(), "§6Taux de change", "Clic droit pour augmenter", "Clic gauche pour diminuer"));
					player.updateInventory();
				}
				if(current.getType() == Material.CHEST_MINECART) { //Deposit of the items in the stock
					if(stack != null) {
						if(player.getInventory().containsAtLeast(stack, shopInfo.getRateValue())) {
							if(rate <= (1728 - stock)) {
								stackChanged.setAmount(rate);
								player.getInventory().removeItem(stackChanged);
								shopInfo.setStock(playerUuid, stock + rate); 
								event.getInventory().setItem(0, Utilities.getItem(Material.CHEST_MINECART, 1, "§6Déposer : " + stack.getType().toString(), "En stock: " + shopInfo.getStock(playerUuid), null));
								event.getInventory().setItem(1, Utilities.getItem(Material.HOPPER_MINECART, 1, "§6Retirer : " + stack.getType().toString(), "En stock: " + shopInfo.getStock(playerUuid), null));
								player.updateInventory();
							}
							else
								player.sendMessage("§cVotre stock est plein !");
						}
					}
				}
				if(current.getType() == Material.HOPPER_MINECART) { //Deposit of the items from the stock to the player inventory
					if(rate <= stock) {
						if(isSpaceInPlayerInventory(player)) {
							stackChanged.setAmount(rate);
							player.getInventory().addItem(stackChanged);
							shopInfo.setStock(playerUuid, stock - rate);
							event.getInventory().setItem(0, Utilities.getItem(Material.CHEST_MINECART, 1, "§6Déposer : " + stack.getType().toString(), "En stock: " + shopInfo.getStock(playerUuid), null));
							event.getInventory().setItem(1, Utilities.getItem(Material.HOPPER_MINECART, 1, "§6Retirer : " + stack.getType().toString(), "En stock: " + shopInfo.getStock(playerUuid), null));
							player.updateInventory();
						}
						else {
							player.sendMessage(Messages.PLUGIN_NAME + " " + Messages.NOT_ENOUGH_PLACE);
						}
					}
					else
						player.sendMessage("§cVous n'avez pas ce nombre d'item disponible dans votre stock !");
				}
				if(current.getType() == Material.COMPARATOR) { //Modify the shop
					player.closeInventory();
					shopInfo.setRate(1);
					shopInfo.inventoryChangePriceShop();
					event.setCancelled(true);
				}
				if(current.getType() == Material.PAPER) { //Taking or deposit money in the shop account
					updateMoneyInShop(shopInfo, player, playerUuid);
					event.getInventory().setItem(6, Utilities.getItem(Material.PAPER, 1, "§6Stock d'argent", "Montant: " + shopInfo.getMoney(playerUuid), "Cliquez pour ajouter 100 euros"));
					player.updateInventory();
					
				}
				if(current.getType() == Material.OAK_SIGN) { //Changing the shop to "purchase" or "sale"
					if(shopInfo.getShopSaleType().equalsIgnoreCase("purchase")) {
						shopInfo.setShopSaleType(playerUuid,"sale");
						event.getInventory().setItem(6, Utilities.getItem(Material.PAPER, 1, "§6Stock d'argent", "Montant: " + shopInfo.getMoney(playerUuid), "Cliquez pour ajouter 100 euros"));	
					}
					else{
						shopInfo.setShopSaleType(playerUuid,"purchase");
						event.getInventory().setItem(6, Utilities.getItem(Material.PAPER, 1, "§6Montant récolté", "Total: " + shopInfo.getMoney(playerUuid), "Cliquez pour récolter"));	
					}
					event.getInventory().setItem(8, Utilities.getItem(Material.OAK_SIGN, 1, "§6Etat du shop :", "§3" + shopInfo.getShopSaleType(), "Cliquez pour changer"));
					player.updateInventory();
				}
				if(event.getSlot() == 4) { //Change the item sold in the shop
					if(stock == 0) {
						player.closeInventory();
						shopInfo.setRate(1);
						shopInfo.inventoryChangeItemShop();
						event.setCancelled(true);
					}
					else {
						player.sendMessage("§cVeuillez vider votre stock avant de changer d'item");
						player.closeInventory();
					}
				}
			}
			//Buyer Inventory
			else if(event.getView().getTitle().equalsIgnoreCase("§8[§cAnthopia Shop§8] Acheteur")) {
				
				event.setCancelled(true);
				buyerInventory(current.getType(), shopInfo, player, current);
				
			}
			//Create Shop Inventory
			else if(event.getView().getTitle().equalsIgnoreCase("§8[§cAnthopia Shop§8] Créer un shop ?")) {
				event.setCancelled(true);
				
				if(current.getType() == Material.RED_STAINED_GLASS_PANE) {
					player.closeInventory();
				}
				else if(current.getType() == Material.GREEN_STAINED_GLASS_PANE) {
					sellItem = pi.get(1);
					if(sellItem == null) {
						player.closeInventory();
						player.sendMessage("§cVous devez choisir un item à vendre !");
					}
					else {
						shopInfo.createShop(ChestShop.getCarpet(), pi.get(1));
						pi.clear();
						player.closeInventory();
					}
				}
				else {
					pi.put(1, current);
					event.getInventory().setItem(4, current);
					player.updateInventory();
				}
			}
			//Switch Item Inventory
			else if(event.getView().getTitle().equalsIgnoreCase("§8[§cAnthopia Shop§8] Change item ?")) {
				event.setCancelled(true); //Stopping the player from getting the item 
				inventoryChangeItemShop(player, shopInfo, current);
				event.setCancelled(true);
			}
			//Modification of Price Inventory
			else if(event.getView().getTitle().equalsIgnoreCase("§8[§cAnthopia Shop§8] Modification")) {
				String playerUUID = shopInfo.getPlayer().getUniqueId().toString();
				
				if(current.getType() == Material.RED_STAINED_GLASS_PANE) {
					player.closeInventory();
					shopInfo.getOwnerShop();
					event.setCancelled(true);
				}
				if(current.getType() == Material.LIGHT_BLUE_STAINED_GLASS) {
					lowerPrice(shopInfo, playerUUID, 10);
					event.setCancelled(true);
				}
				if(current.getType() == Material.BLUE_STAINED_GLASS) {
					lowerPrice(shopInfo, playerUUID, 1);
					event.setCancelled(true);
				}
				if(current.getType() == Material.ORANGE_STAINED_GLASS) {
					increasePrice(shopInfo, playerUUID, 1);
					event.setCancelled(true);
				}
				if(current.getType() == Material.RED_STAINED_GLASS) {
					increasePrice(shopInfo, playerUUID, 10);
					event.setCancelled(true);
				}
				if(current.getType() == Material.BLACK_STAINED_GLASS) {
					resetPrice(shopInfo, playerUUID);
					event.setCancelled(true);
				}
				if(shopInfo.getShopSaleType().equalsIgnoreCase("purchase"))
					event.getInventory().setItem(0, Utilities.getItem(Material.PAPER, 1, "Prix unitaire :", "" + shopInfo.getPurchasePrice(playerUUID), null));
				else
					event.getInventory().setItem(0, Utilities.getItem(Material.PAPER, 1, "Prix unitaire :", "" + shopInfo.getSellingPrice(playerUUID), null));
				player.updateInventory();
			}
			//Delete Inventory
			else if(event.getView().getTitle().equalsIgnoreCase("§8[§cAnthopia Shop§8] Supprimer ?")) {
				event.setCancelled(true);
				if(current.getType() == Material.RED_STAINED_GLASS_PANE) {
					player.closeInventory();
					event.setCancelled(true);
				}
				else if(current.getType() == Material.GREEN_STAINED_GLASS_PANE) {
					if(shopInfo.getStock(playerUuid) == 0 && shopInfo.getMoney(playerUuid) == 0) {
						shopInfo.deletePlayerShopData();
						Location max = new Location(shopInfo.getWorld(), shopInfo.getChestLocX(), shopInfo.getChestLocY()+1, shopInfo.getChestLocZ());
						Location min = new Location(shopInfo.getWorld(), shopInfo.getChestLocX()+1, shopInfo.getChestLocY()-1, shopInfo.getChestLocZ()+1);
						shopInfo.deleteItem(max, min);
						Location carpet = new Location(shopInfo.getWorld(), shopInfo.getChestLocX(), shopInfo.getChestLocY() + 1, shopInfo.getChestLocZ());
						System.out.println(carpet);
						carpet.getBlock().setType(Material.AIR);
						event.setCancelled(true);
						player.closeInventory(); 
					}
					else
						player.sendMessage("§cRetirez vos stocks et votre monnaie du shop avant de le détruire.");
				}	
			}
		}
	}

	/** Check if the material can go to the shop and take the block name.
	 * @param current The ItemStack who is verified.
	 * @return The block name of the itemStack
	 */
	public String getBlockNameMaterial(ItemStack current) {
		if(current.hasItemMeta()) 
			return current.getItemMeta().getDisplayName();
		else 
			return current.getType().toString();
	}
	
	/** Increase the purchase or sale price.
	 * @param shopInfo The shopInfo.
	 * @param playerUUID The player UUID.
	 * @param amount The amount who will be set.
	 */
	public void increasePrice(ShopInfo shopInfo, String playerUUID, int amount) {
		int PP = shopInfo.getPurchasePrice(playerUUID);
		int SP = shopInfo.getSellingPrice(playerUUID);
		if(shopInfo.getShopSaleType().equals("purchase"))
			shopInfo.setPurchasePrice(PP + amount);
		else
			shopInfo.setSellingPrice(SP + amount);
	}
	
	/** Lower the purchase or sale price.
	 * @param shopInfo The shopInfo.
	 * @param playerUUID The player UUID.
	 * @param amount The amount who will be set.
	 */
	public void lowerPrice(ShopInfo shopInfo, String playerUUID, int amount) {
		int PP = shopInfo.getPurchasePrice(playerUUID);
		int SP = shopInfo.getSellingPrice(playerUUID);
		if(shopInfo.getShopSaleType().equals("purchase")) {
			if(PP - amount < 0)
				shopInfo.setPurchasePrice(0);
			else
				shopInfo.setPurchasePrice(PP - amount);
		}
		else {
			if(SP - amount < 0)
				shopInfo.setSellingPrice(0);
			else
				shopInfo.setSellingPrice(SP - amount);
		}
	}
	
	/** Reset the purchase or sale price.
	 * @param shopInfo The shopInfo.
	 * @param playerUUID The player UUID.
	 */
	public void resetPrice(ShopInfo shopInfo, String playerUUID) {
		if(shopInfo.getShopSaleType().equals("purchase"))
			shopInfo.setPurchasePrice(0);
		else
			shopInfo.setSellingPrice(0);
	}
	
	/* Nouvelles Fonctions */
	
	
	public void inventoryChangeItemShop(Player player, ShopInfo shopInfo, ItemStack itemStack) {
		shopInfo.setItem(itemStack);
		shopInfo.replaceItem(itemStack.getType());
		player.closeInventory();
		shopInfo.getOwnerShop();
	}
	
	public void buyerInventory(Material material, ShopInfo shopInfo, Player player, ItemStack itemStack) {
		int amount = 0;
		
		switch(material) {
			case RED_STAINED_GLASS_PANE : { 
				player.closeInventory(); break; 
			}
			case GOLD_BLOCK : {
				amount = 64;
				break;
			}
			case GOLD_INGOT : {
				amount = 32;
				break;
			}
			case GOLD_NUGGET : {
				amount = 16;
				break;
			}
			case IRON_INGOT : {
				amount = 8;
				break;
			}
			case IRON_NUGGET : {
				amount = 1;
				break;
			}
			default: break;
		}
		
		if(amount != 0) {
			if(isSpaceToTrade(player, itemStack, amount)) {
				shopInfo.tradeItem(amount);
			}
			else {
				player.sendMessage(Messages.PLUGIN_NAME + " " + Messages.NOT_ENOUGH_PLACE);
			}
		}
		
	}
	
	public void updateMoneyInShop(ShopInfo shopInfo, Player player, String playerUuid) {
		if(shopInfo.getShopSaleType().equalsIgnoreCase("purchase") && shopInfo.getMoney(playerUuid) > 0) {
			EconomyData.setBalance(player.getUniqueId(), EconomyData.getBalance(player.getUniqueId()) + shopInfo.getMoney(playerUuid));
			EconomyData.addMoney(player.getUniqueId(), shopInfo.getMoney(playerUuid));
			shopInfo.setMoney(playerUuid, 0);
		}
		else {
			EconomyData.setBalance(player.getUniqueId(), EconomyData.getBalance(player.getUniqueId()) - shopInfo.getMoney(playerUuid));
			EconomyData.removeMoney(player.getUniqueId(), 100);
			shopInfo.setMoney(playerUuid, shopInfo.getMoney(playerUuid) + 100);
		}
		
	}
	
	public boolean isSpaceToTrade(Player player, ItemStack stack, int amount) {

		if(isSpaceInPlayerInventory(player) && isSpaceForItems(player, amount)) {
			return true;
		}
		
		return false;
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
	
	
}

