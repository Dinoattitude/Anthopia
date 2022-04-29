package fr.dinoattitude.anthopia.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DatabaseManager {

	private String url;
	private String host;
	private String database;
	private String username;
	private String password;
	private static Connection connection;
	
	private FileConfiguration config;
	private File file;
	
	private final String FILE_DIRECTORY = "plugins/Anthopia";
	private final String FILE_NAME = "database.yml";
	private final String KEY_WORD = "Database.";
	
	public final FileConfiguration getConfig() {
		initConfig();
		return config;
	}
	
	private void initConfig() {
		File f = new File(FILE_DIRECTORY);
		if(!f.exists()) f.mkdirs();
		file = new File(f, FILE_NAME);
		if(!file.exists()) {
			try {
				file.createNewFile();
				
			} catch(IOException ioe) { ioe.printStackTrace();}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public DatabaseManager() {

		getConfig();
		
		this.url = config.getString(KEY_WORD + "url");
		this.host = config.getString(KEY_WORD + "host");
		this.database = config.getString(KEY_WORD + "database");
		this.username = config.getString(KEY_WORD + "username");
		this.password = config.getString(KEY_WORD + "password");

	}
	
	public static Connection getConnexion() {
		return connection;
	}
	
	public void connexion() {
		if(!isOnline()) {
			try {
				connection = DriverManager.getConnection(this.url + this.host + "/" + this.database, this.username, this.password);
				System.out.println("§a[DatabaseManager] Connexion réussie");
				return;
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void deconnexion() {
		if(isOnline()) {
			try {
				connection.close();
				System.out.println("§a[DatabaseManager] Déconnexion réussie");
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isOnline() {
		try {
			if((connection == null) || (connection.isClosed())) {
				return false;
			}
			return true;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
