package reqtool.handler;

import reqtool.TestCaseVerifier;
import reqtool.event.VerifiTestCaseEvent;

import com.google.common.eventbus.Subscribe;

public class VerifyTestCaseHandler implements EventHandler{

	@Subscribe
	public void handleVerifyTestCaseEvent(VerifiTestCaseEvent event) {
		new TestCaseVerifier(event.getSelectedNode()).verifyTestCase();
		event.getGViz().updateGraph();
	}
	
}
