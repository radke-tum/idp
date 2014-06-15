package reqtool.event;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

public class ShowVersionsEvent extends GvizNodeEvent {

	public ShowVersionsEvent(MyNode node, GraphVisualization gViz) {
		super(node, gViz);
	}

}
