package reqtool.event.menu;

import javax.swing.JPopupMenu;

import graph.model.MyNode;
import gui.graph.GraphVisualization;
import reqtool.event.GvizNodeEvent;
import reqtool.event.NodeEvent;

public class CreateReqMenuEvent extends GvizNodeEvent {
	private JPopupMenu popup;

	public CreateReqMenuEvent(MyNode node, JPopupMenu popup, GraphVisualization gViz) {
		super(node, gViz);
		this.setPopup(popup);
	}

	public JPopupMenu getPopup() {
		return popup;
	}

	private void setPopup(JPopupMenu popup) {
		this.popup = popup;
	}

}
