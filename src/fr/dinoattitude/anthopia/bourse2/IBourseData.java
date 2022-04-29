package fr.dinoattitude.anthopia.bourse2;

import org.bukkit.block.Block;

/** Interface for the bourse data api.
 * @author Dinoattitude
 * @since 2.4.4
 * @version 2.4.4
*/
public interface IBourseData {
	
	/** Get the block value from the Bourse map
	 * @param block The block whose value is sought
	 * @return The value of the block
	 */
	public Double getBlockValue(Block block);
	
	/** Set the block value to the Bourse map
	 * @param block The block to update
	 * @param value The new value
	 */
	public void setBlockValue(Block block, Double value);
	
	/**
	 * Save the Bourse data to a database
	 */
	public void saveBourseDataInDatabase();
	
	/**
	 * Save the Bourse data to a file
	 */
	public void saveBourseDataInFile();
	
	/**
	 * Retrieve the Bourse data from a file
	 */
	public void loadBourseDataFromFile();
	
	/** Check if a block is in the Bourse
	 * @param block The block to search
	 * @return true | false
	 */
	public boolean isBlockInBourse(Block block);

}
