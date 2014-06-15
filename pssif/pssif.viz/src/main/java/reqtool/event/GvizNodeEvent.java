package reqtool.event;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

public class GvizNodeEvent extends NodeEvent {
	private GraphVisualization gViz;

	public GvizNodeEvent(MyNode node, GraphVisualization gViz) {
		super(node);
		setGViz(gViz);
	}

	public GraphVisualization getGViz() {
		return gViz;
	}

	private void setGViz(GraphVisualization gViz) {
		this.gViz = gViz;
	}

}
