package fr.dinoattitude.anthopia;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.dinoattitude.anthopia.bourse.EconomyCommand;
import fr.dinoattitude.anthopia.bourse.SalaryAttr;
import fr.dinoattitude.anthopia.bourse.economy_api.BourseData;
import fr.dinoattitude.anthopia.bourse.listeners.BlocksListener;
import fr.dinoattitude.anthopia.commands.CacCommand;
import fr.dinoattitude.anthopia.commands.CoordsCommand;
import fr.dinoattitude.anthopia.commands.DiscordCommand;
import fr.dinoattitude.anthopia.commands.DonCommand;
import fr.dinoattitude.anthopia.commands.EnchantCommand;
import fr.dinoattitude.anthopia.commands.HelpCommand;
import fr.dinoattitude.anthopia.commands.PayCommand;
import fr.dinoattitude.anthopia.commands.QuoteCommand;
import fr.dinoattitude.anthopia.commands.RankCommand;
import fr.dinoattitude.anthopia.commands.RestartCommand;
import fr.dinoattitude.anthopia.commands.RulesCommand;
import fr.dinoattitude.anthopia.commands.SkipNightCommand;
import fr.dinoattitude.anthopia.commands.TestCommand;
import fr.dinoattitude.anthopia.guild.GuildCommand;
import fr.dinoattitude.anthopia.guild.GuildCommandTabCompletion;
import fr.dinoattitude.anthopia.guild.GuildListener;
import fr.dinoattitude.anthopia.listeners.PlayerChatListener;
import fr.dinoattitude.anthopia.listeners.PlayerDeathListener;
import fr.dinoattitude.anthopia.listeners.PlayerJoinListener;
import fr.dinoattitude.anthopia.shops.listeners.ShopInteractionListener;
import fr.dinoattitude.anthopia.shops.listeners.ShopInventoryListener;
import fr.dinoattitude.anthopia.shops.listeners.ShopProtectionListener;
import fr.dinoattitude.anthopia.utils.Messages;
import fr.dinoattitude.anthopia.utils.QuoteData;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Main extends JavaPlugin{

	public static Main INSTANCE;	

	private static Logger log;
	
	private static Economy economy = null;
	private static Permission permission = null;
	
	public static YamlConfiguration LANG;
	public static File LANG_FILE;

	@Override
	public void onEnable() {

		INSTANCE = this;
		
		log = this.getLogger();
		
		QuoteData quoteData = new QuoteData();

		//Controls before runnable
		checkVaultApi();
		loadLang();
		
		//Bourse
		try {
			BourseData.loadBlockPrice();
			quoteData.initFormatedQuotes();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//PluginManager registering events
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new BlocksListener(), this);
		pm.registerEvents(new GuildListener(), this);
		pm.registerEvents(new PlayerJoinListener(), this);
		pm.registerEvents(new PlayerChatListener(), this);
		pm.registerEvents(new PlayerDeathListener(), this);
		pm.registerEvents(new ShopInteractionListener(), this);
		pm.registerEvents(new ShopInventoryListener(), this);
		pm.registerEvents(new ShopProtectionListener(), this);

		//Setting executors to commands
		this.getCommand("money").setExecutor(new EconomyCommand());
		this.getCommand("guild").setExecutor(new GuildCommand());
		//this.getCommand("portal").setExecutor(new PortalCommand());
		this.getCommand("rank").setExecutor(new RankCommand());
		this.getCommand("help").setExecutor(new HelpCommand());
		this.getCommand("pay").setExecutor(new PayCommand());
		this.getCommand("cac40").setExecutor(new CacCommand());
		this.getCommand("guild").setTabCompleter(new GuildCommandTabCompletion());
		this.getCommand("coords").setExecutor(new CoordsCommand());
		this.getCommand("restartauto").setExecutor(new RestartCommand());
		this.getCommand("restartath").setExecutor(new RestartCommand());
		this.getCommand("quote").setExecutor(new QuoteCommand());
		this.getCommand("rules").setExecutor(new RulesCommand());
		this.getCommand("enchant").setExecutor(new EnchantCommand());
		this.getCommand("don").setExecutor(new DonCommand());
		this.getCommand("discord").setExecutor(new DiscordCommand());
		this.getCommand("skipnight").setExecutor(new SkipNightCommand());
		this.getCommand("test").setExecutor(new TestCommand());


		new BukkitRunnable()
		{
		    @Override
		    public void run()
		    {
		    	SalaryAttr salaryAttr = new SalaryAttr();
				salaryAttr.setExpAndSalary();
		    }
		}.runTaskTimer(this, 2 * 15 * 60 * 20L, 2 * 15 * 60 * 20L); //20L = 1s : 15 * 60 * 20L = 15min -> Actuellement 30 minutes
	}

	@Override
	public void onDisable() {
		//Dino : ThreadSafe update in PlayerQuitListener, no longer mandatory
		//Sauvegarde des comptes bancaires des joueurs
		//EconomyData.saveAllPlayersEconomy();
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
	// |                       Vault                          |
	// +------------------------------------------------------+
	
	public void checkVaultApi() {
		if(!setupEconomy()) {
			log.severe("Disabled due to no Vault dependency found!" + getDescription().getName().toString());
			getServer().getPluginManager().disablePlugin(INSTANCE);
			return;
		}
		setupPermissions();
	}

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
	                defConfigStream.close();
	            }
	        } catch(IOException e) {
	            e.printStackTrace();
	            setSevereLog("Couldn't create language file.");
	            setSevereLog("This is a fatal error. Now disabling");
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
	    	setWarningLog("Failed to save lang.yml.");
	    	setWarningLog("Report this stack trace to Dinoattitude.");
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
	
	// +------------------------------------------------------+
	// |                       Logger                         |
	// +------------------------------------------------------+
	
	public static void setInfoLog(String message) {
		log.info(message);
	}
	
	public static void setWarningLog(String message) {
		log.warning(message);
	}
	
	public static void setSevereLog(String message) {
		log.severe(message);
	}

}
