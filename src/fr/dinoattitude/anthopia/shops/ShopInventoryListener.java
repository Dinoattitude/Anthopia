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

	public final String ATH_SHOP_HEADER = "§8[§cAnthopia Shop§8] ",
	SELLER_SHOP_NAME = ATH_SHOP_HEADER + "Vendeur",
	BUYER_SHOP_NAME = ATH_SHOP_HEADER + "Acheteur",
	CREATE_SHOP_NAME = ATH_SHOP_HEADER + "Créer un shop ?",
	CHANGE_ITEM_SHOP_NAME = ATH_SHOP_HEADER + "Change item ?",
	MODIFICATION_SHOP_NAME = ATH_SHOP_HEADER + "Modification",
	DELETE_SHOP_NAME = ATH_SHOP_HEADER + "Supprimer ?";

	public ItemStack currentItem;
	public String playerUUID;
	public int shopStock;
	public int rate;

	@EventHandler
	public void onClick(InventoryClickEvent event) {

		Player player = (Player) event.getWhoClicked();
		currentItem = event.getCurrentItem();
		ShopInfo shopInfo = new ShopInfo(player, player.getWorld(), ChestShop.getChestLocX(), ChestShop.getChestLocY(), ChestShop.getChestLocZ());

		playerUUID = shopInfo.getPlayer().getUniqueId().toString();
		shopStock = shopInfo.getStock(playerUUID);
		rate = shopInfo.getRateValue();

	    if(currentItem == null) {
	      return;
	    }

		//Seller Inventory
	    if(isNammedShop(event, SELLER_SHOP_NAME)) {

			event.setCancelled(true);
			sellerInventory(event, player, shopInfo);
			return;

		}

		//Buyer Inventory
	    if (isNammedShop(event, BUYER_SHOP_NAME)) {

			event.setCancelled(true);
			buyerInventory(currentItem.getType(), shopInfo, player, currentItem);
			return;

		}

		//Create shop
	    if (isNammedShop(event, CREATE_SHOP_NAME)) {
			event.setCancelled(true);

			if(currentItem.getType() == Material.RED_STAINED_GLASS_PANE) {
				player.closeInventory();
				return;
			}

			if(currentItem.getType() != Material.GREEN_STAINED_GLASS_PANE) {
				pi.put(1, currentItem);
				event.getInventory().setItem(4, currentItem);
				player.updateInventory();
				return;
			}

			ItemStack sellItem = pi.get(1);

			if(sellItem == null) {

				player.closeInventory();
				player.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous devez choisir un item à vendre !");
				return;

			}

			shopInfo.createShop(ChestShop.getCarpet(), pi.get(1));
			pi.clear();
			player.closeInventory();
			return;

		}

		//Change shop's item
	    if(isNammedShop(event, CHANGE_ITEM_SHOP_NAME)) {

			event.setCancelled(true);
			inventoryChangeItemShop(player, shopInfo, currentItem);
			event.setCancelled(true);
			return;

		}

		//Modification of item's prices
	    if(isNammedShop(event, MODIFICATION_SHOP_NAME)) {

			String playerUUID = shopInfo.getPlayer().getUniqueId().toString();

			Material clickedMaterial = currentItem.getType();

		    switch(clickedMaterial) {
		      case RED_STAINED_GLASS_PANE: {
		        player.closeInventory();
		        shopInfo.getOwnerShop();
		        break;
		      }
		      case LIGHT_BLUE_STAINED_GLASS: { lowerPrice(shopInfo, playerUUID, 10); break; }
		      case BLUE_STAINED_GLASS: { lowerPrice(shopInfo, playerUUID, 1); break; }
		      case ORANGE_STAINED_GLASS: { increasePrice(shopInfo, playerUUID, 1); break; }
		      case RED_STAINED_GLASS: { increasePrice(shopInfo, playerUUID, 10); break; }
		      case BLACK_STAINED_GLASS: { resetPrice(shopInfo); break; }
			  default: { break; }
		    }

		    event.setCancelled(true);

			int price = shopInfo.getShopSaleType().equalsIgnoreCase("purchase")
			? shopInfo.getPurchasePrice(playerUUID)
			: shopInfo.getSellingPrice(playerUUID);

			ItemStack unitPriceItemStack = Utilities.getItem(Material.PAPER,
			1,
			"Prix unitaire :",
			"" + price,
			null);

			event.getInventory().setItem(0, unitPriceItemStack);
			player.updateInventory();
			return;
		}

		//Delete shop
	    if(isNammedShop(event, DELETE_SHOP_NAME)) {
			event.setCancelled(true);

			if(currentItem.getType() == Material.RED_STAINED_GLASS_PANE) {
				player.closeInventory();
				event.setCancelled(true);
				return;
			}

			if(currentItem.getType() != Material.GREEN_STAINED_GLASS_PANE) {
				return;
			}

			if(!(shopInfo.getStock(playerUUID) == 0 && shopInfo.getMoney(playerUUID) == 0)) {
				player.sendMessage(Messages.PLUGIN_NAME.toString() + "§cRetirez vos stocks et votre monnaie du shop avant de le détruire.");
				return;
			}

			shopInfo.deletePlayerShopData();
			Location max = new Location(shopInfo.getWorld(),
			shopInfo.getChestLocX(),
			shopInfo.getChestLocY() + 1,
			shopInfo.getChestLocZ());

			Location min = new Location(shopInfo.getWorld(),
			shopInfo.getChestLocX() + 1,
			shopInfo.getChestLocY() - 1,
			shopInfo.getChestLocZ() + 1);

			shopInfo.deleteItem(max, min);

			//The carpet is at the same coordinates as max.
			max.getBlock().setType(Material.AIR);
			event.setCancelled(true);
			player.closeInventory();

		}
	}

	public boolean isNammedShop(InventoryClickEvent event, String shopName) {
	   return event.getView().getTitle().equalsIgnoreCase(shopName);
	}

	public void scaleSelector(ShopInfo shopInfo) {
	    if(shopInfo.getRate() == 6) {
	      shopInfo.setRate(2);
	      return;
	    }

	    shopInfo.setRate(shopInfo.getRate() + 1);
	}

    public void withdrawOrDepositItemstack(ItemStack stack, ShopInfo shopInfo, Player player, String player_UUID, InventoryClickEvent event, ItemStack changedStack, boolean status) {

      final int totalItemNumber = status == true ? shopStock - rate : shopStock + rate;

      ItemStack depositItemStack = Utilities.getItem(Material.CHEST_MINECART,
      1,
      "§6Déposer : " + stack.getType().toString(),
      "En stock: " + shopInfo.getStock(player_UUID),
      null);

      ItemStack withdrawItemStack = Utilities.getItem(Material.HOPPER_MINECART,
      1,
      "§6Retirer : " + stack.getType().toString(),
      "En stock: " + shopInfo.getStock(player_UUID),
      null);

      changedStack.setAmount(rate);
      player.getInventory().removeItem(changedStack);
      shopInfo.setStock(player_UUID, totalItemNumber);

      event.getInventory().setItem(0, depositItemStack);
      event.getInventory().setItem(1, withdrawItemStack);

      player.updateInventory();

    }

    public ItemStack getSaleOrPurchaseItemStack(boolean status, ShopInfo shopInfo) {

      final String[] sale = {"§6Stock d'argent", "Montant: ", "Cliquez pour ajouter 100 euros"},
      purchase = {"§6Montant récolté", "Total: ", "Cliquez pour récolter"};

      String[] choosenTab = new String[3];

      choosenTab = status == true ? sale : purchase;

      ItemStack saleOrPurchaseItemStack = Utilities.getItem(Material.PAPER,
      1,
      choosenTab[1],
      choosenTab[2] + shopInfo.getMoney(playerUUID),
      choosenTab[3]);

      return saleOrPurchaseItemStack;

    }

	  public void sellerInventory(InventoryClickEvent event, Player player, ShopInfo shopInfo) {
	    ItemStack stack = shopInfo.getItem(playerUUID);
	    ItemStack changedStack = stack;

	    //Selector for stocking items
	    if(currentItem.getType() == Material.MINECART) {
	      scaleSelector(shopInfo);

	      ItemStack itemStack = Utilities.getItem(Material.MINECART,
	      shopInfo.getRateValue(),
	      "§6Taux de change",
	      "Clic droit pour augmenter",
	      "Clic gauche pour diminuer");

	      event.getInventory().setItem(2, itemStack);

	      player.updateInventory();
	    }

		//Deposit player's items and put them into shop stock
	    if(currentItem.getType() == Material.CHEST_MINECART) {

	      if(stack == null) {
	        return;
	      }

	      if(!player.getInventory().containsAtLeast(stack, shopInfo.getRateValue())) {
	        player.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous n'avez pas le nombre d'items nécessaire.");
	        return;
	      }

	      if(rate >= (1728 - shopStock)) {
	        player.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVotre stock est plein !");
	        return;
	      }

	      withdrawOrDepositItemstack(stack, shopInfo, player, playerUUID, event, changedStack, true);

	    }

		//Withdraw stock items and put it into player's inventory
	    if(currentItem.getType() == Material.HOPPER_MINECART) {

	      if(rate >= shopStock) {
	        player.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVous n'avez pas ce nombre d'item disponible dans votre stock !");
	        return;
	      }

	      if(!isSpaceInPlayerInventory(player)) {
	        player.sendMessage(Messages.NOT_ENOUGH_PLACE.toString());
	        return;
	      }

	      withdrawOrDepositItemstack(stack, shopInfo, player, playerUUID, event, changedStack, false);

	    }

		//Modify the shop's prices
	    if(currentItem.getType() == Material.COMPARATOR) {

	      player.closeInventory();
	      shopInfo.setRate(1);
	      shopInfo.inventoryChangePriceShop();
	      event.setCancelled(true);

	    }

		//Withdraw or deposit money into the shop account
	    if(currentItem.getType() == Material.PAPER) {

	      updateMoneyInShop(shopInfo, player, playerUUID);

	      ItemStack moneyAmountItemStack = Utilities.getItem(Material.PAPER,
	      1,
	      "§6Stock d'argent",
	      "Montant: " + shopInfo.getMoney(playerUUID),
	      "Cliquez pour ajouter 100 euros");

	      event.getInventory().setItem(6, moneyAmountItemStack);
	      player.updateInventory();

	    }

		//Change shop's status to "purchase" or "sale"
	    if(currentItem.getType() == Material.OAK_SIGN) {

	      if(shopInfo.getShopSaleType().equalsIgnoreCase("purchase")) {
	        shopInfo.setShopSaleType(playerUUID,"sale");
	        event.getInventory().setItem(6, getSaleOrPurchaseItemStack(true, shopInfo));
	      }
	      else{
	        shopInfo.setShopSaleType(playerUUID,"purchase");
	        event.getInventory().setItem(6, getSaleOrPurchaseItemStack(false, shopInfo));
	      }

				ItemStack shopState = Utilities.getItem(Material.OAK_SIGN,
				1,
				"§6Etat du shop :",
				"§3" + shopInfo.getShopSaleType(),
				"Cliquez pour changer");

	      event.getInventory().setItem(8, shopState);
	      player.updateInventory();

	    }

			//Change the sold item in the shop
			if(event.getSlot() != 4) {
				return;
			}

			if(shopStock != 0) {
				player.sendMessage(Messages.PLUGIN_NAME.toString() + "§cVeuillez vider votre stock avant de changer d'item.");
				player.closeInventory();
				return;
			}

	    player.closeInventory();
	    shopInfo.setRate(1);
	    shopInfo.inventoryChangeItemShop();
	    event.setCancelled(true);

	  }

	/** Check if the material can go to the shop and take the block name.
	 * @param item The ItemStack who is verified.
	 * @return The block name of the itemStack
	 */
	public String getBlockNameMaterial(ItemStack item) {
		if(item.hasItemMeta())
			return item.getItemMeta().getDisplayName();
		else
			return item.getType().toString();
	}

	/** Increase the purchase or sale price.
	 * @param shopInfo The shopInfo.
	 * @param player_UUID The player UUID.
	 * @param amount The amount who will be set.
	 */
	public void increasePrice(ShopInfo shopInfo, String player_UUID, int amount) {
		int PP = shopInfo.getPurchasePrice(player_UUID);
		int SP = shopInfo.getSellingPrice(player_UUID);
		if(shopInfo.getShopSaleType().equals("purchase"))
			shopInfo.setPurchasePrice(PP + amount);
		else
			shopInfo.setSellingPrice(SP + amount);
	}

	/** Lower the purchase or sale price.
	 * @param shopInfo The shopInfo.
	 * @param player_UUID The player UUID.
	 * @param amount The amount who will be set.
	 */
	public void lowerPrice(ShopInfo shopInfo, String player_UUID, int amount) {
		int PP = shopInfo.getPurchasePrice(player_UUID);
		int SP = shopInfo.getSellingPrice(player_UUID);
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
	 */
	public void resetPrice(ShopInfo shopInfo) {
		if(shopInfo.getShopSaleType().equals("purchase"))
			shopInfo.setPurchasePrice(0);
		else
			shopInfo.setSellingPrice(0);
	}

	public void inventoryChangeItemShop(Player player, ShopInfo shopInfo, ItemStack itemStack) {
		shopInfo.setItem(itemStack);
		shopInfo.replaceItem(itemStack.getType());
		player.closeInventory();
		shopInfo.getOwnerShop();
	}

	public void buyerInventory(Material material, ShopInfo shopInfo, Player player, ItemStack itemStack) {

		if(material == Material.RED_STAINED_GLASS_PANE) {
			player.closeInventory();
			return;
		}

		int amount = itemStack.getAmount();

		if(amount == 0) {
			return;
		}

		if(!isSpaceToTrade(player, amount)) {
			player.sendMessage(Messages.NOT_ENOUGH_PLACE.toString());
			return;
		}

		shopInfo.tradeItem(amount);

	}

	public void updateMoneyInShop(ShopInfo shopInfo, Player player, String player_UUID) {

		if(shopInfo.getShopSaleType().equalsIgnoreCase("purchase") && shopInfo.getMoney(player_UUID) > 0) {
			EconomyData.addMoney(player.getUniqueId(), shopInfo.getMoney(player_UUID));
			shopInfo.setMoney(player_UUID, 0);
			return;
		}

		EconomyData.removeMoney(player.getUniqueId(), 100);
		shopInfo.setMoney(player_UUID, shopInfo.getMoney(player_UUID) + 100);

	}

	public boolean isSpaceToTrade(Player player, int amount) {

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
