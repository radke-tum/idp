package org.pssif.consistencyDataStructures;

import graph.model.MyNode;

import java.util.Map;

import comparedDataStructures.ComparedElementPair;

import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.model.Model;

public class ConsistencyData {

	private Model originModel, newModel;
	private Metamodel metaModel;

	//stores the already compared IDs
	private Map<String,String> IDMapping;
	
	//stores compared Nodes with similarity information
	private ComparedElementPair<MyNode>[] comparedNodes;
	
}
