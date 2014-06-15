package reqtool.handler;

import java.util.Map.Entry;

import javax.swing.JTextField;

import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;
import model.ModelBuilder;

import com.google.common.eventbus.Subscribe;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import reqtool.event.NewDeriveRequirementEvent;
import reqtool.graph.DeriveRequirementPopup;

public class NewDeriveRequirementHandler implements EventHandler {
	
	@Subscribe
	public void handleDeriveRequirement(NewDeriveRequirementEvent event) {
		
		DeriveRequirementPopup popup = new DeriveRequirementPopup();
		popup.showPopup(event.getSelectedNode());
		MyNodeType req = ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_REQUIREMENT);
		
		
		
		
    	for (Entry<Attribute, JTextField> input:popup.getAttrTextFields().entrySet()) {
        	if (  input.getKey().getName().equals(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME)  &&
        			input.getValue().getText()!=null && input.getValue().getText().length()>0  )
        	{
        		
        		MyNode derivedReq = ModelBuilder.addNewNodeFromGUI(input.getValue().getText(), req);
        		ModelBuilder.addNewEdgeGUI(derivedReq, event.getSelectedNode(), ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_BASED_ON), true);
        		event.getGViz().updateGraph();
        		
        	}          
    	}
		
	}

}
