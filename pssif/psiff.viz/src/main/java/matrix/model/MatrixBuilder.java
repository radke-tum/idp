package matrix.model;

import graph.model2.MyEdge2;
import graph.model2.MyEdgeType;
import graph.model2.MyNode2;
import graph.model2.MyNodeType;

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
	
	
	
	
	public LinkedList<MyNode2> findRelevantNodes()
	{
		LinkedList<MyNode2> res = new LinkedList<MyNode2>();
		
		List<MyNode2> search = ModelBuilder.getAllNodes();
		
		if (search !=null)
		{
			for (MyNode2 node : search)
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
	
	public LinkedList<MyEdge2> findRelevantEdges()
	{
		LinkedList<MyEdge2> res = new LinkedList<MyEdge2>();
		
		List<MyEdge2> search = ModelBuilder.getAllEdges();
		
		if (search !=null)
		{
			
			for (MyEdge2 edge : search)
			{
				System.out.println("Edge loop Type "+edge.getEdgeType().getName());
				if (edgesTypes.contains(edge.getEdgeType()))
					res.add(edge);
			}
		}
		
		System.out.println("Found relevant edges "+res.size());
		return res;
	}

	public String[][] getEdgeConnections(LinkedList<MyNode2> nodes, LinkedList<MyEdge2> edges)
	{
		String[][] res = new String[nodes.size()][nodes.size()];
		
		// create HashMap to find connections faster
		
		HashMap<MyNode2,LinkedList<MyEdge2>> mapping = new HashMap<MyNode2,LinkedList<MyEdge2>>();
		
		for (MyEdge2 e :edges)
		{
			MyNode2 source = e.getSourceNode();
			//MyNode dest = e.getDestinationNode();
			
			LinkedList<MyEdge2> tmp = mapping.get(source);
			
			// check if there is already a Connection
			if (tmp==null)
			{ 
				// no entry yet
				tmp = new LinkedList<MyEdge2>();
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
				MyNode2 nodeI = nodes.get(i);
				MyNode2 nodeJ = nodes.get(j);
				
				//check Mapping
				LinkedList<MyEdge2> connections = mapping.get(nodeI);
				if (connections==null)
				{
					// no connections
					res[i][j] ="";
				}
				else
				{
					// there are connections
					//check if there is a connection from NodeI to NodeJ
					LinkedList<MyEdge2> edge = findNode(connections, nodeJ);
					if (edge.size() != 0)
					{
						String s = "";
//<<<<<<< HEAD
						for (MyEdge2 e : edge)
/*=======
						for (int k =0; k<edge.size();k++)
>>>>>>> refs/remotes/origin/attempt3*/
						{
//<<<<<<< HEAD
							s = s + e.getEdgeType().toString();
/*=======
							MyEdge e = edge.get(k);
							s = s + e.getConnectionType().getName();
>>>>>>> refs/remotes/origin/attempt3*/
							if (e.getAttributes().size() != 0) 
							{
								for (String a : e.getAttributes()) 
								{
									s = s + " " + a + " ";
								}
							}
//<<<<<<< HEAD
							//TODO comment in again
							//s = s + " || ";
//=======
						//	if (k!= (edge.size()-1))
							//	s = s + " || ";
//>>>>>>> refs/remotes/origin/attempt3
						}
						res[i][j] = s;
					}
					
					/*if (connections.contains(nodeJ))
					{
						// there exactly this connection
						res[i][j] ="X";
					}*/
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
	
	private LinkedList<MyEdge2> findNode(LinkedList<MyEdge2> connections, MyNode2 node)
	{
		LinkedList<MyEdge2> res = new LinkedList<MyEdge2>();
		for (MyEdge2 e : connections) 
		{
			if (e.getDestinationNode().equals(node)) 
			{
				res.add(e);
			}
		}
		return res;
	}
}
