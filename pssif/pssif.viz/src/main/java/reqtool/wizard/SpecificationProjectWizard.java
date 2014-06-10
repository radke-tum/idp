package reqtool.wizard;

import graph.model.MyEdgeType;
import graph.model.MyNode;

import java.awt.Component;

import model.FileImporter;
import model.ModelBuilder;
import reqtool.SpecificationNodePopup;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.impl.ModelImpl;

public class SpecificationProjectWizard {
	
	private SpecificationNodePopup newSpecPopup;
	private MyNode specificationArtifactNode;
	
	public SpecificationProjectWizard() {
		initModel();
	}

	private void initModel() {
		Model model = new ModelImpl();
		if (model!=null) {
	       ModelBuilder.addModel(ModelBuilder.getMetamodel(), model);
        }
	}

	public boolean open(Component frame) {
		initModel();
		newSpecPopup = new SpecificationNodePopup();

		if (newSpecPopup.showPopup()) {
			
			if (!newSpecPopup.selectedFileImport()) { 
				createSpecificationNode();
			}
			
			else if (newSpecPopup.selectedFileImport()) {
				FileImporter importer = new FileImporter();
				
				if (importer.showPopup(frame)) {
					createSpecificationNode();
					createEdges(importer.getLastImportedModel());
				}
			}
			
			if (newSpecPopup.selectedNewReq()) {
				new RequirementWizard(specificationArtifactNode).open();
			}

			return true;
		}

		return false;
	}

	private void createEdges(Model newModel/*LinkedList<MyNode> excludedNodes*/) {
		MyEdgeType contains = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_CONTAINS);
		for (MyNode node : ModelBuilder.getAllNodes()) {
			
			PSSIFOption<? extends Node> result = node.getNodeType().getType().apply(newModel, node.getNode().getId(), false);
			if (!result.isNone()) {
				ModelBuilder.addNewEdgeGUI(specificationArtifactNode, node, contains, true);
			}
			
			/*
			if (conrainer.getAllNodes().contains(node) && !node.equals(specificationArtifactNode)) {
				ModelBuilder.addNewEdgeGUI(specificationArtifactNode, node, contains, true);
			}
			*/
		}
	}

	private void createSpecificationNode() {
		specificationArtifactNode = ModelBuilder.addNewNodeFromGUI(newSpecPopup.getSpecArtifName(), newSpecPopup.getSelectedSpecArtifType());
	}
	
	public MyNode getSpecificationArtifactNode() {
		return specificationArtifactNode;
	}

	private void setSpecificationArtifactNode(MyNode specificationArtifactNode) {
		this.specificationArtifactNode = specificationArtifactNode;
	}
	
}
