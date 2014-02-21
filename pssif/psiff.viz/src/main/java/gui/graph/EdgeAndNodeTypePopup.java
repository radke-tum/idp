package gui.graph;

import graph.model.MyEdgeType;
import graph.model.MyNodeType;
import graph.operations.NodeAndEdgeTypeFilter;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.ModelBuilder;

public class EdgeAndNodeTypePopup {

	private JPanel NodePanel;
	private JPanel EdgePanel;
	private GraphVisualization graphViz;
	
	public EdgeAndNodeTypePopup(GraphVisualization graphViz)
	{
		this.graphViz = graphViz;
	}
	
	private void evalDialog (int dialogResult)
	{
		if (dialogResult==0)
    	{
    		LinkedList<MyNodeType> selectedNodes = new LinkedList<MyNodeType>();
    		// get all the values of the Nodes
        	Component[] attr = NodePanel.getComponents();       	
        	for (Component tmp :attr)
        	{
        		if ((tmp instanceof JCheckBox))
        		{
        			JCheckBox a = (JCheckBox) tmp;
        			
        			// compare which ones where selected
        			 if (a.isSelected())
        			 {
        				 MyNodeType b = ModelBuilder.getNodeTypes().getValue(a.getText());
        				 selectedNodes.add(b);
        			 }
        				
        		}	
        	}
        	
        	LinkedList<MyEdgeType> selectedEdges = new LinkedList<MyEdgeType>();
    		// get all the values of the edges
        	attr = EdgePanel.getComponents();       	
        	for (Component tmp :attr)
        	{
        		if ((tmp instanceof JCheckBox))
        		{
        			JCheckBox a = (JCheckBox) tmp;
        			
        			// compare which ones where selected
        			 if (a.isSelected())
        			 {
        				 MyEdgeType b = ModelBuilder.getEdgeTypes().getValue(a.getText());
        				 selectedEdges.add(b);
        			 }
        				
        		}	
        	}
    		
        	graphViz.applyNodeAndEdgeFilter(selectedNodes, selectedEdges);
    	}
	}
	
	public void showPopup()
	{
		JPanel allPanel = createPanel();
		
		int dialogResult = JOptionPane.showConfirmDialog(null, allPanel, "Choose the Edge and Node types", JOptionPane.DEFAULT_OPTION);
		
		evalDialog(dialogResult);
	}
	
	private JPanel createPanel()
	{
		MyEdgeType[] edgePossibilities = ModelBuilder.getEdgeTypes().getAllEdgeTypesArray();
		MyNodeType[] nodePossibilities = ModelBuilder.getNodeTypes().getAllNodeTypesArray();
		
		
		JPanel allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		NodePanel = new JPanel(new GridLayout(0, 1));
		
		int nodecounter=0;
		for (MyNodeType attr : nodePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			if (NodeAndEdgeTypeFilter.getVisibleNodeTypes().contains(attr))
			{
				choice.setSelected(true);
				nodecounter++;
			}
			else
				choice.setSelected(false);
			NodePanel.add(choice);
		}
		
		EdgePanel = new JPanel(new GridLayout(0, 1));
		
		int edgecounter =0;
		for (MyEdgeType attr : edgePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			if (NodeAndEdgeTypeFilter.getVisibleEdgeTypes().contains(attr))
			{
				choice.setSelected(true);
				edgecounter++;
			}
			else
				choice.setSelected(false);
			EdgePanel.add(choice);
		}
		JScrollPane scrollNodes = new JScrollPane(NodePanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollNodes.setPreferredSize(new Dimension(200, 400));
			    
	    JScrollPane scrollEdges = new JScrollPane(EdgePanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollEdges.setPreferredSize(new Dimension(200, 400));
		
		final JCheckBox selectAllNodes = new JCheckBox("Select all Node Types");
	    
	    selectAllNodes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
		      {
		        if (selectAllNodes.isSelected())
		        {
		          Component[] attr = NodePanel.getComponents();
		          for (Component tmp : attr) {
		            if ((tmp instanceof JCheckBox))
		            {
		              JCheckBox a = (JCheckBox)tmp;
		              
		              a.setSelected(true);
		            }
		          }
		        }
		        else
		        {
		          Component[] attr = NodePanel.getComponents();
		          for (Component tmp : attr) {
		            if ((tmp instanceof JCheckBox))
		            {
		              JCheckBox a = (JCheckBox)tmp;
		              
		              a.setSelected(false);
		            }
		          }
		        }
		      }
	    });
	    final JCheckBox selectAllEdges = new JCheckBox("Select all Edge Types");
	    
	    selectAllEdges.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
	      {
	        if (selectAllEdges.isSelected())
	        {
	          Component[] attr = EdgePanel.getComponents();
	          for (Component tmp : attr) {
	            if ((tmp instanceof JCheckBox))
	            {
	              JCheckBox a = (JCheckBox)tmp;
	              
	              a.setSelected(true);
	            }
	          }
	        }
	        else
	        {
	          Component[] attr = EdgePanel.getComponents();
	          for (Component tmp : attr) {
	            if ((tmp instanceof JCheckBox))
	            {
	              JCheckBox a = (JCheckBox)tmp;
	              
	              a.setSelected(false);
	            }
	          }
	        }
	      }
	    });
		
	    if (nodecounter== nodePossibilities.length)
	    	selectAllNodes.setSelected(true);
	    else
	    	selectAllNodes.setSelected(false);
	    
	    if (edgecounter== edgePossibilities.length)
	    	selectAllEdges.setSelected(true);
	    else
	    	selectAllEdges.setSelected(false);
	    
		c.gridx = 0;
		c.gridy = 0;
		allPanel.add(new JLabel("Choose Node Types"),c);
		c.gridx = 1;
		c.gridy = 0;
		allPanel.add(new JLabel("Choose Connection Types"),c);
	    c.gridx = 0;
	    c.gridy = 1;
	    allPanel.add(scrollNodes, c);
	    c.gridx = 1;
	    c.gridy = 1;
	    allPanel.add(scrollEdges, c);
	    c.gridx = 0;
	    c.gridy = 2;
	    allPanel.add(selectAllNodes, c);
	    c.gridx = 1;
	    c.gridy = 2;
	    allPanel.add(selectAllEdges, c);
		
		
		allPanel.setPreferredSize(new Dimension(400,500));
		allPanel.setMaximumSize(new Dimension(400,500));
		allPanel.setMinimumSize(new Dimension(400,500));
		
    	return allPanel;
    	
	}
}
