import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Element 
{
	
	private int pointX, pointY;
	private int height, width;
	private Rectangle bounds;
	private BufferedImage image;
	private BufferedImage imageLocked;
	private BufferedImage currentImage;
	private boolean highlight = false;
	private boolean locked = true;
	
	/**
	 * Creates a new graphical element
	 * @param pointX The X coordinate
	 * @param pointY The Y coordinate
	 * @param width The width of the element
	 * @param height The height of the element
	 * @param image The image that the element will use
	 * @param imageLocked The image that the element will use if it is locked
	 */
	public Element (int pointX, int pointY, int width, int height, BufferedImage image, BufferedImage imageLocked)
	{
		this.pointX = pointX;
		this.pointY = pointY;
		this.width = width;
		this.height = height;
		this.image = image;
		this.currentImage = image;
		this.imageLocked = imageLocked;
		
		highlight = false;
		bounds = new Rectangle(pointX, pointY, height, width);
	}
	
	/**
	 * Paints the element
	 * @param g2 The Graphics2D Object
	 */
	public void paintElement(Graphics2D g2)
	{
		g2.drawImage(currentImage,pointX, pointY, null);
		//If the element is highlighted, will draw an outline
		if (highlight)
		{
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.GREEN);
			g2.drawRect(pointX, pointY, height, width);
		}
	}
	
	/**
	 * Paints the boundaries of the element
	 * @param g2 The Graphics2D Object
	 * @param color The color of the boundaries
	 */
	public void paintRectangle(Graphics2D g2, Color color)
	{
		g2.setColor(color);
		g2.drawRect(pointX, pointY, height, width);
	}
	
	/**
	 * Gets the X coordinate
	 * @return The X coordinate
	 */
	public int getX()
	{
		return pointX;
	}
	
	/**
	 * Gets the Y coordinate
	 * @return The Y coordinate
	 */
	public int getY()
	{
		return pointY;
	}
	
	/**
	 * Highlights the element
	 */
	public void highlight()
	{
		highlight = true;
	}
	
	/**
	 * Removes the highlight of the element
	 */
	public void unhighlight()
	{
		highlight = false;
	}
	
	/**
	 * Checks to see if the element is locked
	 * @return True if the element is locked, false if it isnt
	 */
	public boolean isLocked()
	{
		return locked;
	}
	
	/**
	 * Unlocks the element
	 */
	public void unlock()
	{
		locked = false;
		currentImage = image;
	}
	
	/**
	 * Locks the element
	 */
	public void lock()
	{
		locked = true;
		if (!imageLocked.equals(null))
			currentImage = imageLocked;
			
	}
	
	/**
	 * Checks to see if the element contains a point
	 * @param x The X coordinate of the point
	 * @param y The Y coordinate of the point
	 * @return True if it contains the point, false otherwise
	 */
	public boolean containsPoint(int x, int y)
	{
		if (bounds.contains(x,y))
			return true;
		return false;
	}

}
