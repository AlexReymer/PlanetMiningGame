public class Structure 
{
	private int locX;
	private int locY;
	private int level;
	private int type;
	private int oreType;
	
	/**
	 * Creates a new structure object
	 * @param type The type of the structure
	 * @param locX The X coordinate of the structure on the board
	 * @param locY The Y coordinate of the structure on the board
	 * @param level The level of the structure on the board
	 * @param oreType The type of ore the structure has if it has one at all
	 */
	public Structure(int type, int locX, int locY, int level, int oreType) 
	{
		this.locX = locX;
		this.locY = locY;
		this.level = level;
		this.type = type;
		this.oreType = oreType;
	}
	
	/**
	 * Checks to see if the structure is of a certain type
	 * @param type The type to be checked
	 * @return If the structure is that type
	 */ 
	public boolean isType(int type)
	{
		if (this.type == type)
			return true;
		return false;
	}
	
	/**
	 * If the structure has an ore of a type
	 * @param oreType The type of ore
	 * @return If the structure has that ore type
	 */
	public boolean hasOreOfType(int oreType)
	{
		if (this.oreType == oreType)
			return true;
		return false;
	}
	
	/**
	 * Gets the column of the structure
	 * @return The column of the structure
	 */
	public int getX()
	{
		return locX;
	}
	
	/**
	 * Gets the row of the structure
	 * @return The row of the structure
	 */
	public int getY()
	{
		return locY;
	}
	
	/**
	 * Gets the level of the structure
	 * @return The level of the structure
	 */
	public int getLevel()
	{
		return level;
	}

}
