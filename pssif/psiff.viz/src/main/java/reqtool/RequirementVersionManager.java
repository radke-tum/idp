package reqtool;


import graph.model.MyNode;
import de.tum.pssif.core.model.Node;

public class RequirementVersionManager {
	
	public static void createNewVersion(Node node){
		MyNode mNode = RequirementTracer.getMyNode(node);
		System.out.println("Creating new version for "+mNode.getName());
		/*TODO
		 * 
		 * Create new node with same attributes, except version. Take new edges from old node for the new one, and connect the
		 * old one through directed "Evolves-To" to new node.
		 * 
		 */
	}

}
