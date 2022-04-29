package fr.dinoattitude.anthopia.bourse2;

import java.util.UUID;

/** Interface for the salary data api.
 * @author Dinoattitude
 * @since 2.4.4
 * @version 2.4.4
*/
public interface ISalaryData {
	
	/**
	 * Gets the player salary from the salary map
	 * @param uuid The player uuid
	 * @return The player salary
	 */
	public Double getPlayerSalary(UUID uuid);
	
	/**
	 * Reset the salary data of a specified player
	 * @param uuid The player uuid
	 */
	public void resetPlayerSalary(UUID uuid);
	
	/**
	 * Save the salary data to a file
	 */
	public void savePlayerSalaryInFile();
	
	/**
	 * Retrieve the salary data from a file
	 */
	public void loadPlayerSalaryFromFile();
	
	/**
	 * Check if a player is in the salary map
	 * @param uuid The player uuid
	 * @return Boolean
	 */
	public boolean isPlayerInMap(UUID uuid);
	
}
