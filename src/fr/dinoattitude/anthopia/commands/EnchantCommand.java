package fr.dinoattitude.anthopia.commands;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class EnchantCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender,Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) return true;
		
		final Player player = (Player) sender;
		
		ItemStack mending = new ItemStack(Material.ENCHANTED_BOOK, 1);
		
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta)mending.getItemMeta();
		meta.addStoredEnchant(Enchantment.MENDING, 1, true);
		mending.setItemMeta(meta);
		
		if(player.getInventory().getItemInMainHand().getType().equals(Material.BOW) && player.getInventory().containsAtLeast(mending, 1)) {
			ItemStack arc = player.getInventory().getItemInMainHand();
			String ndla = "arc";
			if(!arc.getItemMeta().hasDisplayName()) ndla = arc.getItemMeta().getDisplayName();
			
			player.getInventory().removeItem(new ItemStack[] { mending });
			arc.addUnsafeEnchantment(Enchantment.MENDING, 1);
			
			player.getInventory().remove(arc);
			player.getInventory().addItem(new ItemStack[] { arc });
			
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 10.0F, 1.0F);
			player.sendMessage("§aVotre §o§b" + ndla + " à été enchanté !");
			return true;
		}
		
		player.sendMessage("§8[§l§cAnthopia§r§8] §cVous devez avoir un livre Mending dans votre inventaire et un arc dans votre main !");
		return false;
	}
	

}
