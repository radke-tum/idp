package reqtool.handler;

import reqtool.RequirementVersionManager;
import reqtool.event.HideVersionsEvent;
import reqtool.event.ShowVersionsEvent;

import com.google.common.eventbus.Subscribe;

public class VersionVisibilityHandler implements EventHandler {

	@Subscribe
	public void handleShowVersionsEvent(ShowVersionsEvent event) {
		RequirementVersionManager.showVersions(event.getSelectedNode());
		event.getGViz().updateGraph();
	}
	
	@Subscribe
	public void handleHideVersionsEvent(HideVersionsEvent event) {
		RequirementVersionManager.hideVersions(event.getSelectedNode());
		event.getGViz().updateGraph();
	}
	
}
