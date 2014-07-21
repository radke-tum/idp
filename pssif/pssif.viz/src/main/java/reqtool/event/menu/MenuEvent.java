package reqtool.event.menu;

import graph.model.MyNode;
import gui.graph.GraphVisualization;

import javax.swing.JMenu;

import reqtool.event.NodeEvent;

public class MenuEvent extends NodeEvent {
	private JMenu menu;
	private GraphVisualization gViz;
	
	public MenuEvent(MyNode node, JMenu menu) {
		super(node);
		this.setMenu(menu);
	}
	
	public MenuEvent(MyNode node, JMenu menu, GraphVisualization gViz) {
		super(node);
		this.setMenu(menu);
		this.setGViz(gViz);
	}

	public JMenu getMenu() {
		return menu;
	}

	private void setMenu(JMenu menu) {
		this.menu = menu;
	}

	public GraphVisualization getGViz() {
		return gViz;
	}

	private void setGViz(GraphVisualization gViz) {
		this.gViz = gViz;
	}

}
