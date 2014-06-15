package reqtool.handler;

import reqtool.event.CreateTestCaseEvent;
import reqtool.graph.TestCaseCreatorPopup;

import com.google.common.eventbus.Subscribe;

public class CreateTestCaseHandler implements EventHandler {
	
	@Subscribe
	public void handleNewTestCaseEvent(CreateTestCaseEvent event) {
		TestCaseCreatorPopup crPopup = new TestCaseCreatorPopup(event.getSelectedNode());
		crPopup.showPopup();
		event.getGViz().updateGraph();
	}
	
}
