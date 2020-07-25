import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Gui extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener
{
	private HexBoard[] worldLevels = new HexBoard[9];
	private List<Hexagon> visibleBoard = new ArrayList<Hexagon>();
	private List<Structure> structureList = new ArrayList<Structure>();
	private List<Element> menuBars = new ArrayList<Element>();
	private Element[] buttons = new Element[11];
	private Element[] arrowsLeft = new Element[5];
	private Element[] arrowsRight = new Element[5];
	private Element requirements;
	private Hexagon currentHex;
	private int currentElement;
	private int activeLevel;
	
	private JMenuBar topMenuBar;

	private final int BOARD_HEIGHT = 25;
	private final int BOARD_WIDTH = 25;
	private final int BOARD_SIDE = 64;
	
	private final int LAND = 0;
	private final int MOUNTAIN = 1;
	private final int DESERT = 3;
	private final int GRAVEL = 4;
	private final int ROCK = 5;
	private final int LAVA = 6;
	
	private final int TIN = 0;
	private final int COPPER = 1;
	private final int IRON = 2;
	private final int SILVER = 3;
	private final int GOLD = 4;
	private final int EMERALD = 5;
	
	private final int RUBY = 7;
	private final int DIAMOND = 8;
	private final int ADAMANTITE = 9;
	
	private int rValue = Main.CalculateRValue(BOARD_SIDE);;
	private int hValue = Main.CalculateHValue(BOARD_SIDE);;
	private int newX;
	private int newY;
	private int keyX;
	private int keyY;
	
	private int maxPower = 10;
	private int power = maxPower;
	private int credits = 1500;
	private int creditsLoss = 0;
	private int creditsGain = 0;
	
	private int influenceSci = 50;
	private int sciChange = 5;
	private int influenceInd = 50;
	private int indChange = 5;
	private int influenceCom = 50;
	private int comChange = 5;
	
	private int[] resources = new int[5];
	private int[] structureLocks = new int[13];
	
	private boolean menuActive;
	private boolean tutActive = false;
	private boolean showReq = false;
	private Element mainMenu;
	private Element helpControls;
	private Element helpMechanics;
	private Element helpBuildings;
	private Element helpResources;
	private boolean showControls;
	private boolean showMechanics;
	private boolean showBuildings;
	private boolean showResources;
	
	private Element[] menuButtons;
	
	private JFrame frame;
	private JMenu main;
	private JMenuItem ridoutMode;
	private JMenuItem exit;
	private JMenuItem controls;
	private JMenuItem mechanics;
	private JMenuItem buildings;
	private JMenuItem resourcesMenu;
	private JMenuItem view;
	private JMenuItem exitHelp;
	
	private int placeInitialBase = 1;
	
	/**
	 *Creates the frame and main menu
	 */
	public Gui()
	{
		setPreferredSize(new Dimension(1080,700));
		
		//Creating the frame
		frame = new JFrame("Planet Mining Project");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		
		//Adds all of the listeners to the Gui
		addKeyListener(this);
		setFocusable(true);
		requestFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
		
		//Creates the buttons for the main menu
		menuActive = true;
		mainMenu = new Element(0,0, 700, 1080, Main.mainMenu, null);
		menuButtons = new Element[4];
		menuButtons[0] = new Element(100, 75, 100, 200, Main.mainStart, Main.buttonLocked);
		menuButtons[1] = new Element(100, 75+150, 100, 200, Main.mainTutorial, Main.buttonLocked);
		menuButtons[3] = new Element(100, 75+300, 100, 200, Main.mainCredits, Main.buttonLocked);
		menuButtons[2] = new Element(100, 75+450, 100, 200, Main.mainExit, Main.buttonLocked);
		
		frame.pack();
	}

	/**
	 * The initialization of the game itself
	 */
	public void start()
	{
		//Initializing all variables
		newX = 0; newY = 0; keyX = 0; keyY = 0;
		structureLocks[0] = 1;
		menuBars.add(new Element(0,0,700,100, Main.constructionBar, null));
		menuBars.add(new Element(0,0,50,1080, Main.resourceBar, null));
		menuBars.add(new Element(100,50, 50, 360, Main.influenceBar, null));
		menuBars.add(new Element(905,50, 250, 175, Main.tradeBar, null));
		helpControls = new Element(200, 200, 319, 614, Main.mainControls, null);
		helpMechanics = new Element(200, 200, 319, 614, Main.mainMechanics, null);
		helpBuildings = new Element(200, 200, 319, 614, Main.mainBuildings, null);
		helpResources = new Element(200, 200, 319, 614, Main.mainResources, null);
		
		//Creating the structure buttons
		for (int buttonNo = 0; buttonNo < buttons.length; buttonNo++)
		{
			buttons[buttonNo] = new Element(6, 52+50*(buttonNo), 45,90, Main.button, Main.buttonLocked);
			buttons[buttonNo].lock();
		}
		
		//Creating the trade menu arrows
		for (int arrowNo = 0; arrowNo < arrowsRight.length; arrowNo++)
		{
			arrowsRight[arrowNo] = new Element(1030, 82+39*(arrowNo), 18,29, Main.arrowRight, null);
			arrowsLeft[arrowNo] = new Element(976, 82+39*(arrowNo), 18,29, Main.arrowLeft, null);
		}
		
		currentElement = 0;
		buttons[0].highlight();
		
		for (int i = 1; i < structureLocks.length; i++)
			structureLocks[i] = 0;
		for (int i = 0; i < resources.length; i++)
			resources[i] = 0;
		
		//Generating all of the levels
		for (int level = 8; level > 0; level--)
			worldLevels[level] = new HexBoard(BOARD_SIDE, rValue, hValue, newX, newY, keyX, keyY, BOARD_WIDTH, BOARD_HEIGHT, level);
		worldLevels[0] = new HexBoard(BOARD_SIDE, rValue, hValue, newX, newY, keyX, keyY, BOARD_WIDTH, BOARD_HEIGHT, 0);
		activeLevel = 0;
		
		menuActive = false;
		
		//Creating the top menu
		topMenuBar = new JMenuBar();
		main = new JMenu("Menu");
		JMenu help = new JMenu("Help");
		JMenu credits = new JMenu("Credits");
		topMenuBar.add(main);
		topMenuBar.add(help);
		topMenuBar.add(credits);
		
		//Creating all the items for the menu
		ridoutMode = new JMenuItem("Ridout Mode");
		exit = new JMenuItem("Exit");
		controls = new JMenuItem("Controls");
		mechanics = new JMenuItem("Mechanics");
		buildings = new JMenuItem("Buildings");
		resourcesMenu = new JMenuItem("Resources");
		exitHelp = new JMenuItem("Exit Help");
		view = new JMenuItem("View");
		main.add(ridoutMode);
		ridoutMode.addActionListener(this);
		main.add(exit);
		exit.addActionListener(this);
		help.add(controls);
		controls.addActionListener(this);
		help.add(mechanics);
		mechanics.addActionListener(this);
		help.add(buildings);
		buildings.addActionListener(this);
		help.add(resourcesMenu);
		resourcesMenu.addActionListener(this);
		help.add(exitHelp);
		exitHelp.addActionListener(this);
		credits.add(view);
		view.addActionListener(this);
		
		//Creating the Content Pane
		Container contentPane = frame.getContentPane();
		contentPane.add(topMenuBar, BorderLayout.NORTH);
		
		frame.pack();
		
		//Creates a list that stores only the visible tiles on the board
		for (int row = 0; row < BOARD_HEIGHT; row++)
		{
			for (int col = 0; col < BOARD_WIDTH; col++)
			{
				visibleBoard.add(worldLevels[0].getBoard()[col][row]);
			}
		}
		
		currentHex = worldLevels[0].getBoard()[0][0];
	}	
	
	/**
	 * A method that draws all of the game's graphics
	 */
	public void paintComponent (Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		if (!menuActive)
		{
			//Draws all the visible tiles and their properties
			for (Hexagon hex : visibleBoard)
			{
				Polygon hexPolygon = hex.getPolygon();
				if (hex.getLevel() < activeLevel)
				{
					g2.setColor(Color.BLACK);
					g2.fill(hexPolygon);
				}
				else
				{
					if (hex.isTileOfType(LAND))
					{
						g.drawImage(Main.tileWastes,(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					}
					else if (hex.isTileOfType(MOUNTAIN))
					{
						g.drawImage(Main.tileMountain,(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					}
					else if (hex.isTileOfType(DESERT))
					{
						g.drawImage(Main.tileDesert,(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					}
					else if (hex.isTileOfType(ROCK))
					{
						g.drawImage(Main.tileRocks[hex.getLevel()-1],(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					}
					else if (hex.isTileOfType(GRAVEL))
					{
							g.drawImage(Main.tileGravel,(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					}
					else if (hex.isTileOfType((LAVA)))
					{
						g.drawImage(Main.tileLava,(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					}
				
					if (hex.isOreOfType(COPPER))
						g.drawImage(Main.oreCopper,(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					else if (hex.isOreOfType(TIN))
						g.drawImage(Main.oreTin,(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					else if (hex.isOreOfType(IRON))
						g.drawImage(Main.oreIron,(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					else if (hex.isOreOfType(SILVER))
						g.drawImage(Main.oreSilver,(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					else if (hex.isOreOfType(GOLD))
						g.drawImage(Main.oreGold,(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					else if (hex.isOreOfType(EMERALD))
						g.drawImage(Main.oreEmerald,(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					else if (hex.isOreOfType(RUBY))
						g.drawImage(Main.oreRuby,(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					else if (hex.isOreOfType(DIAMOND))
						g.drawImage(Main.oreDiamond,(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					else if (hex.isOreOfType(ADAMANTITE))
						g.drawImage(Main.oreAdamantite,(int)((hex.getX())-rValue-8+keyX), (int)((hex.getY())+keyY), null);
					
					
				
					//Changes opacity of tiles based on the current level
					if (hex.getLevel() != 0)
					{
						g2.setColor(new Color(0,0,0,hex.getLevel()*10));
						g2.fill(hexPolygon);
					}
				}
				g2.setColor(Color.BLACK);
				g2.setStroke(new BasicStroke(2));
				
				g.drawPolygon(hexPolygon);
				
			}
			
			//Draws all the structures
			for (Structure s : structureList)
			{
				if (s.getLevel() >= activeLevel)
				{
					if (s.isType(0))
						g.drawImage(Main.baseColony, (int)((s.getX())-3*rValue-9+keyX), (int)((s.getY())-BOARD_SIDE-hValue+keyY-8), null);
					else if (s.isType(1))
					{
						if (s.hasOreOfType(TIN))
							g.drawImage(Main.mineTin,(int)((s.getX())-rValue-8+keyX), (int)((s.getY())+keyY), null);
						else if (s.hasOreOfType(COPPER))
							g.drawImage(Main.mineCopper,(int)((s.getX())-rValue-8+keyX), (int)((s.getY())+keyY), null);
						else if (s.hasOreOfType(IRON))
							g.drawImage(Main.mineIron,(int)((s.getX())-rValue-8+keyX), (int)((s.getY())+keyY), null);
						else if (s.hasOreOfType(SILVER))
							g.drawImage(Main.mineSilver,(int)((s.getX())-rValue-8+keyX), (int)((s.getY())+keyY), null);
						else if (s.hasOreOfType(GOLD))
							g.drawImage(Main.mineGold,(int)((s.getX())-rValue-8+keyX), (int)((s.getY())+keyY), null);
						
					}
					else if (s.isType(2))
					{
						g.drawImage(Main.genSolar,(int)((s.getX())-rValue-8+keyX), (int)((s.getY())+keyY), null);
					}
					else if (s.isType(3))
					{
						g.drawImage(Main.genFusion,(int)((s.getX())-rValue-8+keyX), (int)((s.getY())+keyY), null);
					}
					else if (s.isType(4))
					{
						g.drawImage(Main.genThermal,(int)((s.getX())-rValue-8+keyX), (int)((s.getY())+keyY), null);
					}
					else if (s.isType(5))
					{
						g.drawImage(Main.factoryBronze,(int)((s.getX())-rValue-8+keyX), (int)((s.getY())+keyY), null);
					}
					else if (s.isType(6))
					{
						g.drawImage(Main.factorySteel,(int)((s.getX())-rValue-8+keyX), (int)((s.getY())+keyY), null);
					}
					else if (s.isType(7))
					{
						g.drawImage(Main.mineAdamantite,(int)((s.getX())-rValue-8+keyX), (int)((s.getY())+keyY), null);
					}
					else if (s.isType(8))
					{
						g.drawImage(Main.foundry,(int)((s.getX())-rValue-8+keyX), (int)((s.getY())+keyY), null);
					}
					else if (s.isType(9))
					{
						g.drawImage(Main.emporium,(int)((s.getX())-rValue-8+keyX), (int)((s.getY())+keyY), null);
					}
					else if (s.isType(10))
					{
						g.drawImage(Main.lab,(int)((s.getX())-rValue-8+keyX), (int)((s.getY())+keyY), null);
					}
				}
			}
			
			//Draws the current tile
			g2.setColor(Color.RED);
			g2.setStroke(new BasicStroke(6));
			g.drawPolygon(currentHex.getPolygon());
			
			//Draws the different menus
			for (Element e : menuBars)
				e.paintElement(g2);
			//Draws all of the buttons
			for (Element e : buttons)
				e.paintElement(g2);
			//Draws all of the arrows
			for (Element e : arrowsRight)
				e.paintElement(g2);
			for (Element e : arrowsLeft)
				e.paintElement(g2);
			
			//Draws all of the text
			Main.futureEarth = Main.futureEarth.deriveFont(18f);
			createText(Integer.toString(credits), g2, 45, 34, Color.WHITE);
			for (int i = 0; i < resources.length; i++)
				createText(Integer.toString(resources[i]), g2, 170+120*i, 34, Color.WHITE);
			
			if (power > 0)
				createText(Integer.toString(power), g2, 1000, 34, Color.WHITE);
			else
				createText(Integer.toString(power), g2, 1000, 34, Color.RED);
			createText(Integer.toString(influenceInd), g2, 150, 82, Color.WHITE);
			createText(Integer.toString(influenceCom), g2, 260, 82, Color.WHITE);
			createText(Integer.toString(influenceSci), g2, 380, 82, Color.WHITE);
			Main.futureEarth = Main.futureEarth.deriveFont(12f);
			createText("Control", g2, 16, 72, Color.WHITE);
			createText("Center", g2, 16, 82, Color.WHITE);
			createText("Mine", g2, 32, 130, Color.WHITE);
			createText("Solar", g2, 24, 172, Color.WHITE);
			createText("Gen", g2, 32, 182, Color.WHITE);
			createText("Fusion", g2, 24, 222, Color.WHITE);
			createText("Gen", g2, 32, 232, Color.WHITE);
			createText("Thermal", g2, 16, 272, Color.WHITE);
			createText("Gen", g2, 32, 282, Color.WHITE);
			createText("Bronze", g2, 16, 322, Color.WHITE);
			createText("Factory", g2, 16, 332, Color.WHITE);
			createText("Steel", g2, 24, 372, Color.WHITE);
			createText("Factory", g2, 16, 382, Color.WHITE);
			createText("Adamant", g2, 16, 422, Color.WHITE);
			createText("Mine", g2, 32, 432, Color.WHITE);
			createText("Foundry", g2, 16, 482, Color.WHITE);
			createText("Emporium", g2, 12, 530, Color.WHITE);
			createText("Lab", g2, 32, 580, Color.WHITE);
			
			//Draws the tutorial slides if they are active
			if (showReq)
				requirements.paintElement(g2);
			if (showControls)
				helpControls.paintElement(g2);
			if (showMechanics)
				helpMechanics.paintElement(g2);
			if (showBuildings)
				helpBuildings.paintElement(g2);
			if (showResources)
				helpResources.paintElement(g2);
			
				
			
		}
		else
		{
			//Draws everything on the main menu
			mainMenu.paintElement(g2);
			if (tutActive)
				g.drawImage(Main.mainTut, 0, 0, null);
			for (Element e : menuButtons)
				e.paintElement(g2);
		}
	}	
	
	/**
	 * Draws a given text object
	 * @param text The text to be drawn
	 * @param g2 The Graphics2D object
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 * @return The shape to be drawn
	 */
	private Shape createText(String text, Graphics2D g2, int x, int y, Color color)
	{
		TextLayout tl = new TextLayout(text, Main.futureEarth, g2.getFontRenderContext());
		Shape shape = tl.getOutline(null);
		g2.translate(x, y);
		g2.setColor(color);
		g2.fill(shape);
		g2.setColor(Color.BLACK);
		g2.translate(-x, -y);
		
		return shape;
	}
	
	/**
	 * Finds the current selected tile
	 * @return The current tile
	 */
	public Hexagon getCurrentHex()
	{
		int currentRow = worldLevels[0].currentRow;
		int currentCol = worldLevels[0].currentCol;
		int level = 0;
	
		//Finds the level of the current tile
		while (worldLevels[level].getBoard()[currentRow][currentCol].isDug() && level != 8)
		{
			level++;
		}
		
		return worldLevels[level].getBoard()[currentRow][currentCol];
	}
	
	/**
	 * Checks all of the construction buttons and disables all that cannot be built
	 */
	public void checkButtons()
	{
		//Checks to see if a Control Center can be built
		if (placeInitialBase == 1 && currentHex.getRow() != 0 && currentHex.getColumn() != 0 && currentHex.getRow() != BOARD_HEIGHT-1 && currentHex.getColumn() != BOARD_WIDTH-1)
			buttons[0].unlock();
		else
			buttons[0].lock();
		//Checks to see if a mine can be built
		if (currentHex.isOre() && !currentHex.isStructure() && credits >= 100 && resources[2] >= 50 && !currentHex.isOreOfType(ADAMANTITE))
			buttons[1].unlock();
		else
			buttons[1].lock();
		//Checks to see if a Solar Generator can be built
		if (currentHex.getLevel() == 0 && !currentHex.isStructure() && resources[0] >= 50 && resources[1] >= 50 && credits >= 100)
		{
			buttons[2].unlock();
		}
		else
		{
			buttons[2].lock();
		}
		//Checks to see if a Fusion Generator can be built
		if (currentHex.getLevel() == 0 && !currentHex.isStructure() && resources[0] >= 50 && resources[1] >= 50 && resources[2] >= 50 && credits >= 100)
		{
			buttons[3].unlock();
		}
		else
		{
			buttons[3].lock();
		}
		//Checks to see if a Thermal Generator can be built
		if (currentHex.isTileOfType(LAVA) && resources[TIN] >= 50 && resources[COPPER] >= 50 && resources[3] >= 100 && credits >= 100)
		{
			buttons[4].unlock();
		}
		else
		{
			buttons[4].lock();
		}
		//Checks to see if a Bronze Factory can be built
		if (!currentHex.isTileOfType(LAVA) && resources[0] >= 100 && resources[1] >= 100 && credits >= 150)
		{
			buttons[5].unlock();
		}
		else
		{
			buttons[5].lock();
		}
		//Checks to see if a Steel Factory can be built
		if (!currentHex.isTileOfType(LAVA) && resources[2] >= 200 && credits >= 150)
		{
			buttons[6].unlock();
		}
		else
		{
			buttons[6].lock();
		}
		//Checks to see if an Adamantite Mine can be built
		if (!currentHex.isStructure() && credits >= 150 && resources[3] >= 50 && currentHex.isOreOfType(ADAMANTITE))
		{
			buttons[7].unlock();
		}
		else
		{
			buttons[7].lock();
		}
		//Checks to see if a Foundry/Emporium/Lab can be built
		if (!currentHex.isStructure() && !currentHex.isTileOfType(LAVA) && resources[3] >= 150 && resources[4] >= 50 && credits >= 200)
		{
			buttons[8].unlock();
			buttons[9].unlock();
			buttons[10].unlock();
		}
		else
		{
			buttons[8].lock();
			buttons[9].lock();
			buttons[10].lock();
		}
		
		
	}
	

	/**
	 * Listener that waits for a key to be pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) 
	{
		if (menuActive)
			return;
		//Moves the grid one tile to the left
		if (e.getKeyCode() == KeyEvent.VK_LEFT) 
		{
			if (keyX < 100)
			{
				keyX = (int) (keyX + 15);
				for (HexBoard level : worldLevels)
					level.moveBoard(BOARD_WIDTH, BOARD_HEIGHT, BOARD_SIDE, rValue, hValue, newX, newY, keyX, keyY);
			}
            
		}
		//Moves the grid one tile to the right
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if (keyX > - BOARD_WIDTH*2*rValue+1100+rValue)
			{
				keyX = (int) (keyX - 15);
				for (HexBoard level : worldLevels)
					level.moveBoard(BOARD_WIDTH, BOARD_HEIGHT, BOARD_SIDE, rValue, hValue, newX, newY, keyX, keyY);
			}
		}
		//Moves the grid one tile up
		else if (e.getKeyCode() == KeyEvent.VK_UP)
		{
			if (keyY < 0)
			{
				keyY = (int) (keyY + 15);
				for (HexBoard level : worldLevels)
					level.moveBoard(BOARD_WIDTH, BOARD_HEIGHT, BOARD_SIDE, rValue, hValue, newX, newY, keyX, keyY);
			}
		}
		//Moves the grid one tile down
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if (keyY > - BOARD_HEIGHT*((2*hValue)))
			{
				keyY = (int) (keyY - 15);
				for (HexBoard level : worldLevels)
					level.moveBoard(BOARD_WIDTH, BOARD_HEIGHT, BOARD_SIDE, rValue, hValue, newX, newY, keyX, keyY);
			}
		}
		//Constructs a building if the button is not locked
		else if (e.getKeyCode() == KeyEvent.VK_Q)
		{
			if(!buttons[currentElement].isLocked() && !currentHex.isStructure())
			{
				if (currentElement == 0)
				{
					creditsLoss += 50;
					credits -= 500;
					placeInitialBase = -1;
				}
				else if (currentElement == 1)
				{
					if (currentHex.isOreOfType(TIN) || currentHex.isOreOfType(COPPER) || currentHex.isOreOfType(IRON))
					{
						influenceInd += 10;
						creditsLoss += 25;
						credits -= 100;
						resources[2] -= 50;
					}
					else if (currentHex.isOreOfType(SILVER) || currentHex.isOreOfType(GOLD))
					{
						influenceCom += 10;
						credits -= 100;
						resources[2] -= 50;
					}
				}
				else if (currentElement == 2)
				{
					creditsLoss += 25;
					maxPower += 10;
					credits -= 100;
					resources[0] -= 50;
					resources[1] -= 50;
				}
				else if (currentElement == 3)
				{
					creditsLoss += 25;
					maxPower += 25;
					credits -= 100;
					resources[0] -= 50;
					resources[1] -= 50;
					resources[2] -= 50;
				}
				else if (currentElement == 4)
				{
					creditsLoss += 25;
					maxPower += 50;
					resources[0] -= 50;
					resources[1] -= 50;
					resources[3] -= 100;
				}
				else if (currentElement == 5)
				{
					resources[0] -= 100;
					resources[1] -= 100;
					credits -= 150;
					creditsGain += 100;			
				}
				else if (currentElement == 6)
				{
					resources[2] -= 200;
					credits -= 150;
				}
				else if (currentElement == 7)
				{
					resources[3] -= 50;
					credits -= 150;
					creditsLoss += 25;
				}
				else if (currentElement > 7)
				{
					resources[3] -= 150;
					resources[4] -= 50;
					credits -= 200;
					creditsLoss += 50;
					if (currentElement == 8)
						indChange--;
					else if (currentElement == 9)
						comChange--;
					else
						sciChange--;
				}
				structureList.add(worldLevels[currentHex.getLevel()].buildStructure(currentElement, currentHex.getColumn(), currentHex.getRow()));
			}
			checkButtons();	
		}
		//Excavates a tile
		else if (e.getKeyCode() == KeyEvent.VK_D)
		{
			if (power <= 0)
			{
				JOptionPane.showMessageDialog(this, "Out of Power!", "ALERT", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (!currentHex.isStructure() && power > 0 && buttons[0].isLocked() && !currentHex.isTileOfType(6) && currentHex.getLevel() != 8)
			{
				//Checks to see if the tile had an ore
				if (currentHex.isOre())
				{
					if (currentHex.isOreOfType(TIN))
						resources[0] += 10;
					else if (currentHex.isOreOfType(COPPER))
						resources[1] += 10;
					else if (currentHex.isOreOfType(IRON))
						resources[2] += 10;
					else if (currentHex.isOreOfType(SILVER))
						credits += 25;
					else if (currentHex.isOreOfType(GOLD))
						credits += 50;
					else if (currentHex.isOreOfType(EMERALD))
					{
						credits += 25;
						influenceSci += 5;
					}
					else if (currentHex.isOreOfType(RUBY))
					{
						credits += 50;
						influenceSci += 10;
					}
					else if (currentHex.isOreOfType(DIAMOND))
					{
						credits += 100;
						influenceSci += 15;
					}
				}
				//Alters the visible board to show the new tile
				currentHex.digTile();
				visibleBoard.remove(currentHex);
				visibleBoard.add(worldLevels[currentHex.getLevel()+1].getBoard()[currentHex.getRow()][currentHex.getColumn()]);
			
				int currentRow = worldLevels[0].currentRow;
				int currentCol = worldLevels[0].currentCol;
				int level = 0;
			
				while (worldLevels[level].getBoard()[currentRow][currentCol].isDug() && level != 8)
				{
					level++;
				}
				currentHex = getCurrentHex();
				checkButtons();
				power -= 1*level;
				currentHex = worldLevels[level].getBoard()[currentRow][currentCol];
			}
			//Removes the ore but does not excavate the tile
			else if (currentHex.getLevel() == 8)
			{
				if (currentHex.isOre())
				{
					if (currentHex.isOreOfType(TIN))
						resources[0] += 10;
					else if (currentHex.isOreOfType(COPPER))
						resources[1] += 10;
					else if (currentHex.isOreOfType(IRON))
						resources[2] += 10;
					else if (currentHex.isOreOfType(SILVER))
						credits += 25;
					else if (currentHex.isOreOfType(GOLD))
						credits += 50;
					else if (currentHex.isOreOfType(EMERALD))
					{
						credits += 25;
						influenceSci += 5;
					}
					else if (currentHex.isOreOfType(RUBY))
					{
						credits += 50;
						influenceSci += 5;
					}
					else if (currentHex.isOreOfType(DIAMOND))
					{
						credits += 100;
						influenceSci += 5;
					}
					currentHex.setOreType(-1);
				}
			}
			
		}
		//Ends the turn
		else if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			//Resets power and changes credits based on the loss and gain
			power = maxPower;
			credits = credits - creditsLoss + creditsGain;
			
			//Subtracts the influence decay
			if (influenceInd > 0)
				influenceInd -= indChange;
			if (influenceCom > 0)
				influenceCom -= comChange;
			if (influenceSci > 0)
				influenceSci -= sciChange;
			
			credits += 5*(influenceInd + influenceCom + influenceSci);
			
			//Checks to see if the player won
			if ((influenceInd >= 100 && indChange < 1) || (influenceSci >= 100 && sciChange < 1) || (influenceCom >= 100 && comChange < 1))
			{
				JOptionPane.showMessageDialog(this, "You have achieved an Influence Victory!!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
				new Gui();
				frame.dispose();
			}
			//Checks to see if the player lost
			else if (credits < 0)
			{
				JOptionPane.showMessageDialog(this, "You went Bankrupt!!!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
				new Gui();
				frame.dispose();
			}
			
			//Adds resources based on all the currently built mines
			for (Structure s : structureList)
			{
				if (s.hasOreOfType(TIN))
					resources[TIN] += 10;
				else if (s.hasOreOfType(COPPER))
					resources[COPPER] += 10;
				else if (s.hasOreOfType(IRON))
					resources[IRON] += 10;
				else if (s.hasOreOfType(SILVER))
					credits += 50;
				else if (s.hasOreOfType(GOLD))
					credits += 100;
				else if (s.hasOreOfType(ADAMANTITE))
					resources[4] += 10;
				
				if (s.isType(6))
					resources[3] += 10;
			}
		}
		//Moves down one level of depth
		else if (e.getKeyCode() == KeyEvent.VK_Z)
		{
			if (activeLevel < 9)
				activeLevel++;
		}
		//Moves up one level of depth
		else if (e.getKeyCode() == KeyEvent.VK_X)
		{
			if (activeLevel > 0)
				activeLevel--;
		}
		repaint();
	}

	
	/**
	 * Listener for when a key is released
	 */
	@Override
	public void keyReleased(KeyEvent e) 
	{
	}

	/**
	 * Listener for when a key is typed
	 */
	@Override
	public void keyTyped(KeyEvent e) 
	{
	}
	
	/**
	 * Listener for when the mouse is dragged
	 */
	@Override
	public void mouseDragged(MouseEvent e) 
	{
	}

	/**
	 * Listener for when the mouse is moved
	 */
	@Override
	public void mouseMoved(MouseEvent e) 
	{
		//Shows the requirements for the structure
		if (!menuActive)
		{
			int counter = 0;
			for (Element element : buttons)
			{
				if (element.containsPoint(e.getX(), e.getY()))
				{
					requirements = new Element(element.getX()+90, element.getY(), 90, 200, Main.reqBars[counter], null);
					showReq = true;
					repaint();
					return;
				}
				counter++;
			}
		}
		//Shows the quick tutorial
		else
		{
			if (menuButtons[1].containsPoint(e.getX(),e.getY()))
			{
				tutActive = true;
				repaint();
				return;
			}
		}
		showReq = false;
		tutActive = false;
		repaint();	
	}

	/**
	 * Listener for when the mouse is clicked
	 */
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		boolean elementClicked = false;
		//Checks to see if the main menu buttons were clicked
		if (menuActive)
		{
			if (menuButtons[0].containsPoint(e.getX(), e.getY()))
			{
				start();
				return;
			}
			else if (menuButtons[2].containsPoint(e.getX(), e.getY()))
			{
				frame.dispose();
				return;
			}
			else if (menuButtons[3].containsPoint(e.getX(), e.getY()))
			{
				JOptionPane.showMessageDialog(this, "Alosha Reymer - Code, Graphics \n" + "user3210514 - Noise Generator \n" +"Toph Gorham - Textures", "Credits", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			else if (mainMenu.containsPoint(e.getX(), e.getY()))
				return;
		}
		//Checks to see if the ingame buttons were clicked
		for (int index = 0; index < buttons.length; index++)
		{
			if (buttons[index].containsPoint(e.getX(), e.getY()))
			{
				elementClicked = true;
				buttons[index].highlight();
				buttons[currentElement].unhighlight();
				currentElement = index;
				
			}
		}
		//Checks to see if any of the trade arrows were clicked
		for (int i = 0; i < 3; i++)
		{
			if ((arrowsRight[i].containsPoint(e.getX(), e.getY())) && credits >= 50)
			{
				if (i != 2)
					credits -= 50;
				else
					credits -= 75;
				resources[i] += 10;
			}
		}
		if ((arrowsRight[3].containsPoint(e.getX(), e.getY())) && credits >= 100)
		{
			credits -= 100;
			resources[3] += 10;
		}
		else if ((arrowsRight[4].containsPoint(e.getX(), e.getY())) && credits >= 100)
		{
			credits -= 200;
			resources[4] += 10;
		}
		for (int i = 0; i < 3; i++)
		{
			if ((arrowsLeft[i].containsPoint(e.getX(), e.getY())) && resources[i] >= 10)
			{
				if (i != 2)
					credits += 50;
				else
					credits += 75;
				resources[i] -= 10;
			}
		}
		if ((arrowsLeft[3].containsPoint(e.getX(), e.getY())) && resources[3] >= 10)
		{
			credits += 100;
			resources[3] -= 10;
		}
		else if ((arrowsLeft[4].containsPoint(e.getX(), e.getY())) && resources[4] >= 10)
		{
			credits += 200;
			resources[4] -= 10;
		}
		//Checks to see if any of the menu bars were clicked
		for (Element element : menuBars)
		{
			if (element.containsPoint(e.getX(), e.getY()))
				elementClicked = true;
		}
		//Chceks to see if a tile was clicked and then selects it
		if (elementClicked == false)
		{
			worldLevels[0].findCurrentHex(e.getX(), e.getY());
			
			currentHex = getCurrentHex();
			
			checkButtons();
		}
		repaint();
	}

	/**
	 * Listener for when the mouse is entered
	 */
	@Override
	public void mouseEntered(MouseEvent e) 
	{
	}

	/**
	 * Listener for when the mouse is exited
	 */
	@Override
	public void mouseExited(MouseEvent e) 
	{	
	}

	/**
	 * Listener for when the mouse is pressed
	 */
	@Override
	public void mousePressed(MouseEvent e) 
	{		
	}

	/**
	 * Listener for when the mouse is released
	 */
	@Override
	public void mouseReleased(MouseEvent e) 
	{			
	}

	/**
	 * Listener for when a menu item is clicked
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		//Returns to the main menu
		if (e.getSource() == exit)
		{
			new Gui();
			frame.dispose();
		}
		else if (e.getSource() == ridoutMode)
		{
			maxPower =  1000;
			credits = 10000;
		}
		//Checks to see when part of the tutorial to show if it is active
		else if (e.getSource() == controls)
		{
			showControls = true;
			showMechanics = false;
			showBuildings = false;
			showResources = false;
			repaint();
		}
		else if (e.getSource() == mechanics)
		{
			showControls = false;
			showMechanics = true;
			showBuildings = false;
			showResources = false;
			repaint();
		}
		else if (e.getSource() == buildings)
		{
			showControls = false;
			showMechanics = false;
			showBuildings = true;
			showResources = false;
			repaint();
		}
		else if (e.getSource() == resourcesMenu)
		{
			showControls = false;
			showMechanics = false;
			showBuildings = false;
			showResources = true;
			repaint();
		}
		else if (e.getSource() == exitHelp)
		{
			showControls = false;
			showMechanics = false;
			showBuildings = false;
			showResources = false;
			repaint();
		}
		else if (e.getSource() == view)
		{
			JOptionPane.showMessageDialog(this, "Alosha Reymer - Code, Graphics \n" + "user3210514 - Noise Generator \n" +"Toph Gorham - Textures", "Credits", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
}