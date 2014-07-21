package reqtool.event;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

public class VerifiTestCaseEvent extends GvizNodeEvent {

	public VerifiTestCaseEvent(MyNode node, GraphVisualization gViz) {
		super(node, gViz);
	}

}
