package graph.model;

import java.util.Collection;
import java.util.LinkedList;

import model.ModelBuilder;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;


public class GraphBuilder {
	
	private Graph<MyNode, MyEdge> g;
	//private boolean detailedNodes;
	private static boolean commentsVisible = false;
	
	/**
	 * Creates a new Graph with a given mode of Display for the Nodes
	 * @param detailedNodes true == more details, false = less details
	 * @return the graph with the according Node details grade
	 */
	public Graph<MyNode, MyEdge> createGraph(boolean detailedNodes)
	{
		if (g==null)
			g = new SparseMultigraph<MyNode,MyEdge>();
			//g = new SetHypergraph<MyNode,MyEdge>();
		
		removeAllNodesAndEdges();
		
		buildGraphFromModel(detailedNodes);
		
		return g;
	}
	
	/**
	 * Change the Display mode of the Nodes on the given graph
	 * @param detailedNodes true == more details, false = less details
	 * @par graph the graph on which the operation should be applied
	 * @return the graph with the according Node details grade
	 */
	public Graph<MyNode, MyEdge> changeNodeDetails(boolean detailedNodes, Graph<MyNode, MyEdge> graph)
	{
		Collection<MyNode> nodes = graph.getVertices();
				
		for (MyNode n : nodes)
		{
			n.setDetailedOutput(detailedNodes);
		}

		return graph;
	}
	
	/**
	 * Remove all Nodes and Edges from the graph
	 * @return the empty graph
	 */
	public Graph<MyNode, MyEdge> removeAllNodesAndEdges ()
	{
		return removeAllNodesAndEdges(g);
	}
	
	/**
	 * Remove all Nodes and Edges from the graph
	 * @param graph the graph on which the operation should be applied
	 * @return the empty graph
	 */
	private Graph<MyNode, MyEdge> removeAllNodesAndEdges (Graph<MyNode, MyEdge> graph)
	{
		LinkedList<MyEdge> edges = new LinkedList<MyEdge>(graph.getEdges());
		
		for (MyEdge e : edges)
		{
			graph.removeEdge(e);
		}
		
		LinkedList<MyNode> nodes = new LinkedList<MyNode>(graph.getVertices());
		
		for (MyNode n : nodes)
		{
			graph.removeVertex(n);
		}
		
		return graph;
	}
	
	/**
	 * Updates the graph. Builds the graph again from the Model with the given mode of details display
	 * @param detailedNodes true == all details, false == less details
	 * @return the graph with the given preferences
	 */
	public Graph<MyNode, MyEdge> updateGraph (boolean detailedNodes)
	{
		this.removeAllNodesAndEdges();
		
		return buildGraphFromModel(detailedNodes);
	}
	
	/**
	 * Build the graph from the Model with the given mode of details for the nodes
	 * @param detailedNodes true == all details, false == less details
	 * @return the graph with the given preferences
	 */
	private Graph<MyNode, MyEdge> buildGraphFromModel (boolean detailedNodes)
	{
		if (commentsVisible)
			System.out.println("buildGraphFromModel ");
		LinkedList<MyEdge> edges = ModelBuilder.getAllEdges();
		LinkedList<MyNode> nodes = ModelBuilder.getAllNodes();
		
		
		for (MyNode n : nodes)
		{
			if (n.isVisible())
			{
				n.setDetailedOutput(detailedNodes);
				g.addVertex(n);
				if (commentsVisible)
					System.out.println("Node is visible "+n.getRealName());
			}
			else
			{
				if (commentsVisible)
					System.out.println("Node not visible "+n.getRealName());
			}
		}
		
		for (MyEdge e : edges)
		{
			if (e.isVisible() && e.getDestinationNode().isVisible() && e.getSourceNode().isVisible())
			{
				if (commentsVisible)
				{
					System.out.println("Edge :"+ e.getEdgeTypeName());
					System.out.println("Source "+e.getSourceNode().getRealName());
					System.out.println("Dest "+e.getDestinationNode().getRealName());
				}
				if (e.isDirected())
				{
					//LinkedList<MyNode> tmp = new LinkedList<MyNode>();
					//tmp.add(e.getSourceNode());
					//tmp.add(e.getDestinationNode());
					g.addEdge(e, e.getSourceNode(), e.getDestinationNode(), EdgeType.DIRECTED);
				}
				else
				{
					/*LinkedList<MyNode> tmp = new LinkedList<MyNode>();
					tmp.add(e.getSourceNode());
					tmp.add(e.getDestinationNode());*/
					g.addEdge(e, e.getSourceNode(), e.getDestinationNode(), EdgeType.UNDIRECTED);
				}
				
				
			}
			
			if (commentsVisible)
			{
				System.out.println("Visibilty "+ e.isVisible());
				System.out.println("Edge-----------------");
			}
				
		}
		
		if (commentsVisible)
			System.out.println("----------------");
		
		return g;
	}
}
