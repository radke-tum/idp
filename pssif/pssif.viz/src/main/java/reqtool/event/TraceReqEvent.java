package reqtool.event;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

public class TraceReqEvent extends GvizNodeEvent {
	
	public TraceReqEvent(MyNode node, GraphVisualization gViz) {
		super(node, gViz);
	}
	
}
