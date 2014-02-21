package gui.matrix;

import graph.model.MyEdgeType;
import graph.model.MyNodeType;

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

import matrix.model.MatrixBuilder;
import model.ModelBuilder;

public class MatrixChooseNodeAndEgeTypePopup {
	
	private JPanel NodePanel;
	private JPanel EdgePanel;
	private MatrixBuilder mbuilder;
	
	public MatrixChooseNodeAndEgeTypePopup(MatrixBuilder mbuilder)
	{
		this.mbuilder =mbuilder; 
	}
	
	private boolean evalDialog (int dialogResult)
	{
		if (dialogResult==0)
    	{
    		System.out.println("new select of Nodes");
    		if (mbuilder.getRelevantNodeTypes() ==null)
    			mbuilder.setRelevantNodeTypes(new LinkedList<MyNodeType>());
    		if (mbuilder.getRelevantEdgesTypes() == null)
    			mbuilder.setRelevantEdgesTypes(new LinkedList<MyEdgeType>());
    		
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
        				 if (!mbuilder.getRelevantNodeTypes().contains(b))
        					 mbuilder.getRelevantNodeTypes().add(b);
        			 }
        			 else
        			 {
        				 MyNodeType b = ModelBuilder.getNodeTypes().getValue(a.getText());
        				 if (mbuilder.getRelevantNodeTypes().contains(b))
        					 mbuilder.getRelevantNodeTypes().remove(b);
        			 }
        				
        		}	
        	}
        	
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
        				 if (!mbuilder.getRelevantEdgesTypes().contains(b))
        					 mbuilder.getRelevantEdgesTypes().add(b);
        			 }
        			 else
        			 {
        				 MyEdgeType b = ModelBuilder.getEdgeTypes().getValue(a.getText());
        				 if (mbuilder.getRelevantEdgesTypes().contains(b))
        					 mbuilder.getRelevantEdgesTypes().remove(b);
        			 }
        				
        		}	
        	}
    		
        	System.out.println("selected Node Types "+mbuilder.getRelevantNodeTypes().size());
        	System.out.println("selected Edge Types "+mbuilder.getRelevantEdgesTypes().size());
        	// can we display something
    		if (mbuilder.getRelevantEdgesTypes().size()>0 && mbuilder.getRelevantNodeTypes().size()>0)
    			return true;
    	}

		return false;
	}
	
	public boolean showPopup()
	{
		JPanel allPanel = createPanel();
		
		int dialogResult = JOptionPane.showConfirmDialog(null, allPanel, "Choose the Edge and Node types", JOptionPane.DEFAULT_OPTION);
		
		return evalDialog(dialogResult);
	}
	
	private JPanel createPanel()
	{
		MyEdgeType[] edgePossibilities = ModelBuilder.getEdgeTypes().getAllEdgeTypesArray();
		MyNodeType[] nodePossibilities = ModelBuilder.getNodeTypes().getAllNodeTypesArray();
		
		
		JPanel allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		NodePanel = new JPanel(new GridLayout(0, 1));
		
		for (MyNodeType attr : nodePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			if (mbuilder.getRelevantNodeTypes()!=null && mbuilder.getRelevantNodeTypes().contains(attr))
				choice.setSelected(true);
			else
				choice.setSelected(false);
			NodePanel.add(choice);
		}
		
		EdgePanel = new JPanel(new GridLayout(0, 1));
		
		for (MyEdgeType attr : edgePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			if (mbuilder.getRelevantEdgesTypes()!=null && mbuilder.getRelevantEdgesTypes().contains(attr))
				choice.setSelected(true);
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
