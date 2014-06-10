package reqtool.event;

import java.util.HashMap;

import reqtool.handler.EventHandler;

public class EventBus {
	private HashMap<Event, EventHandler> observers;
	
	public void post(Event event) {
		observers.get(event).handleEvent(event);
	}
	
	public void register(EventHandler<Event> handler) {
		
	}

}
