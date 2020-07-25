import java.awt.Polygon;


public class Hexagon
{		
	//Finds the vertices of a hexagon given side length and a point
	private int hValue;
	private int rValue;
	private int side;
	private int xPoint;
	private int yPoint;
	private int level;
	private int row;
	private int column;
	private int type;
	private boolean empty = false;
	private boolean structure = false;
	private int ore = -1;
	private Polygon hexagon;
	
	/**
	 * Creates a hexagon object to act as a tile
	 * @param hValue The length of h of a hexagon
	 * @param rValue The length of r of a hexagon
	 * @param side The length of the sides of a hexagon
	 * @param xPoint The X coordinate of the top point
	 * @param yPoint The Y coordinate of the top point
	 * @param level The level of the hexagon
	 * @param row The row of the hexagon
	 * @param column The column of the hexagon
	 */
	public Hexagon(int hValue, int rValue, int side, int xPoint, int yPoint, int level, int row, int column)
	{
		this.hValue = hValue;
		this.rValue = rValue;
		this.side = side;
		this.xPoint = xPoint;
		this.yPoint = yPoint;
		this.level = level;
		this.row = row;
		this.column = column;
		
		//Creates the polygon of the tile to act of boundaries
		hexagon = new Polygon();
		hexagon.addPoint(xPoint,yPoint);
		hexagon.addPoint((int)xPoint+(int)rValue,(int)yPoint+(int)hValue);
		hexagon.addPoint((int)xPoint+(int)rValue,(int)yPoint+side+(int)hValue);
		hexagon.addPoint((int)xPoint,(int)yPoint+side+(int)(2*hValue));
		hexagon.addPoint((int)xPoint-(int)rValue,(int)yPoint+side+(int)hValue);
		hexagon.addPoint((int)xPoint-(int)rValue,(int)yPoint+(int)hValue);
		
	}
	
	/**
	 * Moves the hexagon to a new point
	 * @param xPoint The new X coordinate of the top point
	 * @param yPoint The new Y coordinate of the top point
	 */
	public void moveHexagon(int xPoint, int yPoint)
	{	
		hexagon = new Polygon();
		hexagon.addPoint(xPoint,yPoint);
		hexagon.addPoint((int)xPoint+(int)rValue,(int)yPoint+(int)hValue);
		hexagon.addPoint((int)xPoint+(int)rValue,(int)yPoint+side+(int)hValue);
		hexagon.addPoint((int)xPoint,(int)yPoint+side+(int)(2*hValue));
		hexagon.addPoint((int)xPoint-(int)rValue,(int)yPoint+side+(int)hValue);
		hexagon.addPoint((int)xPoint-(int)rValue,(int)yPoint+(int)hValue);
	}
	
	/**
	 * Checks to see if the hexagon contains a point
	 * @param x The X coordinate of the click
	 * @param y The Y coordinate of the click
	 * @return If the hexagon contains the point or not
	 */
	public boolean containsPoint(int x, int y)
	{
		if (hexagon.contains(x,y))
			return true;
		return false;
	}
	
	/**
	 * Makes the tile considered a structure
	 */
	public void constructStructure()
	{
		structure = true;
	}
	
	/**
	 * Checks if the tile is a structure
	 * @return True if the tile is a structure, false otherwise
	 */
	public boolean isStructure()
	{
		return structure;
	}
	
	/**
	 * Gets the polygon of the tile
	 * @return The Polygon
	 */
	public Polygon getPolygon()
	{
		return hexagon;
	}
	
	/**
	 * Excavates the tile
	 */
	public void digTile()
	{
		empty = true;
	}
	
	/**
	 * Checks to see if the tile is excavated
	 * @return If the tile is excavated or not
	 */
	public boolean isDug()
	{
		if (empty == true)
			return true;
		return false;
	}
	
	/**
	 * Sets the tile to a certain type
	 * @param type The type of the tile
	 */
	public void setTileType(int type)
	{
		this.type = type;
	}
	
	/**
	 * Sets the ore of the tile to a certain type
	 * @param oreType The type of the ore
	 */
	public void setOreType(int oreType)
	{
		ore = oreType;
	}
	
	/**
	 * Gets the type of the ore
	 * @return The type of the ore
	 */
	public int getOreType()
	{
		return ore;
	}
	
	/**
	 * Checks to see if the tile is an ore
	 * @return if the tile is an ore or not
	 */
	public boolean isOre()
	{
		if (ore > -1)
			return true;
		return false;
	}
	
	/**
	 * Checks to see if the tile is an ore of a certain type
	 * @param type The type of the ore
	 * @return If the ore is that type or not
	 */
	public boolean isOreOfType(int type)
	{
		if (this.ore == type)
			return true;
		return false;
	}
	
	/**
	 * Checks to see if the tile is of a certain type
	 * @param type The type of the tile
	 * @return If the tile is that type or not
	 */
	public boolean isTileOfType(int type)
	{
		if (this.type == type)
			return true;
		return false;
	}
	
	/**
	 * Gets the X coordinate of the top point
	 * @return The X coordinate
	 */
	public int getX()
	{
		return xPoint;
	}
	
	/**
	 * Gets the Y coordinate of the top point
	 * @return The Y coordinate
	 */
	public int getY()
	{
		return yPoint;
	}
	
	/**
	 * Gets the level of the tile
	 * @return The level of the tile
	 */
	public int getLevel()
	{
		return level;
	}
	
	/**
	 * Gets the row of the tile
	 * @return The row of the tile
	 */
	public int getRow()
	{
		return row;
	}
	
	/**
	 * Gets the column of the tile
	 * @return The column of the tile
	 */
	public int getColumn()
	{
		return column;
	}

}
