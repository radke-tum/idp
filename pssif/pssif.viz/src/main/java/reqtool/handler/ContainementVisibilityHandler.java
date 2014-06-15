package reqtool.handler;

import reqtool.RequirementToolbox;
import reqtool.event.HideContainementEvent;
import reqtool.event.ShowContainementEvent;

import com.google.common.eventbus.Subscribe;

public class ContainementVisibilityHandler implements EventHandler {
	
	@Subscribe
	public void handlerShowContainement(ShowContainementEvent event) {
		RequirementToolbox.showContainment(event.getSelectedNode());
		event.getGViz().updateGraph();
	}
	
	@Subscribe
	public void handlerHideContainement(HideContainementEvent event) {
		RequirementToolbox.hideContainment(event.getSelectedNode());
		event.getGViz().updateGraph();
	}

}
