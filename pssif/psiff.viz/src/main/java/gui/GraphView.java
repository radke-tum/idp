package gui;

import de.tum.pssif.core.metamodel.Attribute;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.annotations.AnnotatingModalGraphMouse.ModeKeyAdapter;
import edu.uci.ics.jung.visualization.picking.PickedState;
import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyEdgeTypes;
import graph.model.MyNode;
import graph.model.MyNodeType;
import graph.operations.GraphViewContainer;
import graph.operations.AttributeFilter;
import graph.operations.NodeAndEdgeTypeFilter;
import gui.graph.AttributeFilterPopup;
import gui.graph.EdgeAndNodeTypePopup;
import gui.graph.GraphVisualization;
import gui.graph.HighlightNodePopup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
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
	private JButton nodeHighlight;
	private JButton collapseExpand;
	private JButton typeFilter;
	private JButton nodeAttributeFilter;
	private JButton edgeAttributeFilter;
	private boolean active;
	private JTable tableNodeAttributes;
	private DefaultTableModel nodeAttributesModel;
	
	private Dimension screenSize;

	public GraphView(/*Dimension parentDimension*/)
	{
		active = false;
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//screenSize = parentDimension;
		int x = (int) (screenSize.width*0.85);
		int y = (int) (screenSize.height*0.9);
		if (nodeDetails==null)
			graph = new GraphVisualization(new Dimension(x,y),true);
		else
			graph = new GraphVisualization(new Dimension(x,y), nodeDetails.isSelected());
		
		addNodeChangeListener();
	}
	
	public JPanel getGraphPanel()
	{
		int betweenComps =15;
		int betweenLabelandComp=10;
		
		parent = new JPanel();
        parent.setLayout(new BorderLayout());
		
		JPanel graphpanel = new JPanel();		
		
		VisualizationViewer<MyNode, MyEdge> vv = graph.getVisualisationViewer();
		
		graphpanel.add(vv);
		
		parent.add(graphpanel,BorderLayout.CENTER);
		
		JPanel information = new JPanel();

		information.setBackground(Color.LIGHT_GRAY);

		int x = (int) (screenSize.width*0.15);
		int y = (int) (screenSize.height);
		Dimension d = new Dimension(x,y);
		information.setMaximumSize(d);
		information.setMinimumSize(d);
		information.setPreferredSize(d);
		information.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		
		int i = -1;
		
		parent.add(information, BorderLayout.EAST);
		
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		JLabel lblNodeName = new JLabel("Node Name");
		c.gridy = (i++);
		information.add(lblNodeName,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenLabelandComp),c);
		
		nodename = new JLabel("");
		c.gridy = (i++);
		information.add(nodename,c);
		
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		JLabel lblNodeType = new JLabel("Node Type");
		c.gridy = (i++);
		information.add(lblNodeType,c);
		
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenLabelandComp),c);
		nodetype = new JLabel("");
		c.gridy = (i++);
		information.add(nodetype,c);
		
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		JLabel lblNodeAttributes = new JLabel("Node Attributes");
		c.gridy = (i++);
		information.add(lblNodeAttributes,c);
		
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenLabelandComp),c);
		
		int scrolly = (int)( y*0.1);

		JScrollPane jScrollPane = new JScrollPane(createAttributTable());
		jScrollPane.setPreferredSize(new Dimension(x, scrolly));
		jScrollPane.setMaximumSize(new Dimension(x, scrolly));
		jScrollPane.setMinimumSize(new Dimension(x, scrolly));
		
		c.gridy = (i++);
		information.add(jScrollPane,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		
		JLabel lblVisDetails = new JLabel("Visualization Details");
		c.gridy = (i++);
		information.add(lblVisDetails,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		
		nodeDetails = new JCheckBox();
		nodeDetails.setText("Node Details");
		c.gridy = (i++);
		information.add(nodeDetails,c);
		nodeDetails.setSelected(true);
		graph.setNodeVisualisation(true);
		nodeDetails.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent item) {			
				JCheckBox parent = (JCheckBox)item.getSource();
				if (parent.isSelected())
				{
					graph.setNodeVisualisation(true);
					// checkbox selected
				}
				else
				{
					// checkbox not selected
					graph.setNodeVisualisation(false);
				}
				
			}
		});
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		
		nodeHighlight = new JButton("Select Highlighted Nodes");
		nodeHighlight.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseHighlightNodes();
			}
		});
		c.gridy = (i++);
		information.add(nodeHighlight,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		
		
		JLabel lblDepthSpinner = new JLabel("Search Depth");
		c.gridy = (i++);
		information.add(lblDepthSpinner,c);
		
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenLabelandComp),c);
		
		int currentDepth = 1;
	    SpinnerModel depthModel = new SpinnerNumberModel(currentDepth, //initial value
	                                       1, //min
	                                       currentDepth + 100, //max
	                                       1);
	    final JSpinner spinner = new JSpinner(depthModel);
	    
	    spinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				Object value = spinner.getValue();
				if (value instanceof Integer)
				{
					int depth = (Integer) value;
					graph.setFollowEdgeTypes(depth);
				}
				
			}
		});
	    
	    c.gridy = (i++);
		information.add(spinner,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		
		
		
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
		c.gridy = (i++);
		information.add(collapseExpand,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		
		/*typeFilter = new JButton("Select Node & Edge Types");
		typeFilter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseEdgeAndNodeTypes();
			}
		});
		c.gridy = (i++);
		information.add(typeFilter,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);*/
		
		nodeAttributeFilter = new JButton("Filter Nodes  by Attribute");
		nodeAttributeFilter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AttributeFilterPopup filter = new AttributeFilterPopup(true,false);
				
				filter.showPopup();

				graph.updateGraph();

			}
		});
		c.gridy = (i++);
		information.add(nodeAttributeFilter,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		
		edgeAttributeFilter = new JButton("Filter Edges  by Attribute");
		edgeAttributeFilter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AttributeFilterPopup filter = new AttributeFilterPopup(false,true);
				filter.showPopup();

				graph.updateGraph();

			}
		});
		c.gridy = (i++);
		information.add(edgeAttributeFilter,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		
		
		return parent;
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
				
				//System.out.println(e.getType());
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
				        	//System.out.println("Update");
				        	
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
		/*graph.applyNodeAndEdgeFilter(ModelBuilder.getNodeTypes().getAllNodeTypes(), ModelBuilder.getEdgeTypes().getAllEdgeTypes());
		
		FRLayout<MyNode2, MyEdge2> l = new FRLayout<MyNode2, MyEdge2>(graph.getGraph());
    	graph.getVisualisationViewer().setGraphLayout(l);
    	
    	graph.getVisualisationViewer().repaint();*/
		new ModelBuilder();
		this.getGraph().updateGraph();
		//ModelBuilder.printVisibleStuff();
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
	
	private void chooseHighlightNodes()
	{
		HighlightNodePopup popup = new HighlightNodePopup(graph);
		
		popup.showPopup();

	}
	
	private void chooseEdgeAndNodeTypes()
	{
		EdgeAndNodeTypePopup popup = new EdgeAndNodeTypePopup(graph);
		
		popup.showPopup();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
