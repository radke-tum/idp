package reqtool.event;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

public class NewVersionEvent extends GvizNodeEvent {

	public NewVersionEvent(MyNode node, GraphVisualization gViz) {
		super(node, gViz);
	}

}
