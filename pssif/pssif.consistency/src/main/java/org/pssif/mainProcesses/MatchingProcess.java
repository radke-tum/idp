package org.pssif.mainProcesses;

import java.util.Iterator;
import java.util.Set;

import org.pssif.consistencyDataStructures.ConsistencyData;
import org.pssif.consistencyLogic.MatchMethod;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * @author Andreas TODO
 */
public class MatchingProcess {

	/**
	 * @param originalModel
	 *            the model which was first imported
	 * @param newModel
	 *            the model which was recently imported
	 * @param metaModel
	 *            the metamodel according to the two models
	 * @param consistencyData
	 *            the data of the matching process will be stored here and can
	 *            be accesed both by the Comparsion and the matching process
	 * @param matchMethods
	 *            these are the matching metrics for the current matching
	 *            operation
	 */
	public MatchingProcess(Model originalModel, Model newModel,
			Metamodel metaModel, ConsistencyData consistencyData,
			Set<MatchMethod> matchMethods) {

		this.originalModel = originalModel;
		this.newModel = newModel;
		this.metaModel = metaModel;
		this.consistencyData = consistencyData;
		this.matchMethods = matchMethods;
	}

	private Model originalModel, newModel;
	private Metamodel metaModel;

	private ConsistencyData consistencyData;

	private Set<MatchMethod> matchMethods;

	/**
	 * @param tempNodeOrigin
	 * @param tempNodeNew
	 * @param actTypeOriginModel
	 *            TODO
	 * @param actTypeNewModel
	 */
	public void startMatchingProcess(Node tempNodeOrigin, Node tempNodeNew,
			NodeType actTypeOriginModel, NodeType actTypeNewModel) {
		double currentMetricResult;

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();

			if (currentMethod.isActive()) {
				// TODO: get weigthed result here
				currentMetricResult = currentMethod.executeMatching(
						tempNodeOrigin, tempNodeNew, originalModel, newModel,
						metaModel, actTypeOriginModel, actTypeNewModel);

				saveMatchMethodResult(currentMethod, currentMetricResult);
			}
		}

	}

	/**
	 * @param currentMethod
	 * @param currentMetricResult TODO
	 * @throws RuntimeException
	 */
	public void saveMatchMethodResult(MatchMethod currentMethod, double currentMetricResult)
			throws RuntimeException {
		switch (currentMethod.getMatchMethod()) {
		case EXACT_STRING_MATCHING: 
		case DEPTH_MATCHING:
		case STRING_EDIT_DISTANCE_MATCHING:
		case HYPHEN_MATCHING:
		case LINGUISTIC_MATCHING:
		case VECTOR_SPACE_MODEL_MATCHING:
		case LATENT_SEMANTIC_INDEXING_MATCHING:
		default:
			throw new RuntimeException(
					"The last applied metric couln't be found.");
		}
	}

}
