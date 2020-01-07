package Assignment1Package;

/*Assignment #1:
 this program is used 
 to simulate the projectile 
 motion of a ball*/

//imports:
import java.awt.Color;		//Change Color
import acm.graphics.GLine;  //Create lines 
import acm.graphics.GOval;  //Create the ball (circle)
import acm.program.*;//Create a Graphical program 
import java.util.Scanner;//Scanner to bet input from user

public class Bounce extends GraphicsProgram
	{
		private static final long serialVersionUID = 1L; //class serialVersionUID
		
		private static int WIDTH = 600;		
	    private static int HEIGHT = 600;		
	    private static final int OFFSET = 200;
	    /*define our canvas in Width,
		Hight and 
		Offset*/
	    
	    double bSize;									//the radius of the ball in meters 
	    public static final double g=9.8;				//Gravitational constant 9.8 m/(s^2)
	    public static final double pi=3.141592654;		//To Convert degrees to radians
	    public static final double Xinit = 5.0;		    //Initial ball X location
	    public static final double TICK = 0.1;		    //Clock Tick duration
	    public static final double ETHR = 0.01;		    //If either Vx or Vy < ETHR STOP
	    public static final double XMAX = 100.0;		//Maximum value of X 
	    public static final double YMAX = 100.0;		//Maximum value of Y
	    public static final double PD = 1;		        //Trace point diameter
	    public static final double SCALE = HEIGHT/XMAX; //Pixels/meter
	    public static final double k=0.0016;			//parameter k
	    public static final double m=1;					//mass=1kg in this Assignment 
	    public final double Yinit = bSize;      		//initial ball y location
	    public final static boolean TEST = true;		//print if test true 
	    public void run() 
		{
	    	setBackground(Color.DARK_GRAY);	    	//Changing background color to Dark Gray 
		
	    	
	    	/*getting the inputs
			  from the user*/
	    	@SuppressWarnings("resource")
			
	    	Scanner uservalues = new Scanner(System.in);
			System.out.println("Enter the initial velocity of the ball in meters/second [0,100]: ");
			double V0 = uservalues.nextDouble();
			
			while(V0<0 || V0>100)
			{
				System.out.println("enter a value between 0 and 100");
				System.out.println("Enter the initial velocity of the ball in meters/second [0,100]: ");
				V0 = uservalues.nextDouble();
			}
			
			System.out.println("Enter the launch angle in degrees [0,90]: ");
			double theta = uservalues.nextDouble();
			
			while(theta<0 || theta>90)
			{
				System.out.println("enter a value between 0 and 90");
				System.out.println("Enter the launch angle in degrees [0,90]: ");
				theta = uservalues.nextDouble();
			}

			System.out.println("Enter energy loss parameter [0,1]: ");
			double loss = uservalues.nextDouble();
			
			while(loss<0 || loss>1)
			{
				System.out.println("enter a value between 0 and 1");
				System.out.println("Enter energy loss parameter [0,1]: ");
				loss = uservalues.nextDouble();
			}
			
			System.out.println("Enter the radius of the ball in meters [0.1,5.0]: ");
			double bSize = uservalues.nextDouble();
			
			while(bSize<0.1 || bSize>5.0)
			{
				System.out.println("enter a value between 0.1 and 5.0");
				System.out.println("Enter the radius of the ball in meters [0.1,5.0]: ");
				bSize = uservalues.nextDouble();
			}
		    
			double diameter = 2*bSize; //define the diameter for ease of use diameter = 2 x radius
			
	    	this.resize(WIDTH,HEIGHT+OFFSET); //set the size of the Applet (in pixels)
			GLine ground = new GLine(0,HEIGHT,WIDTH+1400,HEIGHT);//Draws the"Ground line" 
			ground.setColor(Color.WHITE);						//Setting the color of the ground line to white 
			add(ground);										//adding the ground line
			GOval ball = new GOval((Xinit)*SCALE,HEIGHT-(diameter*SCALE),SCALE*diameter,SCALE*diameter);//create and show the ball at the initial position
			ball.setFilled(true);			//fill the ball color 
			ball.setColor(Color.CYAN);		//set the ball color to Cyan 
			add(ball);						//Adding the ball on the screen 
			
			//initialize simulation parameters:
			double Vt = g/(4*pi*k*bSize*bSize);		//terminal velocity=Vt
			double Vox = V0*Math.cos(theta*pi/180);	//X component of initial velocity
			double Voy = V0*Math.sin(theta*pi/180); //Y component of initial velocity
			double X,Y,Xlast=0,Ylast=0,Vx,Vy;		//defining parameters used in the loop
			double time=0;							//set the initial value of time to zero(0)
			double X0=0;							
			while (true)
			{
				X = Vox*Vt/g*(1-Math.exp(-g*time/Vt)); //X position
				Y = bSize+ Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time; //Y position
				
				/*estimate Vx and 
				Vy from difference*/
				Vx = (X-Xlast)/TICK;	
				Vy = (Y-Ylast)/TICK; 
				
				time+=TICK;//incrementing the time 
				Xlast=X;
				Ylast=Y;
				
				/*Converting the
				 coordinates to pixels*/
				int ScrX = (int) ((X-bSize)*SCALE);
				int ScrY = (int) (HEIGHT-(Y+bSize)*SCALE);
				
				/*Creating the 
				 black points*/
				GOval points = new GOval(ScrX+(X0+bSize)*SCALE,ScrY+bSize*SCALE,1,1);
				points.setFilled(true);
				points.setColor(Color.white);		//Setting the dot colors to white 
				if (TEST)
				{
					System.out.printf("\n t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy:%.2f\n",//printing the results in the console 
							time,X0+X,Y,Vx,Vy);
				}
				if(Vy<0 && ScrY + diameter*SCALE >= HEIGHT) //if the ball hits the ground:
				{ 
					X0+=X;
					
					/*Calculating the new kinetic 
					 * energy considering the loss 
					 * parameter and deducing the 
					 * new Velocities from them
					 */
					double KineticX = 0.5*Vx*Vx*(1-loss);
					double KineticY = 0.5*Vy*Vy*(1-loss);
					Vox =  Math.sqrt(2*KineticX);
					Voy = Math.sqrt(2*KineticY);
					
					Y= 600-diameter*SCALE;//Putting the ball on the ground 
					time=0;//Setting the time to zero(0)
					
					if((KineticX<=ETHR || KineticY<=ETHR))//If Kinetic energy is smaller than the treshhold:
					{
						break;//break the loop 
					}
				}
				else//if Kinetic energy is larger than the treshhold
				{
					ball.setLocation(ScrX+(X0)*SCALE,ScrY);//setting the new location of the ball
					add(points);//adding the black points
					pause(50);	//pausing the program for 0.05 seconds
					
					/*If the ball
					 * is going out
					 * of the screen:
					 * Widen the applet
					 * canvas
					 */
				
					if(ScrX+(X0)*SCALE>=WIDTH)
					{
						WIDTH+=1400; //Widening the Canvas
						this.resize(WIDTH,HEIGHT);
					}
				}
			}	
			//run();  //running the program again (uncomment this part if wanted so)
		}
	}
