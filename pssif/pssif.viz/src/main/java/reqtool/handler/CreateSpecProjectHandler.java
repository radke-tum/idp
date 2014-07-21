package reqtool.handler;

import com.google.common.eventbus.Subscribe;

import reqtool.event.CreateSpecProjectEvent;
import reqtool.wizard.SpecificationProjectWizard;

public class CreateSpecProjectHandler implements EventHandler {
	private SpecificationProjectWizard reqProjImporter;
	
	public CreateSpecProjectHandler() {
		reqProjImporter = new SpecificationProjectWizard();
	}

	@Subscribe
	public boolean hanldeSpecProjectEvent(CreateSpecProjectEvent event) {
		return reqProjImporter.open(event.getFrame());
	}
	
}
