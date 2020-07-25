import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main
{
	static BufferedImage tileDesert = null;
	static BufferedImage tileMountain = null;
	static BufferedImage tileWastes = null;
	static BufferedImage tileDirt = null;
	static BufferedImage tileGravel = null;
	static BufferedImage tileRock2 = null;
	static BufferedImage tileLava = null;
	static BufferedImage oreCopper = null;
	static BufferedImage oreTin = null;
	static BufferedImage oreIron = null;
	static BufferedImage oreSilver = null;
	static BufferedImage oreGold = null;
	static BufferedImage oreEmerald = null;
	static BufferedImage oreRuby = null;
	static BufferedImage oreDiamond = null;
	static BufferedImage oreAdamantite = null;
	static BufferedImage mineCopper = null;
	static BufferedImage mineTin = null;
	static BufferedImage mineIron = null;
	static BufferedImage mineSilver = null;
	static BufferedImage mineGold = null;
	static BufferedImage mineAdamantite = null;
	static BufferedImage genSolar = null;
	static BufferedImage genFusion = null;
	static BufferedImage genThermal = null;
	static BufferedImage foundry = null;
	static BufferedImage emporium= null;
	static BufferedImage lab = null;
	static BufferedImage factoryBronze = null;
	static BufferedImage factorySteel = null;
	static BufferedImage resourceBar = null;
	static BufferedImage influenceBar = null;
	static BufferedImage constructionBar = null;
	static BufferedImage buttonLocked = null;
	static BufferedImage button = null;
	static BufferedImage mainButton = null;
	static BufferedImage mainMenu = null;
	static BufferedImage mainStart = null;
	static BufferedImage mainTutorial = null;
	static BufferedImage mainCredits = null;
	static BufferedImage mainControls = null;
	static BufferedImage mainMechanics = null;
	static BufferedImage mainBuildings = null;
	static BufferedImage mainResources = null;
	static BufferedImage mainTut = null;
	static BufferedImage mainExit = null;
	static BufferedImage baseColony = null;
	static BufferedImage tradeBar = null;
	static BufferedImage arrowRight = null;
	static BufferedImage arrowLeft = null;
	static BufferedWriter fileOut;
	static BufferedReader fileIn;
	static BufferedReader fileIn2;
	static Font futureEarth;
	static BufferedImage[] tileRocks = new BufferedImage[8];
	static BufferedImage[] reqBars = new BufferedImage[11];

	//h = sin( 30°) * s
	public static int CalculateHValue(int side)
	{
		int hValue = (int) (Math.sin(Math.toRadians(30))*side);
		return hValue;
		
	}
	
	//r = cos( 30°) * s
	public static int CalculateRValue(int side)
	{
		int rValue = (int) (Math.cos(Math.toRadians(30))*side);
		return rValue;
	}

	public static void main(String[] args) throws IOException, FontFormatException
	{
		//Initializes all images
		for (int i = 0; i < tileRocks.length; i++)
			tileRocks[i] = ImageIO.read(new File("graphics\\tileRock"+i+".png"));
		for (int i = 0; i < reqBars.length; i++)
			reqBars[i] = ImageIO.read(new File("graphics\\reqbar"+i+".jpg"));
		resourceBar = ImageIO.read(new File("graphics\\resourcebar.jpg"));
		constructionBar = ImageIO.read(new File("graphics\\constructionbar.jpg"));
		influenceBar = ImageIO.read(new File("graphics\\influencebar.jpg"));
		buttonLocked = ImageIO.read(new File("graphics\\constructionLock.png"));
		tileMountain = ImageIO.read(new File("graphics\\tileMountain.png"));
		tileDesert = ImageIO.read(new File("graphics\\tileDesert.png"));
		tileWastes = ImageIO.read(new File("graphics\\tileWastes.png"));
		tileGravel = ImageIO.read(new File("graphics\\tileGravel.png"));
		tileRock2 = ImageIO.read(new File("graphics\\tileRock2.png"));
		tileLava = ImageIO.read(new File("graphics\\tileLava.png"));
		oreCopper = ImageIO.read(new File("graphics\\oreCopper.png"));
		oreEmerald = ImageIO.read(new File("graphics\\oreEmerald.png"));
		oreRuby = ImageIO.read(new File("graphics\\oreRuby.png"));
		oreDiamond = ImageIO.read(new File("graphics\\oreDiamond.png"));
		oreAdamantite = ImageIO.read(new File("graphics\\oreAdamantite.png"));
		mineAdamantite = ImageIO.read(new File("graphics\\mineAdamantite.png"));
		oreTin = ImageIO.read(new File("graphics\\oreTin.png"));
		oreIron = ImageIO.read(new File("graphics\\oreIron.png"));
		oreSilver = ImageIO.read(new File("graphics\\oreSilver.png"));
		oreGold = ImageIO.read(new File("graphics\\oreGold.png"));
		mineCopper = ImageIO.read(new File("graphics\\mineCopper.png"));
		mineTin = ImageIO.read(new File("graphics\\mineTin.png"));
		mineIron = ImageIO.read(new File("graphics\\mineIron.png"));
		mineSilver = ImageIO.read(new File("graphics\\mineSilver.png"));
		mineGold = ImageIO.read(new File("graphics\\mineGold.png"));
		baseColony = ImageIO.read(new File("graphics\\baseMain.png"));
		genSolar = ImageIO.read(new File("graphics\\genSolar.png"));
		genFusion = ImageIO.read(new File("graphics\\genFusion.png"));
		genThermal = ImageIO.read(new File("graphics\\genThermal.png"));
		factoryBronze = ImageIO.read(new File("graphics\\factoryBronze.png"));
		factorySteel = ImageIO.read(new File("graphics\\factorySteel.png"));
		foundry = ImageIO.read(new File("graphics\\foundry.png"));
		emporium = ImageIO.read(new File("graphics\\emporeum.png"));
		lab = ImageIO.read(new File("graphics\\lab.png"));
		button = ImageIO.read(new File("graphics\\button.png"));
		mainButton = ImageIO.read(new File("graphics\\mainButton.jpg"));
		mainStart = ImageIO.read(new File("graphics\\mainStart.jpg"));
		mainTutorial = ImageIO.read(new File("graphics\\mainTutorial.jpg"));
		mainCredits = ImageIO.read(new File("graphics\\mainCredits.jpg"));
		mainControls = ImageIO.read(new File("graphics\\mainControls.jpg"));
		mainMechanics = ImageIO.read(new File("graphics\\mainMechanics.jpg"));
		mainBuildings = ImageIO.read(new File("graphics\\mainBuildings.jpg"));
		mainResources = ImageIO.read(new File("graphics\\mainResources.jpg"));
		mainTut = ImageIO.read(new File("graphics\\mainTut.jpg"));
		mainExit = ImageIO.read(new File("graphics\\mainExit.jpg"));
		mainMenu = ImageIO.read(new File("graphics\\mainMenu.jpg"));
		tradeBar = ImageIO.read(new File("graphics\\tradeBar.jpg"));
		arrowRight = ImageIO.read(new File("graphics\\arrowRight.png"));
		arrowLeft = ImageIO.read(new File("graphics\\arrowLeft.png"));
		futureEarth = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("Mechfire.otf"));
		
		//Starts the game
		new Gui();
	}
}
