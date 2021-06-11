package fr.dinoattitude.anthopia.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

/** An enum for requesting strings from the message file
 * @author Dinoattitude
 * @version 3.0.0
 */
public enum Messages {
	
	PLUGIN_NAME("plugin-name", "§8[§l§cAnthopia§r§8] "),
	UNKNOWN_PLAYER("unknown-player", "§cCe joueur n'existe pas."),
	INCORRECT_COMMAND("incorrect-command", "§cCommande Incorrecte : "),
	INCOMPLETE_COMMAND("incomplete-command", "§cCommande Incomplète : "),
	NOT_ENOUGH_MONEY("not-enough-money", "§cVous n'avez pas assez d'argent."),
	NOT_ENOUGH_CB_MONEY("not-enough-cb-money", "§cVous n'avez pas assez d'argent dans votre compte bancaire."),
	NO_PERMS("no-permissions", "§cVous n'avez pas les permissions necessaires pour effectuer cette action."),
	INCOMPLETE_COMMAND_COORD("incomplete-command-coords", "§cVous avez oublié une coordonnée."),
	INCORRECT_COMMAND_COORD("incorrect-command-coords", "§cVous avez rentré une mauvaise coordonnée."),
	INVALIDE_NAME("invalide_name", "§cVous avez rentré un nom déjà existant.");
	
    private String path;
    private String def;
    private static YamlConfiguration MESSAGES;
    
    /** Message enum constructor
     * @param path The string path
     * @param start The defeult string
     */
    Messages(String path, String start) {
        this.path = path;
        this.def = start;
    }
    
    /** Set the {@code YamlConfiguration} to use.
    * @param config The config to set.
    */
    public static void setFile(YamlConfiguration config) {
        MESSAGES = config;
    }
    
    @Override
    public String toString() {
        if (this == PLUGIN_NAME)
            return ChatColor.translateAlternateColorCodes('&', MESSAGES.getString(this.path, def)) + " ";
        return ChatColor.translateAlternateColorCodes('&', MESSAGES.getString(this.path, def));
    }
    
    /** Get the default value of the path.
    * @return The default value of the path.
    */
    public String getDefault() {
        return this.def;
    }
 
    /** Get the path to the string.
    * @return The path to the string.
    */
    public String getPath() {
        return this.path;
    }

}
