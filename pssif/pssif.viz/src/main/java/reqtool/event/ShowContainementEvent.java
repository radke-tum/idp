package reqtool.event;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

public class ShowContainementEvent extends GvizNodeEvent {

	public ShowContainementEvent(MyNode node, GraphVisualization gViz) {
		super(node, gViz);
	}

}
