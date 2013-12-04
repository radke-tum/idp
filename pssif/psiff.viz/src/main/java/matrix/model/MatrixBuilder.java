package matrix.model;

import graph.model.ConnectionType;
import graph.model.MyEdge;
import graph.model.MyNode;
import graph.model.NodeType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import model.Model;

public class MatrixBuilder {
	
	private LinkedList<NodeType> nodeTypes;
	private LinkedList<ConnectionType>  edgesTypes;
	
	public MatrixBuilder(LinkedList<NodeType> nodeTypes, LinkedList<ConnectionType>  edgesTypes)
	{
		/*Model m= new Model();
		m.MockData();*/
		
		this.nodeTypes= nodeTypes;
		this.edgesTypes = edgesTypes;
	}
	
	public MatrixBuilder()
	{
		/*Model m= new Model();
		m.MockData();*/
	}
	
	
	
	
	public LinkedList<MyNode> findRelevantNodes()
	{
		LinkedList<MyNode> res = new LinkedList<>();
		
		List<MyNode> search = Model.getAllNodes();
		
		if (search !=null)
		{
			for (MyNode node : search)
			{
				System.out.println("Node loop "+node.getName()+" Type "+node.getNodeType().getName());
				if (nodeTypes.contains(node.getNodeType()))
					res.add(node);
			}
		}
		
		System.out.println("Found relevant nodes "+res.size());
		return res;
	}
	
	public LinkedList<MyEdge> findRelevantEdges()
	{
		LinkedList<MyEdge> res = new LinkedList<>();
		
		List<MyEdge> search = Model.getAllEdges();
		
		if (search !=null)
		{
			
			for (MyEdge edge : search)
			{
				System.out.println("Edge loop Type "+edge.getConnectionType().getName());
				if (edgesTypes.contains(edge.getConnectionType()))
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
		
		HashMap<MyNode,LinkedList<MyNode>> mapping = new HashMap<MyNode,LinkedList<MyNode>>();
		
		for (MyEdge e :edges)
		{
			MyNode source = e.getSourceNode();
			MyNode dest = e.getDestinationNode();
			
			LinkedList<MyNode> tmp = mapping.get(source);
			
			// check if there is already a Connection
			if (tmp==null)
			{ 
				// no entry yet
				tmp = new LinkedList<>();
				tmp.add(dest);
				mapping.put(source, tmp);
			}
			else
			{
				// there is an entry
				tmp.add(dest);
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
				LinkedList<MyNode> connections = mapping.get(nodeI);
				if (connections==null)
				{
					// no connections
					res[i][j] ="";
				}
				else
				{
					// there are connections
					//check if there is a connection from NodeI to NodeJ
					if (connections.contains(nodeJ))
					{
						// there exactly this connection
						res[i][j] ="X";
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


	public LinkedList<NodeType> getRelevantNodeTypes() {
		return nodeTypes;
	}




	public void setRelevantNodeTypes(LinkedList<NodeType> nodeTypes) {
		this.nodeTypes = nodeTypes;
	}




	public LinkedList<ConnectionType> getRelevantEdgesTypes() {
		return edgesTypes;
	}




	public void setRelevantEdgesTypes(LinkedList<ConnectionType> edgesTypes) {
		this.edgesTypes = edgesTypes;
	}
}
