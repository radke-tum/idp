package reqtool.handler;

import com.google.common.eventbus.Subscribe;

import reqtool.event.ResolveIssueEvent;
import reqtool.graph.IssueResolverPopup;

public class ResolveIssueHandler implements EventHandler{
	
	@Subscribe
	public void resolveIssue(ResolveIssueEvent event) {
		IssueResolverPopup popup = new IssueResolverPopup(event.getSelectedNode());
		popup.showPopup();
		event.getGViz().updateGraph();
	}
}
