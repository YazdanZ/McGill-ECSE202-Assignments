import java.awt.Color;
import acm.graphics.GOval;

/**
 * This class simulates the trajectory of multiple balls 
 * (defined by the instance variable NUMBALLS) starting from the center
 * of the screen and from the ground 
 * For the objects to run concurrently we extend Thread
 * 
 * @author Yazdan Zinati
 *
 */
public class aBall extends Thread
{
    public static final double g=9.8;				//Gravitational constant 9.8 m/(s^2)
    public static final double pi=3.141592654;		//To Convert degrees to radians
    public static final double TICK = 0.1;		    //Clock Tick duration
    public static final double ETHR = 0.01;		    //If either Vx or Vy < ETHR STOP
    public static final double XMAX = 100.0;		//Maximum value of X 
    public static final double YMAX = 100.0;		//Maximum value of Y
    public static final double k=0.0001;			//parameter k
    public static final double m=1;					//mass=1kg in this Assignment 	
	
    private double Xi;
	private double Yi;
	private double Vo;
	private double theta;
	double bSize;
	private Color bColor;
	private double bLoss;
	public boolean RunState;
	
	/*To make the GOval object 
	 * accessible outside the class we 
	 * set a getter as follows:
	 */
	public GOval Balls;
	public GOval getBall()	
	{
		return Balls;
	}
	
	
	
	/**
	* The constructor specifies the parameters for simulation. They are
	*
	* @param Xi double The initial X position of the center of the ball
	* @param Yi double The initial Y position of the center of the ball
	* @param Vo double The initial velocity of the ball at launch
	* @param theta double Launch angle (with the horizontal plane)
	* @param bSize double The radius of the ball in simulation units
	* @param bColor Color The initial color of the ball
	* @param bLoss double Fraction [0,1] of the energy lost on each bounce
	*/
	
	public aBall(double Xi, double Yi, double Vo, double theta, double bSize, Color bColor, double bLoss)
	{
		this.Xi = Xi; // Get simulation parameters
		this.Yi = Yi;
		this.Vo = Vo;
		this.theta = theta;
		this.bSize=bSize;
		this.bColor = bColor;
		this.bLoss = bLoss;
		
		/*Create instance of ball using the arguments of 
		 * aBall generated randomly in the bSim class
		 */
		Balls = new GOval(gUtil.ScrX(Xi-bSize),gUtil.ScrY(bSize*2),(double) gUtil.getScale()*2*bSize, (double) gUtil.getScale()*2*bSize);
		Balls.setFilled(true);			//fill the ball color 
		Balls.setColor(bColor);			//set the ball color to random color
		
		RunState=true;
	}
	
	/**
	 * This run method uses mainly the code of Assignment 1
	 * Is executed concurrently with the main program 
	 * @param  void
	 * @return void
	 */
	
	public void run()
	{
		//initialize simulation parameters:
		double Vt = g/(4*pi*k*bSize*bSize);		//terminal velocity=Vt
		double Vox = Vo*Math.cos(theta*pi/180);	//X component of initial velocity
		double Voy = Vo*Math.sin(theta*pi/180); //Y component of initial velocity
		double X,Y,Xlast=0,Ylast=0,Vx,Vy;		//defining parameters used in the loop
		double time=0;							//set the initial value of time to zero(0)
		double X0=(gUtil.getWidth()/(2*gUtil.getScale()));
		while (RunState)
		{
			X = Vox*Vt/g*(1-Math.exp(-g*time/Vt)); //X position
			Y = (bSize+ Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time);
			
			/*estimate Vx and 
			Vy from difference*/
			Vx = (X-Xlast)/TICK;	
			Vy = (Y-Ylast)/TICK; 
			
			time+=TICK;//incrementing the time 
			Xlast=X;
			Ylast=Y;
			
			/*Converting the
			 coordinates to pixels
			 Using the gUtil helper 
			 class*/
			int ScrX =(gUtil.ScrX(X-bSize));
			int ScrY =(gUtil.ScrY(Y+bSize));
			
			GOval points = new GOval(ScrX+(X0+bSize)*gUtil.getScale(),ScrY+bSize*gUtil.getScale(),1,1);
			points.setFilled(true);
			points.setColor(Color.white);		//Setting the dot colors to white 
			
			if (Vy<0 && ScrY+gUtil.ScrD(2*bSize)>=gUtil.getHeight())			 //if the ball hits the ground:
			{ 
				
				X0+=X;
				
				/*Calculating the new kinetic 
				energy considering the loss 
				parameter and deducing the 
				new Velocities from them*/
				double KineticX = 0.5*Vx*Vx*(1-bLoss);
				double KineticY = 0.5*Vy*Vy*(1-bLoss);
				
				if(theta<90)	//If the ball is thrown to the right:
				 {
					Vox = Math.sqrt(2*KineticX);	//make it bounce to the right
				 }
				else			//If the ball is not thrown to the right:(but left)
				{
					Vox = -Math.sqrt(2*KineticX);	//make it bounce to the left 
				}
				Voy = Math.sqrt(2*KineticY);
				
				Y= (gUtil.getHeight()-gUtil.ScrD(2*bSize));	//Putting the ball on the ground 
				time=0;										//re-Setting the time to zero(0)
				
				if((KineticX<=ETHR || KineticY<=ETHR))		//If Kinetic energy is smaller than the treshhold:
				{
					Balls.setLocation(ScrX+gUtil.getScale()*(X0),gUtil.ScrY(2*bSize));//setting the new location of the ball(On the ground line)
					break;//break the loop 
				}
			}
			else//if Kinetic energy is larger than the treshhold
			{
				Balls.setLocation(ScrX+gUtil.getScale()*(X0),ScrY);//setting the new location of the ball
				try { 					
					Thread.sleep(50);// pause for 50 milliseconds
					} catch (InterruptedException e) {
					e.printStackTrace();
					}
			}
		}
		RunState=false;
	}

	
	
	/**
	 * This class is used to move the 
	 * ball to specified coordinates in order 
	 * to sort in decending order from left to right
	 * @param x
	 * @param y
	 * @return void 
	 */
	
	void moveTo(double x, double y)
	{
		double ScrX=x; 
		double ScrY=y;
		Balls.setLocation(gUtil.ScrX(ScrX),gUtil.ScrY(ScrY));
	}
}
