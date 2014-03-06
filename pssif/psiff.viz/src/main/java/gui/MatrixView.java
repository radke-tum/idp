package gui;

import graph.model.MyEdge;
import graph.model.MyNode;
import gui.matrix.MatrixChooseNodeAndEgeTypePopup;
import gui.matrix.RowLegendTable;
import gui.matrix.TableColumnAdjuster;
import gui.matrix.VerticalTableHeaderCellRenderer;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import matrix.model.ExcelExport;
import matrix.model.MatrixBuilder;

public class MatrixView {

	//private GridLayout experimentLayout;
	private JPanel matrixPanel;
	private MatrixBuilder mbuilder;
	private String[][] content;
	private LinkedList<MyNode> nodes;
	private LinkedList<MyEdge> edges;
	private ExcelExport xml_exporter;
	private boolean active;

	public MatrixView() {
		this.matrixPanel = new JPanel();
		this.mbuilder = new MatrixBuilder();
		this.xml_exporter = new ExcelExport();
		active = false;	
	}
	
	private void drawMatrix()
	{
		edges = mbuilder.findRelevantEdges();
		nodes=  mbuilder.findRelevantNodes();
		matrixPanel = drawPanels(nodes,edges);
	}
	
	private JPanel drawPanels(LinkedList<MyNode> nodes, LinkedList<MyEdge> edges)
	{
		JPanel Content = new JPanel(new BorderLayout());
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
			
			content = mbuilder.getEdgeConnections(nodes, edges);


			JTable mainTable = new JTable(content,legend);
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
		{
			// create an information Panel
			JPanel allPanel = new JPanel(new GridBagLayout());
			allPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			JLabel label = new JLabel("Please select a least one EdgeType and one NodeType");
			label.setFont(new Font("Arial", Font.ITALIC, 25));
			allPanel.add(label);
			return allPanel;
		}	
	}
	
	public boolean chooseNodes()
	{
		MatrixChooseNodeAndEgeTypePopup popup = new MatrixChooseNodeAndEgeTypePopup(mbuilder);
		
		return popup.showPopup();
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
