package reqtool.event.menu;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

import javax.swing.JMenu;

public class TraceReqMenuEvent extends MenuEvent {
	
	public TraceReqMenuEvent(MyNode node, JMenu menu, GraphVisualization gViz) {
		super(node, menu, gViz);
	}
	
}
