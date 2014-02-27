package gui;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import graph.model.MyEdge;
import graph.model.MyNode;
import graph.model.MyNodeType;
import gui.graph.EdgeAndNodeTypePopup;
import gui.graph.GraphVisualization;
import gui.graph.HighlightNodePopup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import model.ModelBuilder;

public class GraphView {
	private JPanel parent;
	private GraphVisualization graph;
	
	private JLabel nodename;
	private JLabel nodetype;
	private JCheckBox nodeDetails;
	private JButton collapseExpand;
	private boolean active;
	private JTable tableNodeAttributes;
	private DefaultTableModel nodeAttributesModel;
	
	private Dimension screenSize;
	private static int betweenComps =10;

	public GraphView(/*Dimension parentDimension*/)
	{
		active = false;
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//screenSize = parentDimension;
		//int x = (int) (screenSize.width*0.85);
		//int y = (int) (screenSize.height*0.9);
		int x = (int) (screenSize.width);
		int y = (int) (screenSize.height*0.75);
		if (nodeDetails==null)
			graph = new GraphVisualization(new Dimension(x,y),true);
		else
			graph = new GraphVisualization(new Dimension(x,y), nodeDetails.isSelected());
		
		addNodeChangeListener();
	}
	
	public JPanel getGraphPanel()
	{
		parent = new JPanel();
        parent.setLayout(new BorderLayout());
		
        parent.add(addGraphViz(),BorderLayout.CENTER);
        
        parent.add(addInformationPanel(), BorderLayout.SOUTH);
        
        return parent;
	}
	
	private JPanel addGraphViz()
	{
		JPanel graphpanel = new JPanel();		
		
		VisualizationViewer<MyNode, MyEdge> vv = graph.getVisualisationViewer();
		
		graphpanel.add(vv);
		return graphpanel;
	}
	
	private JPanel addInformationPanel()
	{
		JPanel information = new JPanel();

		int x = (int) (screenSize.width);
		int y = (int) (screenSize.height*0.15);
		Dimension d = new Dimension(x,y);
		
		information.setMaximumSize(d);
		information.setMinimumSize(d);
		information.setPreferredSize(d);
		
		information.setLayout(new BorderLayout());
		
		// selected Node Information
		information.add(addNodeInformationPanel(), BorderLayout.WEST);

		// attributes table 
		information.add(addAttributePanel(x, y), BorderLayout.CENTER);
		
		// Basic Graph operations
		information.add(addBasicOperationPanel(), BorderLayout.EAST);
		
		return information;
	}
	
	private JPanel addNodeInformationPanel()
	{
		GridBagConstraints c = new GridBagConstraints();	
		int ypos = -1;
		
		// selected Node Information
		JPanel nodeInfos = new JPanel();
		nodeInfos.setLayout(new GridBagLayout());
		nodeInfos.setBackground(Color.LIGHT_GRAY);
		
		c.gridx = 0;
		JLabel lblNodeName = new JLabel("Node Name");
		ypos++;
		c.gridy = ypos;
		nodeInfos.add(lblNodeName,c);

		nodename = new JLabel("");
		ypos++;
		c.gridy = ypos;
		nodeInfos.add(nodename,c);
		
		ypos++;
		c.gridy = ypos;
		nodeInfos.add(Box.createVerticalStrut(betweenComps),c);
		
		JLabel lblNodeType = new JLabel("Node Type");
		ypos++;
		c.gridy = ypos;
		nodeInfos.add(lblNodeType,c);
		nodetype = new JLabel("");
		ypos++;
		c.gridy = ypos;
		nodeInfos.add(nodetype,c);
		
		c.gridx =1;
		c.gridheight=ypos;
		nodeInfos.add(Box.createHorizontalStrut(10),c);
		
		return nodeInfos;
	}
	
	private JPanel addAttributePanel(int sizeX, int sizeY)
	{
		JPanel attributeInfos = new JPanel();
		attributeInfos.setLayout(new BorderLayout());
		attributeInfos.setBackground(Color.LIGHT_GRAY);
		
		JLabel lblNodeAttributes = new JLabel("Node Attributes");

		attributeInfos.add(lblNodeAttributes,BorderLayout.NORTH);
		
		int scrollx = (int) (sizeX*0.8);
		int scrolly = (int) (sizeY*0.9);

		JScrollPane jScrollPane = new JScrollPane(createAttributTable());
		jScrollPane.setPreferredSize(new Dimension(scrollx, scrolly));
		jScrollPane.setMaximumSize(new Dimension(scrollx, scrolly));
		jScrollPane.setMinimumSize(new Dimension(scrollx, scrolly));
		
		attributeInfos.add(jScrollPane,BorderLayout.CENTER);
		
		return attributeInfos;
	}
	
	private JPanel addBasicOperationPanel()
	{
		GridBagConstraints c = new GridBagConstraints();	
		int ypos = -1;
		
		JPanel basicOperations = new JPanel();
		basicOperations.setLayout(new GridBagLayout());
		basicOperations.setBackground(Color.LIGHT_GRAY);

		JLabel lblVisDetails = new JLabel("Visualisation Details");
		ypos++;
		c.gridy = ypos;
		c.gridx = 1;
		basicOperations.add(lblVisDetails,c);
		ypos++;
		c.gridy = ypos;
		
		nodeDetails = new JCheckBox("Node Details");
		ypos++;
		c.gridy = ypos;
		basicOperations.add(nodeDetails,c);
		nodeDetails.setSelected(true);
		graph.setNodeVisualisation(true);
		nodeDetails.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent item) {			
				JCheckBox parent = (JCheckBox)item.getSource();
				if (parent.isSelected())
				{
					// checkbox selected
					graph.setNodeVisualisation(true);
				}
				else
				{
					// checkbox not selected
					graph.setNodeVisualisation(false);
				}
				
			}
		});
		ypos++;
		c.gridy = ypos;
		basicOperations.add(Box.createVerticalStrut(betweenComps),c);
		

		
		JLabel lblDepthSpinner = new JLabel("Search Depth");
		ypos++;
		c.gridy = ypos;
		basicOperations.add(lblDepthSpinner,c);	
	
		int currentDepth = 1;
	    SpinnerModel depthModel = new SpinnerNumberModel(currentDepth, //initial value
	                                       1, //min
	                                       currentDepth + 100, //max
	                                       1);
	    JSpinner spinner = new JSpinner(depthModel);
	    spinner.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
				
				Object value = ((JSpinner)e.getSource()).getValue();
				if (value instanceof Integer)
				{
					int depth = (Integer) value;
					graph.setFollowEdgeTypes(depth);
				}
				
			}
		});
	    ypos++;
		c.gridy = ypos;
	    basicOperations.add(spinner,c);
	    
	    ypos++;
		c.gridy = ypos;
		basicOperations.add(Box.createVerticalStrut(betweenComps),c);
		
		collapseExpand = new JButton("Collapse/Expand Node");
		collapseExpand.setEnabled(false);
		collapseExpand.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (graph.isExpandable())
				{
					graph.ExpandNode(nodeDetails.isSelected());
					collapseExpand.setText("Collapse Node");	
				}
				
				if (graph.isCollapsable())
				{
					graph.collapseNode();
					collapseExpand.setText("Expand Node");
				}
			}
		});
		ypos++;
		c.gridy = ypos;
		basicOperations.add(collapseExpand,c);
		
		c.gridx =0;
		c.gridheight=ypos;
		basicOperations.add(Box.createHorizontalStrut(10),c);
		
		return basicOperations;
	}
	
	private JTable createAttributTable()
	{
		nodeAttributesModel = new DefaultTableModel(){
			
	        public boolean isCellEditable(int rowIndex, int columnIndex) {
	            if (columnIndex==0 || columnIndex==2 || columnIndex==3)
	            	return false;
	            else
	            	return true;
	        }
		};
		nodeAttributesModel.addColumn("Attribute");
		nodeAttributesModel.addColumn("Value");
		nodeAttributesModel.addColumn("Unit");
		nodeAttributesModel.addColumn("Type");
		
		tableNodeAttributes = new JTable(nodeAttributesModel);
		tableNodeAttributes.getModel().addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE)
				{
					int row = e.getFirstRow();
			        int column = e.getColumn();
			        TableModel model = (TableModel)e.getSource();
			       
			        Object data = model.getValueAt(row, column);
			        String attributeName = (String) model.getValueAt(row, 0);
					
			        // get the data to test if there was some data entered
			        String testdata = (String) data;
			        // do not trigger the event again if we previously wrote null back
			        if (data !=null && testdata.length()>0)
			        {
				        Set<MyNode> selectedNodes = graph.getVisualisationViewer().getPickedVertexState().getPicked();
				        if (!selectedNodes.isEmpty() && selectedNodes.size()==1)
				        {
				        	MyNode selectedNode = selectedNodes.iterator().next();
				        	
				        	boolean res = selectedNode.updateAttribute(attributeName, data);
				        	
				        	if (!res)
				        	{
				        		model.setValueAt(null, row, column);
				        		JPanel errorPanel = new JPanel();
				        		
				        		errorPanel.add(new JLabel("The value does not match the attribute data type"));
				        		
				        		JOptionPane.showMessageDialog(null, errorPanel, "Ups something went wrong", JOptionPane.ERROR_MESSAGE);
				        	}
				        }
			        }
				}
			}
		});
		
		return tableNodeAttributes;
	}
	
	public GraphVisualization getGraph() {
		return graph;
	}
	
	public void resetGraph()
	{
		new ModelBuilder();
		this.getGraph().updateGraph();
	}
	
	private void updateSidebar(String nodeName, MyNodeType nodeType, LinkedList<LinkedList<String>> attributes)
	{
		if (nodeName==null)
			this.nodename.setText("");
		else
			this.nodename.setText(nodeName);
		if (nodeType==null)
			this.nodetype.setText("");
		else
			this.nodetype.setText(nodeType.getName());
		if(attributes==null)
		{
			for (int i = 0; i<nodeAttributesModel.getRowCount();i++)
			{
				nodeAttributesModel.removeRow(i);
			}
			nodeAttributesModel.setRowCount(0);
		}
		else
		{
			for (int i = 0; i<nodeAttributesModel.getRowCount();i++)
			{
				nodeAttributesModel.removeRow(i);
			}
			nodeAttributesModel.setRowCount(0);
			
			for (LinkedList<String> currentAttr: attributes)
			{
				String name = currentAttr.get(0);
				String value = currentAttr.get(1);
				String unit = currentAttr.get(2);
				String type = currentAttr.get(3);
				nodeAttributesModel.addRow(new String[]{name,value,unit,type});
			}
		}
		
		if (graph.isCollapsable())
		{
			collapseExpand.setText("Collapse Node");
		}
		
		if (graph.isExpandable())
		{
			collapseExpand.setText("Expand Node");
		}
		
		if (!graph.isCollapsable() && !graph.isExpandable())
		{
			collapseExpand.setText("Collapse/Expand Node");
			collapseExpand.setEnabled(false);
		}
		else
			collapseExpand.setEnabled(true);
		
	}
	
	private void addNodeChangeListener()
	{
		final PickedState<MyNode> pickedState = graph.getVisualisationViewer().getPickedVertexState();

		pickedState.addItemListener(new ItemListener() {

		    @Override
		    public void itemStateChanged(ItemEvent e) {
		        Object subject = e.getItem();

		        if (subject instanceof MyNode) {
		        	MyNode vertex = (MyNode) subject;
		            if (pickedState.isPicked(vertex)) 
		              updateSidebar(vertex.getName(), vertex.getNodeType(), vertex.getAttributes());
		            else
		            	updateSidebar(null, null,null);
		        }
		        else
		        	updateSidebar(null, null,null);
		        
		    }
		});
		Set<MyNode> s =pickedState.getPicked();
		if (s==null)
		{
			updateSidebar(null, null,null);
		}
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
