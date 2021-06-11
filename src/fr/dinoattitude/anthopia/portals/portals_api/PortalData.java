package fr.dinoattitude.anthopia.portals.portals_api;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PortalData {
	

	private static FileConfiguration config;
	private File file;
	private final String TAG = "Portals.";
	

	/** Return the configuration of the Portals file
	 * @return The Portals configuration
	 */
	public final FileConfiguration getConfig() {
		initConfigPortal();
		return config;
	}
	
	
	/** Initializes the configuration of the Portals file */
	private void initConfigPortal() {
		File f = new File("plugins/Anthopia/Portals");
		if(!f.exists()) f.mkdirs();
		file = new File(f, "Portals.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			}catch(IOException ioe){ ioe.printStackTrace();}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	/** Sets the Portals data. Used to create a Portal.
	 * @param guildName The Portal name
	 * @param owerUuid The owner UUID
	 */
	public void setPortalData(String portalName, Integer coordX, Integer coordY, Integer coordZ) {
		getConfig();
		try {
			config.set(TAG + portalName + ".X",coordX);
			config.set(TAG + portalName + ".Y",coordY);
			config.set(TAG + portalName + ".Z",coordZ);
			config.set(TAG + portalName + ".Link","");
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPortalName(String portalName) {
		getConfig();
		return config.getString(TAG + portalName);
	}
	

}
