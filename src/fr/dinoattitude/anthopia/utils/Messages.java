package fr.dinoattitude.anthopia.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

/** An enum for requesting strings from the message file
 * @author Dinoattitude
 * @version 2.4.3
 */
public enum Messages {
	
	PLUGIN_NAME("plugin-name", "�8[�l�cAnthopia�r�8]"),
	UNKNOWN_PLAYER("unknown-player", "�cCe joueur n'existe pas."),
	INCORRECT_COMMAND("incorrect-command", "�cCommande Incorrecte : "),
	INCOMPLETE_COMMAND("incomplete-command", "�cCommande Incompl�te : "),
	NOT_ENOUGH_MONEY("not-enough-money", "�cVous n'avez pas assez d'argent."),
	NOT_ENOUGH_CB_MONEY("not-enough-cb-money", "�cVous n'avez pas assez d'argent dans votre compte bancaire."),
	INCORRECT_ARGS("not-enough-args", "�cLe nombre d'arguments sp�cifi�s est incorrect."),
	NO_PERMS("no-permissions", "�cVous n'avez pas les permissions necessaires pour effectuer cette action."),
	NO_GUILD("no-guild", "�cVous n'apartenez � aucune guilde"),
	GUILD_NOT_FOUND("guild-not-found", "�cLa guilde sp�cifi� en param�tre n'existe pas."),
	ERR_NOT_FOUND("err-not-found", "�Anthopia pas trouv� de r�sultat pour cette requ�te."),
	INCOMPLETE_COMMAND_COORD("incomplete-command-coords", "�cVous avez oubli� une coordonn�e."),
	INCORRECT_COMMAND_COORD("incorrect-command-coords", "�cVous avez rentr� une mauvaise coordonn�e."),
	INVALIDE_NAME("invalide_name", "�cVous avez rentr� un nom d�j� existant."),
	DISABLE_CMD("disable_name", "�cCette commande est d�sactiv� temporairement."),
	TEST("test", "�eCeci est un test de fonctionnement des messages."),
	NOT_ENOUGH_PLACE("not_enough_place", "�cVous n'avez pas assez de place dans votre inventaire.");
	
    private String path;
    private String def;
    private static YamlConfiguration MESSAGES;
    
    /** Message enum constructor
     * @param path The string path
     * @param start The default string
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
        return ChatColor.translateAlternateColorCodes('&', MESSAGES.getString(PLUGIN_NAME.path, def)) + " " 
            + ChatColor.translateAlternateColorCodes('&', MESSAGES.getString(this.path, def));
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
