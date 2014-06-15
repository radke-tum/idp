package reqtool.event.bus;

import reqtool.event.Event;
import reqtool.handler.ContainementVisibilityHandler;
import reqtool.handler.CreateSpecProjectHandler;
import reqtool.handler.CreateTestCaseHandler;
import reqtool.handler.EventHandler;
import reqtool.handler.NewDeriveRequirementHandler;
import reqtool.handler.NewVersionHandler;
import reqtool.handler.ResolveIssueHandler;
import reqtool.handler.TraceReqEventHandler;
import reqtool.handler.VerifyTestCaseHandler;
import reqtool.handler.VersionVisibilityHandler;
import reqtool.handler.menu.CreateReqMenuEventHandler;
import reqtool.handler.menu.TraceReqMenuEventHandler;
import reqtool.handler.menu.VersionsVisibilityMenuHandler;

import com.google.common.eventbus.EventBus;

public class ReqToolReqistry {
	private EventBus eventBus;
	private static ReqToolReqistry instance;
	
	public static ReqToolReqistry newInstance() {
		instance = null;
		return getInstance();
	}
	
	public static ReqToolReqistry getInstance() {
		if (instance == null) {
			instance = new ReqToolReqistry();
		}
		return instance;
	}
	
	private ReqToolReqistry() {
		eventBus = new EventBus();
		initStaticReqVisitors();
		initStaticReqMenuVisitors();
	}
	
	public void post(Event event) {
		eventBus.post(event);
	}
	
	public void register(EventHandler handler) {
		 eventBus.register(handler);
	}

	private void initStaticReqVisitors() {
		
		register(new ResolveIssueHandler());
		register(new NewVersionHandler());
		register(new TraceReqEventHandler());
		register(new VersionVisibilityHandler());
		register(new CreateTestCaseHandler());
		register(new VerifyTestCaseHandler());
		register(new ContainementVisibilityHandler());
		register(new CreateSpecProjectHandler());
		register(new NewDeriveRequirementHandler());
		
	}
	
	private void initStaticReqMenuVisitors() {
		
		register(new CreateReqMenuEventHandler());
		register(new TraceReqMenuEventHandler());
		register(new VersionsVisibilityMenuHandler());
		
	}
	
}
