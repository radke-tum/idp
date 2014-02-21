package graph.operations;

import graph.model.MyEdgeType;
import graph.model.MyNodeType;

import java.util.LinkedList;

/**
 * Contains information about a certain View on the Graph
 *
 */
public class GraphViewContainer {
	
	private LinkedList<MyNodeType> selectedNodeTypes;
	private LinkedList<MyEdgeType> selectedEdgeTypes;
	private String viewName;
	
	/**
	 * Creates a a new container with all the information about a certain View on the Graph
	 * @param selectedNodeTypes : which Node Types should be visible
	 * @param selectedEdgeTypes : which Edge Types should be visible
	 * @param viewName : the Name of the view
	 */
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
