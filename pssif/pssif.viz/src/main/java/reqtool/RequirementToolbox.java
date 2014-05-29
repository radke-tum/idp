package reqtool;

import graph.model.MyNode;
import model.ModelBuilder;
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

}
