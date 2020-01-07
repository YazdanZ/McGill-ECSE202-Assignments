
/*
 * This class is a helper class
 * converting X and Y
 * positions to screen coordinates,
 * containing the setters and 
 * getters for the screen parameters such as 
 * the Width,Height,Scale and the Offset  
 */
public class gUtil 
{
	//defining the parameters related to the screen and coordinates:
	private static final int WIDTH = 1200;  	 		//Screen coordinates (Width)
	private static final int HEIGHT = 600;		 		//(Height)
	private static final int OFFSET = 200;		 		//Offset
	private static final double SCALE= getHeight()/100;	//pixels per meter
	
	
	public static int ScrX(double Xsim)		//Convert simulation coordiante x to screen coordinate
	{
		return (int) (Xsim*getScale());
	}
	public static int ScrY(double Ysim)		//Converts simulation coordinate y to screen coordinates
	{
		return (int) (getHeight()-Ysim*getScale());
	}
	public static int ScrD(double Dsim)		//Converts simulation distances to screen distances 
	{
		return (int) (Dsim*getScale());
	}
	public static int getHeight() 			//getter for the Height
	{
		return HEIGHT;
	}
	public static int getWidth() 			//getter for the Width
	{
		return WIDTH;
	}
	public static double getScale() 		//getter for the Scale
	{
		return SCALE;
	}
	public static int getOffset()			//getter for the Offset 
	{
		return OFFSET;
	}
}
//fin
