package gui.graph;



import graph.model2.MyNodeType;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class MyListColorRenderer extends DefaultListCellRenderer  
	{  
	private static final long serialVersionUID = 1L;
	
		//private HashMap theChosen = new HashMap();
	    private HashMap<MyNodeType, Color> colorMapper;

	    public Component getListCellRendererComponent( JList list,  
	    		Object value, int index, boolean isSelected,  
	            boolean cellHasFocus )  
	    {  
	        super.getListCellRendererComponent( list, value, index,  
	                isSelected, cellHasFocus );  
	        
	       /* if( isSelected )  
	        {  
	            theChosen.put( value, "chosen" );  
	        }  */
	        
	        //System.out.println("getListCellRendererComponent ");
	        
	        MyNodeType t = (MyNodeType) value;
	       // System.out.println(t);
	        if( colorMapper.containsKey( t ) )  
	        {  
	            Color c = colorMapper.get(t);
	           // System.out.println("Color "+c);
	            
	            setBackground(c);
	        }  


	        return( this );  
	    }
	    
	    public void setColor (MyNodeType type , Color c)
	    {
	    	 System.out.println("Put Color "+c);
	    	this.colorMapper.put(type, c);
	    }
	    
	    public HashMap<MyNodeType, Color> getColorMapping()
	    {
	    	return this.colorMapper;
	    }
	    
	    public MyListColorRenderer()
	    {
	    	super();
	    	colorMapper = new HashMap<MyNodeType, Color>();
	    	
	    }
	    
	    public void setColors(HashMap<MyNodeType,Color> map)
	    {
	    	this.colorMapper = map;	
	    }
}
