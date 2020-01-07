/**
 * This bTree class is used for 
 * sorting aBall object
 * here we want o sort the balls in decending 
 * order based on their Sizes from left top right
 * balls with approximatly same Size will be in 
 * the same category and will be Stacked one on the other 
 * 
 * @author Yazdan Zinati
 *
 */

public class bTree 
{	
	
	/**
	 * This method is a wrapper for sNode 
	 * @param data
	 * @return void
	 */
	public void addNode(aBall data)
	{
		root=sNode(root , data);
	}
	
	
	
	/**
	 * This method adds new entries into bTree
	 * @param root
	 * @param data
	 * @return root 
	 */
	private bNode sNode(bNode root, aBall data)
	{
		if (root == null)
		{
			bNode node = new bNode();
			node.data = data;
			node.left = null;
			node.right = null;
			root=node;
			return root;
		}
		else if(data.bSize<root.data.bSize)
		{
			root.left=sNode(root.left , data);
		}
		else
		{
			root.right = sNode(root.right , data);
		}
		
		return root;
	}
	
	
	/**
	 * in_order method - in_order traversal via call to recursive method
	 * @param void
	 * @retun void 
	 */
	public void in_order()
	{
		traverse_inorder(root);
	}
	
	
	/**
	 * traverse_inorder - recursively traverses tree in order(LEFT-Root-RIGHT) and prints each node 
	 * @param root
	 * @retun void
	 */
	private void traverse_inorder(bNode root)
	{
		if(root.left!=null) traverse_inorder(root.left);
		System.out.println(root.data.bSize);
		if(root.right!=null) traverse_inorder(root.right);
	}
	
	
	/**
	 * isRunning method determines weather or not the simnulation is still running 
	 * @return simulation running status
	 */
	boolean isRunning()
	{
		run=false;
		Scan(root);
		return run;
	}
	
	/**
	 * 
	 * @param root
	 */
	void Scan(bNode root)
	{
		if(root.left!=null) Scan(root.left);
		if(root.data.RunState) run =true;
		if(root.right!=null) Scan(root.right);
	}
	
	
	
	/**
	 * stackBalls sorts balls by diameter from left to right
	 * @retun void  
	 */
	void stackBalls()
	{
		Y1=0;
		X1=0;
		Move(root);
	}
	double lastSize=0;
	double X1,Y1=0;
	void Move(bNode root)
	{
		if(root.left!=null)
		{
			Move(root.left);
		}
		
		if(root.data.bSize-lastSize>DELTASIZE)	//if the ball is not in the same size range:
		{
			X1=X1+root.data.bSize*2;			//put this mew ball next to the previous stack 
			Y1=root.data.bSize*2;				//put the ball on the ground line 
		}
		else							//if the ball is in the same size range:
		{
			Y1+=root.data.bSize*2;		//put the ball with the same size on top of the balls with the same size range
		}
		root.data.moveTo(X1,Y1);
		lastSize=root.data.bSize;
		if(root.right!=null)
		{
			Move(root.right);
		}
	}
	
	/**
	 * A simple bNode class for use by bTree
	 * @author Yazdan Zinati 
	 *
	 */
	
	public class bNode 
	{
		aBall data;
		bNode right;
		bNode left;
	}
	
	
	//instance variable:
	bNode root = null;
	boolean run;
	double X0;
	double DELTASIZE = 0.1;
	
}
