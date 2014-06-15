package reqtool.event;

import java.awt.Component;

public class CreateSpecProjectEvent implements Event {
	private Component frame;
	
	public CreateSpecProjectEvent(Component frame) {
		this.setFrame(frame);
	}

	public Component getFrame() {
		return frame;
	}

	private void setFrame(Component frame) {
		this.frame = frame;
	}

}
