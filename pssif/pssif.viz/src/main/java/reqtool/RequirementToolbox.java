package reqtool;

import java.util.Vector;

import graph.model.MyEdge;
import graph.model.MyEdgeType;
import graph.model.MyNode;
import graph.model.MyNodeType;
import model.ModelBuilder;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Node;

public class RequirementToolbox {

	public static MyNode getMyNode(Node node) {
		for (MyNode n : ModelBuilder.getAllNodes()) {
			if (n.getNode().equals(node)) {
				return n;
			}
		}
		return null;
	}
	
	public static void showContainment(MyNode specNode){
		MyEdgeType contains = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_CONTAINS);
		for (MyEdge e : ModelBuilder.getAllEdges()){
			if (e.getSourceNode().equals(specNode)&&e.getEdgeType().equals(contains)){
				getMyNode(e.getDestinationNode().getNode()).setVisible(true);
			}
			
	}	
		
		
	}
	public static void hideContainment(MyNode specNode){
		MyEdgeType contains = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_CONTAINS);
		for (MyEdge e : ModelBuilder.getAllEdges()){
			if (e.getSourceNode().equals(specNode)&&e.getEdgeType().equals(contains)){
				getMyNode(e.getDestinationNode().getNode()).setVisible(false);
			}
			
			
	}	
		
		
	}
	public static boolean hasContainment(MyNode specNode){
	MyEdgeType contains = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_CONTAINS);
		for (MyEdge e : ModelBuilder.getAllEdges()){
			if (e.getSourceNode().equals(specNode)&&e.getEdgeType().equals(contains)){
				return true;
			}
			
	}
	 return false;
	}

	public static boolean containmentIsVisible(MyNode specNode) {
		MyEdgeType contains = ModelBuilder.getEdgeTypes().getValue(PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_INCLUSION_CONTAINS);
		for (MyEdge e : ModelBuilder.getAllEdges()){
			if (e.getSourceNode().equals(specNode)&&e.getEdgeType().equals(contains)){
				return e.getDestinationNode().isVisible();
			}
		
	}
	
		return false;
	}
	
	public static Vector<MyNodeType> getSpecArtifTypes() {
		Vector<MyNodeType> specificationTypes = new Vector<MyNodeType>();

		MyNodeType specType = ModelBuilder.getNodeTypes().getValue(PSSIFCanonicMetamodelCreator.N_SPEC_ARTIFACT);
		specificationTypes.add(specType);

		for (NodeType nodeType : specType.getType().getSpecials()) {
			specificationTypes.add(new MyNodeType(nodeType));
		}
		return specificationTypes;
	}
	
}
