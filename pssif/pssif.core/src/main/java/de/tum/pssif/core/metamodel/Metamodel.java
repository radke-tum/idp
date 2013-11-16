package de.tum.pssif.core.metamodel;

public interface Metamodel {
	NodeType create(String name);

	EdgeType create(String name, String inName, NodeType inType,
			Multiplicity inMult, String outName, NodeType outType,
			Multiplicity outMult);

	NodeType findNodeType(String name);

	EdgeType findEdgeType(String name);
}
