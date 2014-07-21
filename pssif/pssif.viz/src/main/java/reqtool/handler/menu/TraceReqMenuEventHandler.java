package reqtool.handler.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import reqtool.RequirementTracer;
import reqtool.event.TraceReqEvent;
import reqtool.event.UntraceReqEvent;
import reqtool.event.bus.ReqToolReqistry;
import reqtool.event.menu.TraceReqMenuEvent;

import com.google.common.eventbus.Subscribe;

public class TraceReqMenuEventHandler implements MenuEventHandler {
	
	@Subscribe
	public void handleTraceReqMenuEvent(final TraceReqMenuEvent event) {
		JMenuItem submenu;
		
		if (RequirementTracer.isTracedNode(event.getSelectedNode())) {
			submenu = new JMenuItem("Untrace requirement");
			submenu.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ReqToolReqistry.getInstance().post(new UntraceReqEvent(event.getSelectedNode(), event.getGViz()));
				}

			});
		} else {
			submenu = new JMenuItem("Trace requirement");
			submenu.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ReqToolReqistry.getInstance().post(new TraceReqEvent(event.getSelectedNode(), event.getGViz()));
				}
			});
		}
		
		event.getMenu().add(submenu);
	}

}
