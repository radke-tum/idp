package reqtool;

import graph.model.MyEdge;
import graph.model.MyNode;
import gui.graph.GraphVisualization;

import java.util.LinkedList;

import reqtool.graph.IssueResolverPopup;
import model.ModelBuilder;
import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;

public class IssueResolver {
	private MyNode selectedNode;
	
	public IssueResolver (MyNode myNode) {
		this.selectedNode = myNode;
	}
	
	public void resolveIssue () {
		
		System.out.println("Resolving Issue for Issue: "+selectedNode.getName());
		
		LinkedList<MyNode> solArts = getSolArtifacts();
		System.out.println("Resolving Issue for  TestCase: "+getTestCase().getName());
		
		String condition = getTestCase().getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT).getOne().get(getTestCase().getNode()).getOne().asString();
				//getNodeType().getType().getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT).getOne().get(getTestCase().getNode()).;
		
		System.out.println("TestCase Condition Leading to Issue: "+condition);
		
		
		System.out.println("Resolving Issue for  Requirement: "+getRequirement().get(0).getName());
		
		
		if (solArts.size()==0){
			System.out.println("No Solution Artifact related to this Issue!");
		}
		else if (solArts.size()==1){
			MyNode solArt = solArts.get(0);
			System.out.println("Resolving Issue for  Solution Artifact: "+solArt.getName());
			MyNode changeProposal = ModelBuilder.addNewNodeFromGUI("ChangeProposal for Issue"+selectedNode.getName() , ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_CHANGE_PROPOSAL));
			ModelBuilder.addNewEdgeGUI(selectedNode, changeProposal, ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_LEADS_TO), true);
			MyNode decision = ModelBuilder.addNewNodeFromGUI("Decision for ChangeProposal"+selectedNode.getName() , ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_DECISION));
			ModelBuilder.addNewEdgeGUI(changeProposal, decision, ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_LEADS_TO), true);
			MyNode changeEvent = ModelBuilder.addNewNodeFromGUI("ChangeEvent"+selectedNode.getName(),  ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_CHANGE_EVENT));
			ModelBuilder.addNewEdgeGUI(selectedNode, changeEvent, ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_REFERENTIAL), true);
			ModelBuilder.addNewEdgeGUI(changeProposal, changeEvent, ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_REFERENTIAL), true);
			ModelBuilder.addNewEdgeGUI(decision, changeEvent, ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_REFERENTIAL), true);
		}
		else {
			System.out.println("More than 1 Solution Artifact - TODO");
		}
			
		
		
	}
	
	public void createEventNodes(){
		
		
	}
	
	public LinkedList<MyNode> getSolArtifacts() {
		TestCaseVerifier test = new TestCaseVerifier(this.getTestCase());
		return test.getVerifiedSolArtifacts();
	}
	
	public LinkedList<MyNode> getRequirement() {
		LinkedList<MyNode> nodes = new LinkedList<MyNode>();
		for (MyEdge e: ModelBuilder.getAllEdges()){
			if (e.getSourceNode().equals(this.getTestCase()) && ((MyNode) e.getDestinationNode()).getNodeType().equals(ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_REQUIREMENT))){
				nodes.add((MyNode) e.getDestinationNode());
			}
		}
		return nodes;
	}

	public MyNode getTestCase(){
		MyNode node = null;
		for (MyEdge e: ModelBuilder.getAllEdges()){
			if (e.getDestinationNode().equals(selectedNode) && e.getEdgeType().equals(ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_LEADS_TO))){
				node =  (MyNode) e.getSourceNode();
			}
		}
		return node;
	}

}
