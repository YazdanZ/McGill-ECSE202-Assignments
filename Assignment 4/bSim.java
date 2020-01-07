import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import acm.program.GraphicsProgram;
import acm.graphics.GLine;
import acm.util.RandomGenerator;
import acm.gui.TableLayout;


/**
 * 
 * bSim (Ball Simulation): This class is the main class in this project.
 * It creates an instance of each ball and store the balls into a binary tree so 
 * they could be accessed later for histogramming and stacking when the simulation is over.
 * This class is also responsible for setting up the display and the GUI user interface(the JcomboBoxes,
 * integer and double fields and the panels).
 *
 */
public class bSim extends GraphicsProgram implements  ItemListener 
{
	private static final long serialVersionUID = 1L;
	
	
	public static void main(String[] args) 
	{
		new bSim().start(args); 					//begin the execution of a Tread
	}
	
	
	/**
	 * setBackground-This method is used to set the background
	 * elements of this program such as the ground line, the display 
	 * width and height and the background color. 
	 */
	void setBackgrounds()
	{
		setBackground(Color.DARK_GRAY);	    												//Changing the background color  
		GLine ground = new GLine(0,gUtil.getHeight(),gUtil.getWidth(),gUtil.getHeight());	//Draws the"Ground line" 
		ground.setColor(Color.WHITE);														//Setting the color of the ground line 
		add(ground); 																		//Adding the ground line to the display
	}
	
	
	public void init()
	{
		this.resize(gUtil.getWidth()+335,gUtil.getHeight()+gUtil.getOffset());					//Resizing the canvas to our desired dimentions
		setBackgrounds();																		//Calling the setBackground() method 

		ran = RandomGenerator.getInstance();													//Creating an instance of the RandomGenerator 
		
		SetNumFields();
		ComboAction();
		addCheckBox();

		addActionListeners();
		addMouseListeners();
		setComboListeners();
		addNumFieldListeners();
		ran.setSeed( (long) 424242 );									//Planting the random seed specified in the Assignment 

		while(true)
		{
			pause(200);
			if(simEnable)
			{
				doSim();
				bSimCombo.setSelectedIndex(0);
			}
		}
		
	}
	
	
	/**
	 * doSim-(do Simulation): This method creates an instance of 
	 * the aBall and the bTree class and is the method responsible 
	 * for running the simulation
	 */
	void doSim()
	{

		/*
		 * If the boolean is equal to false we will create a new binary 
		 * tree. Otherwise if the boolean is true we won't create a new 
		 * binary tree. For example we want to create a new binary tree 
		 * each time the user clears the screen but not when the simulation 
		 * runs or stops.
		 */
		if(newbTree==false)													
		{
			myTree = new bTree();											//Creating an instance of bTree
			newbTree=true;													//Ressetting the boolean responsible for creating a new binary tree to true	
		}
		
		for (int j=1; j<=SNumBalls ; j++)									//A for loop to generate random numbers for each parameter 
		{
			double bSize = ran.nextDouble(SMinSize,SMaxSize);
			Color bColor = ran.nextColor();
			double bLoss = ran.nextDouble(SLossMin,SLossMax);
			double Vo = ran.nextDouble(SMinVel,SMaxVel);
			double theta = ran.nextDouble(SThetaMin,SThetaMax);
			double Xi = ((gUtil.getWidth()/2)/gUtil.getScale());
			double Yi = bSize*2;
	
			aBall TheBalls = new aBall(Xi,Yi,Vo,theta,bSize,bColor,bLoss,this);	//Creates an instance of the aBall class
			add(TheBalls.Balls);												//Adding the balls to the display 
			myTree.addNode(TheBalls);		
			TheBalls.start();													//Begin the execution of the thread 
		}													
		simEnable=false;

	}
	
	
	/**
	 * doStack: This method calls the stackBalls method inside bTree 
	 * class to Histogramme the balls and is called when the jComboBox 
	 * Stack is selected 
	 * @param myTree
	 */
	void doStack(bTree myTree)
	{
		myTree.stackBalls();													//Sorting the balls With the same radius range from left to right
	}
	
	
	/**
	 * jumpacolumn: This method jumps a column forward 
	 * in the tablelayout used when we create the numeric fields
	 * @return	an empty string to fill the column and jump to the
	 */
	JLabel jumpacolumn()
	{
		return new JLabel("				           ");
	}

	
	/**
	 * addCheckBox()-This method creates,sets the color
	 * and the listeners for the Checkbox used to set 
	 * the tracepoints on and off on the display 
	 */
	void addCheckBox() 
	{
		JCheckBox tracecheckbox = new JCheckBox("trace points"); 			//Create a new CheckBox and set its text 
		tracecheckbox.setFont(new java.awt.Font("Arial", Font.BOLD, 14));	//Set the font of the text for the checkbox
		
		/*
		 * Setting the foreground and background Colors 
		 */
		tracecheckbox.setBackground(Color.YELLOW);
		tracecheckbox.setForeground(Color.MAGENTA);
	
		add(tracecheckbox,SOUTH);											//Adding the CheckBox to the bottom of the screen 
		
		/*
		 * Setting the Item Listener for the tracepoint Checkbox
		 */
		tracecheckbox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
				/*
				 * If the Checkbox is selected set the boolean
				 * allowing the dots to be created and added to 
				 * the screen to true using a setter defined in aBall class
				 */
				if(e.getStateChange()==1)
				{
					aBall.setdot_off_on(true);
				}
				
				/*
				 * If the Checkbox us not selected set the boolean 
				 * allowing the creation of the tracepoints to false 
				 * so they could not be created 
				 */
				else 
				{
					aBall.setdot_off_on(false);
				}
			}
	});

	}
	
	
	/**
	 * SetNumFields(Set Numeric Values Field): This method is used to set the 
	 * integer and double fields on the screen and adding them to a single jPanel 
	 * on the east of the screen 
	 */
	void SetNumFields()
	{
		JPanel ThePanel = new JPanel();										//Creating one JPanel 
		ThePanel.setLayout(new TableLayout(30,1));	
		ThePanel.add(new JLabel("General Simulation Parameters:"));
		ThePanel.add(jumpacolumn());
		ThePanel.add(jumpacolumn());
		ThePanel.add(jumpacolumn());


		int numballmedium=(NumBallMax-NumBallMin)/2;
		NumBalls = new SetNumericFields("NUMBALLS:",NumBallMin,numballmedium,NumBallMax);
		ThePanel.add(NumBalls.ThePanel);
	
		double minsizemedium = (MinSizeMax-MinSizeMin)/2;
		MinSize = new SetNumericFields("MINSIZE:",MinSizeMin,minsizemedium,MinSizeMax);
		ThePanel.add(MinSize.ThePanel);

		double maxsizemedium = (MaxSizeMax-MaxSizeMin)/2;
		MaxSize = new SetNumericFields("MAXSIZE:",MaxSizeMin,maxsizemedium,MaxSizeMax);
		ThePanel.add(MaxSize.ThePanel);
		
		double lossminmedium = (LossMinMax-LossMinMin)/2;
		LossMin = new SetNumericFields("LOSSMIN:",LossMinMin,lossminmedium,LossMinMax);
		ThePanel.add(LossMin.ThePanel);
		
		double lossmaxmedium = (LossMaxMax-LossMaxMin)/2;
		LossMax = new SetNumericFields("LOSSMAX:",LossMaxMin,lossmaxmedium,LossMaxMax);
		ThePanel.add(LossMax.ThePanel);
		
		double minvelmedium = (MinVelMax-MinVelMin)/2;
		MinVel = new SetNumericFields("VELMIN:",MinVelMin,minvelmedium,MinVelMax);
		ThePanel.add(MinVel.ThePanel);
		
		double maxvelmedium = (MinVelMax-MinVelMin)/2;
		MaxVel = new SetNumericFields("VELMAX:",MaxVelMin,maxvelmedium,MaxVelMax);
		ThePanel.add(MaxVel.ThePanel);
		
		double thminmedium=(ThMinMax-ThMinMin)/2;
		ThMin = new SetNumericFields("THMIN:",ThMinMin,thminmedium,ThMinMax);
		ThePanel.add(ThMin.ThePanel);
		
		double thmaxmedium=(ThMaxMax-ThMaxMin)/2;
		ThMax = new SetNumericFields("THMAX:",ThMaxMin,thmaxmedium,ThMaxMax);
		ThePanel.add(ThMax.ThePanel);

		add(ThePanel,EAST);
	}
	
	
	/**
	 * addnNumFieldListeners(Add Numeric Field Listeners)
	 * This method adds The Numeric Field listeners for each
	 * numeric field 
	 */
	void addNumFieldListeners()
	{
		NumBalls.Ifield.addActionListener((ActionListener)this);
		MinSize.dfield.addActionListener((ActionListener)this);
		MaxSize.dfield.addActionListener((ActionListener)this);
		LossMin.dfield.addActionListener((ActionListener)this);
		LossMax.dfield.addActionListener((ActionListener)this);
		MinVel.dfield.addActionListener((ActionListener)this);
		MaxVel.dfield.addActionListener((ActionListener)this);
		ThMin.dfield.addActionListener((ActionListener)this);
		ThMax.dfield.addActionListener((ActionListener)this);
	}
	
	
	/**
	 * This method determines a change in the value 
	 * of a numeric field, if that's the case it would
	 * get the value entered by the user and store it into a 
	 * new parameter. This method will also change the value of the 
	 * label next to it using a setter in SetNumField Class 
	 */

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==NumBalls.Ifield)
		{
			SNumBalls=NumBalls.getIValue();
			NumBalls.setIValue(NumBalls.getIValue());
		}
		else if(e.getSource()==MinSize.dfield)
		{
			SMinSize=MinSize.getDValue();
			MinSize.setDValue(MinSize.getDValue());
		}
		else if(e.getSource()==MaxSize.dfield)
		{
			SMaxSize=MaxSize.getDValue();
			MaxSize.setDValue(MaxSize.getDValue());
		}
		else if(e.getSource()==LossMin.dfield)
		{
			SLossMin=LossMin.getDValue();
			LossMin.setDValue(LossMin.getDValue());
		}
		else if(e.getSource()==LossMax.dfield)
		{
			SLossMax=LossMax.getDValue();
			LossMax.setDValue(LossMax.getDValue());
		}
		else if(e.getSource()==MinVel.dfield)
		{
			SMinVel=MinVel.getDValue();
			MinVel.setDValue(MinVel.getDValue());
		}
		else if(e.getSource()==MaxVel.dfield)
		{
			SMaxVel=MaxVel.getDValue();
			MaxVel.setDValue(MaxVel.getDValue());
		}
		else if(e.getSource()==ThMin.dfield)
		{
			SThetaMin=ThMin.getDValue();
			ThMin.setDValue(ThMin.getDValue());
		}
		else if(e.getSource()==ThMax.dfield)
		{
			SThetaMax=ThMax.getDValue();
			ThMax.setDValue(ThMax.getDValue());
		}
	}
	
	
	/**
	 * This method sets up the ComboBox for 
	 * the main actions such as run,stop,stack 
	 * and clear then adds them to the screen 
	 */
	void ComboAction()
	{
		HelpCombo = new JComboBox<String>();
		HelpCombo.addItem("Help!");
		
		FileCombo = new JComboBox<String>();
		FileCombo.addItem("File");
		
		EditCombo = new JComboBox<String>();
		EditCombo.addItem("Edit");
		
		bSimCombo = new JComboBox<String>();
		
		/*
		 * Adding Items for bSim Combo
		 */
		bSimCombo.addItem("bSim");
		bSimCombo.addItem("Run");
		bSimCombo.addItem("Stack");
		bSimCombo.addItem("Clear");
		bSimCombo.addItem("Stop");
		bSimCombo.addItem("Quit");
		
		/**
		 * Adding the ComboBoxes to the North(up) of the screen  
		 */
		add(bSimCombo,NORTH);
		add(FileCombo,NORTH);
		add(EditCombo,NORTH);
		add(HelpCombo,NORTH);
	}
	
	
	/**
	 * This method sets the listeners for the combo boxes 
	 */
	void setComboListeners()
	{
		bSimCombo.addItemListener((ItemListener)this);
		EditCombo.addItemListener((ItemListener)this);
		HelpCombo.addItemListener((ItemListener)this);
		FileCombo.addItemListener((ItemListener)this);
	}
	
	
	@Override
	public void itemStateChanged(ItemEvent e)
	{
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")

		JComboBox<String> source = (JComboBox<String>)e.getSource();
		if(source==bSimCombo)
		{
			if(bSimCombo.getSelectedIndex()==1)					//If the run button has been selected:
			{
				this.simEnable=true;							//Start the simulation by changing the value of this boolean allowing the execution of doSim to true
			}
			else if(bSimCombo.getSelectedIndex()==2)			//If the Stack button has been selected:
			{
				aBall.setRunState(false);						//Stop the simulation before stacking 
				doStack(myTree);								//Stack the balls 
			}
			else if(bSimCombo.getSelectedIndex()==3)			//If the Clear button has been selected:
			{
				removeAll();									//remove all objects from screen 
				setBackgrounds();								//reset the background 
				newbTree=false;									//the false value of this boolean allows the creation of a new binary tree 
			}
			else if(bSimCombo.getSelectedIndex()==4)			//If the Stop button has been selected:
			{
				aBall.setRunState(false);						//Stop the simulation from the aBall using a setter for the boolean allowing the simulation
			}
			else if(bSimCombo.getSelectedIndex()==5)			//If the Quit is selected:
			{
				System.exit(0);									//Quit the application 
			}
		}
	}
	
	
	/**
	 * This is a getter for the number of balls 
	 * @return the number of balls 
	 */
	public static int getNumballs()
	{
		return NUMBALLS;
	}

	
	
	

	
	
	//instance variables:
	
	
	/**
	 * The JCombo Boxes Instance Variables
	 */
	private JComboBox<String> bSimCombo;
	private JComboBox<String> FileCombo;
	private JComboBox<String> EditCombo;
	private JComboBox<String> HelpCombo;
	
	
	/**
	 * Global simulation parameters that are inputed by the user into the text boxes
	 */
	private int SNumBalls=NUMBALLS;
	private double SMinSize=MINSIZE;
	private double SMaxSize=MAXSIZE;
	private double SLossMin=EMIN;
	private double SLossMax=EMAX;
	private double SMinVel=VoMIN;
	private double SMaxVel=VoMAX;
	private double SThetaMin=ThetaMIN;
	private double SThetaMax=ThetaMAX;
	
	
	/**
	 * Fixed parameters from the previous assignment (Assignment #3)
	 */
	private static final int NUMBALLS = 60;	 		//Number of balls to stimulate 
	private static final double MINSIZE = 1.0;	 	//Minimum ball radius (m)
	private static final double MAXSIZE = 7.0;  	//Maximum ball radius (m)
	private static final double EMIN = 0.2;		 	//Minimum loss coefficient
	private static final double EMAX = 0.6;		 	//Maximum loss coefficient
	private static final double VoMIN = 40.0;	 	//Minimum velocity (m/s)
	private static final double VoMAX = 50.0;	 	//Maximum velocity (m/s)
	private static final double ThetaMIN = 80.0; 	//Minimum launch angle	(degrees)
	private static final double ThetaMAX = 100.0;	//Minimum launch angle (degrees)

	
	/**
	 * Simulation instance variables
	 */
	RandomGenerator ran;		//random generator instance 
	boolean simEnable=false;	//boolean to Enable/Disable The Simulation 
	boolean newbTree=false;		//boolean that controls whether or not a new binary tree is created 
	bTree myTree;		
	static JCheckBox tracecheckbox;//the checkbox for the traces 
	/**
	 * Instance Variables defining the Max and Min bounds for all variables 
	 */
	private static final int NumBallMax = 255;	 	 
	private static final int NumBallMin = 1;	 	 
	private static final double MinSizeMin = 1.0;	
	private static final double MinSizeMax = 25.0;	 	
	private static final double MaxSizeMin = 1.0;	 	
	private static final double MaxSizeMax = 25.0;	 	
	private static final double LossMinMin = 0.0;	
	private static final double LossMinMax = 1.0;	 	
	private static final double LossMaxMin = 0.0;	
	private static final double LossMaxMax = 1.0;	 	
	private static final double MinVelMin = 1.0;	 	
	private static final double MinVelMax = 200.0;
	private static final double MaxVelMin = 1.0;	 	
	private static final double MaxVelMax = 200.0;	
	private static final double ThMinMin = 1.0; 	
	private static final double ThMinMax = 180.0;
	private static final double ThMaxMin = 1.0; 	
	private static final double ThMaxMax = 180.0;
	
	
	/**
	 * SetNumericFields Instance Variables 
	 */
	private SetNumericFields NumBalls;
	private SetNumericFields MinSize;
	private SetNumericFields MaxSize;
	private SetNumericFields LossMin;
	private SetNumericFields LossMax;
	private SetNumericFields	MinVel;
	private SetNumericFields	MaxVel;
	private SetNumericFields	ThMin;
	private SetNumericFields	ThMax;	
}
