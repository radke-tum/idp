package graph.operations;

import graph.model.MyEdgeType;
import graph.model.MyNodeType;
import gui.GraphView;

import java.util.HashMap;
import java.util.LinkedList;

public class MasterFilter {
		
	private LinkedList<String> activeNodeAttributFilters;
	private LinkedList<String> activeEdgeAttributFilters;
	
	private LinkedList<String> activeNodeAndEdgeTypeFilters;
	private HashMap<String, GraphViewContainer> nodeAndEdgeTypeFilters;
		
	private GraphView graphview;
	
	public MasterFilter(GraphView graphview)
	{
		this.graphview = graphview;
		this.activeEdgeAttributFilters = new LinkedList<String>();
		this.activeNodeAttributFilters = new LinkedList<String>();
		this.activeNodeAndEdgeTypeFilters = new LinkedList<String>();
		this.nodeAndEdgeTypeFilters = this.graphview.getGraph().getAllGraphViews();
	}
	
	public void addNodeAttributFilter(String condition, boolean activate)
	{
		activeNodeAttributFilters.add(condition);
		
		if (activate)
		{
			try
			{
				activeNodeAttributFilters.remove(condition);
				applyNodeAttributeFilter(condition);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void removeNodeAttributFilter(String condition) throws Exception
	{
		activeNodeAttributFilters.remove(condition);
		
		undoNodeAttributeFilter(condition);
		
		AttributeFilter.removeNodeCondition(condition);
		
		applyAllActiveFilters();
	}
	
	public void addEdgeAttributFilter(String condition, boolean activate)
	{
		activeEdgeAttributFilters.add(condition);
		if (activate)
		{
			try
			{
				activeEdgeAttributFilters.remove(condition);
				applyEdgeAttributeFilter(condition);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	public void removeEdgeAttributFilter(String condition) throws Exception
	{
		activeEdgeAttributFilters.remove(condition);
		
		undoEdgeAttributeFilter(condition);
		
		AttributeFilter.removeEdgeCondition(condition);
		
		applyAllActiveFilters();
		
	}
	
	public void applyNodeAttributeFilter(String condition) throws Exception
	{
		activeNodeAttributFilters.add(condition);
		
		AttributeFilter.applyNodeCondition(condition);
		
		//this.graphview.getGraph().updateGraph();
		applyAllActiveFilters();
	}
	
	public void undoNodeAttributeFilter(String condition) throws Exception 
	{
		activeNodeAttributFilters.remove(condition);
		
		AttributeFilter.undoNodeCondition(condition/*, activeNodeAttributFilters*/);
		
		applyAllActiveFilters();
	}
	
	public void applyEdgeAttributeFilter(String condition) throws Exception
	{
		activeEdgeAttributFilters.add(condition);
		
		AttributeFilter.applyEdgeCondition(condition);
		
		//this.graphview.getGraph().updateGraph();
		applyAllActiveFilters();
	}
	
	public void undoEdgeAttributeFilter(String condition) throws Exception
	{
		activeEdgeAttributFilters.remove(condition);
		
		AttributeFilter.undoEdgeCondition(condition/*,activeEdgeAttributFilters*/);
		
		applyAllActiveFilters();
	}
	
	public void addNodeAndEdgeTypeFilter(LinkedList<MyNodeType> nodes, LinkedList<MyEdgeType> edges, String viewName, boolean activate)
	{
		this.activeNodeAndEdgeTypeFilters.add(viewName);
		
		if (activate)
		{
			applyNodeAndEdgeTypeFilter(viewName);
			
			applyAllActiveFilters();
		}
	}
	
	public void removeNodeAndEdgeTypeFilter(String condition)
	{
		this.activeNodeAndEdgeTypeFilters.remove(condition);
		
		undoNodeAndEdgeTypeFilter(condition);
		
		applyAllActiveFilters();
		
	}
	
	public void applyNodeAndEdgeTypeFilter(String name)
	{
		activeNodeAndEdgeTypeFilters.add(name);
		
		GraphViewContainer gvc = this.nodeAndEdgeTypeFilters.get(name);
		NodeAndEdgeTypeFilter.filter(gvc.getSelectedNodeTypes(), gvc.getSelectedEdgeTypes(), name);
		
		//this.graphview.getGraph().updateGraph();
		applyAllActiveFilters();
	}
	
	public void undoNodeAndEdgeTypeFilter(String name)
	{
		activeNodeAndEdgeTypeFilters.remove(name);
		
		NodeAndEdgeTypeFilter.undoFilter(name);
		
		applyAllActiveFilters();
		
	}
	
	private void applyAllActiveFilters()
	{
		applyAllActiveNodeAndEdgeTypeFilters();
		applyAllActiveAttributFilters();
		
		this.graphview.getGraph().updateGraph();
	}
	
	private void applyAllActiveAttributFilters()
	{
		try
		{
			for (String condition : activeNodeAttributFilters)
			{
				AttributeFilter.applyNodeCondition(condition);
			}
			
			for (String condition : activeEdgeAttributFilters)
			{
				AttributeFilter.applyEdgeCondition(condition);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void applyAllActiveNodeAndEdgeTypeFilters()
	{
		NodeAndEdgeTypeFilter.applyAllFilters();
	}
	
	public boolean NodeAttributFilterActive (String name)
	{
		return activeNodeAttributFilters.contains(name);
	}
	
	public boolean EdgeAttributFilterActive (String name)
	{
		return activeEdgeAttributFilters.contains(name);
	}
	
	public boolean NodeAndEdgeTypeFilterActive (String name)
	{
		return activeNodeAndEdgeTypeFilters.contains(name);
	}
}
