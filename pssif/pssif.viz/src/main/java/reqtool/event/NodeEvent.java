package reqtool.event;

import graph.model.MyNode;

public abstract class NodeEvent implements Event {
	private MyNode selectedNode;
	
	public NodeEvent(MyNode node) {
		selectedNode = node;
	}

	public MyNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(MyNode selectedNode) {
		this.selectedNode = selectedNode;
	}
	
}
