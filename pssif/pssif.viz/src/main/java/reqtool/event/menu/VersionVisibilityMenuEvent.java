package reqtool.event.menu;

import javax.swing.JMenu;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

public class VersionVisibilityMenuEvent extends MenuEvent {

	public VersionVisibilityMenuEvent(MyNode node, JMenu menu, GraphVisualization gViz) {
		super(node, menu, gViz);
	}

}
