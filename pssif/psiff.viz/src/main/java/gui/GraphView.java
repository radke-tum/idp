package gui;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import graph.listener.GraphVisualization;
import graph.model.ConnectionType;
import graph.model.MyEdge;
import graph.model.MyNode;
import graph.model.NodeType;

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
import javax.swing.ScrollPaneConstants;

public class GraphView {
	private JPanel parent;
	private GraphVisualization graph;
	
	private JLabel nodename;
	private JLabel nodetype;
	private JList<String> nodeattributes;
	private JCheckBox nodeDetails;
	private JButton nodeHighlight;
	
	private Dimension screenSize;
	
	public GraphView()
	{
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) (screenSize.width*0.9);
		int y = (int) (screenSize.height*0.9);
		if (nodeDetails==null)
			graph = new GraphVisualization(new Dimension(x,y),true);
		else
			graph = new GraphVisualization(new Dimension(x,y),nodeDetails.isSelected());
		
		addNodeChangeListener();
	}
	
	public JPanel getGraphPanel()
	{
		int betweenComps =15;
		int betweenLabelandComp=10;
		
		parent = new JPanel();
        parent.setLayout(new BorderLayout());
		
		JPanel graphpanel = new JPanel();
		graphpanel.setBackground(Color.YELLOW);
		
		
		VisualizationViewer<MyNode, MyEdge> vv = graph.getVisualisationViewer();
		
		graphpanel.add(vv);
		
		parent.add(graphpanel,BorderLayout.CENTER);
		
		JPanel information = new JPanel();
		information.setBackground(Color.GREEN);
		int x = (int) (screenSize.width*0.1);
		int y = (int) (screenSize.height);
		Dimension d = new Dimension(x,y);
		information.setMaximumSize(d);
		information.setMinimumSize(d);
		information.setPreferredSize(d);

		
		parent.add(information, BorderLayout.EAST);
		information.setLayout(new BoxLayout(information, BoxLayout.Y_AXIS));
		
		information.add(Box.createVerticalStrut(betweenComps));
		JLabel lblNodeName = new JLabel("Node Name");
		information.add(lblNodeName);
		information.add(Box.createVerticalStrut(betweenLabelandComp));
		
		nodename = new JLabel("");
		information.add(nodename);

		information.add(Box.createVerticalStrut(betweenComps));
		JLabel lblNodeType = new JLabel("Node Type");
		information.add(lblNodeType);
		
		information.add(Box.createVerticalStrut(betweenLabelandComp));
		nodetype = new JLabel("");
		information.add(nodetype);
		
		information.add(Box.createVerticalStrut(betweenComps));
		JLabel lblNodeAttributes = new JLabel("Node Attributes");
		information.add(lblNodeAttributes);
		
		information.add(Box.createVerticalStrut(betweenLabelandComp));
		
		
		nodeattributes = new JList<String>();
		String[] a = new String[]{};
		
		nodeattributes.setListData(a);
		nodeattributes.setVisibleRowCount(5);
		int scrolly = (int)( y*0.1);

		JScrollPane jScrollPane = new JScrollPane(nodeattributes);
		//jScrollPane.setViewportView(nodeattributes);
		jScrollPane.setPreferredSize(new Dimension(x, scrolly));
		jScrollPane.setMaximumSize(new Dimension(x, scrolly));
		jScrollPane.setMinimumSize(new Dimension(x, scrolly));
		
		information.add(jScrollPane);
		information.add(Box.createVerticalStrut(betweenComps));
		
		JLabel lblVisDetails = new JLabel("Visualization Details");
		information.add(lblVisDetails);
		information.add(Box.createVerticalStrut(betweenComps));
		
		nodeDetails = new JCheckBox();
		nodeDetails.setText("Node Details");
		information.add(nodeDetails);
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
					System.out.println("Test");
				}
				else
				{
					// checkbox not selected
					System.out.println("Test2");
					graph.setNodeVisualisation(false);
				}
				
			}
		});
		information.add(Box.createVerticalStrut(betweenComps));
		
		nodeHighlight = new JButton("Select Highlighted Nodes");
		nodeHighlight.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseHighlightNodes();
			}
		});
		information.add(nodeHighlight);
		information.add(Box.createVerticalStrut(betweenComps));
		
		
		
		return parent;
	}

	public GraphVisualization getGraph() {
		return graph;
	}
	
	public void resetGraph()
	{
		FRLayout<MyNode, MyEdge> l = new FRLayout<MyNode, MyEdge>(graph.getGraph());
    	graph.getVisualisationViewer().setGraphLayout(l);
    	
    	graph.getVisualisationViewer().repaint();
	}
	
	private void updateSidebar(String nodeName, NodeType nodeType, List<String> attributes)
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
			String[] s = new String[0];
			this.nodeattributes.setListData(s);
		}
		else
		{
			String[] s = new String[attributes.size()];
			
			int counter=0;
			for (String a : attributes)
			{
				s[counter]=a;
				counter++;
			}
			this.nodeattributes.setListData(s);
		}
		
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
		ConnectionType[] edgePossibilities = ConnectionType.values();
		
		JPanel allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		JPanel EdgePanel = new JPanel(new GridLayout(0, 1));
		
		LinkedList<ConnectionType> already = graph.getHighlightNodes();
		for (ConnectionType attr : edgePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			if (already!=null && already.contains(attr))
				choice.setSelected(true);
			else
				choice.setSelected(false);
			EdgePanel.add(choice);
		}
		
		c.gridx = 1;
		c.gridy = 0;
		allPanel.add(new JLabel("Choose Connection Types"),c);

		c.gridx = 1;
		c.gridy = 1;
		allPanel.add(EdgePanel,c);
		
		JComponent[] inputs = new JComponent[] {
				allPanel
    	};
		
		LinkedList<ConnectionType> res = new LinkedList<>();
		
		allPanel.setPreferredSize(new Dimension(200,500));
		allPanel.setMaximumSize(new Dimension(200,500));
		allPanel.setMinimumSize(new Dimension(200,500));
		JScrollPane p = new JScrollPane (allPanel, 
	            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
	            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		int dialogResult = JOptionPane.showConfirmDialog(null, p, "Choose the attributes", JOptionPane.DEFAULT_OPTION);
    	
    	if (dialogResult==0)
    	{
    		
    		// get all the values of the edges
    		Component[] attr = EdgePanel.getComponents();       	
        	for (Component tmp :attr)
        	{
        		if ((tmp instanceof JCheckBox))
        		{
        			JCheckBox a = (JCheckBox) tmp;
        			
        			// compare which ones where selected
        			 if (a.isSelected())
        			 {
        				 ConnectionType b = ConnectionType.getValue(a.getText());
        				 res.add(b);
        			 }
        				
        		}	
        	}
    	}
    	graph.setHighlightNodes(res);

	}
	
}
