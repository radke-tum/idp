package graph.operations;

import graph.model2.MyEdgeType;
import graph.model2.MyNodeType;

import java.util.LinkedList;

public class GraphViewContainer {
	
	private LinkedList<MyNodeType> selectedNodeTypes;
	private LinkedList<MyEdgeType> selectedEdgeTypes;
	private String viewName;
	
	public GraphViewContainer(LinkedList<MyNodeType> selectedNodeTypes, LinkedList<MyEdgeType> selectedEdgeTypes, String viewName)
	{
		this.viewName = viewName;
		this.selectedEdgeTypes = selectedEdgeTypes;
		this.selectedNodeTypes = selectedNodeTypes;
	}
	
	public GraphViewContainer()
	{
		this.selectedEdgeTypes = new LinkedList<MyEdgeType>();
		this.selectedNodeTypes = new LinkedList<MyNodeType>();
	}

	public LinkedList<MyNodeType> getSelectedNodeTypes() {
		return selectedNodeTypes;
	}

	public void setSelectedNodeTypes(LinkedList<MyNodeType> selectedNodeTypes) {
		this.selectedNodeTypes = selectedNodeTypes;
	}

	public LinkedList<MyEdgeType> getSelectedEdgeTypes() {
		return selectedEdgeTypes;
	}

	public void setSelectedEdgeTypes(LinkedList<MyEdgeType> selectedEdgeTypes) {
		this.selectedEdgeTypes = selectedEdgeTypes;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
}
