package reqtool.wizard;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import model.ModelBuilder;
import reqtool.graph.RequirementFromSpecPopup;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;

public class RequirementWizard {
	private MyNode requirementNode;
	private MyNode specificationNode;
	private RequirementFromSpecPopup newReqPopup;
	
	public RequirementWizard(MyNode specNode) {
		specificationNode = specNode;
	}
	
	public void open() {
		newReqPopup = new RequirementFromSpecPopup();
		if (newReqPopup.showPopup()) {
			createRequirementNode();
			
			if (newReqPopup.selectedAuthorCheckBox()){
				createAuthor();
				
			}
			
			if (newReqPopup.selectedPrincipalCheckBox()){
				createPrincipalNode();
				
			}
			if (true){
				createAbsLevelNode();
				
			}
		
		}
	}

	private void createAbsLevelNode() {
		
		//MyNodeType absLevel = ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_ABSTRACTION_LEVEL);
		//MyEdgeType belongs = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_BELONGS_TO);
		//MyNode absLevelNode = ModelBuilder.addNewNodeFromGUI(newReqPopup.getAbstractionLevelName(), absLevel);
		//ModelBuilder.addNewEdgeGUI(requirementNode, absLevelNode, belongs, true);
		requirementNode.getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ABS_LEVEL).getOne().set(requirementNode.getNode(), PSSIFOption.one(PSSIFValue.create(newReqPopup.getAbstractionLevelName())));
		
	}

	private void createPrincipalNode() {
		MyNodeType principal = ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_PRINCIPAL);
		MyEdgeType requests = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CAUSAL_REQUESTS);
		MyNode principalNode = ModelBuilder.addNewNodeFromGUI(newReqPopup.getPrincipalName(), principal);
		ModelBuilder.addNewEdgeGUI(principalNode, requirementNode, requests, true);
		
	}

	private void createAuthor() {
		
		MyNodeType author = ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_AUTHOR);
		MyEdgeType creates = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CAUSAL_CREATES);
		MyNode authorNode = ModelBuilder.addNewNodeFromGUI(newReqPopup.getAuthorName(), author);
		ModelBuilder.addNewEdgeGUI(authorNode, requirementNode, creates, true);
		
	}

	private void createRequirementNode() {
		MyNodeType requirement = ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_REQUIREMENT);
		MyEdgeType defines = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_REFERENTIAL_DEFINES);
		requirementNode = ModelBuilder.addNewNodeFromGUI(newReqPopup.getReqName(), requirement);
		ModelBuilder.addNewEdgeGUI(specificationNode, requirementNode, defines, true);
	}

}
