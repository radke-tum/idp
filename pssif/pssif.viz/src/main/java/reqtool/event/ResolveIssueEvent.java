package reqtool.event;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

public class ResolveIssueEvent extends GvizNodeEvent {
	
	public ResolveIssueEvent(MyNode node, GraphVisualization gViz) {
		super(node, gViz);
	}
	
}
