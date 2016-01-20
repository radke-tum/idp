package gui.mouse;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import graph.model.IMyNode;
import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyJunctionNodeType;
import graph.model.MyNode;
import graph.model.MyNodeType;
import gui.graph.GraphVisualization;
import model.ModelBuilder;

public class MouseCommands {
	private GraphVisualization gViz;
	public MouseCommands(GraphVisualization gv)
	{
		this.gViz = gv;
	}
	
	public void createNode(Point2D p)
	{
	  	JTextField NodeName = new JTextField();
    	MyNodeType[] nodetypes = ModelBuilder.getNodeTypes().getAllNodeTypesArray();
    	//MyJunctionNodeType[] junctiontyps = ModelBuilder.getJunctionNodeTypes().getAllJunctionNodeTypesArray();
    	//MyNodeAndJunctionType[] types = new MyNodeAndJunctionType[nodetypes.length + junctiontyps.length];
    	//int i = 0;
    	//for (MyNodeType x : nodetypes)
    	//	types[i++] = x;
    	//for (MyJunctionNodeType x : junctiontyps)
    	//	types[i++] = x;
    	
    	
    	JComboBox<MyNodeType> nodeType = new JComboBox<MyNodeType>(nodetypes);
    	
    	JComponent[] inputs = new JComponent[] {
    			new JLabel("Node Name"),
    			NodeName,
    			new JLabel("Nodetype"),
    			nodeType
    	};						
    	
    	JOptionPane.showMessageDialog(null, inputs, "Create new Node Dialog", JOptionPane.PLAIN_MESSAGE);
    	
    	// check if the user filled all the input field
    	if (NodeName.getText()!=null && NodeName.getText().length()>0)
    	{
    		IMyNode mynode;
    		if (nodeType.getSelectedItem() instanceof MyNodeType)
    			mynode = ModelBuilder.addNewNodeFromGUI(NodeName.getText(), (MyNodeType) nodeType.getSelectedItem());
    		else
    			mynode = ModelBuilder.addNewJunctionNodeFromGUI(NodeName.getText(), (MyJunctionNodeType) nodeType.getSelectedItem());
    		ModelBuilder.printVisibleStuff();
    		
    		if (gViz != null)
    		{
    			try{
    			gViz.getVisualisationViewer().getGraphLayout().setLocation(mynode, p);
    			} catch(Exception e){System.out.println("Error in Set Location");}
    			gViz.updateGraph();
    		}
    	}       
	}
	
	public void changeNodeTypeFunction(MyNode node)
	{
    	MyNodeType[] possibilities = ModelBuilder.getNodeTypes().getAllNodeTypesArray();
    	JComboBox<MyNodeType> nodeType = new JComboBox<MyNodeType>(possibilities);
    	
    	JComponent[] inputs = new JComponent[] {
    			//new JLabel("Node Name"),
    			//NodeName,
    			new JLabel("Nodetype"),
    			nodeType
    	};						
    	
    	JOptionPane.showMessageDialog(null, inputs, "Change Node Type Dialog", JOptionPane.PLAIN_MESSAGE);
    	node.setNodeType((MyNodeType) nodeType.getSelectedItem());
    	
    	//updating edges from and to this node based on the new type
    	ArrayList<MyEdge> edges = new ArrayList<MyEdge>();
    	for (MyEdge edge : ModelBuilder.getAllEdges())
    		if ((edge.getSourceNode() == node) || (edge.getDestinationNode() == node))
    			edges.add(edge);
    	for (MyEdge edge : edges)
	    	if (!isEdgePossible(edge))
	    		edge.setEdgeType(ModelBuilder.getEdgeTypes().getValue("Edge"));
    	
    	gViz.updateGraph();      

	}
	
	private boolean isEdgePossible(MyEdge edge)
	{
		try{
			MyNode srcNode = (MyNode) edge.getSourceNode();
			MyNode dstNode = (MyNode) edge.getDestinationNode();
			LinkedList<MyEdgeType> edgeTypes = 
					ModelBuilder.getPossibleEdges(srcNode.getNodeType().getType(), dstNode.getNodeType().getType());
			return (edgeTypes != null && edgeTypes.contains(edge.getEdgeType()));
		} catch(Exception e){return false;}
	}

}
