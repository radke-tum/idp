package graph.operations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.tum.pssif.core.metamodel.EdgeType;

import model.ModelBuilder;

import graph.model.MyEdge;
import graph.model.MyEdgeTypes;
import graph.model.MyNode;

/**
 * Allows to collapse or expand Nodes
 * @author Luc
 *
 */
public class MyCollapser {
	

	private MyNode recStartNode;
	private LinkedList<MyEdge> newInEdges;
	private LinkedList<MyEdge> newOutEdges;
	private LinkedList<MyNode> touchedNodes;
	
	private HashMap<MyNode,CollapseContainer> history;
	private CollapseContainer container;
	
	public MyCollapser()
	{
		this.history = new HashMap<MyNode, CollapseContainer>();
	}
	
	/*public Graph<MyNode2,MyEdge2> collapseGraph(Graph<MyNode2,MyEdge2> g, MyNode2 startNode) 
	{
		// save copy
		container = new CollapseContainer();
		
		recStartNode=startNode;
		newInEdges = new LinkedList<InfoContainer>();
		newOutEdges = new LinkedList<InfoContainer>();
		touchedNodes = new LinkedList<MyNode2>();
		
		recCollapseGraph(g,startNode,true,new LinkedList<MyNode2>());
		
		for (InfoContainer ic : newInEdges)
		{
			if (!touchedNodes.contains(ic.getEdgeSource()))
			{
				g.addEdge(ic.getEdge(), ic.getEdgeSource(), ic.getEdgeDestintation(),EdgeType.DIRECTED);
				container.addNewEdges(ic);
			}
		}
		
		for (InfoContainer ic : newOutEdges)
		{
			if (!touchedNodes.contains(ic.getEdgeDestintation()))
			{
				g.addEdge(ic.getEdge(), ic.getEdgeSource(), ic.getEdgeDestintation(),EdgeType.DIRECTED);
				container.addNewEdges(ic);
			}
			
		}
		
		history.put(startNode, container);
		
		return g;
	}*/
	
	/**
	 * collapses the Graph at the given Node
	 * @param startNode
	 */
	public void collapseGraph( MyNode startNode) 
	{
		container = new CollapseContainer();
		
		recStartNode=startNode;
		newInEdges = new LinkedList<MyEdge>();
		newOutEdges = new LinkedList<MyEdge>();
		touchedNodes = new LinkedList<MyNode>();
		
		// remove all the collapsed nodes from the graph
		recCollapseGraph(startNode,true,new LinkedList<MyNode>());
		
		// add new Edges which where previously connected to on of the Nodes which are are now collapsed
		// the collapsed Node was the source of the Edge
		for (MyEdge ic : newInEdges)
		{
			if (!touchedNodes.contains(ic.getSourceNode()))
			{
				//g.addEdge(ic.getEdge(), ic.getEdgeSource(), ic.getEdgeDestintation(),EdgeType.DIRECTED);
				ModelBuilder.addCollapserEdge(ic);
				container.addNewEdges(ic);
			}
		}
		
		// add new Edges which where previously connected to on of the Nodes which are are now collapsed
		// the collapsed Node was the destination of the Edge
		for (MyEdge ic : newOutEdges)
		{
			if (!touchedNodes.contains(ic.getDestinationNode()))
			{
				//g.addEdge(ic.getEdge(), ic.getEdgeSource(), ic.getEdgeDestintation(),EdgeType.DIRECTED);
				ModelBuilder.addCollapserEdge(ic);
				container.addNewEdges(ic);
			}
			
		}
		
		// store all the information about the collapse operation
		history.put(startNode, container);
		
	}
	
	/**
	 * Remove all the Nodes and Edges from the graph which should not be displayed anymore after the collapse operation
	 * @param startNode where to start the collapse operation
	 * @param start should be set true, if called from outside
	 * @param work Nodes which have to be processed
	 */
	private void recCollapseGraph ( MyNode startNode, boolean start, LinkedList<MyNode> work)
	{
		//System.out.println("Start: "+start);
		if (start)
		{
			LinkedList<MyEdge> out = findOutgoingEdges(startNode);//g.getOutEdges(startNode);
			
			LinkedList<MyNode> tmp = new LinkedList<MyNode>();
			LinkedList<MyEdge> del = new LinkedList<MyEdge>();
			
			for (MyEdge e : out)
			{
				EdgeType parent = e.getEdgeType().getType().getGeneral();
				
				boolean test = false;
				// test if one of the outgoing edges is an containment
				if (parent!=null && parent.getName()!="Edge")
					test = parent.getName().equals(MyEdgeTypes.CONTAINMENT);
				else
					test = e.getEdgeType().getName().equals(MyEdgeTypes.CONTAINMENT);
				
				// if it was a containment edge, the connected nodes have to get further treatment
				if (test)
				{
					MyNode next = e.getDestinationNode();//g.getDest(e);
					// add them to the list of Edges which have to be deleted
					del.add(e);
					tmp.add(next);
				}
			}
			
			// add all the containment Nodes to the future work 
			work.addAll(tmp);
			touchedNodes.addAll(tmp);
			
			// remove the Edges from the graph
			for (int i = 0;i<del.size();i++)
			{
				MyEdge e = del.get(i);
				
				container.addOldEdge(e);//g.getSource(e), g.getDest(e)));
				
				e.setVisible(false);
			}
		}
		
		// if there are still some Nodes which must be checked
		if (work.size()>0)
		{
			MyNode workNode = work.getFirst();
			
			List<MyEdge> out = findOutgoingEdges(workNode);//new LinkedList<MyEdge2>();
			List<MyEdge> in = findIncomingEdges(workNode);//new LinkedList<MyEdge2>();
			
			if (in!=null)
			{
				// remove all the incoming edges and connect them to Nodes which will be available after the collapse operation
				for (int i = 0; i<in.size();i++)
				{
					//System.out.println("loop in "+i);
					MyEdge e= in.get(i);
					
					MyNode source = e.getSourceNode();//g.getSource(e);
					
					container.addOldEdge(e);//g.getSource(e), g.getDest(e)));
					//g.removeEdge(e);
					e.setVisible(false);
					newInEdges.add(new MyEdge(e.getEdge(), e.getEdgeType(), source, recStartNode));// InfoContainer(e, source, recStartNode));
					//g.addEdge(e, source, recStartNode);
				}
			}
			if (out!=null)
			{	
				// remove all the outgoing edges and connect them to Nodes which will be available after the collapse operation
				for (int i = 0; i<out.size();i++)
				{
				//	System.out.println("loop out "+out.size());
					MyEdge e= out.get(i);
					MyNode dest = e.getDestinationNode();//g.getDest(e);
			//		System.out.println("Remove Edge  "+e.getEdgeType().getName());
					container.addOldEdge(e);//g.getSource(e), g.getDest(e)));
					//g.removeEdge(e);
					e.setVisible(false);
			//		System.out.println("Add to work  "+dest.getName());
					newOutEdges.add(new MyEdge(e.getEdge(),e.getEdgeType(),recStartNode, e.getDestinationNode()));
					if (e.getEdgeType().getName().equals(MyEdgeTypes.CONTAINMENT) && !work.contains(dest) )
					{
						work.add(dest);
						touchedNodes.add(dest);
						
					}
				}
			}
			//System.out.println("workNode " + (workNode==null)+" name "+ workNode.getName());
			container.addOldNode(workNode);
			//g.removeVertex(workNode);
			workNode.setVisible(false);
			work.remove(workNode);
			
		//	System.out.println("Work to do "+work.size());
			//for (MyNode tmp :work)
		//	{
		//		System.out.println("Node : " + tmp.getRealName());
		//	}
		//	System.out.println("		Tried to remove Node : " + workNode.getRealName());
			//if (work.size()>0)
		//	System.out.println("----------------------");
			recCollapseGraph( null, false, work);
		}
	}
	
	/*public Graph<MyNode2,MyEdge2> expandNode(Graph<MyNode2,MyEdge2> g, MyNode2 startNode, boolean nodeDetails)
	{
		CollapseContainer container = this.history.get(startNode);
		
		if (container==null)
			return g;
		else
		{
			LinkedList<InfoContainer> workEdges = container.getNewEdges();
			
			// remove added Edges
			for (InfoContainer ic :workEdges)
			{
				g.removeEdge(ic.getEdge());
			}
			
			// add old Nodes
			LinkedList<MyNode2> nodes = container.getOldNodes();
			
			for (MyNode2 n : nodes)
			{
				if (n!=null)
				{
					n.setDetailedOutput(nodeDetails);
					g.addVertex(n);
				}
			}
			
			// add old Edges
			workEdges = container.getOldEdges();
			for (InfoContainer ic :workEdges)
			{
				g.addEdge(ic.getEdge(), ic.getEdgeSource(), ic.getEdgeDestintation(), EdgeType.DIRECTED);
			}
			
			this.history.remove(startNode);
			return g;
		}
	}*/
	
	/**
	 * Expands the graph again from a given startNode
	 * @param startNode  the node which should be expanded
	 * @param nodeDetails the mode of details display for the Node which should be applied
	 */
	public void expandNode( MyNode startNode, boolean nodeDetails)
	{
		// was there any collapse operation executed previously 
		CollapseContainer container = this.history.get(startNode);
		
		//if yes undo all the operation
		if (container!=null)
		{
			LinkedList<MyEdge> workEdges = container.getNewEdges();
			
			// remove added Edges
			for (MyEdge ic :workEdges)
			{
				ModelBuilder.removeCollapserEdge(ic);
			}
			
			// add old Nodes
			LinkedList<MyNode> nodes = container.getOldNodes();
			
			for (MyNode n : nodes)
			{
				if (n!=null)
				{
					MyNode tmp = ModelBuilder.findNode(n.getNode());
					
					tmp.setDetailedOutput(nodeDetails);
					tmp.setVisible(true);
					
				}
			}
			
			// add old Edges
			workEdges = container.getOldEdges();
			for (MyEdge ic :workEdges)
			{
				MyEdge tmp = ModelBuilder.findEdge(ic.getEdge());
				
				tmp.setVisible(true);
			}
			
			this.history.remove(startNode);
		}
	}
	
	/**
	 * Test if a certain Node is expandable
	 * @param startNode the Node which should be tested
	 * @return true if the node is expandable, fals if not
	 */
	public boolean isExpandable (MyNode startNode)
	{
		CollapseContainer container = this.history.get(startNode);
		
		if (container==null)
			return false;
		else
			return true;
	}
	
	/**
	 * Test if a Node can collapsed
	 * @param startNode the node which should be tested
	 * @return true if the node can be collapsed, false otherwise
	 */
	public boolean isCollapsable (MyNode startNode)
	{
		LinkedList<MyEdge> out = findOutgoingEdges(startNode);//g.getOutEdges(startNode);
		for (MyEdge e : out)
		{
			if (e.getEdgeType().getName().equals(MyEdgeTypes.CONTAINMENT))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Was any collapse operation executed which is not yet undone(expanded)
	 * @return true if there are operations which have to be undone(expanded), false otherwise
	 */
	public boolean CollapserActive()
	{
		if (this.history.size()>0)
			return true;
		else
			return false;
	}
	/**
	 * remove all the expand operations which are not yet executed
	 */
	public void reset()
	{
		this.history.clear();
	}
	
	/**
	 * Find for a given Node all the outgoing edges
	 * @param node the node which should be searched
	 * @return a list with all the outgoing Edges
	 */
	private LinkedList<MyEdge> findOutgoingEdges (MyNode node)
	{
		LinkedList<MyEdge> res = new LinkedList<MyEdge>();
		
		for (MyEdge e :ModelBuilder.getAllEdges())
		{
			if (e.isVisible() && e.getSourceNode().equals(node))
				res.add(e);
		}
		
		return res;
	}
	
	/**
	 * Find for a given Node all the incoming edges
	 * @param node the node which should be searched
	 * @return a list with all the incoming Edges
	 */
	private LinkedList<MyEdge> findIncomingEdges (MyNode node)
	{
		LinkedList<MyEdge> res = new LinkedList<MyEdge>();
		
		for (MyEdge e :ModelBuilder.getAllEdges())
		{
			if (e.isVisible() && e.getDestinationNode().equals(node))
				res.add(e);
		}
		return res;
	}
}
