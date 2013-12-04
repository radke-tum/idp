package gui;

import graph.model.ConnectionType;
import graph.model.MyEdge;
import graph.model.MyNode;
import graph.model.NodeType;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.LinkedList;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import matrix.model.MatrixBuilder;

public class MatrixVisualization {

	//private GridLayout experimentLayout;
	private JPanel matrixPanel;
	private MatrixBuilder mbuilder;

	public MatrixVisualization() {

		matrixPanel = new JPanel();
		mbuilder = new MatrixBuilder();
		
	}
	
	
	
	
	private void drawMatrix()
	{
		LinkedList<MyEdge> edges = mbuilder.findRelevantEdges();
		LinkedList<MyNode> nodes=  mbuilder.findRelevantNodes();
		matrixPanel = drawPanels(nodes,edges);
	}
	
	private JPanel drawPanels(LinkedList<MyNode> nodes, LinkedList<MyEdge> edges)
	{
		JPanel Content = new JPanel();
		createMatrixContent(Content, nodes, edges);
		
		return Content;
	}
	
	
	private void createMatrixContent (JPanel p, LinkedList<MyNode> nodes, LinkedList<MyEdge> edges)
	{
		if (nodes.size()>0 && edges.size()>0)
		{
			String[] legend = new String[nodes.size()];
			
			int counter =0;
			// create Legend
			for (MyNode n : nodes)
			{
				legend[counter] = n.getName();
				counter++;				
			}
			
			String[][] results = mbuilder.getEdgeConnections(nodes, edges);


			JTable mainTable = new JTable(results,legend);
			JScrollPane scrollPane = new JScrollPane(mainTable);
			JTable rowTable = new RowLegendTable(mainTable);
			
			//scrollPane.add(rowTable);
			scrollPane.setRowHeaderView(rowTable);
			//scrollPane.setPreferredSize(mainTable.getSize());
			//scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,rowTable.getTableHeader());
			
			mainTable.setEnabled(false);
			mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableColumnAdjuster tca = new TableColumnAdjuster(mainTable);
			tca.adjustColumns();
			
			p.add(scrollPane);
			
		}
	}
	
	public JPanel getVisualization()
	{
		boolean display = true;
		
		if (mbuilder.getRelevantNodeTypes()==null || mbuilder.getRelevantEdgesTypes() ==null ||
				(mbuilder.getRelevantNodeTypes()!=null && mbuilder.getRelevantNodeTypes().size()==0) ||
				(mbuilder.getRelevantEdgesTypes() !=null && mbuilder.getRelevantEdgesTypes().size()==0))
			display =chooseNodes();
		
		if (display)
		{
			drawMatrix();
			return matrixPanel;
		}
		else
			return new JPanel();
	}
	
	/**
	 * Choose Nodes Popup
	 * @return
	 */
	public boolean chooseNodes()
	{
		ConnectionType[] edgePossibilities = ConnectionType.values();
		NodeType[] nodePossibilities = NodeType.values();
		
		
		JPanel allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel NodePanel = new JPanel(new GridLayout(0, 1));
		
		for (NodeType attr : nodePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			if (mbuilder.getRelevantNodeTypes()!=null && mbuilder.getRelevantNodeTypes().contains(attr))
				choice.setSelected(true);
			else
				choice.setSelected(false);
			NodePanel.add(choice);
		}
		
		JPanel EdgePanel = new JPanel(new GridLayout(0, 1));
		
		for (ConnectionType attr : edgePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			if (mbuilder.getRelevantEdgesTypes()!=null && mbuilder.getRelevantEdgesTypes().contains(attr))
				choice.setSelected(true);
			else
				choice.setSelected(false);
			EdgePanel.add(choice);
		}
		
		c.gridx = 0;
		c.gridy = 0;
		allPanel.add(new JLabel("Choose Node Types"),c);
		c.gridx = 1;
		c.gridy = 0;
		allPanel.add(new JLabel("Choose Connection Types"),c);
		c.gridx = 0;
		c.gridy = 1;
		allPanel.add(NodePanel,c);
		c.gridx = 1;
		c.gridy = 1;
		allPanel.add(EdgePanel,c);
		
		
		allPanel.setPreferredSize(new Dimension(200,500));
		allPanel.setMaximumSize(new Dimension(200,500));
		allPanel.setMinimumSize(new Dimension(200,500));
		JScrollPane p = new JScrollPane (allPanel, 
	            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
	            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		/*p.setSize(200, 500);
		p.setPreferredSize(200,500);*/
		//p.add(allPanel);
				JComponent[] inputs = new JComponent[] {
						p//allPanel
		    	};
		
		int dialogResult = JOptionPane.showConfirmDialog(null, p, "Choose the attributes", JOptionPane.DEFAULT_OPTION);
    	
    	if (dialogResult==0)
    	{
    		if (mbuilder.getRelevantNodeTypes() ==null)
    			mbuilder.setRelevantNodeTypes(new LinkedList<NodeType>());
    		if (mbuilder.getRelevantEdgesTypes() == null)
    			mbuilder.setRelevantEdgesTypes(new LinkedList<ConnectionType>());
    		
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
        				 NodeType b = NodeType.getValue(a.getText());
        				 if (!mbuilder.getRelevantNodeTypes().contains(b))
        					 mbuilder.getRelevantNodeTypes().add(b);
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
        				 ConnectionType b = ConnectionType.getValue(a.getText());
        				 if (!mbuilder.getRelevantEdgesTypes().contains(b))
        					 mbuilder.getRelevantEdgesTypes().add(b);
        			 }
        				
        		}	
        	}
    		
        	System.out.println("selected Node Types "+mbuilder.getRelevantNodeTypes().size());
        	System.out.println("selected Edge Types "+mbuilder.getRelevantEdgesTypes().size());
        	// can we display something
    		if (mbuilder.getRelevantEdgesTypes().size()>0 && mbuilder.getRelevantNodeTypes().size()>0)
    			return true;
    		else
    			return false;
    	}
    	else
    	{
    		return false;
    	}
    	
    	
	}
	
}