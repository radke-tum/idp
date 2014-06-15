package reqtool.handler;

import com.google.common.eventbus.Subscribe;

import reqtool.RequirementTracer;
import reqtool.event.TraceReqEvent;
import reqtool.event.UntraceReqEvent;

public class TraceReqEventHandler implements EventHandler {

	@Subscribe
	public void handleTraceReqEvent(TraceReqEvent event) {
		RequirementTracer.traceRequirement(event.getSelectedNode());
		event.getGViz().stopTracingNodes();
	}
	
	@Subscribe
	public void handleUntraceReqEvent(UntraceReqEvent event) {
		RequirementTracer.stopTracing();
		event.getGViz().traceNodes();
	}
}
