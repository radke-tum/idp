package gui.graph;

import graph.model2.MyEdgeType;

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

public class HighlightNodePopup {
	
	private JPanel popupPanel;
	private JPanel edgePanel;
	private GraphVisualization graphViz;
	
	public HighlightNodePopup(GraphVisualization graphViz)
	{
		this.graphViz = graphViz;
	}
	
	private JPanel createPanel()
	{
		MyEdgeType[] edgePossibilities = ModelBuilder.getEdgeTypes().getAllEdgeTypesArray();
		
		JPanel allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		edgePanel = new JPanel(new GridLayout(0, 1));
		
		LinkedList<MyEdgeType> highlightNodes = graphViz.getHighlightNodes();
		int allcounter =0;
		for (MyEdgeType attr : edgePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			if (highlightNodes!=null && highlightNodes.contains(attr))
			{
				choice.setSelected(true);
				allcounter++;
			}
			else
				choice.setSelected(false);
			edgePanel.add(choice);
		}
		
		
		JScrollPane scrollEdges = new JScrollPane(edgePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollEdges.setPreferredSize(new Dimension(200, 400));
		
		final JCheckBox selectAllEdges = new JCheckBox("Select all Edge Types");
	    

		selectAllEdges.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
	        if (selectAllEdges.isSelected())
	        {
	          Component[] attr = edgePanel.getComponents();
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
	          Component[] attr = edgePanel.getComponents();
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
	    
	    // set Select all edges Checkbox to true if all where selected
	    if (allcounter!=0 && allcounter ==highlightNodes.size() )
	    	selectAllEdges.setSelected(true);
		
		
		c.gridx = 1;
		c.gridy = 0;
		allPanel.add(new JLabel("Choose Connection Types"),c);

		c.gridx = 1;
		c.gridy = 1;
		allPanel.add(scrollEdges,c);
		
		c.gridx = 1;
		c.gridy = 2;
		allPanel.add(selectAllEdges,c);
		
		allPanel.setPreferredSize(new Dimension(200,500));
		allPanel.setMaximumSize(new Dimension(200,500));
		allPanel.setMinimumSize(new Dimension(200,500));

		return allPanel;
	}
	
	private void evalDialog (int dialogResult)
	{
		LinkedList<MyEdgeType> res = new LinkedList<MyEdgeType>();
		
		if (dialogResult==0)
    	{
    		
    		// get all the values of the edges
    		Component[] attr = edgePanel.getComponents();       	
        	for (Component tmp :attr)
        	{
        		//System.out.println("test");
        		if ((tmp instanceof JCheckBox))
        		{
        			JCheckBox a = (JCheckBox) tmp;
        		//	System.out.print(a.getText()+"  ");
        		//	System.out.println(a.isSelected());
        			
        			// compare which ones where selected
        			 if (a.isSelected())
        			 {
        		//		 System.out.println(a.getText());
        				 MyEdgeType b = ModelBuilder.getEdgeTypes().getValue(a.getText());
        				 res.add(b);
        			 }
        				
        		}	
        	}
    	}
		graphViz.setHighlightNodes(res);
	}
	
	public void showPopup()
	{
		popupPanel = createPanel();
		
		int dialogResult = JOptionPane.showConfirmDialog(null, popupPanel, "Choose the highlighted Edge types", JOptionPane.DEFAULT_OPTION);
		
		evalDialog(dialogResult);
	}
}
