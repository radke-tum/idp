package reqtool;

import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;
import gui.graph.GraphVisualization;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.ModelBuilder;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Node;

public class TestCaseCreator {

	public static LinkedList<MyNode> getRequirementSatisfyNodes(MyNode requirementNode) {
		MyEdgeType satisfies = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_LOGICAL_SATISFIES);

		LinkedList<MyNode> nodes = new LinkedList<MyNode>();
		for (MyEdge myEdge : ModelBuilder.getAllEdges()) {
			if (myEdge.getDestinationNode().equals(requirementNode)	&& myEdge.getEdgeType().equals(satisfies)) {
				nodes.add((MyNode) myEdge.getSourceNode());
			}
		}
		
		return nodes;
	}

	public static void createTestCase(GraphVisualization gViz, MyNode mNode, List<MyNode> solutionArtifacts) {

		System.out.println("Creating Test Case for " + mNode.getName());

		MyNodeType testCaseNodeType = ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_TEST_CASE);

		MyEdgeType basedOn = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_BASED_ON);

		ModelBuilder.addNewNodeFromGUI("TC" + mNode.getNode().getId(), testCaseNodeType);

		/*
		 * Create edge "based on" between testcase and requirement
		 */
		MyNode solutionArtifactNode = null;
		MyNode testCaseNode = null;
		MyNodeType solutionArtifact = ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_SOL_ARTIFACT);
		for (MyNode myNode : ModelBuilder.getAllNodes()) {
			String name = myNode.getName().toString();
			String id = "TC" + mNode.getNode().getId().toString();

			if (name.equals(id)) {
				testCaseNode = myNode;
				boolean hasEdge = false;
				for (MyEdge edge: ModelBuilder.getAllEdges()) {
					if (edge.getSourceNode().equals(myNode) && edge.getDestinationNode().equals(mNode) && edge.getEdgeType().equals(basedOn)) {
						hasEdge = true;
					}
				}
				if (! hasEdge) {
					ModelBuilder.addNewEdgeGUI(myNode, mNode, basedOn, true);
				}
			}
			if (myNode.getNodeType().equals(solutionArtifact)) {
				solutionArtifactNode = myNode;
			}

		}

		/*
		 * Create verifies edge between testcase and solution artifact requirement
		 */
		verifyTestCase(solutionArtifacts, testCaseNode);

		gViz.updateGraph();
	}

	private static boolean verifyTestCase(List<MyNode> solArtifNodes, MyNode testCaseNode) {
		MyEdgeType verifies = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_LOGICAL_VERIFIES);
		for(MyNode solArtfNode:solArtifNodes) {
			ModelBuilder.addNewEdgeGUI(testCaseNode, solArtfNode, verifies, true);
		}
		return true;
	}

}