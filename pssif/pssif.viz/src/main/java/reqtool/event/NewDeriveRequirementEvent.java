package reqtool.event;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

public class NewDeriveRequirementEvent extends GvizNodeEvent{

	public NewDeriveRequirementEvent(MyNode node, GraphVisualization gViz) {
		super(node, gViz);
	}

}
