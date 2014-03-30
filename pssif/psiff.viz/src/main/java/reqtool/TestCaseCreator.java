package reqtool;

import model.ModelBuilder;
import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.metamodel.impl.EdgeTypeImpl;
import de.tum.pssif.core.metamodel.impl.NodeTypeImpl;
import de.tum.pssif.core.model.Node;

public class TestCaseCreator {
	
	public static void createTestCase(Node node) {
		MyNode mNode = RequirementTracer.getMyNode(node);
		System.out.println("Creating Test Case for "+mNode.getName());
		MyNodeType testCaseNodeType = new MyNodeType(new NodeTypeImpl(PSSIFCanonicMetamodelCreator.N_TEST_CASE));
		MyEdgeType basedOn = new MyEdgeType(new EdgeTypeImpl(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_BASED_ON), 0);
		
		
		/*
		ModelBuilder.addNewNodeFromGUI("TC"+mNode.getNode().getId(), testCaseNodeType);
		
		
		for (MyNode newNode : ModelBuilder.getAllNodes()){
			   
		if (newNode.getNode().getId().toString().equals("TC"+mNode.getNode().toString())){
				
			ModelBuilder.addNewEdgeGUI(newNode, mNode, basedOn , true);
			 
		}
			   
			  }
		
		for (MyEdge edge : ModelBuilder.getAllEdges()){
			System.out.println(edge.getEdgeInformations());
		}
		*/
	}
	
}