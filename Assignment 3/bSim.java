import java.awt.Color;
import acm.program.GraphicsProgram;
import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.util.RandomGenerator;
/**
 * This class in the main class in this Project
 * (Assignment #3)
 * This class sets uo the display and an instance of each ball
 * runs until terminated by user
 * The binary tree in bTree class record the balls created so they could be accessed later
 * when  this simulation is terminated
 * 
 *
 */
public class bSim extends GraphicsProgram
{
	private static final long serialVersionUID = 1L;
	
	//Defining parameters used in this program:
	private static final int NUMBALLS = 60;	 		//Number of balls to stimulate 
	private static final double MINSIZE = 1.0;	 	//Minimum ball radius (m)
	private static final double MAXSIZE = 7.0;  	//Maximum ball radius (m)
	private static final double EMIN = 0.2;		 	//Minimum loss coefficient
	private static final double EMAX = 0.6;		 	//Maximum loss coefficient
	private static final double VoMIN = 40.0;	 	//Minimum velocity (m/s)
	private static final double VoMAX = 50.0;	 	//Maximum velocity (m/s)
	private static final double ThetaMIN = 80.0; 	//Minimum launch angle	(degrees)
	private static final double ThetaMAX = 100.0;	//Minimum launch angle (degrees)

	public static int getNumballs()
	{
		return NUMBALLS;
	}
	
	
	public static void main(String[] args) 
	{
		new bSim().start(args); 					//Used to begin the execution of thread
	}
	
	
	
	/**
	 * run method calls the aBall instance when
	 * started and the code of this method runs 
	 * concurrently with the main program in bSim
	 * @param void 
	 * @return void
	 */
	
	public void run()
	{
    	setBackground(Color.DARK_GRAY);	    												//Changing background color to Dark Gray 
		this.resize(gUtil.getWidth(),gUtil.getHeight()+gUtil.getOffset());					//Resizing the canvas to our desired dimentions
		GLine ground = new GLine(0,gUtil.getHeight(),gUtil.getWidth(),gUtil.getHeight());	//Draws the"Ground line" 
		ground.setColor(Color.WHITE);														//Setting the color of the ground line to white 
		add(ground); 																		//Adding the ground line
			
		/*
		 * making a random generator:
		 * then finding random simulation parameters
		 * using a for loop 
		 */
		RandomGenerator ran = RandomGenerator.getInstance();
		bTree myTree = new bTree();											//Creating an instance of bTree
		ran.setSeed( (long) 424242 );										//Planting the random seed specified in the Assignment 

		for (int j=1; j<=NUMBALLS ; j++)									//A for loop to generate random numbers for each parameter 
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
			TheBalls.start();												//Begin the execution of the thread 
			myTree.addNode(TheBalls);										//Saves the instance
		}	
		while (myTree.isRunning());											//This while loops blocks the rest of the program until the simulatin is completely terminated
		GLabel Label1 = new GLabel("CR to continue");				//Prompting the user with the message:"Click to continue" to start sorting the balls
		Label1.setLocation(gUtil.getWidth()-Label1.getWidth()-50,gUtil.getHeight()-Label1.getHeight());
		Label1.setColor(Color.RED);											//Setting the Glabel color to red 
		add(Label1);														//Adding the label to the screen 
		waitForClick();														//The program waits for the user mouse input(click) to continue 
		Label1.setLabel("All Stacked!");									//Changin the message of the GLabel
		myTree.stackBalls();												//Sorting the balls With the same radius range from left to right
	}
}
