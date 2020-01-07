/*
 ============================================================================
 Name        : database.c
 Author      : Yazdan Zinati
 Version     : 5.1.4
 Copyright   : Your copyright notice
 Description : A simple program to manage a small database of student
			 : records using B-Trees for storage. This program responds to
			 : the following commands:
			 :  LN     List all the records in the database ordered by Last name.
			 :  LI     List all the records in the database ordered by student ID.
			 :  FN     Prompts for a name and lists the record of the student with the corresponding name.
			 :  FI     Prompts for a name and lists the record of the student with the corresponding ID.
			 :  HELP   Prints this list.
			 :  ?      Prints this list.
			 :  Q      Exits the program.
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAXLEN 20
#define false 0	//false 
#define true !false	//true 

int search_match; //this boolean turns to 1 when the searched name or id matches the entered one 
int ext = 0; //we use this boolean in our main loop  

struct SRecord // The student record object
{
	char Last[MAXLEN]; 	//Last Name 
	char First[MAXLEN]; //First Name 
	int ID, marks;		//defining the ID and marks 
	struct SRecord* right; 	//right child 
	struct SRecord* left;	//left child 
};

struct SRecord *makeNode(struct SRecord *data)//the makenode funtion
{
	struct SRecord *newnode = (struct SRecord*) malloc(sizeof(struct SRecord));
	(newnode)->ID = data->ID;
	(newnode)->marks = data->marks;
	(newnode)->left = NULL;
	(newnode)->right = NULL;
	strcpy((newnode)->First, data->First);
	strcpy((newnode)->Last, data->Last);
	return newnode;
}


/*
adds a new node by decending to the leaf node using 
a while loop insted of a recursion- Iterative method 
*/
struct SRecord *addNodeID(struct SRecord *root, struct SRecord *data)// adds the nodes for IDs the the BN 
{
	struct SRecord *current;
	if (root == NULL)//empty tree 
	{
		root = makeNode(data);
	}
	/*
	If not empty decend to the leaf node
	according to the input data 
	*/
	else
	{
		current = root;
		while (true)
		{
				if (data->ID < (current)->ID)//Id < id at node, branch left 
				{
					if (current->left == NULL)//leaf node 
					{
						current->left = makeNode(data);//attach a new node here 
						break;
					}
					else//otherwise: 
					{
						current = current->left;//keep traversing 
					}
				}
				else//new ID >= id at node, branch right 
				{
					if (current->right == NULL)//leaf node 
					{
						current->right = makeNode(data);//atyach a node here 
						break;
					}
					else//otherwise 
					{
						current = current->right;//keep traversing 
					}
				}
		}
	} return root;
}
/*
adds a new node by decending to the leaf node using 
a while loop insted of a recursion- Iterative method 
*/
struct SRecord *addNodename(struct SRecord* root, struct SRecord* data)//Creates the nodes for the names in the Tree 
{
	struct SRecord *current;
	if (root == NULL)//empty tree 
	{
		root = makeNode(data);
	}
	/*
	If not empty decend to the leaf node
	according to the input data 
	*/
	else
	{
		current = root;
		while (true)
		{
			if (strcmp(data->Last, (current)->Last) < 0)
			{
				if (current->left == NULL)//leaf node 
				{
					current->left = makeNode(data);//attach a new node here 
					break;
				}
				else
				{
					current = current->left;//otherwise keep traversing 
				}
			}
			else
			{
				if (current->right == NULL)//leaf node 
				{
					current->right = makeNode(data);//attach a node here 
					break;
				}
				else
				{
					current = current->right;//otherwise, keep traversing 
				}
			}
		}
	}return root;
}

void inorder(struct SRecord* root)//inorder Tree traversal 
{
	if (root == NULL) return;

	inorder(root->left);
	{
		printf("%-15s %-15s %d \t %d \n", root->First, root->Last, root->ID, root->marks);
	}
	inorder(root->right);
}
void search_Name(struct SRecord* root, char* sName)//Name search function 
{
	if (root->left != NULL) search_Name(root->left, sName);

	if (strcasecmp(sName, root->Last) == 0)	//if the name entered is equal to the corresponding index 
	{
		search_match = 1; //This boolean turns to 1 when the  name is found 
		printf("Student Name: %s %s \n Student ID: %d \n Total Grade: %d \n", root->First, root->Last, root->ID, root->marks);//print this student's record 
	}

	if (root->right != NULL) search_Name(root->right, sName);
}
void search_ID(struct SRecord* root, int sID)//ID search function 
{
	if (root->left != NULL) search_ID(root->left, sID);

	if (root->ID == sID)
	{
		search_match = 1;//This boolean turns to 1 when the entered ID is found in the database 
		printf("Student Name: %s %s \n Student ID: %d \n Total Grade: %d \n", root->First, root->Last, root->ID, root->marks);//Print the records of the student corresponding to this ID
	}

	if (root->right != NULL) search_ID(root->right, sID);
}

void help() //Help function, prints command list
{
	printf("LN List all the records in the database ordered by last name.\n");
	printf("LI List all the records in the database ordered by student ID.\n");
	printf("FN Prompts for a name and lists the record of the student with the corresponding name.\n");
	printf("FI Prompts for a name and lists the record of the student with the Corresponding ID.\n");
	printf("HELP Prints this list.\n");
	printf("? Prints this list.\n");
	printf("Q and Quit Exit the program.\n\n");

	return;
}
int main(int argc, char* argv[]) // Main entry point is here.  Program uses the standard Command Line Interface
{

	// Internal declarations:

	FILE* NAMESIDS;        // File descriptor (an object)!
	FILE* MARKS;           // Will have two files open

	struct SRecord* rName, * rID; //root name and root ID 
	struct SRecord data;

	// Argument check for the program to have 3 arguments 
	if (argc != 3)
	{
		printf("Usage: sdb [Names+IDs] [marks] \n");
		return -1;
	}


	/* Attempt to open the user-specified file.  If no file with
	 the supplied name is found, exit the program with an error
`	 message.*/
	if ((NAMESIDS = fopen(argv[1], "r")) == NULL)
	{
		printf("Can't read from file %s\n", argv[1]);//Error msg when the NamesIDs.txt file DNE
		return -2;
	}

	if ((MARKS = fopen(argv[2], "r")) == NULL)
	{
		printf("Can't read from file %s\n", argv[2]);//Error msg generated when marks.txt DNE 
		fclose(NAMESIDS);
		return -3;
	}

	// Initialize B-Trees by creating the binary tree root pointers and setting them to null:
	rName = NULL;
	rID = NULL;

	//  Read through the NamesIDs and marks files, record by record.
	int NumRecords = 0;

	printf("Building database...\n"); //print this message 


	while (fscanf(NAMESIDS, "%s%s%d", &(data.First[0]), &(data.Last[0]), &(data.ID)) != EOF) //EOD stands for "End of line" 
	{
		//  Read in the data.  If the files are not the same length, the shortest one terminates reading.
		fscanf(MARKS, "%d", &(data.marks));
		NumRecords++;

		//add the two addName and addID functions in this space after the creation using a anon iterative method 
		rName = addNodename(rName, &data);
		if (&rName == NULL)
		{
			printf("Error\n");//Error msg when the rootname is null 
			return -4;
		}

		rID = addNodeID(rID, &data);
		if (&rID == NULL)
		{
			printf("Error\n");//Error msg when the rootID is null
			return -5;
		}
	}
	// Close files once we're done
	fclose(NAMESIDS);
	fclose(MARKS);

	printf("Finished, %d records found...\n", NumRecords);//print  the number of records found 

	char cmd[20]; ///used for reading the commands 

	while (ext == 0)//while this integer (that we use as a bool is 0 the program will run)
	{
		printf("sdb: ");						
		scanf("%s", cmd);					  // read command (read cmd)


		// List by Name
		if (strcasecmp(cmd, "LN") == 0) // List all records sorted by name
		{
			if (rName == NULL) printf("The root name is NULL");
			printf("Student Record Database sorted by Last Name\n\n");
			inorder(rName);
			printf("\n");
		}

		// List by ID
		else if (strcasecmp(cmd, "LI") == 0) // List all records sorted by ID
		{
			if (rID == NULL) printf("rootID is NULL");
			printf("Student Record Database sorted by Student ID\n\n");
			inorder(rID);
			printf("\n");
		}

		// Find record that matches Name
		else if (strcasecmp(cmd, "FN") == 0)
		{
			search_match = 0;//This integer is 0 as long as the entered name hasn't been found 
			char sName[10];
			printf("Enter name to search: ");//promt the user to enter the name of a name 
			scanf("%s", sName);
			search_Name(rName, sName);//run the name search function 

			//if we don't find a match for the entered name
			if (search_match == 0)printf("There is no student with that name.\n");//If the name is not found print this msg 
		}


		// Find record that matches ID
		else if (strcasecmp(cmd, "FI") == 0)
		{
			search_match = 0;//This integer is 1 as long as the entered ID, hasn't been found 
			int sID;
			printf("Enter ID to search: ");//prompt the user to enter a ID 
			scanf("%d", &sID);
			search_ID(rID, sID);//execute the ID search funtion 

			//If we don't find a match for the entered ID 
			if (search_match == 0)printf("There is no student with that ID.\n");//Print this msg if the ID id not found 
		}

		// Help
		else if ((strcasecmp(cmd, "HELP") == 0) || (strcasecmp(cmd, "?") == 0))
		{
			help();//execute the help funtion 
		}


		// Quit
		else if ((strcasecmp(cmd, "Quit") == 0)||(strcasecmp(cmd, "Q") == 0)) // Quit
		{
			printf("Program terminated...\n");//print this msg before quitting the program 
			return 0;//Quit the program 
		}

		// Command not understood
		else	//If the command is not defined : 
		{
			printf("Command not understood.\n"); 
		}
	}

}
