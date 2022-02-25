package fr.dinoattitude.anthopia.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class QuoteData {
	
	private FileConfiguration config;
	private File file;
	private final String FILE_FOLDER_NAME = "Quotes"; 
	private final String TAG = "Quotes.";
	
	private List<String> quotesListOfAllPlayers = new ArrayList<String>();
	private static HashMap<UUID, List<String>> quotes = new HashMap<UUID, List<String>>();

	
	public final FileConfiguration getQuoteConfig() {
		initQuoteConfig();
		return config;
	}
	
	private void initQuoteConfig() {
		File f = new File("plugins/Anthopia/" + FILE_FOLDER_NAME);
		if(!f.exists()) f.mkdirs();
		file = new File(f, FILE_FOLDER_NAME + ".yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
				
			}catch(IOException ioe) { ioe.printStackTrace();}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void addQuoteToList(String quote) {
		this.quotesListOfAllPlayers.add(quote);
	}
	
	public List<String> getQuoteList() {
		return this.quotesListOfAllPlayers;
	}
	
	public void addQuoteToHashmapList(UUID uuid, String quote) {
		List<String> quoteList = quotes.get(uuid) == null ? new ArrayList<String>() : quotes.get(uuid);
		quoteList.add(quote);
		quotes.put(uuid, quoteList);
	}
	
	/** 
	 * Get a random quote 
	 * @return the random quote
	 */
	public String getRandomQuote() {
		initFormatedQuotes();
		List<String> listValues = getQuoteList();
		int randomIndex = new Random().nextInt(listValues.size());
		String randomQuote = listValues.get(randomIndex);
		return randomQuote;
	}
	
	/** 
	 * Get a random quote sorted by an uuid
	 * @param playerUuid the player's uuid
	 * @return the random quote
	 */
	public String getRandomQuoteByPlayer(UUID playerUuid) {
		initFormatedQuotes();
		List<String> valuesList = new ArrayList<String>();
		for (Entry<UUID, List<String>> entry : quotes.entrySet()) {
		    if(entry.getKey() == playerUuid) {
		    	for (String quote : entry.getValue()) {
		    		valuesList.add(quote);
				}
		    }
		    	
		}
		
		int randomIndex = new Random().nextInt(valuesList.size());
		String randomQuote = valuesList.get(randomIndex);
		return randomQuote;
	}
	
	/**
	 * Initialize the hashmap containing the quotes
	 */
	public void initFormatedQuotes(){
		getQuoteConfig();
		
		Integer quoteNumber = getQuoteNumber();
		if(quoteNumber == null) {
			return;
		}
		
		String quote = null;
		String quoteMessage = null;
		String quotePlayer = null;
		String quoteDate = null;
		
		for(int i = 1; i <= quoteNumber; i++) {
			quoteMessage = config.getString(TAG + "quote-" + i + ".message");
			quotePlayer = config.getString(TAG + "quote-" + i + ".player");
			quoteDate = config.getString(TAG + "quote-" + i + ".date");

			quote = "§3Le " + quoteDate + " par §9" + Bukkit.getOfflinePlayer(UUID.fromString(quotePlayer)).getName() + "§7: §f" + quoteMessage;
			addQuoteToList(quote);
			addQuoteToHashmapList(UUID.fromString(quotePlayer), quote);
		}
	}
	
	/** 
	 * Get the number of existing quotes
	 * @return the quote number
	 */
	public Integer getQuoteNumber() {
		getQuoteConfig();
		Integer quoteNumber = config.getInt(TAG + "number");
		return quoteNumber;
	}
	
	/** 
	 * Set the number of existing quotes
	 */
	public void setQuoteNumber(int number) {
		getQuoteConfig();
		try {
			config.set(TAG + "number", number);
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set the new quote in the yaml file
	 * @param playerUuid the player's uuid
	 * @param message the quoted message
	 * @param date the date 
	 */
	public void setQuote(String playerUuid, String message, String date) {
		getQuoteConfig();
		
		Integer quoteNumber = getQuoteNumber();
		
		if(quoteNumber == null) {
			quoteNumber = 1;
		}

		quoteNumber++;
		
		try {
			
			config.set(TAG + "quote-" + quoteNumber + ".message", message);
			config.set(TAG + "quote-" + quoteNumber + ".player", playerUuid);
			config.set(TAG + "quote-" + quoteNumber + ".date", date);
			config.save(file);
			
			addQuoteToHashmapList(UUID.fromString(playerUuid), message);
			setQuoteNumber(quoteNumber);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check if the message send is already a quote
	 * @param message the message to verify
	 * @return false / true
	 */
	public boolean isNewQuote(String message) {
		List<String> valuesList = getQuoteList();
		for(String quote : valuesList) {
			if(quote.equalsIgnoreCase(message)) return false;
		}
		return true;
	}
	
	/**
	 * Check if the quotes hashmap is empty
	 * @return true or false
	 */
	public boolean isQuoteEmpty() {
		return quotes.isEmpty();
	}
	
}
