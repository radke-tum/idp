package gui.checkboxtree;

import graph.model.MyEdgeType;
import graph.model.MyNodeType;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import model.ModelBuilder;

/**
 * A Tree to selected Edge Types. Provides already certain type of functionality
 * @author Luc
 *
 */
public class CheckBoxTree{
	
	private LinkedList<MyEdgeType> selectedValues;
	
	/**
	 * create a new Instance
	 */
	public CheckBoxTree(){
		selectedValues = new LinkedList<MyEdgeType>();
	}
	
	/**
	 * Create a new JTree with checkboxes
	 * @param edges a Map(Name of the EdgeType, EdgeType Object) which should be displayed in the Tree
	 * @param selectedEdges which edges where already previously selected. So the should now also be selected
	 * @return a new JTree with the appropriated preferences
	 */
	public JTree createTree(TreeMap<String,LinkedList<MyEdgeType>> edges, LinkedList<MyEdgeType> selectedEdges)
	{
		selectedValues = selectedEdges;
		
		// get number of entries in the Treemap
		int counter =0;
		
		for (Entry<String, LinkedList<MyEdgeType>> tmp : edges.entrySet())
		{
			// the key
			counter++;
			// the values
			counter = counter +tmp.getValue().size();
		}
		
		// don't miss the root
		counter++;
		
		//create the Tree
		CheckNode[] nodes = new CheckNode[counter];
		// the root
		nodes[0] = new CheckNode("All EdgeTypes");
		
		// add all the others
		int nodeCounter =1;
		for (Entry<String, LinkedList<MyEdgeType>> tmp : edges.entrySet())
		{
			// add the parent
			String parentName = tmp.getKey();
			nodes[nodeCounter] = new CheckNode(parentName);
			
			int tmpCounter = nodeCounter;
			nodeCounter++;
			
			// add the children
			LinkedList<MyEdgeType> children = tmp.getValue();
			for (MyEdgeType et : children)
			{
				nodes[nodeCounter] = new CheckNode(et.getName());
				if (selectedEdges.contains(et))
					nodes[nodeCounter].setSelected(true);
				else
					nodes[nodeCounter].setSelected(false);
				// connect parent and child
				nodes[tmpCounter].add(nodes[nodeCounter]);
				nodeCounter++;
			}
			
			// connect parent and root
			nodes[0].add(nodes[tmpCounter]);
		}
		
	    JTree tree = new JTree( nodes[0] );
	    tree.setCellRenderer(new CheckBoxRenderer());
	   
	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
	    tree.putClientProperty("JTree.lineStyle", "Angled");
	    tree.addMouseListener(new NodeSelectionListener(tree));
	    
	    return tree;
	}
	
	/**
	 * create a new JTree. No Edge Type should be selected from the beginning
	 * @param edges a Map(Name of the EdgeType, EdgeType Object) which should be displayed in the Tree
	 * @return a new JTree with the appropriated preferences
	 */
	public JTree createTree(TreeMap<String,LinkedList<MyEdgeType>> edges)
	{
		return createTree(edges, new LinkedList<MyEdgeType>());
	}
	
	/**
	 * Get all the selected Edge Types from the JTree
	 * @return a list with all the selected Edge Types
	 */
	public LinkedList<MyEdgeType> evalTree()
	{
		System.out.println("------------");
		for(MyEdgeType et : selectedValues)
		{
			System.out.println(et.getName()); 
		}
		System.out.println("------------");
		return selectedValues;
	}
	
	/**
	 * Check if the the given Checkbox is selected or not. Evaluate this event 
	 * @param node the checkbox which changed
	 */
	private void evalMouseSelection(CheckNode node)
	{
	 	String name =String.valueOf(node.getUserObject());

	    if (node.isLeaf()) {
	    	
	       MyEdgeType et = ModelBuilder.getEdgeTypes().getValue(name);
	       if(et!=null)
	       {
	    	   if(node.isSelected)
	    	   {
	    		   //System.out.println("add "+et.getName());
		    	   selectedValues.add(et);
	    	   }
	    	   else
	    	   {
	    		 //  System.out.println("remove "+et.getName());
				   selectedValues.remove(et);
	    	   } 
	       }
	    }
	    else
	    {
	    	// the checkbox was an parent Type. So his children have to be checked
	    	for (int i=0;i<node.getChildCount();i++)
	    	{
	    		CheckNode child = (CheckNode) node.getChildAt(i);
	    		evalMouseSelection(child);
	    	}
	    }
	}
	
	/**
	 * Provides some basic functionalities in the Tree Gui
	 * @author Luc
	 *
	 */
	class NodeSelectionListener extends MouseAdapter {
    JTree tree;
    
    NodeSelectionListener(JTree tree) {
      this.tree = tree;
    }
    
    public void mouseClicked(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      int row = tree.getRowForLocation(x, y);
      TreePath  path = tree.getPathForRow(row);
      //TreePath  path = tree.getSelectionPath();
      if (path != null) {
        CheckNode node = (CheckNode)path.getLastPathComponent();
        boolean isSelected = ! (node.isSelected());
        node.setSelected(isSelected);
        if (node.getSelectionMode() == CheckNode.DIG_IN_SELECTION) {
          if ( isSelected) {
            tree.expandPath(path);
          } else {
            tree.collapsePath(path);
          }
        }
        ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
        // I need revalidate if node is root.  but why?
        if (row == 0) {
          tree.revalidate();
          tree.repaint();
        }
        // which edgeType is now really selected
        evalMouseSelection (node);
      }
    }
  }
}

