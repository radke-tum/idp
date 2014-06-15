package reqtool.event;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

public class UntraceReqEvent extends GvizNodeEvent {
	
	public UntraceReqEvent(MyNode node, GraphVisualization gViz) {
		super(node, gViz);
	}
	
}
