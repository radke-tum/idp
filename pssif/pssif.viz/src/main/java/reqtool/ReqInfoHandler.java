package reqtool;

import com.google.common.eventbus.Subscribe;

import reqtool.event.ReqInfoEvent;
import reqtool.graph.RequirementInfoPopup;
import reqtool.handler.EventHandler;

public class ReqInfoHandler implements EventHandler {
	
	@Subscribe
	public void handleEvent(ReqInfoEvent event) {
		if (new RequirementInfoPopup(event.getSelectedNode()).showPopup() ) {
		}
	}

}
