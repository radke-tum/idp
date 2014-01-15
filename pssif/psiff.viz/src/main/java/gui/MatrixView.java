package gui;

import graph.model2.MyEdge2;
import graph.model2.MyEdgeType;
import graph.model2.MyNode2;
import graph.model2.MyNodeType;
import gui.matrix.RowLegendTable;
import gui.matrix.TableColumnAdjuster;
import gui.matrix.VerticalTableHeaderCellRenderer;

import java.awt.Color;

import java.awt.BorderLayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import matrix.model.MatrixBuilder;
import matrix.model.XMLExport;
import model.ModelBuilder;

public class MatrixView {

	//private GridLayout experimentLayout;
	private JPanel matrixPanel;
	private MatrixBuilder mbuilder;
	private String[][] content;
	private LinkedList<MyNode2> nodes;
	private LinkedList<MyEdge2> edges;
	private XMLExport xml_exporter;
	private boolean active;

	public MatrixView() {
		this.matrixPanel = new JPanel();
		this.mbuilder = new MatrixBuilder();
		this.xml_exporter = new XMLExport();
		active = false;	
	}
/*<<<<<<< HEAD
=======

>>>>>>> refs/remotes/origin/attempt3*/
	
	private void drawMatrix()
	{
		edges = mbuilder.findRelevantEdges();
		nodes=  mbuilder.findRelevantNodes();
		matrixPanel = drawPanels(nodes,edges);
	}
	
	private JPanel drawPanels(LinkedList<MyNode2> nodes, LinkedList<MyEdge2> edges)
	{
		JPanel Content = new JPanel(new BorderLayout());
		createMatrixContent(Content, nodes, edges);
		
		
		return Content;
	}
	
	
	private void createMatrixContent (JPanel p, LinkedList<MyNode2> nodes, LinkedList<MyEdge2> edges)
	{
		if (nodes.size()>0 && edges.size()>0)
		{
			String[] legend = new String[nodes.size()];
			
			int counter =0;
			// create Legend
			for (MyNode2 n : nodes)
			{
				legend[counter] = n.getName();
				counter++;				
			}
			
			content = mbuilder.getEdgeConnections(nodes, edges);


			JTable mainTable = new JTable(content,legend);
			JScrollPane scrollPane = new JScrollPane(mainTable);
			
			
			/*Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (int) (screenSize.width*0.85);
			int y = (int) (screenSize.height*0.9);
			
			Dimension d = new Dimension(x,y);
			scrollPane.setPreferredSize(d);*/
			
			
			JTable rowTable = new RowLegendTable(mainTable);
			
			//scrollPane.add(rowTable);
			scrollPane.setRowHeaderView(rowTable);
			//scrollPane.setPreferredSize(mainTable.getSize());
			//scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,rowTable.getTableHeader());
			
			mainTable.setEnabled(false);
			mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableColumnAdjuster tca = new TableColumnAdjuster(mainTable);
			tca.adjustColumns();
			
			TableCellRenderer headerRenderer = new VerticalTableHeaderCellRenderer();
		    Enumeration<TableColumn> columns = mainTable.getColumnModel().getColumns();
		      while (columns.hasMoreElements())
		      {
		        TableColumn tc = (TableColumn)columns.nextElement();
		        tc.setHeaderRenderer(headerRenderer);
		      }
			
			p.add(scrollPane, BorderLayout.CENTER);
			
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
			exportButton(this.matrixPanel);
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
		MyEdgeType[] edgePossibilities = ModelBuilder.getEdgeTypes().getAllEdgeTypesArray();
		MyNodeType[] nodePossibilities = ModelBuilder.getNodeTypes().getAllNodeTypesArray();
		
		
		JPanel allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		final JPanel NodePanel = new JPanel(new GridLayout(0, 1));
		
		for (MyNodeType attr : nodePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			if (mbuilder.getRelevantNodeTypes()!=null && mbuilder.getRelevantNodeTypes().contains(attr))
				choice.setSelected(true);
			else
				choice.setSelected(false);
			NodePanel.add(choice);
		}
		
		final JPanel EdgePanel = new JPanel(new GridLayout(0, 1));
		
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

		
		JComponent[] inputs = new JComponent[] {
						allPanel
		    	};
		
		int dialogResult = JOptionPane.showConfirmDialog(null, inputs, "Choose the Edge and Node types", JOptionPane.DEFAULT_OPTION);
    	
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
    		else
    			return false;
    	}
    	else
    	{
    		return false;
    	}
	}
	
	private void exportButton(JPanel panel)
	  {
	    JPanel optionPanel = new JPanel();
		JButton button = new JButton("Export Matrix");
	    
	    button.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	        JFileChooser saveFile = new JFileChooser();
	        saveFile.setSelectedFile(new File(System.getProperty("user.home") + "\\matrix_export.xls"));
	        saveFile.setDialogTitle("Select the file location");
	        saveFile.setApproveButtonText("Save");
	        
	        int returnVal = saveFile.showOpenDialog(null);
	        if (returnVal == 0)
	        {
	          File file = saveFile.getSelectedFile();
	          MatrixView.this.xml_exporter.createXMLExport(content, nodes, file);
	        }
	      }
	    });
	    optionPanel.add(button);
	    
	    panel.add(optionPanel, BorderLayout.EAST);
	  }




	public boolean isActive() {
		return active;
	}




	public void setActive(boolean active) {
		this.active = active;
	}
	
}
