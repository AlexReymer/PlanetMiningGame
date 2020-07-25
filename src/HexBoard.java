import com.heresysoft.arsenal.utils.Noise;

public class HexBoard 
{
	private final int NONE = -1;
	private final int LAND = 0;
	private final int MOUNTAIN = 1;
	private final int DESERT = 3;
	private final int GRAVEL = 4;
	private final int ROCK = 5;
	private final int LAVA = 6;
	
	private int height;
	private int width;
	private int level;
	private Hexagon[][] hexGrid;
	private int[][] perlinArray;
	
	public int currentRow = 0;
	public int currentCol = 0;

	/**
	 * Creates a new HexBoard object
	 * @param side The length of the side of each hexagon tile
	 * @param rValue The length of r of each hexagon tile
	 * @param hValue The length of h of each hexagon tile
	 * @param newX The X coordinate of the top point of the initial tile
	 * @param newY The Y coordinate of the top point of the initial tile
	 * @param keyX The amount by which each tile is moved in the X axis
	 * @param keyY The amount by which each tile is moved in the Y axis
	 * @param width The width of the board
	 * @param height The height of the board
	 * @param level The level of the board
	 */
	public HexBoard(int side, int rValue, int hValue, int newX, int newY, int keyX, int keyY, int width, int height, int level)
	{
		this.width = width;
		this.height = height;
		this.level = level;
		hexGrid = new Hexagon[width+1][height+1];
		perlinArray = new int[height][width];
		
		//Creates all of the Hexagon objects
		for (int col = 0; col < height; col++)
		{
			for (int row = 0; row < width; row++)
			{
				//The initial hexagon
				if (col == 0 && row == 0)
				{
					hexGrid[row][col] = new Hexagon(hValue, rValue, side, rValue+keyX, keyY, level, row, col);
					newX = newX+3*rValue;
				}
				//The last hexagon of an even row
				else if (row == width-1 && col%2 == 0)
				{
					hexGrid[row][col] = new Hexagon(hValue, rValue, side, newX+keyX, newY+keyY, level, row, col); 
					newX = 0;
					newY = newY + hValue + side; 
					
				}
				//The last hexagon of an odd row
				else if (row == width-1 && col%2 != 0)
				{
					hexGrid[row][col] = new Hexagon(hValue, rValue, side, newX+keyX, newY+keyY, level, row, col); 
					newX = 0+ rValue;
					newY = newY + hValue + side; 
					
				}
				//All standard hexagons
				else
				{
					hexGrid[row][col] = new Hexagon(hValue, rValue, side, newX+keyX, newY+keyY, level, row, col); 
					newX = newX+2*rValue;
				}			
						
			}
		}
		
		//Randomly generates the the ground level
		if (level == 0)
		{
			perlinArray = generateTurbulence();
			generateEmptyLand();
		}
		//Randomly generates all the other levels
		else if (level >= 1)
		{
			perlinArray = generatePerlinNoise();
			generateUnderground();
		}
		
	}	
	
	/**
	 * Generates the ground level
	 */
	private void generateEmptyLand()
	{				
		for (int col = 0; col < height; col++)
		{
			for (int row = 0; row < width; row++)
			{
				int zValue = perlinArray[row][col];
				//Based on the generated value, sets the type of the tile	
				if (zValue >= 210)
					hexGrid[row][col].setTileType(MOUNTAIN);
				else if (zValue < 210 && zValue >= 175)
					hexGrid[row][col].setTileType(LAND);
				else if (zValue < 175 && zValue >= 125)
					hexGrid[row][col].setTileType(DESERT);	
			}
		}	
	}
	
	/**
	 * Generates all of the underground levels
	 */
	private void generateUnderground()
	{
		int noOfClusters = (int) Math.round(Math.random()*(11)+5);
		
		for (int col = 0; col < height; col++)
		{
			for (int row = 0; row < width; row++)
			{
				int zValue = perlinArray[row][col];
				//Based on a randomly generated value sets the type of the tile
				if (level < 6)
				{	
					if (zValue > 150)
						hexGrid[row][col].setTileType(ROCK);
					else if (zValue <= 150)
						hexGrid[row][col].setTileType(GRAVEL);
				}
				else
				{
					if (zValue >= 210)
						hexGrid[row][col].setTileType(LAVA);
					else if (zValue > 150 && zValue < 210)
						hexGrid[row][col].setTileType(ROCK);
					else if (zValue <= 150)
						hexGrid[row][col].setTileType(GRAVEL);
				}
					
			}
		}
		//Generates all of the ores
		for (int cluster = 0; cluster < noOfClusters; cluster++)
		{
			int initialRow = (int)Math.round(Math.random()*height);
			int initialCol = (int)Math.round(Math.random()*width);
			int range = (int)Math.round(Math.random()*(1)+1);
			int oreRoll = (int)Math.round(Math.random()*(level+1));

			for (int col = initialCol-range; col <= initialCol+range; col++)
			{
				for (int row = initialRow-range; row <= initialRow+range; row++)
				{
					if (row > 0 && row < height && col > 0 && col < width)
					{
							int roll = (int)Math.round(Math.random()*(2)+2);
							
							if ((roll > Math.abs(col-initialCol) || roll > Math.abs(row-initialRow)) && hexGrid[row][col].isOreOfType(NONE) && !hexGrid[row][col].isTileOfType(LAVA))
								hexGrid[row][col].setOreType(oreRoll);
					}
				}
			}
		}
	}
	
	/**
	 * Generates the values for the ground level (Credits go to user3210514 for providing the noise generator)
	 * @return An array of all the Z values
	 */
	private int[][] generateTurbulence()
	{
		//Generates the data
		double[] data = Noise.normalize(Noise.turbulence(width, height, 7));
		int[][] dataArray = new int[height][width];
		
	    int widthIndex = 0;
	    int heightIndex = 0;
	    
	    //Puts all of the generated data into an array
	    for (int i = 0; i < data.length; i++)
	    {
	    	data[i] = 255 * data[i];
	        dataArray[widthIndex][heightIndex] = (int)Math.round(data[i]);
	        widthIndex++;
	        if (widthIndex == width)
	        {
	            widthIndex = 0;
	            heightIndex++;
	        }
	    }
	    
	    return dataArray;
	}
	
	/**
	 * Generates the values for the underground levels (Credits go to user3210514 for providing the noise generator)
	 * @return An array of all the Z values
	 */
	private int[][] generatePerlinNoise()
	{
		//Generates the data
		double[] data = Noise.normalize(Noise.perlinNoise(width, height, 7));
		int[][] dataArray = new int[height][width];
		
	    int widthIndex = 0;
	    int heightIndex = 0;
	    
	    //Puts all of the generated data into an array
	    for (int i = 0; i < data.length; i++)
	    {
	    	data[i] = 255 * data[i];
	        dataArray[widthIndex][heightIndex] = (int)Math.round(data[i]);
	        widthIndex++;
	        if (widthIndex == width)
	        {
	            widthIndex = 0;
	            heightIndex++;
	        }
	    }
	    
	    return dataArray;
	}
	
	/**
	 * Creates collision for all of the tiles affected by the structure
	 * @param type The type of the structure
	 * @param col The column of the structure
	 * @param row The row of the structure
	 * @return The Structure object
	 */
	public Structure buildStructure(int type, int col, int row)
	{
		//Control Center
		if (type == 0)
		{
			this.hexGrid[row][col].constructStructure();
			this.hexGrid[row-1][col].constructStructure();
			this.hexGrid[row+1][col].constructStructure();	
			this.hexGrid[row][col-1].constructStructure();
			this.hexGrid[row][col+1].constructStructure();
			
			if (col%2 != 0)
			{
				this.hexGrid[row-1][col-1].constructStructure();
				this.hexGrid[row-1][col+1].constructStructure();
			}
			else
			{
				this.hexGrid[row+1][col-1].constructStructure();
				this.hexGrid[row+1][col+1].constructStructure();
			}
		}
		//All other structures
		else if (type >= 1)
		{
			this.hexGrid[row][col].constructStructure();
		}
		
		return new Structure(type, hexGrid[row][col].getX(), hexGrid[row][col].getY(), hexGrid[row][col].getLevel(), hexGrid[row][col].getOreType());
	}
	
	/**
	 * Moves the hexagon board a given amount
	 * @param width The width of the board
	 * @param height The height of the board
	 * @param side The length of the side of each hexagon tile
	 * @param rValue The length of r of each hexagon tile
	 * @param hValue The length of h of each hexagon tile
	 * @param newX The X coordinate of the top point of the initial tile
	 * @param newY The Y coordinate of the top point of the initial tile
	 * @param keyX The amount by which each tile is moved in the X axis
	 * @param keyY The amount by which each tile is moved in the Y axis
	 */
	public void moveBoard (int width, int height, int side, int rValue, int hValue, int newX, int newY, int keyX, int keyY)
	{
		for (int row = 0; row < height; row++)
		{
			for (int col = 0; col < width; col++)
			{
				//The initial hexagon
				if (row == 0 && col == 0)
				{
					hexGrid[col][row].moveHexagon(0+rValue+keyX, keyY);
					newX = newX+3*rValue;
				}
				//The last hexagon of an even row
				else if (col == width-1 && row%2 == 0)
				{
					hexGrid[col][row].moveHexagon(newX+keyX, newY+keyY); 
					newX = 0;
					newY = newY + hValue + side; 
					
				}
				//The last hexagon of an odd row
				else if (col == width-1 && row%2 != 0)
				{
					hexGrid[col][row].moveHexagon(newX+keyX, newY+keyY); 
					newX = 0+ rValue;
					newY = newY + hValue + side; 
					
				}
				//All standard hexagons
				else
				{
					hexGrid[col][row].moveHexagon(newX+keyX, newY+keyY); 
					newX = newX+2*rValue;
				}			
			}
		}
	}
	
	/**
	 * Checks to see if a hexagon on this board contains an point
	 * @param cTileX The X coordinate of the point
	 * @param cTileY The Y coordinate of the point
	 */
	public void findCurrentHex(int cTileX, int cTileY)
	{
		//Goes through all of the hexes
		for (int col = 0; col < height; col++)
		{
			for (int row = 0; row < width; row++)
			{
				if (hexGrid[row][col].containsPoint(cTileX, cTileY))
				{
					currentRow = row;
					currentCol = col;
				}
			}
		}
	}
	
	/**
	 * Gets the current Hexagon
	 * @return The current Hexagon
	 */
	public Hexagon getCurrentHex()
	{
		return hexGrid[currentRow][currentCol];
	}
	
	/**
	 * Gets the Board
	 * @return The Board
	 */
	public Hexagon[][] getBoard()
	{
		return hexGrid;
	}
}
