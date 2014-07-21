package reqtool.handler;

import com.google.common.eventbus.Subscribe;

import reqtool.event.NewVersionEvent;
import reqtool.graph.VersionManagerPopup;

public class NewVersionHandler implements EventHandler{

	@Subscribe
	public void handleEvent(NewVersionEvent event) {
		if (VersionManagerPopup.showPopup(event.getSelectedNode())) {
			event.getGViz().updateGraph();
		}
	}
	
}
