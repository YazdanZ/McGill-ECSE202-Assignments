import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import acm.gui.DoubleField;
import acm.gui.IntField;
import acm.gui.TableLayout;

public class SetNumericFields 
{
	JPanel ThePanel;
	JLabel Name;
	JLabel minlabel;
	JLabel maxlabel;
	JLabel print;
	double dmin,dmax,imin,imax;
	DoubleField dfield;
	IntField Ifield;
	String namex;
	public SetNumericFields(String name,Double min,Double value,Double max)
	{
		ThePanel=new JPanel();
		ThePanel.setLayout(new TableLayout(1,5));
		Name = new JLabel(name);
		Name.setForeground(Color.blue);
		minlabel = new JLabel(min.toString()+"<");
		maxlabel = new JLabel("<"+max.toString());
		dfield = new DoubleField(value,min,max);
		print = new JLabel(value.toString());
		print.setForeground(Color.MAGENTA);
		
		/*
		 * Setting the Size of the tables 
		 */
		ThePanel.add(Name,"width=100");
		ThePanel.add(minlabel,"width=25");
		ThePanel.add(dfield,"width=70");
		ThePanel.add(maxlabel,"width=70");
		ThePanel.add(print,"width=70");
		dmin=min;
		dmax=max;
		namex=name;
	}
	public SetNumericFields(String name,Integer min,Integer value,Integer max)
	{
		ThePanel=new JPanel();
		ThePanel.setLayout(new TableLayout(1,5));
		Name = new JLabel(name);
		Name.setForeground(Color.blue);
		minlabel = new JLabel(min.toString()+"<");
		maxlabel = new JLabel("<"+max.toString());
		Ifield = new IntField(value,min,max);
		print = new JLabel(value.toString());
		print.setForeground(Color.MAGENTA);
		
		/*
		 * Setting the Size of the tables 
		 */
		ThePanel.add(Name,"width=100");
		ThePanel.add(minlabel,"width=25");
		ThePanel.add(Ifield,"width=70");
		ThePanel.add(maxlabel,"width=70");
		ThePanel.add(print,"width=70");
		imin=min;
		imax=max;
		namex=name;
	}

	/**
	 * This is a getter for the value entered in the Integer field 
	 * @return value entered in the intfield 
	 */
	public int getIValue()
	{
		return Ifield.getValue();
	}
	
	
	/**
	 * This is a getter for the value entered in the double field 
	 * @return The value of the double field 
	 */
	public double getDValue() 
	{
		return dfield.getValue();
	}

	/**
	 * this is a setter for the value of the numbers
	 * displayed next to the integer and double fields 
	 * @param Integer to set value 
	 */
	void setIValue(Integer arg)
	{
		print.setText(arg.toString());
	}
	
	
	/**
	 * This is a setter for the value of the number 
	 * displayed next to the Integer and double fields 
	 * @param Double to set value 
	 */
	void setDValue(Double arg)
	{
		print.setText(arg.toString());
	}
}
