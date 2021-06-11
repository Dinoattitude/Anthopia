package fr.dinoattitude.anthopia;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import biz.princeps.landlord.api.ILandLord;
import fr.dinoattitude.anthopia.bourse.EconomyCommand;
import fr.dinoattitude.anthopia.bourse.listeners.BlocksListener;
import fr.dinoattitude.anthopia.bourse.listeners.ClaimListener;
import fr.dinoattitude.anthopia.guild.GuildCommand;
import fr.dinoattitude.anthopia.guild.GuildListener;
import fr.dinoattitude.anthopia.utils.Messages;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Main extends JavaPlugin{
	
	public static Main INSTANCE;

	@SuppressWarnings("unused")
	private ILandLord llAPI;
	private static Economy economy = null;
	private static Permission permission = null;
	
	public static YamlConfiguration LANG;
	public static File LANG_FILE;
	
	@Override
	public void onEnable() {
		
		INSTANCE = this;
		
		//Controls before runnable
		checkLandLordApi();
		loadLang();
		
		//PluginManager registering events
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new BlocksListener(), this);
		pm.registerEvents(new ClaimListener(), this);
		pm.registerEvents(new GuildListener(), this);
		
		//Setting executors to commands
		this.getCommand("money").setExecutor(new EconomyCommand());
		this.getCommand("guild").setExecutor(new GuildCommand());
	}
	
	@Override
	public void onDisable() {
		//On met rien dedans pour le moment (Pas besoin)
	}
	
	//############################################################
	//# +------------------------------------------------------+ #
	//# |                   Configuration                      | #
	//# +------------------------------------------------------+ #
	//############################################################
	
	//Gets the main instance for runnable for example
	public static Main getInstance() {
		return INSTANCE;
	}
	
	// +------------------------------------------------------+ 
	// |                      Landlord                        | 
	// +------------------------------------------------------+ 
	
	//Check if landlord is working
	public void checkLandLordApi() {
		try {
            this.llAPI = (ILandLord) getServer().getPluginManager().getPlugin("Landlord");
        } catch (NoClassDefFoundError ex) {
            getLogger().warning("Landlord missing!");
            return;
        }
	}
	
	// +------------------------------------------------------+ 
	// |                       Vault                          | 
	// +------------------------------------------------------+ 
	
	//Check if Vault's economy is working
	public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
	
	//Check if Vault's permissions are working
	public boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
        return permission != null;
    }
	
	//Gets the Vault's economy
	public static Economy getEconomy() {
        return economy;
    }
	
	//Gets the Vault's permissions
	public static Permission getPermissions() {
        return permission;
    }
	
	// +------------------------------------------------------+ 
	// |                     Lang File                        | 
	// +------------------------------------------------------+ 
	
	/** Load the lang.yml file. */
	public void loadLang() {
	    File lang = new File(getDataFolder(), "lang.yml");
	    if (!lang.exists()) {
	        try {
	            getDataFolder().mkdir();
	            lang.createNewFile();
	            InputStream defConfigStream = this.getResource("lang.yml");
	            if (defConfigStream != null) {
	                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(lang);
	                defConfig.save(lang);
	                Messages.setFile(defConfig);
	            }
	        } catch(IOException e) {
	            e.printStackTrace(); 
	            getLogger().severe("[Anthopia] Couldn't create language file.");
	            getLogger().severe("[Anthopia] This is a fatal error. Now disabling");
	            this.setEnabled(false); 
	        }
	    }
	    YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
	    for(Messages item:Messages.values()) {
	        if (conf.getString(item.getPath()) == null) {
	            conf.set(item.getPath(), item.getDefault());
	        }
	    }
	    Messages.setFile(conf);
	    Main.LANG = conf;
	    Main.LANG_FILE = lang;
	    try {
	        conf.save(getLangFile());
	    } catch(IOException e) {
	    	getLogger().log(Level.WARNING, "Anthopia: Failed to save lang.yml.");
	    	getLogger().log(Level.WARNING, "Anthopia: Report this stack trace to Dinoattitude.");
	        e.printStackTrace();
	    }
	}
	
	/**
	* Gets the lang.yml config.
	* @return The lang.yml config.
	*/
	public YamlConfiguration getLang() {
	    return LANG;
	}
	 
	/**
	* Get the lang.yml file.
	* @return The lang.yml file.
	*/
	public File getLangFile() {
	    return LANG_FILE;
	}


}
