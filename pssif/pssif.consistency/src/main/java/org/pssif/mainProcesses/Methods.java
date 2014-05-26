package org.pssif.mainProcesses;

import java.util.UUID;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Node;

public class Methods {

	/**
	 * This method returns to a given node & according nodetype his global ID of
	 * the model
	 * 
	 * @param tempNodeOrigin
	 *            the node from the original modal
	 * @param actTypeOriginModel
	 *            the type of the node
	 * @return a string consisting of the global unique ID of a node from the
	 *         model
	 */
	public static String findGlobalID(Node tempNodeOrigin,
			NodeType actTypeOriginModel) {
		String globalID = "Name not available";

		Attribute globalIDAttribute = actTypeOriginModel.getAttribute(
				PSSIFConstants.BUILTIN_ATTRIBUTE_GLOBAL_ID).getOne();

		globalID = globalIDAttribute.get(tempNodeOrigin).getOne().asString();

		return globalID;
	}

	/**
	 * Get the name from the Node object
	 * 
	 * @return the actual name or "Name not available" if the name was not
	 *         defined
	 * @author Luc
	 */
	public static String findName(NodeType actType, Node actNode) {
		String name = "Name not available";
		// find the name of the Node
		PSSIFOption<Attribute> tmp = actType
				.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
		if (tmp.isOne()) {
			Attribute nodeName = tmp.getOne();

			if (nodeName.get(actNode) != null) {
				PSSIFValue value = null;
				if (nodeName.get(actNode).isOne()) {
					value = nodeName.get(actNode).getOne();
					name = value.asString();
				}
				if (nodeName.get(actNode).isNone()) {
					name = "Name not available";
				}
			} else
				name = "Name not available";
		}

		return name;
	}
}
