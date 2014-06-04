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

public class AttributeMatcher extends MatchMethod {

	private final String[] pssifAttributes = {
			PSSIFConstants.BUILTIN_ATTRIBUTE_NAME,
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
		// TODO Auto-generated constructor stub
	}

	@Override
	public double executeMatching(Node tempNodeOrigin, Node tempNodeNew,
			Model originalModel, Model newModel, Metamodel metaModel,
			NodeType actTypeOriginModel, NodeType actTypeNewModel,
			String labelOrigin, String labelNew, List<Token> tokensOrigin,
			List<Token> tokensNew) {
		// TODO Auto-generated method stub
		double result = 0;

		result = iterateOverAttributeTypes(tempNodeOrigin, actTypeOriginModel,
				tempNodeNew, actTypeNewModel);
		return result;
	}

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

					System.out.println("Node Origin has the attribute: "
							+ attrOrigin.getName() + " with value: "
							+ tempValueOrigin);

				}
			}

			if (optionNew.isOne()) {
				attrNew = optionNew.getOne();

				attrValueNew = attrNew.get(tempNodeNew);

				if (attrValueNew.isOne()) {
					tempValueNew = attrValueNew.getOne().asString();

					nrOfAttributesNew++;

					System.out.println("Node New has the attribute: "
							+ attrNew.getName() + " with value: "
							+ tempValueNew);

				}
			}

			if ((tempValueOrigin != null) && (tempValueNew != null)) {
				if (tempValueOrigin.equals(tempValueNew)) {
					nrOfSimilarAttributes++;
				}
			}

		}
		System.out.println("Similarity: " + (nrOfSimilarAttributes / (Math.max(nrOfAttributesOrigin,
				nrOfAttributesNew))));
		return (nrOfSimilarAttributes / (Math.max(nrOfAttributesOrigin,
				nrOfAttributesNew)));

	}
}