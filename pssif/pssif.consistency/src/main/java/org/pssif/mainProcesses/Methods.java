package org.pssif.mainProcesses;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.pssif.consistencyDataStructures.Token;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.exception.PSSIFIllegalAccessException;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;

/**
This project is part of the Product-Service System integration framework. It is responsible for keeping consistency between different requirements models or versions of models.
Copyright (C) 2014 Andreas Genz

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Feel free to contact me via eMail: genz@in.tum.de
*/


/**
 * A class that provides several methods used in the PSSIF consistency checker
 * 
 * @author Andreas
 * 
 */
public class Methods {

	/**
	 * This method returns to a given node & according nodetype his global ID of
	 * the model
	 * 
	 * @param tempNodeOrigin
	 *            the node from the original modal
	 * @param nodeTypeBase
	 *            the type of the node
	 * @return a string consisting of the global unique ID of a node from the
	 *         model
	 */
	public static String findGlobalID(Node tempNodeOrigin,
			NodeTypeBase nodeTypeBase) {
		String globalID = "Global-ID not available";

		if (tempNodeOrigin == null) {
			throw new NullPointerException(
					"The node of type: "
							+ nodeTypeBase.getName()
							+ " which GLOBAL_ID should be retrieved is null. "
							+ "Maybe the node wasn't transferred correctly to the new model.");
		} else {
			if (nodeTypeBase.getAttribute(
					PSSIFConstants.BUILTIN_ATTRIBUTE_GLOBAL_ID).isOne()) {
				Attribute globalIDAttribute = nodeTypeBase.getAttribute(
						PSSIFConstants.BUILTIN_ATTRIBUTE_GLOBAL_ID).getOne();

				if (globalIDAttribute.get(tempNodeOrigin).isOne()) {
					globalID = globalIDAttribute.get(tempNodeOrigin).getOne()
							.asString();
				} else {
					throw new PSSIFIllegalAccessException(
							"The GLOBAL_ID Attribute couln't be found for the given node: "
									+ findName(nodeTypeBase, tempNodeOrigin)
									+ " Maybe the global id assignment was changed!");
				}

				return globalID;
			} else {
				throw new PSSIFIllegalAccessException(
						"The GLOBAL_ID Attribute couln't be found for the given nodetype: "
								+ nodeTypeBase.getName()
								+ " Maybe the attribute groups were changed!");
			}
		}

	}
	
	/**
	 * @author Andreas
	 * @return whether the given edge is directed or not
	 */
	@SuppressWarnings("unused")
	private static boolean isEdgeDirected(EdgeType type, Edge edge) {
		Collection<AttributeGroup> attrgroups = type.getAttributeGroups();

		boolean isDirectedEdge = false;

		if (attrgroups != null) {
			for (AttributeGroup ag : attrgroups) {

				Collection<Attribute> attr = ag.getAttributes();

				for (Attribute a : attr) {
					if (!a.getName().equals(
							PSSIFConstants.BUILTIN_ATTRIBUTE_DIRECTED)) {
						continue;
					}

					PSSIFOption<PSSIFValue> attrvalue = a.get(edge);

					if (attrvalue != null) {
						isDirectedEdge = attrvalue.getOne().asBoolean()
								.booleanValue();
					}
				}
			}
		}

		return isDirectedEdge;
	}

	/**
	 * This method concatenates the words of the given token list to a single
	 * String whereby each word is separated by a space. The result is used for
	 * the calculation of the levenshtein distance.
	 * 
	 * @param tokens
	 * @return a concatenated String based on the given token list
	 * 
	 */
	public static String getStringFromTokens(List<Token> tokens) {
		String result = "";

		for (Token token : tokens) {
			result += token.getWord() + " ";
		}

		return result;
	}

	/**
	 * @param a
	 *            number for which the logarithm (base 2) shall be calculated)
	 * @return the logarithm of base two of the given number
	 */
	public static double logarithmBaseTwo(double x) {
		return Math.log(x) / Math.log(2.0);
	}

	/**
	 * Get the name from the Node object
	 * 
	 * @return the actual name or "Name not available" if the name was not
	 *         defined
	 * @author Luc
	 */
	public static String findName(NodeTypeBase nodeTypeBase, Node actNode) {
		String name = "Name not available";
		// find the name of the Node
		PSSIFOption<Attribute> tmp = nodeTypeBase
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
