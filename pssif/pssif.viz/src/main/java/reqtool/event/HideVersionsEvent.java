package reqtool.event;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

public class HideVersionsEvent extends GvizNodeEvent {

	public HideVersionsEvent(MyNode node, GraphVisualization gViz) {
		super(node, gViz);
	}

}
