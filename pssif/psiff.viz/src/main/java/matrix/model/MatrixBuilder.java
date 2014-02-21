package matrix.model;

import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import model.ModelBuilder;

public class MatrixBuilder {
	
	private LinkedList<MyNodeType> nodeTypes;
	private LinkedList<MyEdgeType>  edgesTypes;
	
	public MatrixBuilder(LinkedList<MyNodeType> nodeTypes, LinkedList<MyEdgeType>  edgesTypes)
	{
		this.nodeTypes= nodeTypes;
		this.edgesTypes = edgesTypes;
	}
	
	public MatrixBuilder()
	{
	}
	
	
	
	
	public LinkedList<MyNode> findRelevantNodes()
	{
		LinkedList<MyNode> res = new LinkedList<MyNode>();
		
		List<MyNode> search = ModelBuilder.getAllNodes();
		
		if (search !=null)
		{
			for (MyNode node : search)
			{
				System.out.println("Node loop "+node.getName()+" Type "+node.getNodeType().getName());
				if (nodeTypes.contains(node.getNodeType()))
				{
					res.add(node);
				}
			}
		}
		
		System.out.println("Found relevant nodes "+res.size());
		return res;
	}
	
	public LinkedList<MyEdge> findRelevantEdges()
	{
		LinkedList<MyEdge> res = new LinkedList<MyEdge>();
		
		List<MyEdge> search = ModelBuilder.getAllEdges();
		
		if (search !=null)
		{
			
			for (MyEdge edge : search)
			{
				System.out.println("Edge loop Type "+edge.getEdgeType().getName());
				if (edgesTypes.contains(edge.getEdgeType()))
					res.add(edge);
			}
		}
		
		System.out.println("Found relevant edges "+res.size());
		return res;
	}

	public String[][] getEdgeConnections(LinkedList<MyNode> nodes, LinkedList<MyEdge> edges)
	{
		String[][] res = new String[nodes.size()][nodes.size()];
		
		// create HashMap to find connections faster
		
		HashMap<MyNode,LinkedList<MyEdge>> mapping = new HashMap<MyNode,LinkedList<MyEdge>>();
		
		for (MyEdge e :edges)
		{
			MyNode source = e.getSourceNode();
			//MyNode dest = e.getDestinationNode();
			
			LinkedList<MyEdge> tmp = mapping.get(source);
			
			// check if there is already a Connection
			if (tmp==null)
			{ 
				// no entry yet
				tmp = new LinkedList<MyEdge>();
				tmp.add(e);
				mapping.put(source, tmp);
			}
			else
			{
				// there is an entry
				tmp.add(e);
				mapping.put(source, tmp);
			}
		}
		
		
		// content
		for (int i =0; i<res.length; i++)
		{
			for (int j=0; j<res[i].length;j++)
			{
				MyNode nodeI = nodes.get(i);
				MyNode nodeJ = nodes.get(j);
				
				//check Mapping
				LinkedList<MyEdge> connections = mapping.get(nodeI);
				if (connections==null)
				{
					// no connections
					res[i][j] ="";
				}
				else
				{
					// there are connections
					//check if there is a connection from NodeI to NodeJ
					LinkedList<MyEdge> edge = findNode(connections, nodeJ);
					if (edge.size() != 0)
					{
						String s = "";

						for (MyEdge e : edge)
						{

							s = s + e.getEdgeType().toString();

							if (e.getAttributes().size() != 0) 
							{
								for (String a : e.getAttributes()) 
								{
									s = s + " " + a + " ";
								}
							}
						}
						res[i][j] = s;
					}
					else
					{
						// there are connections, but no connection between NodeI to NodeJ
						res[i][j] ="";
					}
				}
			}
		}
		
		return res;
	}


	public LinkedList<MyNodeType> getRelevantNodeTypes() {
		return nodeTypes;
	}

	public void setRelevantNodeTypes(LinkedList<MyNodeType> nodeTypes) {
		this.nodeTypes = nodeTypes;
	}

	public LinkedList<MyEdgeType> getRelevantEdgesTypes() {
		return edgesTypes;
	}

	public void setRelevantEdgesTypes(LinkedList<MyEdgeType> edgesTypes) {
		this.edgesTypes = edgesTypes;
	}
	
	private LinkedList<MyEdge> findNode(LinkedList<MyEdge> connections, MyNode node)
	{
		LinkedList<MyEdge> res = new LinkedList<MyEdge>();
		for (MyEdge e : connections) 
		{
			if (e.getDestinationNode().equals(node)) 
			{
				res.add(e);
			}
		}
		return res;
	}
}
