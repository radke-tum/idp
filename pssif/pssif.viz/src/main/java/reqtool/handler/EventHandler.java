package reqtool.handler;

import reqtool.event.Event;

public interface EventHandler<Event> {
	
	public void handleEvent(Event event);

}
