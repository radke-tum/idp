package de.tum.pssif.core.metamodel;

public interface Metamodel {
	NodeType create(String name);

	EdgeType create(String name, String inName, NodeType in,
			Multiplicity inMult, String outName, NodeType out,
			Multiplicity outMult);

	NodeType findNodeType(String name);

	EdgeType findEdgeType(String name);
}
