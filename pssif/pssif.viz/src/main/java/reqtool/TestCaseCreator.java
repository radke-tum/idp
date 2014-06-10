package reqtool;

import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;
import gui.graph.GraphVisualization;

import java.util.LinkedList;
import java.util.List;

import model.ModelBuilder;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;

public class TestCaseCreator {

	public static void createTestCase(GraphVisualization gViz, MyNode mNode, List<MyNode> solutionArtifacts) {

		System.out.println("Creating Test Case for " + mNode.getName());

		MyNodeType testCaseNodeType = ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_TEST_CASE);

		MyEdgeType basedOn = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_BASED_ON);

		MyNode testCaseNode = ModelBuilder.addNewNodeFromGUI("TC" + mNode.getNode().getId(), testCaseNodeType);

		/*
		 * Create edge "based on" between testcase and requirement
		*/
		ModelBuilder.addNewEdgeGUI(testCaseNode, mNode, basedOn, true);

		/*
		 * Create verifies edge between testcase and solution artifact requirement
		 */
		createVerifyEdges(solutionArtifacts, testCaseNode);

		gViz.updateGraph();
	}

	private static boolean createVerifyEdges(List<MyNode> solArtifNodes, MyNode testCaseNode) {
		MyEdgeType verifies = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_LOGICAL_VERIFIES);
		for(MyNode solArtfNode:solArtifNodes) {
			ModelBuilder.addNewEdgeGUI(testCaseNode, solArtfNode, verifies, true);
		}
		return true;
	}

}