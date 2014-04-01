package reqtool;

import model.ModelBuilder;
import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;
import gui.graph.GraphVisualization;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.metamodel.impl.EdgeTypeImpl;
import de.tum.pssif.core.metamodel.impl.NodeTypeImpl;
import de.tum.pssif.core.model.Node;

public class TestCaseCreator {

	public static void createTestCase(GraphVisualization gViz, Node node) {

		MyNode mNode = RequirementTracer.getMyNode(node);
		System.out.println("Creating Test Case for " + mNode.getName());

		MyNodeType testCaseNodeType = ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_TEST_CASE);

		MyEdgeType basedOn = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_BASED_ON);

		ModelBuilder.addNewNodeFromGUI("TC" + mNode.getNode().getId(), testCaseNodeType);

		/*
		 * Create edge "based on" between testcase requirement
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
		 * Create verifies edge between testcase and solution artifact of the
		 * requirement
		 */
		verifyTestCase(mNode, testCaseNode);

		gViz.updateGraph();

		for (MyEdge edge : ModelBuilder.getAllEdges()) {
			System.out.println(edge.getEdgeInformations());
		}

	}

	private static boolean verifyTestCase(MyNode requirementNode, MyNode testCaseNode) {
		MyEdgeType verifies = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_LOGICAL_VERIFIES);
		MyEdgeType satisfies = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_LOGICAL_SATISFIES);
		
		MyNodeType solutionArtifact = ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_SOL_ARTIFACT);
		MyNode solArtfNode = null;
		//if (solutionArtifactNode != null) {
			boolean createVerfifies = false;
			for (MyEdge myEdge : ModelBuilder.getAllEdges()) {
				if (myEdge.getSourceNode().equals(requirementNode)
						&& 
						myEdge.getEdgeType().equals(satisfies)
						//solutionArtifact.getType().getSpecials().contains(((MyNode)myEdge.getDestinationNode()).getNodeType().getType())
						) {
					solArtfNode = (MyNode) myEdge.getDestinationNode();
					createVerfifies = true;
				}
			}
			if (createVerfifies) {
				ModelBuilder.addNewEdgeGUI(testCaseNode, solArtfNode, verifies, true);
			}
			return true;
		//}
		//return false;
	}

}