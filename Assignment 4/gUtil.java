/**
 * This class is a helper class 
 * that converts simulation coordinates to 
 * screen coordinates and contains the getters 
 * for screen parameters such as Width,Height,Scale 
 * and offset
 * 
 *
 */
public class gUtil 
{
	//defining the parameters related to the screen and coordinates:
	private static final int WIDTH = 1200;  	 		//Screen coordinates (Width)
	private static final int HEIGHT = 600;		 		//(Height)
	private static final int OFFSET = 200;		 		//Offset
	private static final double SCALE= getHeight()/100;	//pixels per meter
	
	
	
	
	/**
	 * Converts simulation X to Screen X 
	 * @param Xsim
	 * @return X Screen Coordinate
	 */
	
	public static int ScrX(double Xsim)		//Convert simulation coordiante x to screen coordinate
	{
		return (int) (Xsim*getScale());
	}
	
	
	
	/**
	 * Converts simulation Y to Screen Y 
	 * @param Ysim
	 * @return Y Screen Coordinate 
	 */
	
	public static int ScrY(double Ysim)		//Converts simulation coordinate y to screen coordinates
	{
		return (int) (getHeight()-Ysim*getScale());
	}
	
	
	
	/**
	 * Converts a simulation dimension to a pixel dimension 
	 * @param Dsim
	 * @return Dimension in pixel  
	 */
	
	public static int ScrD(double Dsim)		//Converts simulation distances to screen distances 
	{
		return (int) (Dsim*getScale());
	}
	
	
	
	/**
	 * get method for the height 
	 * @return Height
	 */
	
	public static int getHeight() 			//getter for the Height
	{
		return HEIGHT;
	}
	
	
	
	/**
	 * get method for width
	 * @return Width
	 */
	
	public static int getWidth() 			//getter for the Width
	{
		return WIDTH;
	}
	
	
	
	/**
	 * get method for the Scale
	 * @return Scale 
	 */
	
	public static double getScale() 		//getter for the Scale
	{
		return SCALE;
	}
	
	
	
	/**
	 * get method for the offset 
	 * @return	Offset 
	 */
	
	public static int getOffset()			//getter for the Offset 
	{
		return OFFSET;
	}
}
