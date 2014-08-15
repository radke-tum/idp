package org.pssif.matchingLogic;

import java.util.List;

import org.pssif.consistencyDataStructures.Token;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
This file is part of PSSIF Consistency. It is responsible for keeping consistency between different requirements models or versions of models.
Copyright (C) 2014 Andreas Genz

    PSSIF Consistency is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    PSSIF Consistency is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with PSSIF Consistency.  If not, see <http://www.gnu.org/licenses/>.
    
    Feel free to contact me via eMail: genz@in.tum.de
*/

/**
 * This class represents an implementation of the attribute matching algorithm.
 * This match method compares the set attributes of two nodes and returns a
 * similarity value based on the set attributes.
 * 
 * @author Andreas
 * 
 */
public class AttributeMatcher extends MatchMethod {

	private static final boolean debugMode = false;

	/**
	 * the attributes which are compared between the nodes
	 */
	private final String[] pssifAttributes = {
			PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT,
			PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_END,
			PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_START,
			PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION,
			PSSIFCanonicMetamodelCreator.A_BLOCK_COST,
			PSSIFCanonicMetamodelCreator.A_DURATION,
			PSSIFCanonicMetamodelCreator.A_HARDWARE_WEIGHT,
			PSSIFCanonicMetamodelCreator.A_REQUIREMENT_PRIORITY,
			PSSIFCanonicMetamodelCreator.A_REQUIREMENT_TYPE };

	public AttributeMatcher(MatchingMethods matchMethod, boolean isActive,
			double weight) {
		super(matchMethod, isActive, weight);
	}

	@Override
	public double executeMatching(Node tempNodeOrigin, Node tempNodeNew,
			Model originalModel, Model newModel, Metamodel metaModelOriginal,
			Metamodel metaModelNew, NodeType actTypeOriginModel,
			NodeType actTypeNewModel, String labelOrigin, String labelNew,
			List<Token> tokensOrigin, List<Token> tokensNew) {
		double result = 0;

		result = iterateOverAttributeTypes(tempNodeOrigin, actTypeOriginModel,
				tempNodeNew, actTypeNewModel);
		return result;
	}

	/**
	 * This method iterates over every given attribute saved in the
	 * pssifAttributes array. Then it looks up for the two nodes if the
	 * attribute is set in both nodes and if yes the values are compared.
	 * 
	 * @param tempNodeOrigin
	 * @param tempNodeOriginType
	 * @param tempNodeNew
	 * @param tempNodeNewType
	 * @return TODO
	 */
	private double iterateOverAttributeTypes(Node tempNodeOrigin,
			NodeType tempNodeOriginType, Node tempNodeNew,
			NodeType tempNodeNewType) {
		PSSIFOption<Attribute> optionOrigin, optionNew;
		Attribute attrOrigin, attrNew;
		PSSIFOption<PSSIFValue> attrValueOrigin, attrValueNew;
		String tempValueOrigin, tempValueNew;

		double nrOfAttributesOrigin = 0;
		double nrOfAttributesNew = 0;
		double nrOfAttributes = pssifAttributes.length;

		double nrOfSimilarAttributes = 0;

		for (String typeName : pssifAttributes) {
			attrOrigin = null;
			attrNew = null;
			attrValueOrigin = null;
			attrValueNew = null;
			tempValueOrigin = null;
			tempValueNew = null;

			optionOrigin = tempNodeOriginType.getAttribute(typeName);
			optionNew = tempNodeNewType.getAttribute(typeName);

			if (optionOrigin.isOne()) {
				attrOrigin = optionOrigin.getOne();

				attrValueOrigin = attrOrigin.get(tempNodeOrigin);

				if (attrValueOrigin.isOne()) {

					tempValueOrigin = attrValueOrigin.getOne().asString();

					nrOfAttributesOrigin++;

					if (debugMode) {
						System.out.println("Node Origin has the attribute: "
								+ attrOrigin.getName() + " with value: "
								+ tempValueOrigin);
					}

				}
			}

			if (optionNew.isOne()) {
				attrNew = optionNew.getOne();

				attrValueNew = attrNew.get(tempNodeNew);

				if (attrValueNew.isOne()) {
					tempValueNew = attrValueNew.getOne().asString();

					nrOfAttributesNew++;

					if (debugMode) {
						System.out.println("Node New has the attribute: "
								+ attrNew.getName() + " with value: "
								+ tempValueNew);
					}

					if (tempValueOrigin != null) {
						String tmpOrigin = tempValueOrigin.replaceAll("\\s+",
								"").toLowerCase();
						String tmpNew = tempValueNew.replaceAll("\\s+", "")
								.toLowerCase();

						if (tmpOrigin.equals(tmpNew)) {
							nrOfSimilarAttributes++;
						}
					}
				}
			}

		}
		if((nrOfAttributesOrigin == 0) && (nrOfAttributesNew == 0)){
			return 1;
		} else {
			return (nrOfSimilarAttributes / (Math.max(nrOfAttributesOrigin,
					nrOfAttributesNew)));
		}
	}
}