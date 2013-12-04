package graph.operations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import graph.model.ConnectionType;
import graph.model.MyEdge;
import graph.model.MyNode;

public class MyCollapser {
	

	private MyNode recStartNode;
	private LinkedList<InfoContainer> newInEdges;
	private LinkedList<InfoContainer> newOutEdges;
	private LinkedList<MyNode> touchedNodes;
	
	private HashMap<MyNode,CollapseContainer> history;
	private CollapseContainer container;
	
	public MyCollapser()
	{
		this.history = new HashMap<MyNode, CollapseContainer>();
	}
	
	public Graph<MyNode,MyEdge> collapseGraph(Graph<MyNode,MyEdge> g, MyNode startNode) 
	{
		// save copy
		container = new CollapseContainer();
		
		recStartNode=startNode;
		newInEdges = new LinkedList<>();
		newOutEdges = new LinkedList<>();
		touchedNodes = new LinkedList<>();
		
		recCollapseGraph(g,startNode,true,new LinkedList<MyNode>());
		
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
	}
	
	
	private void recCollapseGraph (Graph<MyNode,MyEdge> g, MyNode startNode, boolean start, LinkedList<MyNode> work)
	{
		System.out.println("Start: "+start);
		if (start)
		{
			Collection<MyEdge> out = g.getOutEdges(startNode);
			
			LinkedList<MyNode> tmp = new LinkedList<>();
			LinkedList<MyEdge> del = new LinkedList<>();
			for (MyEdge e : out)
			{
				if (e.getConnectionType() == ConnectionType.INCLUDES)
				{
					MyNode next = g.getDest(e);
					del.add(e);
					
					tmp.add(next);
					//System.out.println("Nodes to process "+next.getName());
				}
			}
			work.addAll(tmp);
			touchedNodes.addAll(tmp);
			for (int i = 0;i<del.size();i++)
			{
				System.out.println("delete first start Edge: "+del.get(i).toString());
				
				MyEdge e = del.get(i);
				
				container.addOldEdge(new InfoContainer(e, g.getSource(e), g.getDest(e)));
				g.removeEdge(e);
			}
		}
		
		
		
		if (work.size()>0)
		{
			System.out.println("loop Work "+work.size());
			MyNode workNode = work.getFirst();
			
			Collection<MyEdge> fout = g.getOutEdges(workNode);
			Collection<MyEdge> fin =  g.getInEdges(workNode);
			
			List<MyEdge> out = new LinkedList<>();
			List<MyEdge> in = new LinkedList<>();
			
			if (fin!=null)
			{
				Iterator<MyEdge> it = fin.iterator();
				while(it.hasNext())
				{
					in.add(it.next());
				}
				
				//redirect edges to "collapsed" Node
			
				//Iterator<MyEdge> it = in.iterator();
				for (int i = 0; i<in.size();i++)
				{
					System.out.println("loop in "+i);
					MyEdge e= in.get(i);
					
					MyNode source = g.getSource(e);
					
					container.addOldEdge(new InfoContainer(e, g.getSource(e), g.getDest(e)));
					g.removeEdge(e);
					newInEdges.add(new InfoContainer(e, source, recStartNode));
					//g.addEdge(e, source, recStartNode);
				}
			}
			if (fout!=null)
			{				
				Iterator<MyEdge> it = fout.iterator();
				while(it.hasNext())
				{
					out.add(it.next());
				}
				
				for (int i = 0; i<out.size();i++)
				{
					System.out.println("loop out "+out.size());
					MyEdge e= out.get(i);
					MyNode dest = g.getDest(e);
					System.out.println("Remove Edge  "+e.getConnectionType().getName());
					container.addOldEdge(new InfoContainer(e, g.getSource(e), g.getDest(e)));
					g.removeEdge(e);
					System.out.println("Add to work  "+dest.getName());
					newOutEdges.add(new InfoContainer(e, recStartNode, dest));
					
					if (e.getConnectionType()==ConnectionType.INCLUDES && !work.contains(dest) )
					{
						work.add(dest);
						touchedNodes.add(dest);
						
					}
				}
			}
			//System.out.println("workNode " + (workNode==null)+" name "+ workNode.getName());
			container.addOldNode(workNode);
			g.removeVertex(workNode);
			work.remove(workNode);
			System.out.println("Work to do "+work.size());
			//if (work.size()>0)
			System.out.println("----------------------");
			recCollapseGraph(g, null, false, work);
		}
		
	
	}
	
	public Graph<MyNode,MyEdge> expandNode(Graph<MyNode,MyEdge> g, MyNode startNode)
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
			LinkedList<MyNode> nodes = container.getOldNodes();
			
			for (MyNode n : nodes)
			{
				if (n!=null)
					g.addVertex(n);
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
		
			
	}
	
}
