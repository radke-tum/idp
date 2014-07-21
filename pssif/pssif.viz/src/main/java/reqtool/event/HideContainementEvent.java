package reqtool.event;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

public class HideContainementEvent extends GvizNodeEvent {

	public HideContainementEvent(MyNode node, GraphVisualization gViz) {
		super(node, gViz);
	}

}
