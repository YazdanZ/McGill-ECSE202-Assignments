#include<stdio.h>
#include<stdlib.h>
#include<math.h>

/*
Assignment #5 
this is a simple c program to simulate a ball undergoing a 
projectile motion.In this assignment we will "convert" the code 
we wrote in assignment #1 in java to C programming language 
The ball starts from the lower left side of  the screen and follows a parabolic trajectory. 
Contrary to Assignment #1 , this assignment won't be a graphical program, instead this program will output 
the simulation results into the console. We  will use Newtonian mechanics in order 
to do all the simualtion and calculations.
*/

//Parameters defined in this program: 

//Parameters defined in screen coordinates:
#define WIDTH 600
#define HEIGHT 600
#define OFFSET 200

//Parameters defined in simualtion coordinates: 
#define false 0			//defining False i C
#define true !false		//defining True in C
#define g 9.81			//g Constant 
#define k 0.0016		//Vt constant 
#define   Pi acos(-1.0)	//Pi 

#define Xinit 5.0		//Initial X
#define TICK 0.1		//Clock Tick duration
#define ETHR 0.01		//If KEx or KEy <ETHR--->Break 
#define XMAX 100.0		//Maximum value of X
#define YMAX 100.0		//Maximum value of Y 
/*
The Scale of the Screen used to convert 
simulation Coordinates to Screen coordinates 
*/
#define SCALE HEIGHT/XMAX
int main()
{
	double ScrX, ScrY;
	double V0, theta, loss, bSize;
	
	/*
	Prompting the user and getting input for the
	initial velocity, loss parameter, launch angle 
	and the radius of the ball
	*/
	printf("Enter the initial velocity of the ball in meters/second [0,100] :");
	scanf("%lf", &V0);
	while (V0 > 100 || V0 < 0)	//If the value entered is not in the interval defined, reget input from user 
	{
		printf("ILLEGAL NUMERIC VALUE\n");
		printf("Enter the initial velocity of the ball in meters/second [0,100] :");
		scanf("%lf", &V0);
	}

	printf("Enter the launch angle in degrees [0,90]: ");
	scanf("%lf", &theta);
	while (theta > 90 || theta < 0)//If the value entered is not in the interval defined, reget input from user 
	{
		printf("ILLEGAL NUMERIC VALUE\n");
		printf("Enter the launch angle in degrees [0,90]: ");
		scanf("%lf", &theta);
	}
	
	printf("Enter energy loss parameter [0,1]: ");
	scanf("%lf", &loss);
	while (loss > 1 || loss < 0)//If the value entered is not in the interval defined, reget input from user 
	{
		printf("ILLEGAL NUMERIC VALUE\n");
		printf("Enter energy loss parameter [0,1]: ");
		scanf("%lf", &loss);
	}
	
	printf("Enter the radius of the ball in meters [0.1,5.0]: ");
	scanf("%lf", &bSize);
	while (bSize > 5 || bSize < 0.1)//If the value entered is not in the interval defined, reget input from user 
	{
		printf("ILLEGAL NUMERIC VALUE\n");
		printf("Enter the radius of the ball in meters [0.1,5.0]: ");
		scanf("%lf", &bSize);
	}
	
	//Convert simulation coordinates to screen coordinates 
	ScrX = (Xinit - bSize) * SCALE;	
	ScrY = HEIGHT - (2 * bSize) * SCALE;
	
	//Simulate a parabolic trajectory 
	double Vt = g / (4 * Pi * bSize * k);
	
	//Time is reset at the start of each interval 
	double time = 0;
	double KEx = ETHR, KEy = ETHR;
	double X, X0, Xlast, Vx, Y, Vy, Ylast;

	double Vox = V0 * cos(theta * Pi / 180);
	double Voy = V0 * sin(theta * Pi / 180);
	X0 = Xinit;
	Y = bSize;
	Ylast = Y;
	Xlast = X0;
	
	//Simulation loop
	while (true)
	{
		//Update position 
		X = Vox * Vt / g * (1 - exp(-g * time / Vt));
		Y = bSize + Vt / g * (Voy + Vt) * (1 - exp(-g * time / Vt)) - Vt * time;

		//Estimate the Velocity in X and Y 
		Vx = (X - Xlast) / TICK;
		Vy = (Y - Ylast) / TICK;

		//Print the Simulation Values on the screen 
			printf("t: % .2f X : % .2f Y : % .2f Vx : % .2f Vy : % .2f\n",time, X0 + X, Y, Vx, Vy);

		Xlast = X;	
		Ylast = Y;

		if ((Vy < 0) && (Y <= bSize))	//Detect ball collision with the ground 
		{
			KEx = 0.5 * Vx * Vx * (1 - loss);//Calculate the Kinetic energy in X and Y 
			KEy = 0.5 * Vy * Vy * (1 - loss);
			Vox = sqrt(2 * KEx);	//Calculate the new velocities using the kinetic energy calculated priviously 
			Voy = sqrt(2 * KEy);
			time = 0; //Reset time to zero 
			Y = bSize;	
			X0 += X;
			X = 0;
			Xlast = X;
			Ylast = Y;
		}
		if ((KEy < ETHR) | (KEx < ETHR)) break;//If the kinetic energy is smaller than the treshold terminate the program.
		time += TICK;//Update the time 
	}
	return 0;//Terminate the program
}
