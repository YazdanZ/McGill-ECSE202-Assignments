/*
 * @author Yazdan
 * This class sets up the display, 
 * generates parameters and an instance of each ball,
 * and runs until terminated by the user
 */


//imports:
import java.awt.Color;
import acm.program.GraphicsProgram;
import acm.graphics.GOval;
import acm.graphics.GLine;
import acm.util.RandomGenerator;

public class bSim extends GraphicsProgram
{
	private static final long serialVersionUID = 1L;
	
	//defining parameters used in this program:
	private static final int NUMBALLS = 100;	 	//number of balls to stimulate 
	private static final double MINSIZE = 1.0;	 	//minimum ball radius (m)
	private static final double MAXSIZE = 10.0;  	//maximum ball radius (m)
	private static final double EMIN = 0.1;		 	//minimum loss coefficient
	private static final double EMAX = 0.6;		 	//maximum loss coefficient
	private static final double VoMIN = 40.0;	 	//Minimum velocity (m/s)
	private static final double VoMAX = 50.0;	 	//Maximum velocity (m/s)
	private static final double ThetaMIN = 80.0; 	//Minimum launch angle	(degrees)
	private static final double ThetaMAX = 100.0;	//Minimum launch angle (degrees)

	public static void main(String[] args) 
	{
		new bSim().start(args); 					//used to begin the execution of thread
	}
	
	//the run method:
	public void run()
	{
    	setBackground(Color.DARK_GRAY);	    												//Changing background color to Dark Gray 
		this.resize(gUtil.getWidth(),gUtil.getHeight()+gUtil.getOffset());					//resizing the canvas to our desired dimentions
		GLine ground = new GLine(0,gUtil.getHeight(),gUtil.getWidth(),gUtil.getHeight());	//Draws the"Ground line" 
		ground.setColor(Color.WHITE);														//Setting the color of the ground line to white 
		add(ground); 																		//adding the ground line
			
		/*
		 * making a random generator:
		 * then finding random simulation parameters
		 * using a for loop 
		 */
		RandomGenerator ran = RandomGenerator.getInstance();
		ran.setSeed( (long) 0.12345 );										//planting the random seed specified in the Assignment 

		
		for (int j=1; j<=NUMBALLS ; j++)									//a for loop to generate random numbers for each parameter 
		{
			double bSize = ran.nextDouble(MINSIZE,MAXSIZE);
			Color bColor = ran.nextColor();
			double bLoss = ran.nextDouble(EMIN,EMAX);
			double Vo = ran.nextDouble(VoMIN,VoMAX);
			double theta = ran.nextDouble(ThetaMIN,ThetaMAX);
			double Xi = ((gUtil.getWidth()/2)/gUtil.getScale());
			double Yi = bSize*2;
			
			aBall TheBalls = new aBall(Xi,Yi,Vo,theta,bSize,bColor,bLoss);	//Creates an instance of the aBall class
			add(TheBalls.Balls);
			TheBalls.start();												//begin the execution of the thread 
		}
	}
}
//fin