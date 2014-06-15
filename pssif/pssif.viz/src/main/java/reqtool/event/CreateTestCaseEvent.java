package reqtool.event;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

public class CreateTestCaseEvent extends GvizNodeEvent {

	public CreateTestCaseEvent(MyNode node, GraphVisualization gViz) {
		super(node, gViz);
	}

}
