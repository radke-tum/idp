package de.tum.pssif.core.metamodel;


public interface Metamodel {
	NodeType create(String name);

	EdgeType create(String name, NodeType in, Multiplicity inMult,
			NodeType out, Multiplicity outMult);

	NodeType findNodeType(String name);

	EdgeType findEdgeType(String name);
}
